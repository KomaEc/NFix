package com.gzoltar.client;

import com.gzoltar.client.minimize.strategy.CSPStrategy;
import com.gzoltar.client.minimize.strategy.GreedyStrategy;
import com.gzoltar.client.rmi.IMessage;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.trie.TrieJNI;

public class Minimize {
   public static void main(String... var0) {
      try {
         IMessage var9 = (IMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1]);
         Response var1 = new Response();
         Properties.getInstance().setValues(var9.getProperties());
         Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
         Spectra var2;
         (var2 = Spectra.getInstance()).setGranularity(Properties.GRANULARITY);
         Map var3 = var1.getStatistics();

         assert var3 != null;

         Logger.getInstance().info("* Performing minimization");
         ArrayList var4 = new ArrayList();
         TrieJNI var5;
         (var5 = new TrieJNI()).create();
         switch(Properties.MINIMIZATION_STRATEGY) {
         case CSP:
            Logger.getInstance().info("* Running CSP strategy");
            CSPStrategy var6 = new CSPStrategy();
            var4.addAll(var6.perform(var2));
            break;
         default:
            Logger.getInstance().info("* Running Greedy strategy");
            GreedyStrategy var7 = new GreedyStrategy();
            var4.add(var7.perform(var2));
         }

         var5.destroy();
         Iterator var12 = var4.iterator();

         while(var12.hasNext()) {
            Set var13 = (Set)var12.next();
            Logger.getInstance().info("=== SUITE ===");
            Iterator var10 = var13.iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               Logger.getInstance().info("  " + var11);
            }
         }

         StatisticsVariables.augmentStatisticsData(var2, var3);
         if (Properties.SERIALIZE_SPECTRA) {
            Logger.getInstance().info("* Serializing the spectra object");
            var1.setSpectra(var2);
         }

         Logger.getInstance().info("* Sending data to master");
         var9.setResponse(var1);
      } catch (IllegalArgumentException | IllegalAccessException | IOException | NotBoundException var8) {
         Logger.getInstance().err("", var8);
         System.exit(1);
      }

      System.exit(0);
   }
}
