package org.apache.tools.ant.taskdefs.cvslib;

public class CvsTagEntry {
   private String filename;
   private String prevRevision;
   private String revision;

   public CvsTagEntry(String filename) {
      this(filename, (String)null, (String)null);
   }

   public CvsTagEntry(String filename, String revision) {
      this(filename, revision, (String)null);
   }

   public CvsTagEntry(String filename, String revision, String prevRevision) {
      this.filename = filename;
      this.revision = revision;
      this.prevRevision = prevRevision;
   }

   public String getFile() {
      return this.filename;
   }

   public String getRevision() {
      return this.revision;
   }

   public String getPreviousRevision() {
      return this.prevRevision;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(this.filename);
      if (this.revision == null) {
         buffer.append(" was removed");
         if (this.prevRevision != null) {
            buffer.append("; previous revision was ").append(this.prevRevision);
         }
      } else if (this.revision != null && this.prevRevision == null) {
         buffer.append(" is new; current revision is ").append(this.revision);
      } else if (this.revision != null && this.prevRevision != null) {
         buffer.append(" has changed from ").append(this.prevRevision).append(" to ").append(this.revision);
      }

      return buffer.toString();
   }
}
