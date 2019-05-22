package org.netbeans.lib.cvsclient.response;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class CheckedInResponse implements Response {
   private DateFormat dateFormatter;

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         String var4 = var1.readLine();
         String var5 = var1.readLine();
         File var6 = new File(var2.convertPathname(var3, var4));
         Date var7 = new Date(var6.lastModified());
         Entry var8 = new Entry(var5);
         var8.setConflict(this.getDateFormatter().format(var7));
         if (var8.isNewUserFile() || var8.isUserFileToBeRemoved()) {
            var8.setConflict("dummy timestamp");
         }

         var2.setEntry(var6, var8);
      } catch (IOException var9) {
         throw new ResponseException((Exception)var9.fillInStackTrace(), var9.getLocalizedMessage());
      }
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
