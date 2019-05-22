package org.apache.tools.ant.types.resources.selectors;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.Resource;

public class InstanceOf implements ResourceSelector {
   private static final String ONE_ONLY = "Exactly one of class|type must be set.";
   private Project project;
   private Class clazz;
   private String type;
   private String uri;

   public void setProject(Project p) {
      this.project = p;
   }

   public void setClass(Class c) {
      if (this.clazz != null) {
         throw new BuildException("The class attribute has already been set.");
      } else {
         this.clazz = c;
      }
   }

   public void setType(String s) {
      this.type = s;
   }

   public void setURI(String u) {
      this.uri = u;
   }

   public Class getCheckClass() {
      return this.clazz;
   }

   public String getType() {
      return this.type;
   }

   public String getURI() {
      return this.uri;
   }

   public boolean isSelected(Resource r) {
      if (this.clazz == null == (this.type == null)) {
         throw new BuildException("Exactly one of class|type must be set.");
      } else {
         Class c = this.clazz;
         if (this.type != null) {
            if (this.project == null) {
               throw new BuildException("No project set for InstanceOf ResourceSelector; the type attribute is invalid.");
            }

            AntTypeDefinition d = ComponentHelper.getComponentHelper(this.project).getDefinition(ProjectHelper.genComponentName(this.uri, this.type));
            if (d == null) {
               throw new BuildException("type " + this.type + " not found.");
            }

            try {
               c = d.innerGetTypeClass();
            } catch (ClassNotFoundException var5) {
               throw new BuildException(var5);
            }
         }

         return c.isAssignableFrom(r.getClass());
      }
   }
}
