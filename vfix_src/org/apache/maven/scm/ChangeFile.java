package org.apache.maven.scm;

import java.io.Serializable;

public class ChangeFile implements Serializable {
   private static final long serialVersionUID = 6294855290542668753L;
   private String name;
   private String revision;
   private ScmFileStatus action;
   private String originalName;
   private String originalRevision;

   public ChangeFile(String name) {
      this.setName(name);
   }

   public ChangeFile(String name, String rev) {
      this.setName(name);
      this.setRevision(rev);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getOriginalName() {
      return this.originalName;
   }

   public void setOriginalName(String originalName) {
      this.originalName = originalName;
   }

   public String getOriginalRevision() {
      return this.originalRevision;
   }

   public void setOriginalRevision(String originalRevision) {
      this.originalRevision = originalRevision;
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String revision) {
      this.revision = revision;
   }

   public ScmFileStatus getAction() {
      return this.action;
   }

   public void setAction(ScmFileStatus action) {
      this.action = action;
   }

   public String toString() {
      StringBuilder buffer = new StringBuilder();
      if (this.getAction() != null) {
         buffer.append("[").append(this.getAction()).append("]:");
      }

      buffer.append(this.getName());
      if (this.getRevision() != null) {
         buffer.append(", ").append(this.getRevision());
      }

      if (this.getOriginalName() != null) {
         buffer.append(", originalName=").append(this.getOriginalName());
      }

      if (this.getOriginalRevision() != null) {
         buffer.append(", originalRevision=").append(this.getOriginalRevision());
      }

      return buffer.toString();
   }
}
