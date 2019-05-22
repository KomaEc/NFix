package org.jf.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUtil {
   public static int getConsoleWidth() {
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
         try {
            return attemptMode();
         } catch (Exception var1) {
         }
      } else {
         try {
            return attemptStty();
         } catch (Exception var2) {
         }
      }

      return 80;
   }

   private static int attemptStty() {
      String output = attemptCommand(new String[]{"sh", "-c", "stty size < /dev/tty"});
      if (output == null) {
         return 80;
      } else {
         String[] vals = output.split(" ");
         return vals.length < 2 ? 80 : Integer.parseInt(vals[1]);
      }
   }

   private static int attemptMode() {
      String output = attemptCommand(new String[]{"mode", "con"});
      if (output == null) {
         return 80;
      } else {
         Pattern pattern = Pattern.compile("Columns:[ \t]*(\\d+)");
         Matcher m = pattern.matcher(output);
         return !m.find() ? 80 : Integer.parseInt(m.group(1));
      }
   }

   private static String attemptCommand(String[] command) {
      StringBuffer buffer = null;

      try {
         Process p = Runtime.getRuntime().exec(command);

         String line;
         for(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); (line = reader.readLine()) != null; buffer.append(line)) {
            if (buffer == null) {
               buffer = new StringBuffer();
            }
         }

         if (buffer != null) {
            return buffer.toString();
         } else {
            return null;
         }
      } catch (Exception var5) {
         return null;
      }
   }
}
