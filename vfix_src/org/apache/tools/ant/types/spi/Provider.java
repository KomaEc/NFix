package org.apache.tools.ant.types.spi;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class Provider extends ProjectComponent {
   private String type;

   public String getClassName() {
      return this.type;
   }

   public void setClassName(String type) {
      this.type = type;
   }

   public void check() {
      if (this.type == null) {
         throw new BuildException("classname attribute must be set for provider element", this.getLocation());
      } else if (this.type.length() == 0) {
         throw new BuildException("Invalid empty classname", this.getLocation());
      }
   }
}
