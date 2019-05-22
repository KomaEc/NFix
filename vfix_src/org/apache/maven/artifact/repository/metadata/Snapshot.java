package org.apache.maven.artifact.repository.metadata;

import java.io.Serializable;

public class Snapshot implements Serializable {
   private String timestamp;
   private int buildNumber = 0;
   private boolean localCopy = false;

   public int getBuildNumber() {
      return this.buildNumber;
   }

   public String getTimestamp() {
      return this.timestamp;
   }

   public boolean isLocalCopy() {
      return this.localCopy;
   }

   public void setBuildNumber(int buildNumber) {
      this.buildNumber = buildNumber;
   }

   public void setLocalCopy(boolean localCopy) {
      this.localCopy = localCopy;
   }

   public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
   }
}
