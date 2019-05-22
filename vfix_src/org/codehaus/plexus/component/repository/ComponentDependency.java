package org.codehaus.plexus.component.repository;

public class ComponentDependency {
   private static final String DEAULT_DEPENDENCY_TYPE = "jar";
   private String groupId;
   private String artifactId;
   private String type = "jar";
   private String version;

   public String getArtifactId() {
      return this.artifactId;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("groupId:artifactId:version:type = " + this.groupId + ":" + this.artifactId + ":" + this.version + ":" + this.type);
      return sb.toString();
   }
}
