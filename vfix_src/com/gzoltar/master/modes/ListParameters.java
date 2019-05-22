package com.gzoltar.master.modes;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class ListParameters {
   public static final String NAME = "listParameters";

   public static Option getOption() {
      return new Option("listParameters", "list all the parameters that can be set with -D");
   }

   public static Object execute() {
      ArrayList var0 = new ArrayList();
      Field[] var1;
      int var2 = (var1 = Properties.class.getFields()).length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Field var4;
         if ((var4 = var1[var3]).isAnnotationPresent(Properties.Parameter.class)) {
            Properties.Parameter var5;
            String var6 = (var5 = (Properties.Parameter)var4.getAnnotation(Properties.Parameter.class)).description();
            Class var7;
            if ((var7 = var4.getType()).isEnum()) {
               var6 = var6 + " (Values: " + Arrays.toString(var7.getEnumConstants()) + ")";
            }

            String var11;
            try {
               Object var12;
               if ((var12 = var4.get((Object)null)) == null) {
                  var11 = "";
               } else if (var7.isArray()) {
                  var11 = Arrays.toString((Object[])var12);
               } else {
                  var11 = var12.toString();
               }
            } catch (Exception var8) {
               var11 = "";
            }

            var0.add(new b(var5.key(), var7.getSimpleName(), var6, var11));
         }
      }

      Collections.sort(var0);
      Logger.getInstance().info("Name \t Type \t Default \t Description");
      Iterator var9 = var0.iterator();

      while(var9.hasNext()) {
         b var10 = (b)var9.next();
         Logger.getInstance().info(var10.a + " \t " + var10.b + " \t " + var10.d + " \t " + var10.c);
      }

      return null;
   }
}
