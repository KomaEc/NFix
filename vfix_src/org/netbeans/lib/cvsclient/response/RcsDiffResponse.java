package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.event.FileUpdatedEvent;
import org.netbeans.lib.cvsclient.file.FileHandler;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class RcsDiffResponse implements Response {
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
            while(var5 > 0) {
               var5 = (int)((long)var5 - var1.skip((long)var5));
            }

         } else {
            this.localFile = var7.getAbsolutePath();
            Entry var8 = new Entry(this.entryLine);
            FileHandler var9 = var4 ? var2.getGzipFileHandler() : var2.getUncompressedFileHandler();
            var9.setNextFileDate(var2.getNextFileDate());
            if (!var8.isBinary()) {
               var9.writeRcsDiffFile(var6, this.mode, var1, var5);
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
            var2.updateAdminData(this.localPath, this.repositoryPath, var8);
            FileUpdatedEvent var13 = new FileUpdatedEvent(this, var6);
            var2.getEventManager().fireCVSEvent(var13);
         }
      } catch (IOException var12) {
         throw new ResponseException(var12);
      }
   }

   protected String getEntryConflict(Date var1, boolean var2) {
      return this.getDateFormatter().format(var1);
   }

   public boolean isTerminalResponse() {
      return false;
   }

   protected DateFormat getDateFormatter() {
      if (this.dateFormatter == null) {
         this.dateFormatter = new SimpleDateFormat(Entry.getLastModifiedDateFormatter().toPattern(), Locale.US);
         this.dateFormatter.setTimeZone(Entry.getTimeZone());
      }

      return this.dateFormatter;
   }
}
