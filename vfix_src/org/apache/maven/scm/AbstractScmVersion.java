package org.apache.maven.scm;

import java.io.Serializable;

public abstract class AbstractScmVersion implements ScmVersion, Serializable {
   private static final long serialVersionUID = -3388495744009098066L;
   private String name;

   public AbstractScmVersion(String name) {
      this.setName(name);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      if (name != null) {
         name = name.trim();
      }

      this.name = name;
   }

   public String toString() {
      return this.getName();
   }
}
