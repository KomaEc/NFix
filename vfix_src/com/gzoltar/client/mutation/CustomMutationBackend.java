package com.gzoltar.client.mutation;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.Mutant;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CustomMutationBackend extends MutationBackend {
   public int createMutants(List<String> var1) {
      int var6 = 0;

      try {
         BufferedReader var2 = new BufferedReader(new FileReader(Properties.MUTANTS_LOG));

         String var3;
         while((var3 = var2.readLine()) != null) {
            Logger.getInstance().debug(var3);
            String[] var7 = var3.split(",");

            assert var7.length == 5;

            Mutant var8 = new Mutant(Integer.valueOf(var7[0]), var7[1], var7[2], var7[3], Integer.valueOf(var7[4]), "", "", "");
            Component var4;
            if ((var4 = Spectra.getInstance().getComponent(var8.getComponentName())) == null) {
               Logger.getInstance().warn("There is no component for mutant " + var8.toString());
            } else {
               Logger.getInstance().debug("Adding mutant " + var8.toString() + " to component " + var4.toString());
               var4.addMutant(var8);
               ++var6;
            }
         }

         var2.close();
      } catch (IOException var5) {
         Logger.getInstance().err(var5.getMessage(), var5);
      }

      return var6;
   }
}
