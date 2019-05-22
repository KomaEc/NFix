package com.gzoltar.shaded.org.pitest.maven;

import org.apache.maven.scm.ScmFileStatus;

public enum ScmStatus {
   ADDED(ScmFileStatus.ADDED),
   DELETED(ScmFileStatus.DELETED),
   MODIFIED(ScmFileStatus.MODIFIED),
   RENAMED(ScmFileStatus.RENAMED),
   COPIED(ScmFileStatus.COPIED),
   MISSING(ScmFileStatus.MISSING),
   CHECKED_IN(ScmFileStatus.CHECKED_IN),
   CHECKED_OUT(ScmFileStatus.CHECKED_OUT),
   CONFLICT(ScmFileStatus.CONFLICT),
   PATCHED(ScmFileStatus.PATCHED),
   UPDATED(ScmFileStatus.UPDATED),
   TAGGED(ScmFileStatus.TAGGED),
   LOCKED(ScmFileStatus.LOCKED),
   UNKNOWN(ScmFileStatus.UNKNOWN),
   EDITED(ScmFileStatus.EDITED);

   private final ScmFileStatus status;

   public ScmFileStatus getStatus() {
      return this.status;
   }

   private ScmStatus(ScmFileStatus status) {
      this.status = status;
   }
}
