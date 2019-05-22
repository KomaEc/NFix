package org.codehaus.plexus.util.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;

public abstract class CommandLineUtils {
   private static Map processes = Collections.synchronizedMap(new HashMap());

   public static int executeCommandLine(Commandline cl, StreamConsumer systemOut, StreamConsumer systemErr) throws CommandLineException {
      return executeCommandLine(cl, (InputStream)null, systemOut, systemErr, 0);
   }

   public static int executeCommandLine(Commandline cl, StreamConsumer systemOut, StreamConsumer systemErr, int timeoutInSeconds) throws CommandLineException {
      return executeCommandLine(cl, (InputStream)null, systemOut, systemErr, timeoutInSeconds);
   }

   public static int executeCommandLine(Commandline cl, InputStream systemIn, StreamConsumer systemOut, StreamConsumer systemErr) throws CommandLineException {
      return executeCommandLine(cl, systemIn, systemOut, systemErr, 0);
   }

   public static int executeCommandLine(Commandline cl, InputStream systemIn, StreamConsumer systemOut, StreamConsumer systemErr, int timeoutInSeconds) throws CommandLineException {
      if (cl == null) {
         throw new IllegalArgumentException("cl cannot be null.");
      } else {
         Process p = cl.execute();
         processes.put(new Long(cl.getPid()), p);
         StreamFeeder inputFeeder = null;
         if (systemIn != null) {
            inputFeeder = new StreamFeeder(systemIn, p.getOutputStream());
         }

         StreamPumper outputPumper = new StreamPumper(p.getInputStream(), systemOut);
         StreamPumper errorPumper = new StreamPumper(p.getErrorStream(), systemErr);
         if (inputFeeder != null) {
            inputFeeder.start();
         }

         outputPumper.start();
         errorPumper.start();

         int var16;
         try {
            int returnValue;
            if (timeoutInSeconds <= 0) {
               returnValue = p.waitFor();
            } else {
               long now = System.currentTimeMillis();
               long timeoutInMillis = 1000L * (long)timeoutInSeconds;
               long finish = now + timeoutInMillis;

               while(isAlive(p) && System.currentTimeMillis() < finish) {
                  Thread.sleep(10L);
               }

               if (isAlive(p)) {
                  throw new InterruptedException("Process timeout out after " + timeoutInSeconds + " seconds");
               }

               returnValue = p.exitValue();
            }

            if (inputFeeder != null) {
               synchronized(inputFeeder) {
                  while(!inputFeeder.isDone()) {
                     inputFeeder.wait();
                  }
               }
            }

            synchronized(outputPumper) {
               while(true) {
                  if (outputPumper.isDone()) {
                     break;
                  }

                  outputPumper.wait();
               }
            }

            synchronized(errorPumper) {
               while(!errorPumper.isDone()) {
                  errorPumper.wait();
               }
            }

            processes.remove(new Long(cl.getPid()));
            var16 = returnValue;
         } catch (InterruptedException var29) {
            killProcess(cl.getPid());
            throw new CommandLineException("Error while executing external command, process killed.", var29);
         } finally {
            if (inputFeeder != null) {
               inputFeeder.close();
            }

            outputPumper.close();
            errorPumper.close();
         }

         return var16;
      }
   }

   public static Properties getSystemEnvVars() throws IOException {
      return getSystemEnvVars(!Os.isFamily("windows"));
   }

   public static Properties getSystemEnvVars(boolean caseSensitive) throws IOException {
      Process p = null;
      Properties envVars = new Properties();
      Runtime r = Runtime.getRuntime();
      if (Os.isFamily("windows")) {
         if (Os.isFamily("win9x")) {
            p = r.exec("command.com /c set");
         } else {
            p = r.exec("cmd.exe /c set");
         }
      } else {
         p = r.exec("env");
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String lastKey = null;
      String lastVal = null;

      String line;
      while((line = br.readLine()) != null) {
         int idx = line.indexOf(61);
         if (idx > 0) {
            lastKey = line.substring(0, idx);
            if (!caseSensitive) {
               lastKey = lastKey.toUpperCase(Locale.ENGLISH);
            }

            lastVal = line.substring(idx + 1);
            envVars.setProperty(lastKey, lastVal);
         } else if (lastKey != null) {
            lastVal = lastVal + "\n" + line;
            envVars.setProperty(lastKey, lastVal);
         }
      }

      return envVars;
   }

   public static void killProcess(long pid) {
      Process p = (Process)processes.get(new Long(pid));
      if (p != null) {
         p.destroy();
         System.out.println("killed.");
         processes.remove(new Long(pid));
      } else {
         System.out.println("don't exist.");
      }

   }

   public static boolean isAlive(long pid) {
      return processes.get(new Long(pid)) != null;
   }

   public static boolean isAlive(Process p) {
      try {
         p.exitValue();
         return false;
      } catch (IllegalThreadStateException var2) {
         return true;
      }
   }

   public static String[] translateCommandline(String toProcess) throws Exception {
      if (toProcess != null && toProcess.length() != 0) {
         int normal = false;
         int inQuote = true;
         int inDoubleQuote = true;
         int state = 0;
         StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
         Vector v = new Vector();
         StringBuffer current = new StringBuffer();

         while(tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch(state) {
            case 1:
               if ("'".equals(nextTok)) {
                  state = 0;
               } else {
                  current.append(nextTok);
               }
               break;
            case 2:
               if ("\"".equals(nextTok)) {
                  state = 0;
               } else {
                  current.append(nextTok);
               }
               break;
            default:
               if ("'".equals(nextTok)) {
                  state = 1;
               } else if ("\"".equals(nextTok)) {
                  state = 2;
               } else if (" ".equals(nextTok)) {
                  if (current.length() != 0) {
                     v.addElement(current.toString());
                     current.setLength(0);
                  }
               } else {
                  current.append(nextTok);
               }
            }
         }

         if (current.length() != 0) {
            v.addElement(current.toString());
         }

         if (state != 1 && state != 2) {
            String[] args = new String[v.size()];
            v.copyInto(args);
            return args;
         } else {
            throw new CommandLineException("unbalanced quotes in " + toProcess);
         }
      } else {
         return new String[0];
      }
   }

   /** @deprecated */
   public static String quote(String argument) throws CommandLineException {
      return quote(argument, false, false, true);
   }

   /** @deprecated */
   public static String quote(String argument, boolean wrapExistingQuotes) throws CommandLineException {
      return quote(argument, false, false, wrapExistingQuotes);
   }

   /** @deprecated */
   public static String quote(String argument, boolean escapeSingleQuotes, boolean escapeDoubleQuotes, boolean wrapExistingQuotes) throws CommandLineException {
      if (argument.indexOf("\"") > -1) {
         if (argument.indexOf("'") > -1) {
            throw new CommandLineException("Can't handle single and double quotes in same argument");
         }

         if (escapeSingleQuotes) {
            return "\\'" + argument + "\\'";
         }

         if (wrapExistingQuotes) {
            return '\'' + argument + '\'';
         }
      } else if (argument.indexOf("'") > -1) {
         if (escapeDoubleQuotes) {
            return "\\\"" + argument + "\\\"";
         }

         if (wrapExistingQuotes) {
            return '"' + argument + '"';
         }
      } else if (argument.indexOf(" ") > -1) {
         if (escapeDoubleQuotes) {
            return "\\\"" + argument + "\\\"";
         }

         return '"' + argument + '"';
      }

      return argument;
   }

   public static String toString(String[] line) {
      if (line != null && line.length != 0) {
         StringBuffer result = new StringBuffer();

         for(int i = 0; i < line.length; ++i) {
            if (i > 0) {
               result.append(' ');
            }

            try {
               result.append(StringUtils.quoteAndEscape(line[i], '"'));
            } catch (Exception var4) {
               System.err.println("Error quoting argument: " + var4.getMessage());
            }
         }

         return result.toString();
      } else {
         return "";
      }
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread("CommandlineUtil shutdown") {
         public void run() {
            if (CommandLineUtils.processes != null && CommandLineUtils.processes.size() > 0) {
               System.err.println("Destroying " + CommandLineUtils.processes.size() + " processes");
               Iterator it = CommandLineUtils.processes.values().iterator();

               while(it.hasNext()) {
                  System.err.println("Destroying process..");
                  ((Process)it.next()).destroy();
               }

               System.err.println("Destroyed " + CommandLineUtils.processes.size() + " processes");
            }

         }
      });
   }

   public static class StringStreamConsumer implements StreamConsumer {
      private StringBuffer string = new StringBuffer();
      private String ls = System.getProperty("line.separator");

      public void consumeLine(String line) {
         this.string.append(line).append(this.ls);
      }

      public String getOutput() {
         return this.string.toString();
      }
   }
}
