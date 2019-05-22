package org.jboss.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class Throwables {
   public static String toString(Throwable t) {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      PrintStream stream = new PrintStream(output);
      t.printStackTrace(stream);
      return output.toString();
   }
}
