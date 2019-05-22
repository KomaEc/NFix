package com.gzoltar.master.modes;

import com.gzoltar.master.statistics.StatisticsBackend;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.util.Map;

public class Minimize extends Modes {
   public static final String NAME = "minimize";

   public static Option getOption() {
      return new Option("minimize", "minimization of a test suite");
   }

   public static Object execute() {
      validateClasspathElements();
      Map var0 = Launcher.launch(com.gzoltar.client.Minimize.class.getCanonicalName(), System.getProperty("java.class.path"), (String)null).getStatistics();

      assert var0 != null;

      StatisticsBackend.getInstance().writeData(var0);
      return null;
   }
}
