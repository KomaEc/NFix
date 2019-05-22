package com.gzoltar.client;

import com.gzoltar.client.rmi.IMessage;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.client.utils.ClassUtils;
import com.gzoltar.instrumentation.Logger;
import java.io.File;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.List;

public class ListClasses {
   public static void main(String... var0) {
      try {
         IMessage var8 = (IMessage)LocateRegistry.getRegistry(Integer.parseInt(var0[0])).lookup(var0[1]);
         Properties.getInstance().setValues(var8.getProperties());
         Logger.getInstance().setLogLevel(Properties.LOGLEVEL);
         Response var1 = new Response();
         Logger.getInstance().info("* Collecting classes");
         List var2 = ClassUtils.getNonTestClasses();
         var1.addOutputStatisticsVariable(StatisticsVariables.NUMBER_OF_CLASSES, var2.size());
         if (var2.isEmpty()) {
            Logger.getInstance().warn("No classes to return");
         } else {
            try {
               (new File(Properties.GZOLTAR_DATA_DIR)).mkdirs();
               PrintWriter var3 = new PrintWriter(Properties.GZOLTAR_DATA_DIR + System.getProperty("file.separator") + Properties.CLASSES_FILE, "UTF-8");
               Iterator var4 = var2.iterator();

               while(true) {
                  if (!var4.hasNext()) {
                     var3.close();
                     break;
                  }

                  String var5 = (String)var4.next();
                  var3.println(var5);
               }
            } catch (Exception var6) {
               Logger.getInstance().err("", var6);
            }
         }

         var1.setListOfClasses(var2);
         var8.setResponse(var1);
      } catch (NotBoundException | IllegalArgumentException | IllegalAccessException | RemoteException var7) {
         Logger.getInstance().err("", var7);
         System.exit(1);
      }

      System.exit(0);
   }
}
