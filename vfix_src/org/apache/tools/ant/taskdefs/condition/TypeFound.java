package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.ProjectHelper;

public class TypeFound extends ProjectComponent implements Condition {
   private String name;
   private String uri;

   public void setName(String name) {
      this.name = name;
   }

   public void setURI(String uri) {
      this.uri = uri;
   }

   protected boolean doesTypeExist(String typename) {
      ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
      String componentName = ProjectHelper.genComponentName(this.uri, typename);
      AntTypeDefinition def = helper.getDefinition(componentName);
      if (def == null) {
         return false;
      } else {
         boolean found = def.getExposedClass(this.getProject()) != null;
         if (!found) {
            String text = helper.diagnoseCreationFailure(componentName, "type");
            this.log(text, 3);
         }

         return found;
      }
   }

   public boolean eval() throws BuildException {
      if (this.name == null) {
         throw new BuildException("No type specified");
      } else {
         return this.doesTypeExist(this.name);
      }
   }
}
