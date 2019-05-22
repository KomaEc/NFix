package org.codehaus.groovy.control;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import org.codehaus.groovy.control.io.NullWriter;

public class CompilerConfiguration {
   private static final String JDK5_CLASSNAME_CHECK = "java.lang.annotation.Annotation";
   public static final String POST_JDK5 = "1.5";
   public static final String PRE_JDK5 = "1.4";
   public static final String currentJVMVersion = getVMVersion();
   public static final CompilerConfiguration DEFAULT = new CompilerConfiguration();
   private int warningLevel;
   private String sourceEncoding;
   private PrintWriter output;
   private File targetDirectory;
   private LinkedList<String> classpath;
   private boolean verbose;
   private boolean debug;
   private int tolerance;
   private String scriptBaseClass;
   private ParserPluginFactory pluginFactory;
   private String defaultScriptExtension;
   private Set<String> scriptExtensions;
   private boolean recompileGroovySource;
   private int minimumRecompilationInterval;
   private String targetBytecode;
   private Map<String, Object> jointCompilationOptions;

   public CompilerConfiguration() {
      this.scriptExtensions = new LinkedHashSet();
      this.setWarningLevel(1);
      this.setOutput((PrintWriter)null);
      this.setTargetDirectory((File)null);
      this.setClasspath("");
      this.setVerbose(false);
      this.setDebug(false);
      this.setTolerance(10);
      this.setScriptBaseClass((String)null);
      this.setRecompileGroovySource(false);
      this.setMinimumRecompilationInterval(100);
      String targetByteCode = null;

      try {
         targetByteCode = System.getProperty("groovy.target.bytecode", targetByteCode);
      } catch (Exception var10) {
      }

      if (targetByteCode != null) {
         this.setTargetBytecode(targetByteCode);
      } else {
         this.setTargetBytecode(getVMVersion());
      }

      String tmpDefaultScriptExtension = null;

      try {
         tmpDefaultScriptExtension = System.getProperty("groovy.default.scriptExtension");
      } catch (Exception var9) {
      }

      if (tmpDefaultScriptExtension != null) {
         this.setDefaultScriptExtension(tmpDefaultScriptExtension);
      } else {
         this.setDefaultScriptExtension(".groovy");
      }

      String encoding = null;

      try {
         encoding = System.getProperty("file.encoding", "US-ASCII");
      } catch (Exception var8) {
      }

      try {
         encoding = System.getProperty("groovy.source.encoding", encoding);
      } catch (Exception var7) {
      }

      this.setSourceEncoding(encoding);

      try {
         this.setOutput(new PrintWriter(System.err));
      } catch (Exception var6) {
      }

      try {
         String target = System.getProperty("groovy.target.directory");
         if (target != null) {
            this.setTargetDirectory(target);
         }
      } catch (Exception var5) {
      }

   }

   public CompilerConfiguration(CompilerConfiguration configuration) {
      this.scriptExtensions = new LinkedHashSet();
      this.setWarningLevel(configuration.getWarningLevel());
      this.setOutput(configuration.getOutput());
      this.setTargetDirectory(configuration.getTargetDirectory());
      this.setClasspathList(new LinkedList(configuration.getClasspath()));
      this.setVerbose(configuration.getVerbose());
      this.setDebug(configuration.getDebug());
      this.setTolerance(configuration.getTolerance());
      this.setScriptBaseClass(configuration.getScriptBaseClass());
      this.setRecompileGroovySource(configuration.getRecompileGroovySource());
      this.setMinimumRecompilationInterval(configuration.getMinimumRecompilationInterval());
      this.setTargetBytecode(configuration.getTargetBytecode());
      this.setDefaultScriptExtension(configuration.getDefaultScriptExtension());
      this.setSourceEncoding(configuration.getSourceEncoding());
      this.setOutput(configuration.getOutput());
      this.setTargetDirectory(configuration.getTargetDirectory());
      Map<String, Object> jointCompilationOptions = configuration.getJointCompilationOptions();
      if (jointCompilationOptions != null) {
         jointCompilationOptions = new HashMap((Map)jointCompilationOptions);
      }

      this.setJointCompilationOptions((Map)jointCompilationOptions);
      this.setPluginFactory(configuration.getPluginFactory());
      this.setScriptExtensions(configuration.getScriptExtensions());
   }

   public CompilerConfiguration(Properties configuration) throws ConfigurationException {
      this();
      this.configure(configuration);
   }

   public void configure(Properties configuration) throws ConfigurationException {
      String text = null;
      int numeric = false;
      int numeric = this.getWarningLevel();

      try {
         text = configuration.getProperty("groovy.warnings", "likely errors");
         numeric = Integer.parseInt(text);
      } catch (NumberFormatException var7) {
         text = text.toLowerCase();
         if (text.equals("none")) {
            numeric = 0;
         } else if (text.startsWith("likely")) {
            numeric = 1;
         } else if (text.startsWith("possible")) {
            numeric = 2;
         } else {
            if (!text.startsWith("paranoia")) {
               throw new ConfigurationException("unrecogized groovy.warnings: " + text);
            }

            numeric = 3;
         }
      }

      this.setWarningLevel(numeric);
      text = configuration.getProperty("groovy.source.encoding");
      if (text != null) {
         this.setSourceEncoding(text);
      }

      text = configuration.getProperty("groovy.target.directory");
      if (text != null) {
         this.setTargetDirectory(text);
      }

      text = configuration.getProperty("groovy.target.bytecode");
      if (text != null) {
         this.setTargetBytecode(text);
      }

      text = configuration.getProperty("groovy.classpath");
      if (text != null) {
         this.setClasspath(text);
      }

      text = configuration.getProperty("groovy.output.verbose");
      if (text != null && text.equalsIgnoreCase("true")) {
         this.setVerbose(true);
      }

      text = configuration.getProperty("groovy.output.debug");
      if (text != null && text.equalsIgnoreCase("true")) {
         this.setDebug(true);
      }

      numeric = true;

      try {
         text = configuration.getProperty("groovy.errors.tolerance", "10");
         numeric = Integer.parseInt(text);
      } catch (NumberFormatException var5) {
         throw new ConfigurationException(var5);
      }

      this.setTolerance(numeric);
      text = configuration.getProperty("groovy.script.base");
      if (text != null) {
         this.setScriptBaseClass(text);
      }

      text = configuration.getProperty("groovy.recompile");
      if (text != null) {
         this.setRecompileGroovySource(text.equalsIgnoreCase("true"));
      }

      numeric = true;

      try {
         text = configuration.getProperty("groovy.recompile.minimumIntervall");
         if (text == null) {
            text = configuration.getProperty("groovy.recompile.minimumInterval");
         }

         if (text != null) {
            numeric = Integer.parseInt(text);
         } else {
            numeric = 100;
         }
      } catch (NumberFormatException var6) {
         throw new ConfigurationException(var6);
      }

      this.setMinimumRecompilationInterval(numeric);
   }

   public int getWarningLevel() {
      return this.warningLevel;
   }

   public void setWarningLevel(int level) {
      if (level >= 0 && level <= 3) {
         this.warningLevel = level;
      } else {
         this.warningLevel = 1;
      }

   }

   public String getSourceEncoding() {
      return this.sourceEncoding;
   }

   public void setSourceEncoding(String encoding) {
      if (encoding == null) {
         encoding = "US-ASCII";
      }

      this.sourceEncoding = encoding;
   }

   public PrintWriter getOutput() {
      return this.output;
   }

   public void setOutput(PrintWriter output) {
      if (output == null) {
         this.output = new PrintWriter(NullWriter.DEFAULT);
      } else {
         this.output = output;
      }

   }

   public File getTargetDirectory() {
      return this.targetDirectory;
   }

   public void setTargetDirectory(String directory) {
      if (directory != null && directory.length() > 0) {
         this.targetDirectory = new File(directory);
      } else {
         this.targetDirectory = null;
      }

   }

   public void setTargetDirectory(File directory) {
      this.targetDirectory = directory;
   }

   public List<String> getClasspath() {
      return this.classpath;
   }

   public void setClasspath(String classpath) {
      this.classpath = new LinkedList();
      StringTokenizer tokenizer = new StringTokenizer(classpath, File.pathSeparator);

      while(tokenizer.hasMoreTokens()) {
         this.classpath.add(tokenizer.nextToken());
      }

   }

   public void setClasspathList(List<String> l) {
      this.classpath = new LinkedList(l);
   }

   public boolean getVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public boolean getDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public int getTolerance() {
      return this.tolerance;
   }

   public void setTolerance(int tolerance) {
      this.tolerance = tolerance;
   }

   public String getScriptBaseClass() {
      return this.scriptBaseClass;
   }

   public void setScriptBaseClass(String scriptBaseClass) {
      this.scriptBaseClass = scriptBaseClass;
   }

   public ParserPluginFactory getPluginFactory() {
      if (this.pluginFactory == null) {
         this.pluginFactory = ParserPluginFactory.newInstance(true);
      }

      return this.pluginFactory;
   }

   public void setPluginFactory(ParserPluginFactory pluginFactory) {
      this.pluginFactory = pluginFactory;
   }

   public String getDefaultScriptExtension() {
      return this.defaultScriptExtension;
   }

   public void setScriptExtensions(Set<String> scriptExtensions) {
      if (scriptExtensions == null) {
         scriptExtensions = new LinkedHashSet();
      }

      this.scriptExtensions = (Set)scriptExtensions;
   }

   public Set<String> getScriptExtensions() {
      if (this.scriptExtensions == null || this.scriptExtensions.isEmpty()) {
         this.scriptExtensions = SourceExtensionHandler.getRegisteredExtensions(this.getClass().getClassLoader());
      }

      return this.scriptExtensions;
   }

   public void setDefaultScriptExtension(String defaultScriptExtension) {
      this.defaultScriptExtension = defaultScriptExtension;
   }

   public void setRecompileGroovySource(boolean recompile) {
      this.recompileGroovySource = recompile;
   }

   public boolean getRecompileGroovySource() {
      return this.recompileGroovySource;
   }

   public void setMinimumRecompilationInterval(int time) {
      this.minimumRecompilationInterval = Math.max(0, time);
   }

   public int getMinimumRecompilationInterval() {
      return this.minimumRecompilationInterval;
   }

   public void setTargetBytecode(String version) {
      if ("1.4".equals(version) || "1.5".equals(version)) {
         this.targetBytecode = version;
      }

   }

   public String getTargetBytecode() {
      return this.targetBytecode;
   }

   private static String getVMVersion() {
      try {
         Class.forName("java.lang.annotation.Annotation");
         return "1.5";
      } catch (Exception var1) {
         return "1.4";
      }
   }

   public Map<String, Object> getJointCompilationOptions() {
      return this.jointCompilationOptions;
   }

   public void setJointCompilationOptions(Map<String, Object> options) {
      this.jointCompilationOptions = options;
   }
}
