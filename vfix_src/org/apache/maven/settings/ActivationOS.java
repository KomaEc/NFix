package org.apache.maven.settings;

import java.io.Serializable;

public class ActivationOS implements Serializable {
   private String name;
   private String family;
   private String arch;
   private String version;

   public String getArch() {
      return this.arch;
   }

   public String getFamily() {
      return this.family;
   }

   public String getName() {
      return this.name;
   }

   public String getVersion() {
      return this.version;
   }

   public void setArch(String arch) {
      this.arch = arch;
   }

   public void setFamily(String family) {
      this.family = family;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setVersion(String version) {
      this.version = version;
   }
}
