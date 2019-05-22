package org.testng.reporters;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

public class Files {
   public static String readFile(File f) throws IOException {
      InputStream is = new FileInputStream(f);
      Throwable var2 = null;

      String var3;
      try {
         var3 = readFile((InputStream)is);
      } catch (Throwable var12) {
         var2 = var12;
         throw var12;
      } finally {
         if (is != null) {
            if (var2 != null) {
               try {
                  is.close();
               } catch (Throwable var11) {
                  var2.addSuppressed(var11);
               }
            } else {
               is.close();
            }
         }

      }

      return var3;
   }

   public static String readFile(InputStream is) throws IOException {
      StringBuilder result = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      for(String line = br.readLine(); line != null; line = br.readLine()) {
         result.append(line + "\n");
      }

      return result.toString();
   }

   public static void writeFile(String string, File f) throws IOException {
      f.getParentFile().mkdirs();
      FileWriter fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(string);
      bw.close();
      fw.close();
   }

   public static void copyFile(InputStream from, File to) throws IOException {
      if (!to.getParentFile().exists()) {
         to.getParentFile().mkdirs();
      }

      OutputStream os = new FileOutputStream(to);
      Throwable var3 = null;

      try {
         byte[] buffer = new byte[65536];

         for(int count = from.read(buffer); count > 0; count = from.read(buffer)) {
            os.write(buffer, 0, count);
         }
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (os != null) {
            if (var3 != null) {
               try {
                  os.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               os.close();
            }
         }

      }

   }

   public static String streamToString(InputStream is) throws IOException {
      if (is != null) {
         Writer writer = new StringWriter();
         char[] buffer = new char[1024];

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int n;
            while((n = reader.read(buffer)) != -1) {
               writer.write(buffer, 0, n);
            }
         } finally {
            is.close();
         }

         return writer.toString();
      } else {
         return "";
      }
   }
}
