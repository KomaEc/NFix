package com.gzoltar.master;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.master.modes.Diagnose;
import com.gzoltar.master.modes.Help;
import com.gzoltar.master.modes.ListClasses;
import com.gzoltar.master.modes.ListComponents;
import com.gzoltar.master.modes.ListParameters;
import com.gzoltar.master.modes.ListTests;
import com.gzoltar.master.modes.Minimize;
import com.gzoltar.shaded.org.apache.commons.cli.CommandLine;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import com.gzoltar.shaded.org.apache.commons.cli.OptionBuilder;
import com.gzoltar.shaded.org.apache.commons.cli.Options;
import java.util.Iterator;
import java.util.Properties;

public class CommandLineParameters {
   public static Options getAllPossibleCommandLineOptions() {
      Options var0 = new Options();
      Option var1 = Help.getOption();
      Option var2 = ListParameters.getOption();
      Option var3 = ListClasses.getOption();
      Option var4 = ListTests.getOption();
      Option var5 = ListComponents.getOption();
      Option var6 = Diagnose.getOption();
      Option var7 = Minimize.getOption();
      var0.addOption(var1);
      var0.addOption(var2);
      var0.addOption(var3);
      var0.addOption(var4);
      var0.addOption(var5);
      var0.addOption(var6);
      var0.addOption(var7);
      OptionBuilder.withArgName("property=value");
      OptionBuilder.hasArgs(2);
      OptionBuilder.withValueSeparator();
      OptionBuilder.withDescription("use value for given property");
      var1 = OptionBuilder.create("D");
      var0.addOption(var1);
      return var0;
   }

   public static void handleJavaDOptions(CommandLine var0) {
      Properties var6;
      Iterator var1 = (var6 = var0.getOptionProperties("D")).stringPropertyNames().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         String var3 = var6.getProperty(var2);
         Logger.getInstance().debug("propertyName: " + var3 + ", propertyValue: " + var3);
         System.setProperty(var2, var3);

         try {
            com.gzoltar.client.Properties.getInstance().setValue(var2, var3);
         } catch (IllegalAccessException | IllegalArgumentException var5) {
            Logger.getInstance().err("Invalid value for property " + var2 + ": " + var3 + ". Exception ", var5);
         }
      }

      Logger.getInstance().setLogLevel(com.gzoltar.client.Properties.LOGLEVEL);
   }
}
