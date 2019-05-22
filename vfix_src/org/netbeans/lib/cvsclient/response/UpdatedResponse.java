package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.DefaultFileInfoContainer;
import org.netbeans.lib.cvsclient.event.FileAddedEvent;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.event.FileUpdatedEvent;
import org.netbeans.lib.cvsclient.file.FileHandler;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class UpdatedResponse implements Response {
   private static final boolean DEBUG = false;
   private String localPath;
   private String repositoryPath;
   private String entryLine;
   private String mode;
   protected String localFile;
   private DateFormat dateFormatter;

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         this.localPath = var1.readLine();
         this.repositoryPath = var1.readLine();
         this.entryLine = var1.readLine();
         this.mode = var1.readLine();
         String var3 = var1.readLine();
         boolean var4 = var3.charAt(0) == 'z';
         int var5 = Integer.parseInt(var4 ? var3.substring(1) : var3);
         String var6 = var2.convertPathname(this.localPath, this.repositoryPath);
         File var7 = new File(var6);
         if (var2.getGlobalOptions().isExcluded(var7)) {
            this.skip(var1, var5);
         } else if (this instanceof CreatedResponse && var7.exists()) {
            this.skip(var1, var5);
            DefaultFileInfoContainer var13 = new DefaultFileInfoContainer();
            var13.setType("C");
            var13.setFile(var7);
            var2.getEventManager().fireCVSEvent(new FileInfoEvent(this, var13));
         } else {
            this.localFile = var7.getAbsolutePath();
            Entry var8 = new Entry(this.entryLine);
            FileHandler var9 = var4 ? var2.getGzipFileHandler() : var2.getUncompressedFileHandler();
            var9.setNextFileDate(var2.getNextFileDate());
            if (var8.isBinary()) {
               var9.writeBinaryFile(var6, this.mode, var1, var5);
            } else {
               var9.writeTextFile(var6, this.mode, var1, var5);
            }

            String var10 = null;
            Date var11;
            if (var8.getConflict() != null && var8.getConflict().charAt(0) == '+') {
               if (var8.getConflict().charAt(1) == '=') {
                  var11 = new Date(var7.lastModified());
                  var10 = this.getEntryConflict(var11, true);
               } else {
                  var10 = var8.getConflict().substring(1);
               }
            } else {
               var11 = new Date(var7.lastModified());
               var10 = this.getEntryConflict(var11, false);
            }

            var8.setConflict(var10);
            if (var8.isNewUserFile()) {
               var8.setConflict("dummy timestamp");
            }

            var2.updateAdminData(this.localPath, this.repositoryPath, var8);
            if (var7.exists()) {
               FileAddedEvent var14 = new FileAddedEvent(this, var6);
               var2.getEventManager().fireCVSEvent(var14);
            } else {
               FileUpdatedEvent var15 = new FileUpdatedEvent(this, var6);
               var2.getEventManager().fireCVSEvent(var15);
            }

         }
      } catch (IOException var12) {
         throw new ResponseException(var12);
      }
   }

   private void skip(LoggedDataInputStream var1, int var2) throws IOException {
      while(var2 > 0) {
         var2 = (int)((long)var2 - var1.skip((long)var2));
      }

   }

   protected String getEntryConflict(Date var1, boolean var2) {
      return this.getDateFormatter().format(var1);
   }

   protected DateFormat getDateFormatter() {
      if (this.dateFormatter == null) {
         this.dateFormatter = new SimpleDateFormat(Entry.getLastModifiedDateFormatter().toPattern(), Locale.US);
         this.dateFormatter.setTimeZone(Entry.getTimeZone());
      }

      return this.dateFormatter;
   }

   public boolean isTerminalResponse() {
      return false;
   }
}
