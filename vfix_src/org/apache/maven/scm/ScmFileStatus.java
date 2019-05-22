package org.apache.maven.scm;

import java.io.Serializable;

public final class ScmFileStatus implements Serializable {
   private static final long serialVersionUID = -7840223279162817915L;
   public static final ScmFileStatus ADDED = new ScmFileStatus("added");
   public static final ScmFileStatus DELETED = new ScmFileStatus("deleted");
   public static final ScmFileStatus MODIFIED = new ScmFileStatus("modified");
   public static final ScmFileStatus RENAMED = new ScmFileStatus("renamed");
   public static final ScmFileStatus COPIED = new ScmFileStatus("copied");
   public static final ScmFileStatus MISSING = new ScmFileStatus("missing");
   public static final ScmFileStatus CHECKED_IN = new ScmFileStatus("checked-in");
   public static final ScmFileStatus CHECKED_OUT = new ScmFileStatus("checked-out");
   public static final ScmFileStatus CONFLICT = new ScmFileStatus("conflict");
   public static final ScmFileStatus PATCHED = new ScmFileStatus("patched");
   public static final ScmFileStatus UPDATED = new ScmFileStatus("updated");
   public static final ScmFileStatus TAGGED = new ScmFileStatus("tagged");
   public static final ScmFileStatus LOCKED = new ScmFileStatus("locked");
   public static final ScmFileStatus UNKNOWN = new ScmFileStatus("unknown");
   public static final ScmFileStatus EDITED = new ScmFileStatus("edit");
   private final String name;

   private ScmFileStatus(String name) {
      this.name = name;
   }

   public String toString() {
      return this.name;
   }

   public boolean isStatus() {
      return this == UNKNOWN || this.isDiff();
   }

   public boolean isDiff() {
      return this == ADDED || this == DELETED || this == MODIFIED;
   }

   public boolean isTransaction() {
      return this == CHECKED_IN || this == CHECKED_OUT || this == LOCKED || this == TAGGED || this.isUpdate();
   }

   public boolean isUpdate() {
      return this == CONFLICT || this == UPDATED || this == PATCHED;
   }
}
