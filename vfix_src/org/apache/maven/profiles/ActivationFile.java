package org.apache.maven.profiles;

import java.io.Serializable;

public class ActivationFile implements Serializable {
   private String missing;
   private String exists;

   public String getExists() {
      return this.exists;
   }

   public String getMissing() {
      return this.missing;
   }

   public void setExists(String exists) {
      this.exists = exists;
   }

   public void setMissing(String missing) {
      this.missing = missing;
   }
}
