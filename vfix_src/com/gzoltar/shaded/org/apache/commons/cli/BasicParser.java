package com.gzoltar.shaded.org.apache.commons.cli;

/** @deprecated */
@Deprecated
public class BasicParser extends Parser {
   protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
      return arguments;
   }
}
