package soot;

import com.google.common.base.Joiner;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import soot.options.CGOptions;
import soot.options.Options;
import soot.toolkits.astmetrics.ClassData;

public class Main {
   public static final String versionString = Main.class.getPackage().getImplementationVersion() == null ? "trunk" : Main.class.getPackage().getImplementationVersion();
   private Date start;
   private long startNano;
   private long finishNano;
   public String[] cmdLineArgs = new String[0];

   public Main(Singletons.Global g) {
   }

   public static Main v() {
      return G.v().soot_Main();
   }

   private void printVersion() {
      System.out.println("Soot version " + versionString);
      System.out.println("Copyright (C) 1997-2010 Raja Vallee-Rai and others.");
      System.out.println("All rights reserved.");
      System.out.println();
      System.out.println("Contributions are copyright (C) 1997-2010 by their respective contributors.");
      System.out.println("See the file 'credits' for a list of contributors.");
      System.out.println("See individual source files for details.");
      System.out.println();
      System.out.println("Soot comes with ABSOLUTELY NO WARRANTY.  Soot is free software,");
      System.out.println("and you are welcome to redistribute it under certain conditions.");
      System.out.println("See the accompanying file 'COPYING-LESSER.txt' for details.");
      System.out.println("Visit the Soot website:");
      System.out.println("  http://www.sable.mcgill.ca/soot/");
      System.out.println("For a list of command line options, enter:");
      System.out.println("  java soot.Main --help");
   }

   private void processCmdLine(String[] args) {
      if (!Options.v().parse(args)) {
         throw new OptionsParseException("Option parse error");
      } else {
         Iterator var2;
         if (PackManager.v().onlyStandardPacks()) {
            var2 = PackManager.v().allPacks().iterator();

            while(var2.hasNext()) {
               Pack pack = (Pack)var2.next();
               Options.v().warnForeignPhase(pack.getPhaseName());
               Iterator var4 = pack.iterator();

               while(var4.hasNext()) {
                  Transform tr = (Transform)var4.next();
                  Options.v().warnForeignPhase(tr.getPhaseName());
               }
            }
         }

         Options.v().warnNonexistentPhase();
         if (Options.v().help()) {
            System.out.println(Options.v().getUsage());
            throw new CompilationDeathException(1);
         } else if (Options.v().phase_list()) {
            System.out.println(Options.v().getPhaseList());
            throw new CompilationDeathException(1);
         } else if (!Options.v().phase_help().isEmpty()) {
            var2 = Options.v().phase_help().iterator();

            while(var2.hasNext()) {
               String phase = (String)var2.next();
               System.out.println(Options.v().getPhaseHelp(phase));
            }

            throw new CompilationDeathException(1);
         } else if ((Options.v().unfriendly_mode() || args.length != 0) && !Options.v().version()) {
            if (Options.v().on_the_fly()) {
               Options.v().set_whole_program(true);
               PhaseOptions.v().setPhaseOption("cg", "off");
            }

            this.postCmdLineCheck();
         } else {
            this.printVersion();
            throw new CompilationDeathException(1);
         }
      }
   }

   private void postCmdLineCheck() {
      if (Options.v().classes().isEmpty() && Options.v().process_dir().isEmpty()) {
         throw new CompilationDeathException(0, "No input classes specified!");
      }
   }

   public static void main(String[] args) {
      try {
         v().run(args);
      } catch (OptionsParseException var10) {
      } catch (StackOverflowError var11) {
         System.err.println("Soot has run out of stack memory.");
         System.err.println("To allocate more stack memory to Soot, use the -Xss switch to Java.");
         System.err.println("For example (for 2MB): java -Xss2m soot.Main ...");
         throw var11;
      } catch (OutOfMemoryError var12) {
         System.err.println("Soot has run out of the memory allocated to it by the Java VM.");
         System.err.println("To allocate more memory to Soot, use the -Xmx switch to Java.");
         System.err.println("For example (for 2GB): java -Xmx2g soot.Main ...");
         throw var12;
      } catch (RuntimeException var13) {
         RuntimeException e = var13;
         var13.printStackTrace();
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         var13.printStackTrace(new PrintStream(bos));
         String stackStraceString = bos.toString();

         try {
            String TRACKER_URL = "https://github.com/Sable/soot/issues/new?";
            String commandLineArgs = Joiner.on(" ").join((Object[])args);
            String body = "Steps to reproduce:\n1.) ...\n\nFiles used to reproduce: \n...\n\nSoot version: <pre>" + escape(versionString) + "</pre>\n\nCommand line:\n<pre>" + escape(commandLineArgs) + "</pre>\n\nMax Memory:\n<pre>" + escape(Runtime.getRuntime().maxMemory() / 1048576L + "MB") + "</pre>\n\nStack trace:\n<pre>" + escape(stackStraceString) + "</pre>";
            String title = e.getClass().getName() + " when ...";
            StringBuilder sb = new StringBuilder();
            sb.append("\n\nOuuups... something went wrong! Sorry about that.\n");
            sb.append("Follow these steps to fix the problem:\n");
            sb.append("1.) Are you sure you used the right command line?\n");
            sb.append("    Click here to double-check:\n");
            sb.append("    https://soot-build.cs.uni-paderborn.de/doc/sootoptions/\n");
            sb.append("\n");
            sb.append("2.) Not sure whether it's a bug? Feel free to discuss\n");
            sb.append("    the issue on the Soot mailing list:\n");
            sb.append("    https://github.com/Sable/soot/wiki/Getting-help\n");
            sb.append("\n");
            sb.append("3.) Sure it's a bug? Click this link to report it.\n");
            sb.append("    https://github.com/Sable/soot/issues/new?title=" + URLEncoder.encode(title, "UTF-8") + "&body=" + URLEncoder.encode(body, "UTF-8") + "\n");
            sb.append("    Please be as precise as possible when giving us\n");
            sb.append("    information on how to reproduce the problem. Thanks!");
            System.err.println(sb);
            System.exit(1);
         } catch (UnsupportedEncodingException var9) {
            System.exit(1);
         }
      }

   }

   private static CharSequence escape(CharSequence s) {
      int start = 0;
      int end = s.length();
      StringBuilder sb = new StringBuilder(32 + (end - start));
      int i = start;

      while(i < end) {
         int c = s.charAt(i);
         switch(c) {
         case '"':
         case '&':
         case '\'':
         case '<':
         case '>':
            sb.append(s, start, i);
            sb.append("&#");
            sb.append(c);
            sb.append(';');
            start = i + 1;
         default:
            ++i;
         }
      }

      return sb.append(s, start, end);
   }

   public void run(String[] args) {
      this.cmdLineArgs = args;
      this.start = new Date();
      this.startNano = System.nanoTime();

      try {
         Timers.v().totalTimer.start();
         this.processCmdLine(this.cmdLineArgs);
         this.autoSetOptions();
         System.out.println("Soot started on " + this.start);
         Scene.v().loadNecessaryClasses();
         if (Options.v().ast_metrics()) {
            try {
               OutputStream streamOut = new FileOutputStream("../astMetrics.xml");
               PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
               writerOut.println("<?xml version='1.0'?>");
               writerOut.println("<ASTMetrics>");
               Iterator var4 = G.v().ASTMetricsData.iterator();

               while(var4.hasNext()) {
                  ClassData cData = (ClassData)var4.next();
                  writerOut.println(cData);
               }

               writerOut.println("</ASTMetrics>");
               writerOut.flush();
               streamOut.close();
               return;
            } catch (IOException var6) {
               throw new CompilationDeathException("Cannot output file astMetrics", var6);
            }
         }

         PackManager.v().runPacks();
         if (!Options.v().oaat()) {
            PackManager.v().writeOutput();
         }

         Timers.v().totalTimer.end();
         if (Options.v().time()) {
            Timers.v().printProfilingInformation();
         }
      } catch (CompilationDeathException var7) {
         Timers.v().totalTimer.end();
         if (var7.getStatus() != 1) {
            throw var7;
         }

         return;
      }

      this.finishNano = System.nanoTime();
      System.out.println("Soot finished on " + new Date());
      long runtime = (this.finishNano - this.startNano) / 1000000L;
      System.out.println("Soot has run for " + runtime / 60000L + " min. " + runtime % 60000L / 1000L + " sec.");
   }

   public void autoSetOptions() {
      if (Options.v().no_bodies_for_excluded()) {
         Options.v().set_allow_phantom_refs(true);
      }

      CGOptions cgOptions = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
      String log = cgOptions.reflection_log();
      if (log != null && log.length() > 0) {
         Options.v().set_allow_phantom_refs(true);
      }

      if (Options.v().allow_phantom_refs()) {
         Options.v().set_wrong_staticness(3);
      }

   }
}
