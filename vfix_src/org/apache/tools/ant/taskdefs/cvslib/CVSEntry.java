package org.apache.tools.ant.taskdefs.cvslib;

import java.util.Date;
import java.util.Vector;

public class CVSEntry {
   private Date date;
   private String author;
   private final String comment;
   private final Vector files = new Vector();

   public CVSEntry(Date date, String author, String comment) {
      this.date = date;
      this.author = author;
      this.comment = comment;
   }

   public void addFile(String file, String revision) {
      this.files.addElement(new RCSFile(file, revision));
   }

   public void addFile(String file, String revision, String previousRevision) {
      this.files.addElement(new RCSFile(file, revision, previousRevision));
   }

   public Date getDate() {
      return this.date;
   }

   public void setAuthor(String author) {
      this.author = author;
   }

   public String getAuthor() {
      return this.author;
   }

   public String getComment() {
      return this.comment;
   }

   public Vector getFiles() {
      return this.files;
   }

   public String toString() {
      return this.getAuthor() + "\n" + this.getDate() + "\n" + this.getFiles() + "\n" + this.getComment();
   }
}
