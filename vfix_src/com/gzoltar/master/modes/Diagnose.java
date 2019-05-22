package com.gzoltar.master.modes;

import com.gzoltar.client.Properties;
import com.gzoltar.client.rmi.Response;
import com.gzoltar.instrumentation.testing.launch.JarWithClassPath;
import com.gzoltar.master.statistics.StatisticsBackend;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import java.util.Map;

public class Diagnose extends Modes {
   public static final String NAME = "diagnose";

   public static Option getOption() {
      return new Option("diagnose", "execute a list of test cases and perform fault localization on the project under test");
   }

   public static Object execute() {
      validateClasspathElements();
      Response var0 = Launcher.launch(com.gzoltar.client.Diagnose.class.getCanonicalName(), System.getProperty("java.class.path"), (String)null);
      JarWithClassPath.resetJarPath();
      Map var1 = var0.getStatistics();

      assert var1 != null;

      StatisticsBackend.getInstance().writeData(var1);
      return Properties.SERIALIZE_SPECTRA ? var0.getSpectra() : null;
   }
}
