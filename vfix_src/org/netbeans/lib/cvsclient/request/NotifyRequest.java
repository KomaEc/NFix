package org.netbeans.lib.cvsclient.request;

import java.io.File;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotifyRequest extends Request {
   private static final DateFormat DATE_FORMAT;
   private static final String HOST_NAME;
   private final String request;

   public NotifyRequest(File var1, String var2, String var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("File must not be null!");
      } else {
         StringBuffer var4 = new StringBuffer();
         var4.append("Notify ");
         var4.append(var1.getName());
         var4.append('\n');
         var4.append(var2);
         var4.append('\t');
         var4.append(DATE_FORMAT.format(new Date()));
         var4.append('\t');
         var4.append(HOST_NAME);
         var4.append('\t');
         var4.append(var1.getParent());
         var4.append('\t');
         var4.append(var3);
         var4.append('\n');
         this.request = var4.toString();
      }
   }

   public String getRequestString() {
      return this.request;
   }

   public boolean isResponseExpected() {
      return false;
   }

   static {
      DATE_FORMAT = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy z", Locale.US);
      String var0 = "";

      try {
         var0 = InetAddress.getLocalHost().getHostName();
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      HOST_NAME = var0;
   }
}
