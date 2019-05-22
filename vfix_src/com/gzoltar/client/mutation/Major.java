package com.gzoltar.client.mutation;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.shaded.org.apache.commons.io.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Major extends MutationBackend {
   public int createMutants(List<String> var1) {
      try {
         File var2;
         if ((var2 = new File(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MUTANTS_DIR)).isDirectory() && var2.exists()) {
            FileUtils.deleteDirectory(var2);
         }

         var2.mkdirs();
         Logger.getInstance().debug("Mutants directory " + var2.getAbsolutePath());
         int var3 = 0;
         Iterator var14 = var1.iterator();

         while(true) {
            while(true) {
               String var4;
               File var5;
               int var9;
               String var11;
               while(true) {
                  do {
                     if (!var14.hasNext()) {
                        Logger.getInstance().debug("numberOfMutants: " + var3);
                        return var3;
                     }
                  } while((var4 = (String)var14.next()).contains("$"));

                  Logger.getInstance().debug("Creating mutants for class " + var4);
                  (var5 = new File(var2.getAbsolutePath() + SystemProperties.FILE_SEPARATOR + var4)).mkdir();
                  ArrayList var6;
                  (var6 = new ArrayList()).add(Properties.MAJOR_JAVAC);
                  var6.add("-cp");
                  StringBuilder var7;
                  (var7 = new StringBuilder()).append("." + SystemProperties.PATH_SEPARATOR + Properties.CLASSESDIR);
                  if (Properties.DEPS != null) {
                     String[] var8;
                     var9 = (var8 = Properties.DEPS).length;

                     for(int var10 = 0; var10 < var9; ++var10) {
                        var11 = var8[var10];
                        var7.append(SystemProperties.PATH_SEPARATOR);
                        var7.append(var11);
                     }
                  }

                  var6.add(var7.toString().replace(":", SystemProperties.PATH_SEPARATOR));
                  var6.add("-d");
                  var6.add(var5.getAbsolutePath());
                  var6.add("-XMutator:ALL");
                  var6.add(var4.replace(".", SystemProperties.FILE_SEPARATOR) + ".java");
                  ProcessBuilder var19;
                  (var19 = new ProcessBuilder(var6)).directory(new File(Properties.SOURCEDIR));
                  var19.redirectErrorStream(true);

                  try {
                     var9 = var19.start().waitFor();
                     break;
                  } catch (InterruptedException | IOException var12) {
                     Logger.getInstance().err("Mutation of class " + var4 + " failed", var12);
                  }
               }

               if (var9 != 0) {
                  Logger.getInstance().warn("Mutation of class " + var4 + " failed");
               } else {
                  BufferedReader var22 = new BufferedReader(new FileReader(Properties.SOURCEDIR + SystemProperties.FILE_SEPARATOR + Properties.MUTANTS_LOG));

                  while((var11 = var22.readLine()) != null) {
                     Logger.getInstance().debug(var11);
                     String[] var15 = var11.split(":");

                     assert var15.length == 7;

                     String var17;
                     if ((var17 = var15[4]).contains("@")) {
                        var17 = var17.substring(0, var17.indexOf("@"));
                     }

                     String var18 = "";
                     if (var17.contains(".")) {
                        var18 = var17.substring(0, var17.lastIndexOf("."));
                     }

                     String var21 = var17;
                     if (var18 != "") {
                        var21 = var17.substring(var17.lastIndexOf(".") + 1, var17.length());
                     }

                     var17 = "";
                     if (var15[4].indexOf("@") != -1) {
                        var17 = var15[4].substring(var15[4].indexOf("@") + 1, var15[4].length());
                     }

                     Mutant var16 = new Mutant(Integer.valueOf(var15[0]), var18, var21, var17, Integer.valueOf(var15[5]), var15[1], var15[6], var5.getAbsolutePath());
                     Component var20;
                     if ((var20 = Spectra.getInstance().getComponent(var16.getComponentName())) == null) {
                        Logger.getInstance().warn("There is no component for mutant " + var16.toString());
                     } else {
                        Logger.getInstance().debug("Adding mutant " + var16.toString() + " to component " + var20.toString());
                        var20.addMutant(var16);
                        ++var3;
                     }
                  }

                  var22.close();
               }
            }
         }
      } catch (NumberFormatException | IOException var13) {
         Logger.getInstance().err("", var13);
         return 0;
      }
   }
}
