package org.apache.tools.ant.taskdefs.cvslib;

class RCSFile {
   private String name;
   private String revision;
   private String previousRevision;

   RCSFile(String name, String rev) {
      this(name, rev, (String)null);
   }

   RCSFile(String name, String revision, String previousRevision) {
      this.name = name;
      this.revision = revision;
      if (!revision.equals(previousRevision)) {
         this.previousRevision = previousRevision;
      }

   }

   String getName() {
      return this.name;
   }

   String getRevision() {
      return this.revision;
   }

   String getPreviousRevision() {
      return this.previousRevision;
   }
}
