package groovy.ui;

import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.lang.Script;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.CommandLineParser;
import groovyjarjarcommonscli.HelpFormatter;
import groovyjarjarcommonscli.OptionBuilder;
import groovyjarjarcommonscli.Options;
import groovyjarjarcommonscli.ParseException;
import groovyjarjarcommonscli.PosixParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerInvocationException;

public class GroovyMain {
   private List args;
   private boolean isScriptFile;
   private String script;
   private boolean processFiles;
   private boolean editFiles;
   private boolean autoOutput;
   private boolean autoSplit;
   private String splitPattern = " ";
   private boolean processSockets;
   private int port;
   private String backupExtension;
   private boolean debug = false;
   private CompilerConfiguration conf = new CompilerConfiguration(System.getProperties());

   public static void main(String[] args) {
      processArgs(args, System.out);
   }

   static void processArgs(String[] args, PrintStream out) {
      Options options = buildOptions();

      try {
         CommandLine cmd = parseCommandLine(options, args);
         if (cmd.hasOption('h')) {
            printHelp(out, options);
         } else if (cmd.hasOption('v')) {
            String version = GroovySystem.getVersion();
            out.println("Groovy Version: " + version + " JVM: " + System.getProperty("java.version"));
         } else if (!process(cmd)) {
            System.exit(1);
         }
      } catch (ParseException var5) {
         out.println("error: " + var5.getMessage());
         printHelp(out, options);
      }

   }

   private static void printHelp(PrintStream out, Options options) {
      HelpFormatter formatter = new HelpFormatter();
      PrintWriter pw = new PrintWriter(out);
      formatter.printHelp(pw, 80, "groovy [options] [args]", "options:", options, 2, 4, (String)null, false);
      pw.flush();
   }

   private static CommandLine parseCommandLine(Options options, String[] args) throws ParseException {
      CommandLineParser parser = new PosixParser();
      return parser.parse(options, args, true);
   }

   private static synchronized Options buildOptions() {
      Options options = new Options();
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("path");
      OptionBuilder.withDescription("Specify where to find the class files - must be first argument");
      options.addOption(OptionBuilder.create("classpath"));
      OptionBuilder.withLongOpt("classpath");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("path");
      OptionBuilder.withDescription("Aliases for '-classpath'");
      options.addOption(OptionBuilder.create("cp"));
      OptionBuilder.withLongOpt("define");
      OptionBuilder.withDescription("define a system property");
      OptionBuilder.hasArg(true);
      OptionBuilder.withArgName("name=value");
      options.addOption(OptionBuilder.create('D'));
      OptionBuilder.hasArg(false);
      OptionBuilder.withDescription("usage information");
      OptionBuilder.withLongOpt("help");
      options.addOption(OptionBuilder.create('h'));
      OptionBuilder.hasArg(false);
      OptionBuilder.withDescription("debug mode will print out full stack traces");
      OptionBuilder.withLongOpt("debug");
      options.addOption(OptionBuilder.create('d'));
      OptionBuilder.hasArg(false);
      OptionBuilder.withDescription("display the Groovy and JVM versions");
      OptionBuilder.withLongOpt("version");
      options.addOption(OptionBuilder.create('v'));
      OptionBuilder.withArgName("charset");
      OptionBuilder.hasArg();
      OptionBuilder.withDescription("specify the encoding of the files");
      OptionBuilder.withLongOpt("encoding");
      options.addOption(OptionBuilder.create('c'));
      OptionBuilder.withArgName("script");
      OptionBuilder.hasArg();
      OptionBuilder.withDescription("specify a command line script");
      options.addOption(OptionBuilder.create('e'));
      OptionBuilder.withArgName("extension");
      OptionBuilder.hasOptionalArg();
      OptionBuilder.withDescription("modify files in place; create backup if extension is given (e.g. '.bak')");
      options.addOption(OptionBuilder.create('i'));
      OptionBuilder.hasArg(false);
      OptionBuilder.withDescription("process files line by line using implicit 'line' variable");
      options.addOption(OptionBuilder.create('n'));
      OptionBuilder.hasArg(false);
      OptionBuilder.withDescription("process files line by line and print result (see also -n)");
      options.addOption(OptionBuilder.create('p'));
      OptionBuilder.withArgName("port");
      OptionBuilder.hasOptionalArg();
      OptionBuilder.withDescription("listen on a port and process inbound lines (default: 1960)");
      options.addOption(OptionBuilder.create('l'));
      OptionBuilder.withArgName("splitPattern");
      OptionBuilder.hasOptionalArg();
      OptionBuilder.withDescription("split lines using splitPattern (default '\\s') using implicit 'split' variable");
      OptionBuilder.withLongOpt("autosplit");
      options.addOption(OptionBuilder.create('a'));
      return options;
   }

   private static void setSystemPropertyFrom(String nameValue) {
      if (nameValue == null) {
         throw new IllegalArgumentException("argument should not be null");
      } else {
         int i = nameValue.indexOf("=");
         String name;
         String value;
         if (i == -1) {
            name = nameValue;
            value = Boolean.TRUE.toString();
         } else {
            name = nameValue.substring(0, i);
            value = nameValue.substring(i + 1, nameValue.length());
         }

         name = name.trim();
         System.setProperty(name, value);
      }
   }

   private static boolean process(CommandLine line) throws ParseException {
      List args = line.getArgList();
      if (line.hasOption('D')) {
         String[] values = line.getOptionValues('D');

         for(int i = 0; i < values.length; ++i) {
            setSystemPropertyFrom(values[i]);
         }
      }

      GroovyMain main = new GroovyMain();
      main.conf.setSourceEncoding(line.getOptionValue('c', main.conf.getSourceEncoding()));
      main.isScriptFile = !line.hasOption('e');
      main.debug = line.hasOption('d');
      main.conf.setDebug(main.debug);
      main.processFiles = line.hasOption('p') || line.hasOption('n');
      main.autoOutput = line.hasOption('p');
      main.editFiles = line.hasOption('i');
      if (main.editFiles) {
         main.backupExtension = line.getOptionValue('i');
      }

      main.autoSplit = line.hasOption('a');
      String sp = line.getOptionValue('a');
      if (sp != null) {
         main.splitPattern = sp;
      }

      if (main.isScriptFile) {
         if (args.isEmpty()) {
            throw new ParseException("neither -e or filename provided");
         }

         main.script = (String)args.remove(0);
         if (main.script.endsWith(".java")) {
            throw new ParseException("error: cannot compile file with .java extension: " + main.script);
         }
      } else {
         main.script = line.getOptionValue('e');
      }

      main.processSockets = line.hasOption('l');
      if (main.processSockets) {
         String p = line.getOptionValue('l', "1960");
         main.port = Integer.parseInt(p);
      }

      main.args = args;
      return main.run();
   }

   private boolean run() {
      try {
         if (this.processSockets) {
            this.processSockets();
         } else if (this.processFiles) {
            this.processFiles();
         } else {
            this.processOnce();
         }

         return true;
      } catch (CompilationFailedException var6) {
         System.err.println(var6);
         return false;
      } catch (Throwable var7) {
         Throwable e = var7;
         if (var7 instanceof InvokerInvocationException) {
            InvokerInvocationException iie = (InvokerInvocationException)var7;
            e = iie.getCause();
         }

         System.err.println("Caught: " + e);
         if (this.debug) {
            e.printStackTrace();
         } else {
            StackTraceElement[] stackTrace = e.getStackTrace();

            for(int i = 0; i < stackTrace.length; ++i) {
               StackTraceElement element = stackTrace[i];
               String fileName = element.getFileName();
               if (fileName != null && !fileName.endsWith(".java")) {
                  System.err.println("\tat " + element);
               }
            }
         }

         return false;
      }
   }

   private void processSockets() throws CompilationFailedException, IOException {
      GroovyShell groovy = new GroovyShell(this.conf);
      if (this.isScriptFile) {
         groovy.parse(DefaultGroovyMethods.getText(this.huntForTheScriptFile(this.script)));
      } else {
         groovy.parse(this.script);
      }

      new GroovySocketServer(groovy, this.isScriptFile, this.script, this.autoOutput, this.port);
   }

   public File huntForTheScriptFile(String input) {
      String scriptFileName = input.trim();
      File scriptFile = new File(scriptFileName);
      String[] standardExtensions = new String[]{".groovy", ".gvy", ".gy", ".gsh"};

      for(int i = 0; i < standardExtensions.length && !scriptFile.exists(); ++i) {
         scriptFile = new File(scriptFileName + standardExtensions[i]);
      }

      if (!scriptFile.exists()) {
         scriptFile = new File(scriptFileName);
      }

      return scriptFile;
   }

   private void processFiles() throws CompilationFailedException, IOException {
      GroovyShell groovy = new GroovyShell(this.conf);
      Script s;
      if (this.isScriptFile) {
         s = groovy.parse(this.huntForTheScriptFile(this.script));
      } else {
         s = groovy.parse(this.script, "main");
      }

      if (this.args.isEmpty()) {
         BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         PrintWriter writer = new PrintWriter(System.out);

         try {
            this.processReader(s, reader, writer);
         } finally {
            reader.close();
            writer.close();
         }
      } else {
         Iterator i = this.args.iterator();

         while(i.hasNext()) {
            String filename = (String)i.next();
            File file = this.huntForTheScriptFile(filename);
            this.processFile(s, file);
         }
      }

   }

   private void processFile(Script s, File file) throws IOException {
      if (!file.exists()) {
         throw new FileNotFoundException(file.getName());
      } else {
         if (!this.editFiles) {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            try {
               PrintWriter writer = new PrintWriter(System.out);
               this.processReader(s, reader, writer);
               writer.flush();
            } finally {
               reader.close();
            }
         } else {
            File backup;
            if (this.backupExtension == null) {
               backup = File.createTempFile("groovy_", ".tmp");
               backup.deleteOnExit();
            } else {
               backup = new File(file.getPath() + this.backupExtension);
            }

            backup.delete();
            if (!file.renameTo(backup)) {
               throw new IOException("unable to rename " + file + " to " + backup);
            }

            BufferedReader reader = new BufferedReader(new FileReader(backup));

            try {
               PrintWriter writer = new PrintWriter(new FileWriter(file));

               try {
                  this.processReader(s, reader, writer);
               } finally {
                  writer.close();
               }
            } finally {
               reader.close();
            }
         }

      }
   }

   private void processReader(Script s, BufferedReader reader, PrintWriter pw) throws IOException {
      String lineCountName = "count";
      s.setProperty(lineCountName, BigInteger.ZERO);
      String autoSplitName = "split";
      s.setProperty("out", pw);

      String line;
      while((line = reader.readLine()) != null) {
         s.setProperty("line", line);
         s.setProperty(lineCountName, ((BigInteger)s.getProperty(lineCountName)).add(BigInteger.ONE));
         if (this.autoSplit) {
            s.setProperty(autoSplitName, line.split(this.splitPattern));
         }

         Object o = s.run();
         if (this.autoOutput && o != null) {
            pw.println(o);
         }
      }

   }

   private void processOnce() throws CompilationFailedException, IOException {
      GroovyShell groovy = new GroovyShell(this.conf);
      if (this.isScriptFile) {
         groovy.run(this.huntForTheScriptFile(this.script), this.args);
      } else {
         groovy.run(this.script, "script_from_command_line", this.args);
      }

   }
}
