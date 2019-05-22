package com.gzoltar.shaded.jline.console.internal;

import com.gzoltar.shaded.jline.console.ConsoleReader;
import com.gzoltar.shaded.jline.console.completer.ArgumentCompleter;
import com.gzoltar.shaded.jline.console.completer.Completer;
import com.gzoltar.shaded.jline.console.history.FileHistory;
import com.gzoltar.shaded.jline.internal.Configuration;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ConsoleRunner {
   public static final String property = "com.gzoltar.shaded.jline.history";

   public static void main(String[] args) throws Exception {
      List<String> argList = new ArrayList(Arrays.asList(args));
      if (argList.size() == 0) {
         usage();
      } else {
         String historyFileName = System.getProperty("com.gzoltar.shaded.jline.history", (String)null);
         String mainClass = (String)argList.remove(0);
         ConsoleReader reader = new ConsoleReader();
         if (historyFileName != null) {
            reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".com.gzoltar.shaded.jline-%s.%s.history", mainClass, historyFileName))));
         } else {
            reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".com.gzoltar.shaded.jline-%s.history", mainClass))));
         }

         String completors = System.getProperty(ConsoleRunner.class.getName() + ".completers", "");
         List<Completer> completorList = new ArrayList();
         StringTokenizer tok = new StringTokenizer(completors, ",");

         while(tok.hasMoreTokens()) {
            Object obj = Class.forName(tok.nextToken()).newInstance();
            completorList.add((Completer)obj);
         }

         if (completorList.size() > 0) {
            reader.addCompleter(new ArgumentCompleter(completorList));
         }

         ConsoleReaderInputStream.setIn(reader);

         try {
            Class<?> type = Class.forName(mainClass);
            Method method = type.getMethod("main", String[].class);
            String[] mainArgs = (String[])argList.toArray(new String[argList.size()]);
            method.invoke((Object)null, mainArgs);
         } finally {
            ConsoleReaderInputStream.restoreIn();
         }

      }
   }

   private static void usage() {
      System.out.println("Usage: \n   java [-Djline.history='name'] " + ConsoleRunner.class.getName() + " <target class name> [args]" + "\n\nThe -Djline.history option will avoid history" + "\nmangling when running ConsoleRunner on the same application." + "\n\nargs will be passed directly to the target class name.");
   }
}
