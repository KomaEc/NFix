package org.apache.maven.scm.command.info;

public class InfoItem {
   private String path;
   private String url;
   private String repositoryRoot;
   private String repositoryUUID;
   private String revision;
   private String nodeKind;
   private String schedule;
   private String lastChangedAuthor;
   private String lastChangedRevision;
   private String lastChangedDate;

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getURL() {
      return this.url;
   }

   public void setURL(String url) {
      this.url = url;
   }

   public String getRepositoryRoot() {
      return this.repositoryRoot;
   }

   public void setRepositoryRoot(String repositoryRoot) {
      this.repositoryRoot = repositoryRoot;
   }

   public String getRepositoryUUID() {
      return this.repositoryUUID;
   }

   public void setRepositoryUUID(String repositoryUUID) {
      this.repositoryUUID = repositoryUUID;
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String revision) {
      this.revision = revision;
   }

   public String getNodeKind() {
      return this.nodeKind;
   }

   public void setNodeKind(String nodeKind) {
      this.nodeKind = nodeKind;
   }

   public String getSchedule() {
      return this.schedule;
   }

   public void setSchedule(String schedule) {
      this.schedule = schedule;
   }

   public String getLastChangedAuthor() {
      return this.lastChangedAuthor;
   }

   public void setLastChangedAuthor(String lastChangedAuthor) {
      this.lastChangedAuthor = lastChangedAuthor;
   }

   public String getLastChangedRevision() {
      return this.lastChangedRevision;
   }

   public void setLastChangedRevision(String lastChangedRevision) {
      this.lastChangedRevision = lastChangedRevision;
   }

   public String getLastChangedDate() {
      return this.lastChangedDate;
   }

   public void setLastChangedDate(String lastChangedDate) {
      this.lastChangedDate = lastChangedDate;
   }
}
