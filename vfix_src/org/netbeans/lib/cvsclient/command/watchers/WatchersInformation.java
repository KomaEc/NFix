package org.netbeans.lib.cvsclient.command.watchers;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;
import org.netbeans.lib.cvsclient.util.BugLog;

public class WatchersInformation extends FileInfoContainer {
   public static final String WATCH_EDIT = "edit";
   public static final String WATCH_UNEDIT = "unedit";
   public static final String WATCH_COMMIT = "commit";
   public static final String WATCH_TEMP_EDIT = "tedit";
   public static final String WATCH_TEMP_UNEDIT = "tunedit";
   public static final String WATCH_TEMP_COMMIT = "tcommit";
   private final File file;
   private final List userList = new LinkedList();

   public WatchersInformation(File var1) {
      this.file = var1;
   }

   public File getFile() {
      return this.file;
   }

   void addWatcher(String var1) {
      String var2 = var1.trim();
      var2 = var2.replace('\t', ' ');
      int var3 = var2.indexOf(32);
      if (var3 >= 0) {
         String var4 = var2.substring(0, var3);
         String var5 = var2.substring(var3 + 1);
         this.userList.add(new WatchersInformation.Watcher(var4, var5));
      }

   }

   public Iterator getWatchersIterator() {
      return this.userList.iterator();
   }

   public static class Watcher {
      private final String userName;
      private final String watches;
      private boolean watchingEdit;
      private boolean watchingUnedit;
      private boolean watchingCommit;
      private boolean temporaryEdit;
      private boolean temporaryUnedit;
      private boolean temporaryCommit;

      Watcher(String var1, String var2) {
         this.userName = var1;
         this.watches = var2;
         StringTokenizer var3 = new StringTokenizer(var2, " ", false);

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            if ("edit".equals(var4)) {
               this.watchingEdit = true;
            } else if ("unedit".equals(var4)) {
               this.watchingUnedit = true;
            } else if ("commit".equals(var4)) {
               this.watchingCommit = true;
            } else if ("tcommit".equals(var4)) {
               this.temporaryCommit = true;
            } else if ("tedit".equals(var4)) {
               this.temporaryEdit = true;
            } else if ("tunedit".equals(var4)) {
               this.temporaryUnedit = true;
            } else {
               BugLog.getInstance().bug("unknown = " + var4);
            }
         }

      }

      public String getUserName() {
         return this.userName;
      }

      public String getWatches() {
         return this.watches;
      }

      public boolean isWatchingCommit() {
         return this.watchingCommit;
      }

      public boolean isWatchingEdit() {
         return this.watchingEdit;
      }

      public boolean isWatchingUnedit() {
         return this.watchingUnedit;
      }

      public boolean isTempWatchingCommit() {
         return this.temporaryCommit;
      }

      public boolean isTempWatchingEdit() {
         return this.temporaryEdit;
      }

      public boolean isTempWatchingUnedit() {
         return this.temporaryUnedit;
      }
   }
}
