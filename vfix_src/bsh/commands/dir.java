package bsh.commands;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.StringUtil;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

public class dir {
   static final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

   public static String usage() {
      return "usage: dir( String dir )\n       dir()";
   }

   public static void invoke(Interpreter var0, CallStack var1) {
      String var2 = ".";
      invoke(var0, var1, var2);
   }

   public static void invoke(Interpreter var0, CallStack var1, String var2) {
      File var3;
      try {
         var3 = var0.pathToFile(var2);
      } catch (IOException var16) {
         var0.println("error reading path: " + var16);
         return;
      }

      if (var3.exists() && var3.canRead()) {
         if (!var3.isDirectory()) {
            var0.println("'" + var2 + "' is not a directory");
         }

         String[] var4 = var3.list();
         var4 = StringUtil.bubbleSort(var4);

         for(int var5 = 0; var5 < var4.length; ++var5) {
            File var6 = new File(var2 + File.separator + var4[var5]);
            StringBuffer var7 = new StringBuffer();
            var7.append(var6.canRead() ? "r" : "-");
            var7.append(var6.canWrite() ? "w" : "-");
            var7.append("_");
            var7.append(" ");
            Date var8 = new Date(var6.lastModified());
            GregorianCalendar var9 = new GregorianCalendar();
            var9.setTime(var8);
            int var10 = var9.get(5);
            var7.append(months[var9.get(2)] + " " + var10);
            if (var10 < 10) {
               var7.append(" ");
            }

            var7.append(" ");
            byte var11 = 8;
            StringBuffer var12 = new StringBuffer();

            for(int var13 = 0; var13 < var11; ++var13) {
               var12.append(" ");
            }

            var12.insert(0, var6.length());
            var12.setLength(var11);
            int var14 = var12.toString().indexOf(" ");
            if (var14 != -1) {
               String var15 = var12.toString().substring(var14);
               var12.setLength(var14);
               var12.insert(0, var15);
            }

            var7.append(var12.toString());
            var7.append(" " + var6.getName());
            if (var6.isDirectory()) {
               var7.append("/");
            }

            var0.println(var7.toString());
         }

      } else {
         var0.println("Can't read " + var3);
      }
   }
}
