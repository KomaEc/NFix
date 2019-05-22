package org.apache.maven.scm.command.blame;

import java.io.Serializable;
import java.util.Date;

public class BlameLine implements Serializable {
   private static final long serialVersionUID = 2675122069344705612L;
   private Date date;
   private String revision;
   private String author;
   private String committer;

   public BlameLine(Date date, String revision, String author) {
      this(date, revision, author, author);
   }

   public BlameLine(Date date, String revision, String author, String committer) {
      this.setDate(date);
      this.setRevision(revision);
      this.setAuthor(author);
      this.setCommitter(committer);
   }

   public String getRevision() {
      return this.revision;
   }

   public void setRevision(String revision) {
      this.revision = revision;
   }

   public String getAuthor() {
      return this.author;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getCommitter() {
      return this.committer;
   }

   public void setCommitter(String committer) {
      this.committer = committer;
   }

   public Date getDate() {
      return this.date != null ? (Date)this.date.clone() : null;
   }

   public void setDate(Date date) {
      if (date != null) {
         this.date = new Date(date.getTime());
      } else {
         this.date = null;
      }

   }
}
