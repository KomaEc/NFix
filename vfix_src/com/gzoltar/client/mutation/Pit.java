package com.gzoltar.client.mutation;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.utils.SystemProperties;
import com.gzoltar.shaded.org.apache.commons.io.FileUtils;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.predicate.True;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutant;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Mutater;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.GregorMutationEngine;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.config.DefaultMutationEngineConfiguration;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.config.Mutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.inlinedcode.NoInlinedCodeDetection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Pit extends MutationBackend {
   private Mutater mutater;

   public Pit() {
      Collection var1 = Mutator.defaults();
      DefaultMutationEngineConfiguration var2 = new DefaultMutationEngineConfiguration(True.all(), Collections.emptyList(), var1, new NoInlinedCodeDetection());
      GregorMutationEngine var3 = new GregorMutationEngine(var2);
      this.mutater = var3.createMutator(new ClassPathByteArraySource());
   }

   public int createMutants(List<String> var1) {
      try {
         File var2;
         if ((var2 = new File(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MUTANTS_DIR)).isDirectory() && var2.exists()) {
            FileUtils.deleteDirectory(var2);
         }

         var2.mkdirs();
         Logger.getInstance().debug("Mutants directory " + var2.getAbsolutePath());
         int var3 = 0;
         Iterator var16 = var1.iterator();

         while(var16.hasNext()) {
            String var4 = (String)var16.next();
            int var5 = 1;
            Logger.getInstance().debug("Creating mutants for class " + var4);
            File var6;
            (var6 = new File(var2.getAbsolutePath() + SystemProperties.FILE_SEPARATOR + var4 + ".mutants.log")).createNewFile();
            PrintWriter var18 = new PrintWriter(var6.getAbsolutePath(), "UTF-8");
            File var7;
            (var7 = new File(var2.getAbsolutePath() + SystemProperties.FILE_SEPARATOR + var4)).mkdir();
            Iterator var17 = this.mutater.findMutations(new ClassName(var4)).iterator();

            while(var17.hasNext()) {
               MutationDetails var8 = (MutationDetails)var17.next();
               Mutant var9 = this.mutater.getMutation(var8.getId());
               File var10;
               if ((var10 = new File(var7 + SystemProperties.FILE_SEPARATOR + var5)).exists()) {
                  FileUtils.deleteDirectory(var10);
               }

               var10.mkdir();
               String var11 = var8.getClassName().getPackage().asJavaName();
               File var12;
               (var12 = new File(var10.getAbsolutePath() + SystemProperties.FILE_SEPARATOR + var11.replace(".", SystemProperties.FILE_SEPARATOR))).mkdirs();
               String var13 = var8.getClassName().getNameWithoutPackage().asJavaName();
               String var14 = var8.getMethod().name();
               (var12 = new File(var12.getAbsolutePath() + SystemProperties.FILE_SEPARATOR + var13 + ".class")).createNewFile();
               FileOutputStream var21;
               (var21 = new FileOutputStream(var12)).write(var9.getBytes());
               var21.close();
               com.gzoltar.instrumentation.components.Mutant var19 = new com.gzoltar.instrumentation.components.Mutant(var5, var11, var13, var14, var8.getLineNumber(), var8.getMutator(), var8.getDescription().replace("::", "@"), var10.getAbsolutePath());
               Component var20;
               if ((var20 = Spectra.getInstance().getComponent(var19.getComponentName())) == null) {
                  Logger.getInstance().warn("There is no component for mutant " + var19.toString() + " -> " + var8.toString());
               } else {
                  Logger.getInstance().debug("Adding mutant " + var19.toString() + " to component " + var20.toString());
                  var20.addMutant(var19);
                  var18.println(var5 + ":" + var8.getMutator() + ":" + var8.getClassName().asJavaName() + "@" + var14 + ":" + var8.getLineNumber() + ":" + var8.getDescription().replace("::", "@"));
                  ++var5;
                  ++var3;
               }
            }

            var18.close();
         }

         Logger.getInstance().debug("numberOfMutants: " + var3);
         return var3;
      } catch (IOException var15) {
         Logger.getInstance().err("", var15);
         return 0;
      }
   }
}
