package org.netbeans.lib.cvsclient.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.util.SimpleStringPattern;

public class WrapperUtils {
   private static void parseWrappers(BufferedReader var0, Map var1) throws IOException {
      label25:
      while(true) {
         String var2;
         if ((var2 = var0.readLine()) != null) {
            StringTokenizer var3 = new StringTokenizer(var2);
            SimpleStringPattern var4 = new SimpleStringPattern(var3.nextToken());

            while(true) {
               if (!var3.hasMoreTokens()) {
                  continue label25;
               }

               String var5 = var3.nextToken();
               String var6 = var3.nextToken();
               if (var5.equals("-k")) {
                  int var7 = var6.indexOf(39);
                  int var8 = var6.lastIndexOf(39);
                  if (var7 >= 0 && var8 >= 0) {
                     var6 = var6.substring(var7 + 1, var8);
                  }

                  KeywordSubstitutionOptions var9 = KeywordSubstitutionOptions.findKeywordSubstOption(var6);
                  if (!var1.containsKey(var4)) {
                     var1.put(var4, var9);
                  }
               }
            }
         }

         return;
      }
   }

   public static void readWrappersFromFile(File var0, Map var1) throws IOException, FileNotFoundException {
      parseWrappers(new BufferedReader(new FileReader(var0)), var1);
   }

   public static void readWrappersFromProperty(String var0, Map var1) throws IOException {
      String var2 = System.getProperty(var0);
      if (var2 != null) {
         parseWrappers(new BufferedReader(new StringReader(var2)), var1);
      }

   }

   public static Map mergeWrapperMap(ClientServices var0) throws CommandException {
      String var1 = null;
      HashMap var2 = new HashMap(var0.getWrappersMap());

      try {
         File var3 = new File(System.getProperty("user.home"));
         File var8 = new File(var3, "./cvswrappers");
         var1 = CommandException.getLocalMessage("WrapperUtils.clientDotWrapper.text");
         if (var8.exists()) {
            readWrappersFromFile(var8, var2);
         }

         var1 = CommandException.getLocalMessage("WrapperUtils.environmentWrapper.text");
         readWrappersFromProperty("Env-CVSWRAPPERS", var2);
      } catch (FileNotFoundException var6) {
      } catch (Exception var7) {
         Object[] var4 = new Object[]{var1};
         String var5 = CommandException.getLocalMessage("WrapperUtils.wrapperError.text", var4);
         throw new CommandException(var7, var5);
      }

      return var2;
   }
}
