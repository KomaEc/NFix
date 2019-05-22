package org.apache.maven.settings;

import java.io.Serializable;

public class Activation implements Serializable {
   private boolean activeByDefault = false;
   private String jdk;
   private ActivationOS os;
   private ActivationProperty property;
   private ActivationFile file;

   public ActivationFile getFile() {
      return this.file;
   }

   public String getJdk() {
      return this.jdk;
   }

   public ActivationOS getOs() {
      return this.os;
   }

   public ActivationProperty getProperty() {
      return this.property;
   }

   public boolean isActiveByDefault() {
      return this.activeByDefault;
   }

   public void setActiveByDefault(boolean activeByDefault) {
      this.activeByDefault = activeByDefault;
   }

   public void setFile(ActivationFile file) {
      this.file = file;
   }

   public void setJdk(String jdk) {
      this.jdk = jdk;
   }

   public void setOs(ActivationOS os) {
      this.os = os;
   }

   public void setProperty(ActivationProperty property) {
      this.property = property;
   }
}
