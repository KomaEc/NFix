package com.gzoltar.master.modes;

import com.gzoltar.client.rmi.Response;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.master.statistics.StatisticsBackend;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.util.List;
import java.util.Map;

public class ListComponents extends Modes {
   public static final String NAME = "listComponents";

   public static Option getOption() {
      return new Option("listComponents", "list all components of the software under test");
   }

   public static Object execute() {
      validateClasspathElements();
      Logger.getInstance().info("* Collecting components");
      Response var0;
      List var1 = (var0 = Launcher.launch(com.gzoltar.client.ListComponents.class.getCanonicalName(), System.getProperty("java.class.path"), (String)null)).getListOfComponents();

      assert var1 != null;

      Map var2 = var0.getStatistics();
      StatisticsBackend.getInstance().writeData(var2);
      return var1;
   }
}
