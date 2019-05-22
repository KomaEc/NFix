package org.apache.maven.model;

import java.io.Serializable;

public class RepositoryPolicy implements Serializable {
   private boolean enabled = true;
   private String updatePolicy;
   private String checksumPolicy;

   public String getChecksumPolicy() {
      return this.checksumPolicy;
   }

   public String getUpdatePolicy() {
      return this.updatePolicy;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setChecksumPolicy(String checksumPolicy) {
      this.checksumPolicy = checksumPolicy;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public void setUpdatePolicy(String updatePolicy) {
      this.updatePolicy = updatePolicy;
   }
}
