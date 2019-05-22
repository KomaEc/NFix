package org.apache.maven.plugin.surefire.report;

public class FileReporterUtils {
   public static String stripIllegalFilenameChars(String original) {
      String result = original;
      String illegalChars = getOSSpecificIllegalChars();

      for(int i = 0; i < illegalChars.length(); ++i) {
         result = result.replace(illegalChars.charAt(i), '_');
      }

      return result;
   }

   private static String getOSSpecificIllegalChars() {
      return System.getProperty("os.name").toLowerCase().startsWith("win") ? "\\/:*?\"<>|\u0000" : "/\u0000";
   }
}
