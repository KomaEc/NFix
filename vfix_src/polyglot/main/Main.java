package polyglot.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.util.ErrorQueue;
import polyglot.util.StdErrorQueue;

public class Main {
   private Set source;
   public static final String verbose = "verbose";
   private static Collection timeTopics = new ArrayList(1);

   protected ExtensionInfo getExtensionInfo(List args) throws Main.TerminationException {
      ExtensionInfo ext = null;
      Iterator i = args.iterator();

      while(true) {
         while(i.hasNext()) {
            String s = (String)i.next();
            String extClass;
            if (!s.equals("-ext") && !s.equals("-extension")) {
               if (s.equals("-extclass")) {
                  if (ext != null) {
                     throw new Main.TerminationException("only one extension can be specified");
                  }

                  i.remove();
                  if (!i.hasNext()) {
                     throw new Main.TerminationException("missing argument");
                  }

                  extClass = (String)i.next();
                  i.remove();
                  ext = loadExtension(extClass);
               }
            } else {
               if (ext != null) {
                  throw new Main.TerminationException("only one extension can be specified");
               }

               i.remove();
               if (!i.hasNext()) {
                  throw new Main.TerminationException("missing argument");
               }

               extClass = (String)i.next();
               i.remove();
               ext = loadExtension("polyglot.ext." + extClass + ".ExtensionInfo");
            }
         }

         if (ext != null) {
            return ext;
         }

         return loadExtension("polyglot.ext.jl.ExtensionInfo");
      }
   }

   public void start(String[] argv) throws Main.TerminationException {
      this.start(argv, (ErrorQueue)null);
   }

   public void start(String[] argv, ErrorQueue eq) throws Main.TerminationException {
      this.source = new HashSet();
      List args = this.explodeOptions(argv);
      ExtensionInfo ext = this.getExtensionInfo(args);
      Options options = ext.getOptions();
      Options.global = options;

      try {
         argv = (String[])args.toArray(new String[0]);
         options.parseCommandLine(argv, this.source);
      } catch (UsageError var11) {
         PrintStream out = var11.exitCode == 0 ? System.out : System.err;
         if (var11.getMessage() != null && var11.getMessage().length() > 0) {
            out.println(ext.compilerName() + ": " + var11.getMessage());
         }

         options.usage(out);
         throw new Main.TerminationException(var11.exitCode);
      }

      if (eq == null) {
         eq = new StdErrorQueue(System.err, options.error_count, ext.compilerName());
      }

      Compiler compiler = new Compiler(ext, (ErrorQueue)eq);
      long time0 = System.currentTimeMillis();
      if (!compiler.compile(this.source)) {
         throw new Main.TerminationException(1);
      } else {
         if (Report.should_report((String)"verbose", 1)) {
            Report.report(1, "Output files: " + compiler.outputFiles());
         }

         long start_time = System.currentTimeMillis();
         if (!this.invokePostCompiler(options, compiler, (ErrorQueue)eq)) {
            throw new Main.TerminationException(1);
         } else {
            if (Report.should_report((String)"verbose", 1)) {
               reportTime("Finished compiling Java output files. time=" + (System.currentTimeMillis() - start_time), 1);
               reportTime("Total time=" + (System.currentTimeMillis() - time0), 1);
            }

         }
      }
   }

   protected boolean invokePostCompiler(Options options, Compiler compiler, ErrorQueue eq) {
      if (options.post_compiler != null && !options.output_stdout) {
         Runtime runtime = Runtime.getRuntime();
         Iterator iter = compiler.outputFiles().iterator();
         StringBuffer outputFiles = new StringBuffer();

         while(iter.hasNext()) {
            outputFiles.append((String)iter.next());
            outputFiles.append(" ");
         }

         String command = options.post_compiler + " -classpath " + options.constructPostCompilerClasspath() + " " + outputFiles.toString();
         if (Report.should_report((String)"verbose", 1)) {
            Report.report(1, "Executing post-compiler " + command);
         }

         try {
            Process proc = runtime.exec(command);
            InputStreamReader err = null;

            try {
               err = new InputStreamReader(proc.getErrorStream());
               char[] c = new char[72];
               StringBuffer sb = new StringBuffer();

               int len;
               while((len = err.read(c)) > 0) {
                  sb.append(String.valueOf(c, 0, len));
               }

               if (sb.length() != 0) {
                  eq.enqueue(6, sb.toString());
               }
            } finally {
               err.close();
            }

            proc.waitFor();
            if (!options.keep_output_files) {
               String command2 = "rm " + outputFiles.toString();
               runtime.exec(command2);
            }

            if (proc.exitValue() > 0) {
               eq.enqueue(6, "Non-zero return code: " + proc.exitValue());
               return false;
            }
         } catch (Exception var17) {
            eq.enqueue(6, var17.getMessage());
            return false;
         }
      }

      return true;
   }

   private List explodeOptions(String[] args) throws Main.TerminationException {
      LinkedList ll = new LinkedList();

      for(int i = 0; i < args.length; ++i) {
         if (args[i].startsWith("@")) {
            String fn = args[i].substring(1);

            try {
               BufferedReader lr = new BufferedReader(new FileReader(fn));
               LinkedList newArgs = new LinkedList();

               while(true) {
                  String l = lr.readLine();
                  if (l == null) {
                     lr.close();
                     ll.addAll(newArgs);
                     break;
                  }

                  StringTokenizer st = new StringTokenizer(l, " ");

                  while(st.hasMoreTokens()) {
                     newArgs.add(st.nextToken());
                  }
               }
            } catch (IOException var9) {
               throw new Main.TerminationException("cmdline parser: couldn't read args file " + fn);
            }
         } else {
            ll.add(args[i]);
         }
      }

      return ll;
   }

   public static final void main(String[] args) {
      try {
         (new Main()).start(args);
      } catch (Main.TerminationException var2) {
         if (var2.getMessage() != null) {
            (var2.exitCode == 0 ? System.out : System.err).println(var2.getMessage());
         }

         System.exit(var2.exitCode);
      }

   }

   static final ExtensionInfo loadExtension(String ext) throws Main.TerminationException {
      if (ext != null && !ext.equals("")) {
         Class extClass = null;

         try {
            extClass = Class.forName(ext);
         } catch (ClassNotFoundException var5) {
            throw new Main.TerminationException("Extension " + ext + " not found: could not find class " + ext + "." + var5.getMessage());
         }

         try {
            return (ExtensionInfo)extClass.newInstance();
         } catch (ClassCastException var3) {
            throw new Main.TerminationException(ext + " is not a valid polyglot extension:" + " extension class " + ext + " exists but is not a subclass of ExtensionInfo");
         } catch (Exception var4) {
            throw new Main.TerminationException("Extension " + ext + " could not be loaded: could not instantiate " + ext + ".");
         }
      } else {
         return null;
      }
   }

   private static void reportTime(String msg, int level) {
      Report.report(level, msg);
   }

   static {
      timeTopics.add("time");
   }

   public static class TerminationException extends RuntimeException {
      public final int exitCode;

      public TerminationException(String msg) {
         this(msg, 1);
      }

      public TerminationException(int exit) {
         this.exitCode = exit;
      }

      public TerminationException(String msg, int exit) {
         super(msg);
         this.exitCode = exit;
      }
   }
}
