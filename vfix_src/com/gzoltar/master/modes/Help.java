package com.gzoltar.master.modes;

import com.gzoltar.shaded.org.apache.commons.cli.HelpFormatter;
import com.gzoltar.shaded.org.apache.commons.cli.Option;
import com.gzoltar.shaded.org.apache.commons.cli.Options;

public class Help {
   public static final String NAME = "help";

   public static Option getOption() {
      return new Option("help", "print this message");
   }

   public static Object execute(Options var0) {
      (new HelpFormatter()).printHelp("java -jar com.gzoltar.jar <options>", var0);
      return null;
   }
}
