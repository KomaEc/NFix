package com.gzoltar.instrumentation.runtime;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.testing.launch.ExecutionParameters;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.shaded.org.jacoco.core.analysis.Analyzer;
import com.gzoltar.shaded.org.jacoco.core.analysis.CoverageBuilder;
import com.gzoltar.shaded.org.jacoco.core.analysis.IClassCoverage;
import com.gzoltar.shaded.org.jacoco.core.analysis.IMethodCoverage;
import com.gzoltar.shaded.org.jacoco.core.tools.ExecFileLoader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JaCoCoWrapper {
   public static ExecutionParameters executionParameters = null;
   // $FF: synthetic field
   static final boolean $assertionsDisabled = !JaCoCoWrapper.class.desiredAssertionStatus();

   public static Map<String, ComponentCount> registerCoverage(byte[] var0, boolean var1) {
      HashMap var2 = new HashMap();
      if (var0.length == 0) {
         Logger.getInstance().debug("0 coverage");
         return var2;
      } else {
         try {
            ExecFileLoader var3 = new ExecFileLoader();
            if (var0.length != 1) {
               var3.load((InputStream)(new ByteArrayInputStream(var0)));
            }

            CoverageBuilder var14 = new CoverageBuilder();
            Analyzer var16 = new Analyzer(var3.getExecutionDataStore(), var14);
            Logger.getInstance().debug("Instrumenting classes under test");
            String[] var4;
            int var5 = (var4 = executionParameters.getTargetClasses()).length;

            int var6;
            String var7;
            String[] var8;
            int var9;
            int var10;
            String var11;
            File var12;
            for(var6 = 0; var6 < var5; ++var6) {
               var7 = var4[var6];
               Logger.getInstance().debug("Instrumenting '" + var7 + "' class");
               var9 = (var8 = executionParameters.getClassPath().split(SystemProperties.PATH_SEPARATOR)).length;

               for(var10 = 0; var10 < var9; ++var10) {
                  var11 = var8[var10];
                  if ((var12 = new File(var11 + SystemProperties.FILE_SEPARATOR + var7.replace(".", SystemProperties.FILE_SEPARATOR) + ".class")).exists()) {
                     Logger.getInstance().debug("  from '" + var12.getAbsolutePath() + "'");
                     var16.analyzeAll(var12);
                     break;
                  }
               }
            }

            if (executionParameters.isInstrumentTestClasses()) {
               var5 = (var4 = executionParameters.getTestClasses()).length;

               for(var6 = 0; var6 < var5; ++var6) {
                  var7 = var4[var6];
                  Logger.getInstance().debug("Instrumenting '" + var7 + "' class");
                  var9 = (var8 = executionParameters.getClassPath().split(SystemProperties.PATH_SEPARATOR)).length;

                  for(var10 = 0; var10 < var9; ++var10) {
                     var11 = var8[var10];
                     if ((var12 = new File(var11 + SystemProperties.FILE_SEPARATOR + var7.replace(".", SystemProperties.FILE_SEPARATOR) + ".class")).exists()) {
                        Logger.getInstance().debug("  from '" + var12.getAbsolutePath() + "'");
                        var16.analyzeAll(var12);
                        break;
                     }
                  }
               }
            }

            Logger.getInstance().debug("Collecting coverage");
            HashMap var17 = new HashMap();
            Iterator var18 = var14.getClasses().iterator();

            label133:
            while(var18.hasNext()) {
               IClassCoverage var19 = (IClassCoverage)var18.next();
               StringBuilder var20;
               (var20 = new StringBuilder()).append(var19.getPackageName() == null ? "" : var19.getPackageName().replace("/", "."));
               var20.append("[");
               var20.append(var19.getSourceFileName());
               Logger.getInstance().debug("PACKAGE: " + (var19.getPackageName() == null ? "<null>" : var19.getPackageName().replace("/", ".")));
               Logger.getInstance().debug("  FILE: " + var19.getSourceFileName());
               Logger.getInstance().debug("    CLASS: " + var19.getName().replace("/", ".") + " (isNoMatch? " + var19.isNoMatch() + ")");
               Iterator var21 = var19.getMethods().iterator();

               while(true) {
                  while(true) {
                     if (!var21.hasNext()) {
                        continue label133;
                     }

                     IMethodCoverage var22 = (IMethodCoverage)var21.next();
                     Logger.getInstance().debug("      METHOD: " + var22.getName() + var22.getDesc());
                     if (executionParameters.getGranularity() == Component.Granularity.CLASS && !var22.getName().equals("<init>") && !var22.getName().equals("<clinit>")) {
                        Logger.getInstance().debug("Skipping method as the granularity selected is 'CLASS'");
                     } else if (var19.getSignature() != null && var19.getSignature().equals("Ljava/lang/Enum<L" + var19.getName() + ";>;") && (var22.getName().equals("values") || var22.getName().equals("valueOf"))) {
                        Logger.getInstance().debug("Skipping method '" + var22.getName() + "' as (likely) it only exists in bytecode");
                     } else {
                        for(var10 = var22.getFirstLine(); var10 <= var22.getLastLine(); ++var10) {
                           Logger.getInstance().debug("        LINE: " + var10);
                           if (exists(var22.getLine(var10).getStatus())) {
                              if (isAnonymousClass(var19.getName()) && var22.getName().equals("<init>") && var10 == 1) {
                                 Logger.getInstance().debug("Skipping the first line (#1) of the constructor of an anonymous class");
                              } else {
                                 StringBuilder var23;
                                 (var23 = new StringBuilder(var20)).append(var20.toString());
                                 var23.append("#");
                                 var23.append(var10);
                                 StringBuilder var24;
                                 if (executionParameters.uniqueLineNumbers() && var17.containsKey(var23.toString())) {
                                    var24 = new StringBuilder((String)var17.get(var23.toString()));
                                 } else {
                                    (var24 = new StringBuilder(var20)).append("<");
                                    var24.append(var19.getName().replace("/", "."));
                                    if (executionParameters.getGranularity() == Component.Granularity.METHOD || executionParameters.getGranularity() == Component.Granularity.STATEMENT) {
                                       var24.append("{");
                                       var24.append(var22.getName() + var22.getDesc());
                                    }

                                    var24.append("#");
                                    var24.append(var10);
                                    var17.put(var23.toString(), var24.toString());
                                 }

                                 if (!var1 && !hasBeenCovered(var22.getLine(var10).getStatus())) {
                                    Logger.getInstance().debug("          HAS NOT BEEN COVERED");
                                 } else {
                                    Logger.getInstance().debug("          HAS BEEN COVERED");
                                    if (var1) {
                                       ComponentCount var15 = new ComponentCount(new Component(var24.toString(), var10), 0);
                                       var2.put(var24.toString(), var15);
                                    } else {
                                       var2.put(var24.toString(), (Object)null);
                                    }
                                 }

                                 if (executionParameters.getGranularity() != Component.Granularity.STATEMENT) {
                                    break;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (IOException var13) {
            Logger.getInstance().err(var13.getMessage(), var13);
         }

         return var2;
      }
   }

   private static boolean hasBeenCovered(int var0) {
      switch(var0) {
      case 2:
      case 3:
         return true;
      default:
         return false;
      }
   }

   private static boolean exists(int var0) {
      switch(var0) {
      case 1:
      case 2:
      case 3:
         return true;
      default:
         return false;
      }
   }

   private static boolean isAnonymousClass(String var0) {
      int var1;
      return (var1 = var0.lastIndexOf(36)) < 0 ? false : Character.isDigit(var0.charAt(var1 + 1));
   }
}
