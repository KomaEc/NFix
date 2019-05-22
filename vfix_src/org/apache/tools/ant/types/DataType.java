package org.apache.tools.ant.types;

import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.IdentityStack;

public abstract class DataType extends ProjectComponent implements Cloneable {
   /** @deprecated */
   protected Reference ref;
   /** @deprecated */
   protected boolean checked = true;

   public boolean isReference() {
      return this.ref != null;
   }

   public void setRefid(Reference ref) {
      this.ref = ref;
      this.checked = false;
   }

   protected String getDataTypeName() {
      return ComponentHelper.getElementName(this.getProject(), this, true);
   }

   protected void dieOnCircularReference() {
      this.dieOnCircularReference(this.getProject());
   }

   protected void dieOnCircularReference(Project p) {
      if (!this.checked && this.isReference()) {
         this.dieOnCircularReference(new IdentityStack(this), p);
      }
   }

   protected void dieOnCircularReference(Stack stack, Project project) throws BuildException {
      if (!this.checked && this.isReference()) {
         Object o = this.ref.getReferencedObject(project);
         if (o instanceof DataType) {
            IdentityStack id = IdentityStack.getInstance(stack);
            if (id.contains(o)) {
               throw this.circularReference();
            }

            id.push(o);
            ((DataType)o).dieOnCircularReference(id, project);
            id.pop();
         }

         this.checked = true;
      }
   }

   public static void invokeCircularReferenceCheck(DataType dt, Stack stk, Project p) {
      dt.dieOnCircularReference(stk, p);
   }

   protected Object getCheckedRef() {
      return this.getCheckedRef(this.getProject());
   }

   protected Object getCheckedRef(Project p) {
      return this.getCheckedRef(this.getClass(), this.getDataTypeName(), p);
   }

   protected Object getCheckedRef(Class requiredClass, String dataTypeName) {
      return this.getCheckedRef(requiredClass, dataTypeName, this.getProject());
   }

   protected Object getCheckedRef(Class requiredClass, String dataTypeName, Project project) {
      if (project == null) {
         throw new BuildException("No Project specified");
      } else {
         this.dieOnCircularReference(project);
         Object o = this.ref.getReferencedObject(project);
         if (!requiredClass.isAssignableFrom(o.getClass())) {
            this.log("Class " + o.getClass() + " is not a subclass of " + requiredClass, 3);
            String msg = this.ref.getRefId() + " doesn't denote a " + dataTypeName;
            throw new BuildException(msg);
         } else {
            return o;
         }
      }
   }

   protected BuildException tooManyAttributes() {
      return new BuildException("You must not specify more than one attribute when using refid");
   }

   protected BuildException noChildrenAllowed() {
      return new BuildException("You must not specify nested elements when using refid");
   }

   protected BuildException circularReference() {
      return new BuildException("This data type contains a circular reference.");
   }

   protected boolean isChecked() {
      return this.checked;
   }

   protected void setChecked(boolean checked) {
      this.checked = checked;
   }

   public Reference getRefid() {
      return this.ref;
   }

   protected void checkAttributesAllowed() {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      }
   }

   protected void checkChildrenAllowed() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      }
   }

   public String toString() {
      String d = this.getDescription();
      return d == null ? this.getDataTypeName() : this.getDataTypeName() + " " + d;
   }

   public Object clone() throws CloneNotSupportedException {
      DataType dt = (DataType)super.clone();
      dt.setDescription(this.getDescription());
      if (this.getRefid() != null) {
         dt.setRefid(this.getRefid());
      }

      dt.setChecked(this.isChecked());
      return dt;
   }
}
