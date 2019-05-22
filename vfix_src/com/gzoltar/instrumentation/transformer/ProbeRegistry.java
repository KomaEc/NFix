package com.gzoltar.instrumentation.transformer;

import com.gzoltar.instrumentation.runtime.RuntimeData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ProbeRegistry {
   public static Map<String, ProbeInserter> probeInserters = null;

   public static ProbeInserter newProbeInserter(String var0) {
      if (probeInserters == null) {
         probeInserters = new HashMap();
      }

      if (probeInserters.containsKey(var0)) {
         return (ProbeInserter)probeInserters.get(var0);
      } else {
         ProbeInserter var1 = new ProbeInserter(var0);
         probeInserters.put(var0, var1);
         return var1;
      }
   }

   public static Map<String, ProbeInserter> getProbeInserters() {
      return probeInserters;
   }

   public static void setProbeInserters(Map<String, ProbeInserter> var0) {
      probeInserters = new HashMap(var0);
   }

   public static Map<String, int[]> getAndClearCoverage() {
      HashMap var0 = new HashMap();
      if (probeInserters != null && !probeInserters.isEmpty()) {
         Iterator var1 = (new HashSet(probeInserters.keySet())).iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            var0.put(var2, RuntimeData.getMapHitArrayAndClear(var2));
         }

         return var0;
      } else {
         return var0;
      }
   }
}
