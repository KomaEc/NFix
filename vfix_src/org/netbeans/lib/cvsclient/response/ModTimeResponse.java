package org.netbeans.lib.cvsclient.response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class ModTimeResponse implements Response {
   protected static final SimpleDateFormat dateFormatter;
   protected static final String SERVER_DATE_FORMAT = "dd MMM yyyy HH:mm:ss";

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         Date var4 = dateFormatter.parse(var3.substring(0, var3.length() - 6));
         if (var4.getTime() < 0L) {
            if (var4.getYear() < 100 && var4.getYear() >= 70) {
               var4.setYear(var4.getYear() + 1900);
            } else if (var4.getYear() >= 0 && var4.getYear() < 70) {
               var4.setYear(var4.getYear() + 2000);
            } else {
               var4.setYear(2000 + var4.getYear());
            }
         }

         var2.setNextFileDate(var4);
      } catch (Exception var5) {
         throw new ResponseException(var5);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }

   static {
      dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
      dateFormatter.setTimeZone(Entry.getTimeZone());
   }
}
