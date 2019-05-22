package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Reference;

public class IsReference extends ProjectComponent implements Condition {
   private Reference ref;
   private String type;

   public void setRefid(Reference r) {
      this.ref = r;
   }

   public void setType(String type) {
      this.type = type;
   }

   public boolean eval() throws BuildException {
      if (this.ref == null) {
         throw new BuildException("No reference specified for isreference condition");
      } else {
         Object o = this.getProject().getReference(this.ref.getRefId());
         if (o == null) {
            return false;
         } else if (this.type == null) {
            return true;
         } else {
            Class typeClass = (Class)this.getProject().getDataTypeDefinitions().get(this.type);
            if (typeClass == null) {
               typeClass = (Class)this.getProject().getTaskDefinitions().get(this.type);
            }

            return typeClass == null ? false : typeClass.isAssignableFrom(o.getClass());
         }
      }
   }
}
