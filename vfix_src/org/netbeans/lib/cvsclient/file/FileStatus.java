package org.netbeans.lib.cvsclient.file;

public class FileStatus {
   public static final FileStatus ADDED = new FileStatus("Locally Added");
   public static final FileStatus REMOVED = new FileStatus("Locally Removed");
   public static final FileStatus MODIFIED = new FileStatus("Locally Modified");
   public static final FileStatus UP_TO_DATE = new FileStatus("Up-to-date");
   public static final FileStatus NEEDS_CHECKOUT = new FileStatus("Needs Checkout");
   public static final FileStatus NEEDS_PATCH = new FileStatus("Needs Patch");
   public static final FileStatus NEEDS_MERGE = new FileStatus("Needs Merge");
   public static final FileStatus HAS_CONFLICTS = new FileStatus("File had conflicts on merge");
   public static final FileStatus UNRESOLVED_CONFLICT = new FileStatus("Unresolved Conflict");
   public static final FileStatus UNKNOWN = new FileStatus("Unknown");
   public static final FileStatus INVALID = new FileStatus("Entry Invalid");
   private final String statusString;

   public static FileStatus getStatusForString(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.equals(ADDED.toString())) {
         return ADDED;
      } else if (var0.equals(REMOVED.toString())) {
         return REMOVED;
      } else if (var0.equals(MODIFIED.toString())) {
         return MODIFIED;
      } else if (var0.equals(UP_TO_DATE.toString())) {
         return UP_TO_DATE;
      } else if (var0.equals(NEEDS_CHECKOUT.toString())) {
         return NEEDS_CHECKOUT;
      } else if (var0.equals(NEEDS_MERGE.toString())) {
         return NEEDS_MERGE;
      } else if (var0.equals(NEEDS_PATCH.toString())) {
         return NEEDS_PATCH;
      } else if (var0.equals(HAS_CONFLICTS.toString())) {
         return HAS_CONFLICTS;
      } else if (var0.equals(UNRESOLVED_CONFLICT.toString())) {
         return UNRESOLVED_CONFLICT;
      } else if (var0.equals(UNKNOWN.toString())) {
         return UNKNOWN;
      } else {
         return var0.equals(INVALID.toString()) ? INVALID : null;
      }
   }

   private FileStatus(String var1) {
      this.statusString = var1;
   }

   public String toString() {
      return this.statusString;
   }
}
