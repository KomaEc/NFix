package com.gzoltar.master.modes;

import com.gzoltar.client.rmi.Response;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.master.statistics.StatisticsBackend;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.util.Map;

public class ListTests extends Modes {
   public static final String NAME = "listTests";

   public static Option getOption() {
      return new Option("listTests", "list all test cases that can be executed");
   }

   public static Object execute() {
      validateClasspathElements();
      Logger.getInstance().info("* Collecting test classes");
      Response var0;
      Map var1 = (var0 = Launcher.launch(com.gzoltar.client.ListTests.class.getCanonicalName(), System.getProperty("java.class.path"), (String)null)).getListOfTestClasses();

      assert var1 != null;

      Map var2 = var0.getStatistics();
      StatisticsBackend.getInstance().writeData(var2);
      return var1;
   }
}
