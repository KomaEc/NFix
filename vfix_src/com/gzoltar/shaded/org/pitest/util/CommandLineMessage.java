package com.gzoltar.shaded.org.pitest.util;

public abstract class CommandLineMessage {
   public static void report(String message) {
      StringBuilder sb = new StringBuilder();
      sb.append("---------------------------------------------------------------------------------\n");
      sb.append(message + "\n");
      sb.append("---------------------------------------------------------------------------------\n");
      System.out.println(sb);
   }
}
