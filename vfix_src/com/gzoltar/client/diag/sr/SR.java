package com.gzoltar.client.diag.sr;

import com.gzoltar.client.Properties;
import com.gzoltar.client.diag.strategy.CoverageStrategy;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.utils.SystemProperties;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface SR {
   default void sr(Spectra var1) {
      if (!Properties.PRINT_MATRIX || !(new File(Properties.GZOLTAR_DATA_DIR + SystemProperties.FILE_SEPARATOR + Properties.MATRIX_FILE)).exists()) {
         CoverageStrategy.printMatrix(var1, Properties.GZOLTAR_DATA_DIR, Properties.MATRIX_FILE);
      }

      int[] var10000 = null.a;
      Properties.CANDIDATE_GENERATION_ALGORITHM.ordinal();
      Logger.getInstance().info("* Running Staccato");
      List var2 = (new Staccato()).getFaultyComponents(var1);
      if (!null.a && var2 == null) {
         throw new AssertionError();
      } else {
         Map var7 = this.getRanking(var1);
         if (!null.a && var7 == null) {
            throw new AssertionError();
         } else if (!null.a && var7.size() > var1.getNumberOfComponents()) {
            throw new AssertionError();
         } else {
            Integer var3 = 1;

            for(Iterator var6 = var1.getComponentsSortedByLineNumber().iterator(); var6.hasNext(); var3 = var3 + 1) {
               ComponentCount var4 = (ComponentCount)var6.next();
               Double var5 = 0.0D;
               if (var7.containsKey(var3)) {
                  var5 = (Double)var7.get(var3);
               }

               var4.getComponent().setSuspiciousnessValue(this.getClass().getSimpleName(), var5);
            }

         }
      }
   }

   Map<Integer, Double> getRanking(Spectra var1);

   static {
      boolean var10000 = null.a;
   }
}
