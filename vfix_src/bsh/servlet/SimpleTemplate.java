package bsh.servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SimpleTemplate {
   StringBuffer buff;
   static String NO_TEMPLATE = "NO_TEMPLATE";
   static Map templateData = new HashMap();
   static boolean cacheTemplates = true;

   public static SimpleTemplate getTemplate(String var0) {
      String var1 = (String)templateData.get(var0);
      if (var1 != null && cacheTemplates) {
         if (var1.equals(NO_TEMPLATE)) {
            return null;
         }
      } else {
         try {
            FileReader var2 = new FileReader(var0);
            var1 = getStringFromStream((Reader)var2);
            templateData.put(var0, var1);
         } catch (IOException var3) {
            templateData.put(var0, NO_TEMPLATE);
         }
      }

      return var1 == null ? null : new SimpleTemplate(var1);
   }

   public static String getStringFromStream(InputStream var0) throws IOException {
      return getStringFromStream((Reader)(new InputStreamReader(var0)));
   }

   public static String getStringFromStream(Reader var0) throws IOException {
      StringBuffer var1 = new StringBuffer();
      BufferedReader var2 = new BufferedReader(var0);

      String var3;
      while((var3 = var2.readLine()) != null) {
         var1.append(var3 + "\n");
      }

      return var1.toString();
   }

   public SimpleTemplate(String var1) {
      this.init(var1);
   }

   public SimpleTemplate(Reader var1) throws IOException {
      String var2 = getStringFromStream(var1);
      this.init(var2);
   }

   public SimpleTemplate(URL var1) throws IOException {
      String var2 = getStringFromStream(var1.openStream());
      this.init(var2);
   }

   private void init(String var1) {
      this.buff = new StringBuffer(var1);
   }

   public void replace(String var1, String var2) {
      int[] var3;
      while((var3 = this.findTemplate(var1)) != null) {
         this.buff.replace(var3[0], var3[1], var2);
      }

   }

   int[] findTemplate(String var1) {
      String var2 = this.buff.toString();
      int var3 = var2.length();
      int var4 = 0;

      while(var4 < var3) {
         int var5 = var2.indexOf("<!--", var4);
         if (var5 == -1) {
            return null;
         }

         int var6 = var2.indexOf("-->", var5);
         if (var6 == -1) {
            return null;
         }

         var6 += "-->".length();
         int var7 = var2.indexOf("TEMPLATE-", var5);
         if (var7 == -1) {
            var4 = var6;
         } else if (var7 > var6) {
            var4 = var6;
         } else {
            int var8 = var7 + "TEMPLATE-".length();

            int var9;
            for(var9 = var8; var9 < var3; ++var9) {
               char var10 = var2.charAt(var9);
               if (var10 == ' ' || var10 == '\t' || var10 == '-') {
                  break;
               }
            }

            if (var9 >= var3) {
               return null;
            }

            String var11 = var2.substring(var8, var9);
            if (var11.equals(var1)) {
               return new int[]{var5, var6};
            }

            var4 = var6;
         }
      }

      return null;
   }

   public String toString() {
      return this.buff.toString();
   }

   public void write(PrintWriter var1) {
      var1.println(this.toString());
   }

   public void write(PrintStream var1) {
      var1.println(this.toString());
   }

   public static void main(String[] var0) throws IOException {
      String var1 = var0[0];
      String var2 = var0[1];
      String var3 = var0[2];
      FileReader var4 = new FileReader(var1);
      String var5 = getStringFromStream((Reader)var4);
      SimpleTemplate var6 = new SimpleTemplate(var5);
      var6.replace(var2, var3);
      var6.write(System.out);
   }

   public static void setCacheTemplates(boolean var0) {
      cacheTemplates = var0;
   }
}
