package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public final class PitError extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public PitError(String message, Throwable cause) {
      super(message + info(), cause);
   }

   public PitError(String message) {
      super(message + info());
   }

   private static String info() {
      RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
      return "\n\nPlease copy and paste the information and the complete stacktrace below when reporting an issue\nVM : " + rt.getVmName() + "\n" + "Vendor : " + rt.getVmVendor() + "\n" + "Version : " + rt.getVmVersion() + "\n" + "Uptime : " + rt.getUptime() + "\n" + "Input -> " + createInputString(rt.getInputArguments()) + "\n" + "BootClassPathSupported : " + rt.isBootClassPathSupported() + "\n";
   }

   private static String createInputString(List<String> inputArguments) {
      StringBuilder sb = new StringBuilder();
      FCollection.fold(append(), sb, inputArguments);
      return sb.toString();
   }

   private static F2<StringBuilder, String, StringBuilder> append() {
      return new F2<StringBuilder, String, StringBuilder>() {
         private int position = 0;

         public StringBuilder apply(StringBuilder a, String b) {
            ++this.position;
            return a.append("\n " + this.position + " : " + b);
         }
      };
   }
}
