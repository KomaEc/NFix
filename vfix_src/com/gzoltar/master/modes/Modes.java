package com.gzoltar.master.modes;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;

public abstract class Modes {
   protected static void validateClasspathElements() {
      String[] var0;
      int var1;
      int var2;
      String var3;
      if (Properties.PROJECT_CP == null) {
         if (Properties.CLASSESDIR == null) {
            Logger.getInstance().err("'classesDir' has to be defined", new Exception());
         }

         if (!(new File(Properties.CLASSESDIR)).exists()) {
            Logger.getInstance().err("'classesDir' " + Properties.CLASSESDIR + " does not exist", new Exception());
         }

         if (Properties.DEPS != null) {
            var1 = (var0 = Properties.DEPS).length;

            for(var2 = 0; var2 < var1; ++var2) {
               var3 = var0[var2];
               if (!(new File(var3)).exists()) {
                  Logger.getInstance().err(var3 + " does not exist", new Exception());
               }
            }
         }
      } else {
         var1 = (var0 = Properties.PROJECT_CP.split(SystemProperties.PATH_SEPARATOR)).length;

         for(var2 = 0; var2 < var1; ++var2) {
            var3 = var0[var2];
            if (!(new File(var3)).exists()) {
               Logger.getInstance().err(var3 + " does not exist", new Exception());
            }
         }
      }

      if (Properties.NATIVE_LIBRARIES != null) {
         var1 = (var0 = Properties.NATIVE_LIBRARIES).length;

         for(var2 = 0; var2 < var1; ++var2) {
            var3 = var0[var2];
            if (!(new File(var3)).exists()) {
               Logger.getInstance().err(var3 + " does not exist", new Exception());
            }
         }
      }

   }
}
