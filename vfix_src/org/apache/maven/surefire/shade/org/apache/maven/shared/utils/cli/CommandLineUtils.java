package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.Os;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;

public abstract class CommandLineUtils {
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
      return executeCommandLine(cl, systemIn, systemOut, systemErr, timeoutInSeconds, (Runnable)null);
   }

   public static int executeCommandLine(Commandline cl, InputStream systemIn, StreamConsumer systemOut, StreamConsumer systemErr, int timeoutInSeconds, Runnable runAfterProcessTermination) throws CommandLineException {
      CommandLineCallable future = executeCommandLineAsCallable(cl, systemIn, systemOut, systemErr, timeoutInSeconds, runAfterProcessTermination);
      return future.call();
   }

   private static CommandLineCallable executeCommandLineAsCallable(Commandline cl, InputStream systemIn, StreamConsumer systemOut, StreamConsumer systemErr, final int timeoutInSeconds, final Runnable runAfterProcessTermination) throws CommandLineException {
      if (cl == null) {
         throw new IllegalArgumentException("cl cannot be null.");
      } else {
         final Process p = cl.execute();
         final StreamFeeder inputFeeder = systemIn != null ? new StreamFeeder(systemIn, p.getOutputStream()) : null;
         final StreamPumper outputPumper = new StreamPumper(p.getInputStream(), systemOut);
         final StreamPumper errorPumper = new StreamPumper(p.getErrorStream(), systemErr);
         if (inputFeeder != null) {
            inputFeeder.start();
         }

         outputPumper.start();
         errorPumper.start();
         final CommandLineUtils.ProcessHook processHook = new CommandLineUtils.ProcessHook(p);
         ShutdownHookUtils.addShutDownHook(processHook);
         return new CommandLineCallable() {
            public Integer call() throws CommandLineException {
               Integer var8;
               try {
                  int returnValue;
                  if (timeoutInSeconds <= 0) {
                     returnValue = p.waitFor();
                  } else {
                     long now = System.currentTimeMillis();
                     long timeoutInMillis = 1000L * (long)timeoutInSeconds;
                     long finish = now + timeoutInMillis;

                     while(true) {
                        if (!CommandLineUtils.isAlive(p) || System.currentTimeMillis() >= finish) {
                           if (CommandLineUtils.isAlive(p)) {
                              throw new InterruptedException("Process timeout out after " + timeoutInSeconds + " seconds");
                           }

                           returnValue = p.exitValue();
                           break;
                        }

                        Thread.sleep(10L);
                     }
                  }

                  if (runAfterProcessTermination != null) {
                     runAfterProcessTermination.run();
                  }

                  CommandLineUtils.waitForAllPumpers(inputFeeder, outputPumper, errorPumper);
                  if (outputPumper.getException() != null) {
                     throw new CommandLineException("Error inside systemOut parser", outputPumper.getException());
                  }

                  if (errorPumper.getException() != null) {
                     throw new CommandLineException("Error inside systemErr parser", errorPumper.getException());
                  }

                  var8 = returnValue;
               } catch (InterruptedException var12) {
                  if (inputFeeder != null) {
                     inputFeeder.disable();
                  }

                  outputPumper.disable();
                  errorPumper.disable();
                  throw new CommandLineTimeOutException("Error while executing external command, process killed.", var12);
               } finally {
                  ShutdownHookUtils.removeShutdownHook(processHook);
                  processHook.run();
                  if (inputFeeder != null) {
                     inputFeeder.close();
                  }

                  outputPumper.close();
                  errorPumper.close();
               }

               return var8;
            }
         };
      }
   }

   private static void waitForAllPumpers(StreamFeeder inputFeeder, StreamPumper outputPumper, StreamPumper errorPumper) throws InterruptedException {
      if (inputFeeder != null) {
         inputFeeder.waitUntilDone();
      }

      outputPumper.waitUntilDone();
      errorPumper.waitUntilDone();
   }

   public static Properties getSystemEnvVars() {
      return getSystemEnvVars(!Os.isFamily("windows"));
   }

   public static Properties getSystemEnvVars(boolean caseSensitive) {
      Map<String, String> envs = System.getenv();
      return ensureCaseSensitivity(envs, caseSensitive);
   }

   private static boolean isAlive(Process p) {
      if (p == null) {
         return false;
      } else {
         try {
            p.exitValue();
            return false;
         } catch (IllegalThreadStateException var2) {
            return true;
         }
      }
   }

   public static String[] translateCommandline(String toProcess) throws Exception {
      if (toProcess != null && toProcess.length() != 0) {
         int normal = false;
         int inQuote = true;
         int inDoubleQuote = true;
         int state = 0;
         StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
         List<String> tokens = new ArrayList();
         StringBuilder current = new StringBuilder();

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
                     tokens.add(current.toString());
                     current.setLength(0);
                  }
               } else {
                  current.append(nextTok);
               }
            }
         }

         if (current.length() != 0) {
            tokens.add(current.toString());
         }

         if (state != 1 && state != 2) {
            return (String[])tokens.toArray(new String[tokens.size()]);
         } else {
            throw new CommandLineException("unbalanced quotes in " + toProcess);
         }
      } else {
         return new String[0];
      }
   }

   public static String toString(String... line) {
      if (line != null && line.length != 0) {
         StringBuilder result = new StringBuilder();

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

   static Properties ensureCaseSensitivity(Map<String, String> envs, boolean preserveKeyCase) {
      Properties envVars = new Properties();
      Iterator i$ = envs.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         envVars.put(!preserveKeyCase ? ((String)entry.getKey()).toUpperCase(Locale.ENGLISH) : (String)entry.getKey(), entry.getValue());
      }

      return envVars;
   }

   private static class ProcessHook extends Thread {
      private final Process process;

      private ProcessHook(Process process) {
         super("CommandlineUtils process shutdown hook");
         this.process = process;
         this.setContextClassLoader((ClassLoader)null);
      }

      public void run() {
         this.process.destroy();
      }

      // $FF: synthetic method
      ProcessHook(Process x0, Object x1) {
         this(x0);
      }
   }

   public static class StringStreamConsumer implements StreamConsumer {
      private final StringBuffer string = new StringBuffer();
      private static final String LS = System.getProperty("line.separator");

      public void consumeLine(String line) {
         this.string.append(line).append(LS);
      }

      public String getOutput() {
         return this.string.toString();
      }
   }
}
