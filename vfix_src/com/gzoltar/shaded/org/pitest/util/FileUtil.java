package com.gzoltar.shaded.org.pitest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {
   public static String readToString(InputStream is) throws IOException {
      StringBuilder fileData = new StringBuilder(1000);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      char[] buf = new char[1024];

      int numRead;
      for(boolean var4 = false; (numRead = reader.read(buf)) != -1; buf = new char[1024]) {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
      }

      reader.close();
      return fileData.toString();
   }

   public static String randomFilename() {
      return System.currentTimeMillis() + ("" + Math.random()).replaceAll("\\.", "");
   }
}
