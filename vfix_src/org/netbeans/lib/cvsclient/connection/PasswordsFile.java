package org.netbeans.lib.cvsclient.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.netbeans.lib.cvsclient.file.FileUtils;

public final class PasswordsFile {
   public static String findPassword(String var0) {
      File var1 = new File(System.getProperty("cvs.passfile", System.getProperty("user.home") + "/.cvspass"));
      BufferedReader var2 = null;
      String var3 = null;

      Object var5;
      try {
         var2 = new BufferedReader(new FileReader(var1));

         String var4;
         do {
            if ((var4 = var2.readLine()) == null) {
               return var3;
            }

            var4 = normalize(var4);
         } while(!var4.startsWith(var0 + " "));

         var3 = var4.substring(var0.length() + 1);
         return var3;
      } catch (IOException var15) {
         var5 = null;
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var14) {
            }
         }

      }

      return (String)var5;
   }

   public static Collection listRoots(String var0) {
      ArrayList var1 = new ArrayList();
      File var2 = new File(System.getProperty("cvs.passfile", System.getProperty("user.home") + "/.cvspass"));
      BufferedReader var3 = null;

      Set var5;
      try {
         var3 = new BufferedReader(new FileReader(var2));

         String var4;
         while((var4 = var3.readLine()) != null) {
            var4 = normalize(var4);
            String[] var17 = var4.split(" ");
            if (var17[0].startsWith(var0)) {
               var1.add(var17[0]);
            }
         }

         return var1;
      } catch (IOException var15) {
         var5 = Collections.EMPTY_SET;
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var14) {
            }
         }

      }

      return var5;
   }

   public static void storePassword(String var0, String var1) throws IOException {
      File var2 = new File(System.getProperty("cvs.passfile", System.getProperty("user.home") + File.separatorChar + ".cvspass"));
      BufferedWriter var3 = null;
      BufferedReader var4 = null;

      try {
         String var5 = System.getProperty("line.separator");
         if (var2.createNewFile()) {
            var3 = new BufferedWriter(new FileWriter(var2));
            var3.write(var0 + " " + var1 + var5);
            var3.close();
         } else {
            File var6 = File.createTempFile("cvs", "tmp");
            var4 = new BufferedReader(new FileReader(var2));
            var3 = new BufferedWriter(new FileWriter(var6));
            boolean var7 = false;

            String var8;
            while((var8 = var4.readLine()) != null) {
               if (normalize(var8).startsWith(var0 + " ")) {
                  if (!var7) {
                     var3.write(var0 + " " + var1 + var5);
                     var7 = true;
                  }
               } else {
                  var3.write(var8 + var5);
               }
            }

            if (!var7) {
               var3.write(var0 + " " + var1 + var5);
            }

            var4.close();
            var3.flush();
            var3.close();
            FileUtils.copyFile(var6, var2);
            var6.delete();
         }
      } finally {
         try {
            if (var3 != null) {
               var3.close();
            }

            if (var4 != null) {
               var4.close();
            }
         } catch (Exception var15) {
         }

      }

   }

   private static String normalize(String var0) {
      if (var0.startsWith("/1 ")) {
         var0 = var0.substring("/1 ".length());
      }

      return var0;
   }
}
