package org.apache.maven.model;

import java.io.Serializable;

public class DeploymentRepository extends RepositoryBase implements Serializable {
   private boolean uniqueVersion = true;

   public boolean isUniqueVersion() {
      return this.uniqueVersion;
   }

   public void setUniqueVersion(boolean uniqueVersion) {
      this.uniqueVersion = uniqueVersion;
   }

   public boolean equals(Object obj) {
      return super.equals(obj);
   }
}
