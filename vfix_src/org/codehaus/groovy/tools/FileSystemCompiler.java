package org.codehaus.groovy.tools;

import groovy.lang.GroovySystem;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.HelpFormatter;
import groovyjarjarcommonscli.OptionBuilder;
import groovyjarjarcommonscli.Options;
import groovyjarjarcommonscli.PosixParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.ConfigurationException;
import org.codehaus.groovy.tools.javac.JavaAwareCompilationUnit;

public class FileSystemCompiler {
   private final CompilationUnit unit;
   private static boolean displayStackTraceOnError = false;

   public FileSystemCompiler(CompilerConfiguration configuration) throws ConfigurationException {
      this(configuration, (CompilationUnit)null);
   }

   public FileSystemCompiler(CompilerConfiguration configuration, CompilationUnit cu) throws ConfigurationException {
      if (cu != null) {
         this.unit = cu;
      } else if (configuration.getJointCompilationOptions() != null) {
         this.unit = new JavaAwareCompilationUnit(configuration);
      } else {
         this.unit = new CompilationUnit(configuration);
      }

   }

   public void compile(String[] paths) throws Exception {
      this.unit.addSources(paths);
      this.unit.compile();
   }

   public void compile(File[] files) throws Exception {
      this.unit.addSources(files);
      this.unit.compile();
   }

   public static void displayHelp(Options options) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(80, "groovyc [options] <source-files>", "options:", options, "");
   }

   public static void displayVersion() {
      String version = GroovySystem.getVersion();
      System.err.println("Groovy compiler version " + version);
      System.err.println("Copyright 2003-2010 The Codehaus. http://groovy.codehaus.org/");
      System.err.println("");
   }

   public static int checkFiles(String[] filenames) {
      int errors = 0;
      String[] arr$ = filenames;
      int len$ = filenames.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String filename = arr$[i$];
         File file = new File(filename);
         if (!file.exists()) {
            System.err.println("error: file not found: " + file);
            ++errors;
         } else if (!file.canRead()) {
            System.err.println("error: file not readable: " + file);
            ++errors;
         }
      }

      return errors;
   }

   public static boolean validateFiles(String[] filenames) {
      return checkFiles(filenames) == 0;
   }

   public static void commandLineCompile(String[] args) throws Exception {
      Options options = createCompilationOptions();
      PosixParser cliParser = new PosixParser();
      CommandLine cli = cliParser.parse(options, args);
      if (cli.hasOption('h')) {
         displayHelp(options);
      } else if (cli.hasOption('v')) {
         displayVersion();
      } else {
         displayStackTraceOnError = cli.hasOption('e');
         CompilerConfiguration configuration = generateCompilerConfigurationFromOptions(cli);
         String[] filenames = generateFileNamesFromOptions(cli);
         boolean fileNameErrors = filenames == null;
         if (!fileNameErrors && filenames.length == 0) {
            displayHelp(options);
         } else {
            fileNameErrors = fileNameErrors && !validateFiles(filenames);
            if (!fileNameErrors) {
               doCompilation(configuration, (CompilationUnit)null, filenames);
            }

         }
      }
   }

   public static void main(String[] args) {
      try {
         commandLineCompile(args);
      } catch (Throwable var2) {
         (new ErrorReporter(var2, displayStackTraceOnError)).write(System.err);
         System.exit(1);
      }

   }

   public static void doCompilation(CompilerConfiguration configuration, CompilationUnit unit, String[] filenames) throws Exception {
      File tmpDir = null;

      try {
         if (configuration.getJointCompilationOptions() != null && !configuration.getJointCompilationOptions().containsKey("stubDir")) {
            tmpDir = createTempDir();
            configuration.getJointCompilationOptions().put("stubDir", tmpDir);
         }

         FileSystemCompiler compiler = new FileSystemCompiler(configuration, unit);
         compiler.compile(filenames);
      } finally {
         try {
            if (tmpDir != null) {
               deleteRecursive(tmpDir);
            }
         } catch (Throwable var10) {
            System.err.println("error: could not delete temp files - " + tmpDir.getPath());
         }

      }

   }

   public static String[] generateFileNamesFromOptions(CommandLine cli) {
      String[] filenames = cli.getArgs();
      List<String> fileList = new ArrayList(filenames.length);
      boolean errors = false;
      String[] arr$ = filenames;
      int len$ = filenames.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String filename = arr$[i$];
         if (filename.startsWith("@")) {
            try {
               BufferedReader br = new BufferedReader(new FileReader(filename.substring(1)));

               String file;
               while((file = br.readLine()) != null) {
                  fileList.add(file);
               }
            } catch (IOException var10) {
               System.err.println("error: file not readable: " + filename.substring(1));
               errors = true;
            }
         } else {
            fileList.addAll(Arrays.asList(filenames));
         }
      }

      if (errors) {
         return null;
      } else {
         return (String[])fileList.toArray(new String[fileList.size()]);
      }
   }

   public static CompilerConfiguration generateCompilerConfigurationFromOptions(CommandLine cli) {
      CompilerConfiguration configuration = new CompilerConfiguration();
      if (cli.hasOption("classpath")) {
         configuration.setClasspath(cli.getOptionValue("classpath"));
      }

      if (cli.hasOption('d')) {
         configuration.setTargetDirectory(cli.getOptionValue('d'));
      }

      if (cli.hasOption("encoding")) {
         configuration.setSourceEncoding(cli.getOptionValue("encoding"));
      }

      if (cli.hasOption('j')) {
         Map<String, Object> compilerOptions = new HashMap();
         String[] opts = cli.getOptionValues("J");
         compilerOptions.put("namedValues", opts);
         opts = cli.getOptionValues("F");
         compilerOptions.put("flags", opts);
         configuration.setJointCompilationOptions(compilerOptions);
      }

      return configuration;
   }

   public static Options createCompilationOptions() {
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
      OptionBuilder.withLongOpt("sourcepath");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("path");
      OptionBuilder.withDescription("Specify where to find the source files");
      options.addOption(OptionBuilder.create());
      OptionBuilder.withLongOpt("temp");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("temp");
      OptionBuilder.withDescription("Specify temporary directory");
      options.addOption(OptionBuilder.create());
      OptionBuilder.withLongOpt("encoding");
      OptionBuilder.hasArg();
      OptionBuilder.withArgName("encoding");
      OptionBuilder.withDescription("Specify the encoding of the user class files");
      options.addOption(OptionBuilder.create());
      OptionBuilder.hasArg();
      OptionBuilder.withDescription("Specify where to place generated class files");
      options.addOption(OptionBuilder.create('d'));
      OptionBuilder.withLongOpt("help");
      OptionBuilder.withDescription("Print a synopsis of standard options");
      options.addOption(OptionBuilder.create('h'));
      OptionBuilder.withLongOpt("version");
      OptionBuilder.withDescription("Print the version");
      options.addOption(OptionBuilder.create('v'));
      OptionBuilder.withLongOpt("exception");
      OptionBuilder.withDescription("Print stack trace on error");
      options.addOption(OptionBuilder.create('e'));
      OptionBuilder.withLongOpt("jointCompilation");
      OptionBuilder.withDescription("Attach javac compiler to compile .java files");
      options.addOption(OptionBuilder.create('j'));
      OptionBuilder.withArgName("property=value");
      OptionBuilder.withValueSeparator();
      OptionBuilder.hasArgs(2);
      OptionBuilder.withDescription("name-value pairs to pass to javac");
      options.addOption(OptionBuilder.create("J"));
      OptionBuilder.withArgName("flag");
      OptionBuilder.hasArg();
      OptionBuilder.withDescription("passed to javac for joint compilation");
      options.addOption(OptionBuilder.create("F"));
      return options;
   }

   public static File createTempDir() throws IOException {
      int MAXTRIES = true;
      int accessDeniedCounter = 0;
      File tempFile = null;
      int i = 0;

      while(i < 3) {
         try {
            tempFile = File.createTempFile("groovy-generated-", "-java-source");
            tempFile.delete();
            tempFile.mkdirs();
            break;
         } catch (IOException var7) {
            if (var7.getMessage().startsWith("Access is denied")) {
               ++accessDeniedCounter;

               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var6) {
               }
            }

            if (i == 2) {
               if (accessDeniedCounter == 3) {
                  String msg = "Access is denied.\nWe tried " + accessDeniedCounter + " times to create a temporary directory" + " and failed each time. If you are on Windows" + " you are possibly victim to" + " http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6325169. " + " this is no bug in Groovy.";
                  throw new IOException(msg);
               }

               throw var7;
            }

            ++i;
         }
      }

      return tempFile;
   }

   public static void deleteRecursive(File file) {
      if (file.exists()) {
         if (file.isFile()) {
            file.delete();
         } else if (file.isDirectory()) {
            File[] files = file.listFiles();

            for(int i = 0; i < files.length; ++i) {
               deleteRecursive(files[i]);
            }

            file.delete();
         }

      }
   }
}
