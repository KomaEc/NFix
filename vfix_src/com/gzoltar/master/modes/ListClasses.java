package com.gzoltar.master.modes;

import com.gzoltar.client.rmi.Response;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.master.statistics.StatisticsBackend;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListClasses extends Modes {
   public static final String NAME = "listClasses";

   public static Option getOption() {
      return new Option("listClasses", "list all classes that can be diagnosed");
   }

   public static Object execute() {
      validateClasspathElements();
      Logger.getInstance().info("* Collecting classes");
      Response var0;
      List var1 = (var0 = Launcher.launch(com.gzoltar.client.ListClasses.class.getCanonicalName(), System.getProperty("java.class.path"), (String)null)).getListOfClasses();

      assert var1 != null;

      Map var3 = var0.getStatistics();
      StatisticsBackend.getInstance().writeData(var3);
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         String var2 = (String)var4.next();
         Logger.getInstance().info(var2);
      }

      return var1;
   }
}
