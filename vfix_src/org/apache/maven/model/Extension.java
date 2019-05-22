package org.apache.maven.model;

import java.io.Serializable;

public class Extension implements Serializable {
   private String groupId;
   private String artifactId;
   private String version;

   public String getArtifactId() {
      return this.artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public String getVersion() {
      return this.version;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getKey() {
      return (new StringBuffer(128)).append(this.getGroupId()).append(':').append(this.getArtifactId()).toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Extension)) {
         return false;
      } else {
         Extension e = (Extension)o;
         if (!equal(e.getArtifactId(), this.getArtifactId())) {
            return false;
         } else if (!equal(e.getGroupId(), this.getGroupId())) {
            return false;
         } else {
            return equal(e.getVersion(), this.getVersion());
         }
      }
   }

   private static <T> boolean equal(T obj1, T obj2) {
      return obj1 != null ? obj1.equals(obj2) : obj2 == null;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.getArtifactId() != null ? this.getArtifactId().hashCode() : 0);
      result = 37 * result + (this.getGroupId() != null ? this.getGroupId().hashCode() : 0);
      result = 37 * result + (this.getVersion() != null ? this.getVersion().hashCode() : 0);
      return result;
   }
}
