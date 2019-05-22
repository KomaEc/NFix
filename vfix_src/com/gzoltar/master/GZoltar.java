package com.gzoltar.master;

import com.gzoltar.client.Properties;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.master.modes.Diagnose;
import com.gzoltar.master.modes.Help;
import com.gzoltar.master.modes.ListClasses;
import com.gzoltar.master.modes.ListComponents;
import com.gzoltar.master.modes.ListParameters;
import com.gzoltar.master.modes.ListTests;
import com.gzoltar.master.modes.Minimize;
import com.gzoltar.shaded.org.apache.commons.cli.CommandLine;
import com.gzoltar.shaded.org.apache.commons.cli.DefaultParser;
import com.gzoltar.shaded.org.apache.commons.cli.Options;

public class GZoltar {
   public static void main(String[] var0) {
      ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(Properties.ENABLE_ASSERTIONS_FOR_GZOLTAR);
      Logger.getInstance().info("   ____ _____     _ _               \n  / ___|__  /___ | | |_ __ _ _ __   \n | |  _  / // _ \\| | __/ _` | '__| \n | |_| |/ /| (_) | | || (_| | |     \n  \\____/____\\___/|_|\\__\\__,_|_| \n");
      Properties.getInstance();

      try {
         parseCommandLine(var0);
      } catch (AssertionError var1) {
         Logger.getInstance().err("", var1);
         System.exit(1);
      }

      System.exit(0);
   }

   public static Object parseCommandLine(String[] var0) {
      Options var1 = CommandLineParameters.getAllPossibleCommandLineOptions();
      DefaultParser var2 = new DefaultParser();

      try {
         CommandLine var4;
         CommandLineParameters.handleJavaDOptions(var4 = var2.parse(var1, var0));
         if (var4.hasOption("help")) {
            return Help.execute(var1);
         } else if (var4.hasOption("listParameters")) {
            return ListParameters.execute();
         } else if (var4.hasOption("listClasses")) {
            return ListClasses.execute();
         } else if (var4.hasOption("listTests")) {
            return ListTests.execute();
         } else if (var4.hasOption("listComponents")) {
            return ListComponents.execute();
         } else if (var4.hasOption("diagnose")) {
            return Diagnose.execute();
         } else {
            return var4.hasOption("minimize") ? Minimize.execute() : Help.execute(var1);
         }
      } catch (Exception var3) {
         Logger.getInstance().err("Parsing failed.", var3);
         Logger.getInstance().info("* Computation finished");
         return null;
      }
   }
}
