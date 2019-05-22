package com.gzoltar.client.minimize.strategy;

import com.gzoltar.client.minimize.MinionWrapper;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CSPStrategy {
   public List<Set<String>> perform(Spectra var1) {
      ArrayList var2 = new ArrayList();

      try {
         MinionWrapper var3;
         if (!(var3 = new MinionWrapper()).createMinionInputFile(var1)) {
            Logger.getInstance().err("Creation of input to MINION failed");
            return null;
         } else if (!var3.run()) {
            Logger.getInstance().err("Execution of MINION failed");
            return null;
         } else {
            TestResult[] var8 = (TestResult[])var1.getTestResults().toArray(new TestResult[var1.getNumberOfTestResults()]);
            Iterator var9 = var3.getMinionOutput().iterator();

            while(var9.hasNext()) {
               Set var4 = (Set)var9.next();
               HashSet var5 = new HashSet();
               Iterator var10 = var4.iterator();

               while(var10.hasNext()) {
                  Integer var6 = (Integer)var10.next();
                  var5.add(var8[var6].getName());
               }

               var2.add(var5);
            }

            return var2;
         }
      } catch (IOException var7) {
         Logger.getInstance().err("", var7);
         return null;
      }
   }
}
