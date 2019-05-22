package org.testng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteWatchdog;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.testng.collections.Lists;
import org.testng.internal.Utils;

public class TestNGAntTask extends Task {
   protected CommandlineJava m_javaCommand;
   protected List<ResourceCollection> m_xmlFilesets = Lists.newArrayList();
   protected List<ResourceCollection> m_classFilesets = Lists.newArrayList();
   protected File m_outputDir;
   protected File m_testjar;
   protected File m_workingDir;
   private Integer m_timeout;
   private List<String> m_listeners = Lists.newArrayList();
   private List<String> m_methodselectors = Lists.newArrayList();
   private String m_objectFactory;
   protected String m_testRunnerFactory;
   private boolean m_delegateCommandSystemProperties = false;
   protected Environment m_environment = new Environment();
   protected String m_mainClass = TestNG.class.getName();
   protected boolean m_dump;
   private boolean m_dumpEnv;
   private boolean m_dumpSys;
   protected boolean m_assertEnabled = true;
   protected boolean m_haltOnFailure;
   protected String m_onHaltTarget;
   protected String m_failurePropertyName;
   protected boolean m_haltOnSkipped;
   protected String m_skippedPropertyName;
   protected boolean m_haltOnFSP;
   protected String m_fspPropertyName;
   protected String m_includedGroups;
   protected String m_excludedGroups;
   protected String m_parallelMode;
   protected String m_threadCount;
   protected String m_dataproviderthreadCount;
   protected String m_configFailurePolicy;
   protected Boolean m_randomizeSuites;
   public String m_useDefaultListeners;
   private String m_suiteName = "Ant suite";
   private String m_testName = "Ant test";
   private Boolean m_skipFailedInvocationCounts;
   private String m_methods;
   private TestNGAntTask.Mode mode;
   private List<ReporterConfig> reporterConfigs;
   private String m_testNames;
   private Integer m_verbose;
   private Integer m_suiteThreadPoolSize;
   private String m_xmlPathInJar;

   public TestNGAntTask() {
      this.mode = TestNGAntTask.Mode.testng;
      this.reporterConfigs = Lists.newArrayList();
      this.m_testNames = "";
      this.m_verbose = null;
   }

   public void setParallel(String parallel) {
      this.m_parallelMode = parallel;
   }

   public void setThreadCount(String threadCount) {
      this.m_threadCount = threadCount;
   }

   public void setDataProviderThreadCount(String dataproviderthreadCount) {
      this.m_dataproviderthreadCount = dataproviderthreadCount;
   }

   public void setUseDefaultListeners(String f) {
      this.m_useDefaultListeners = f;
   }

   public void setHaltonfailure(boolean value) {
      this.m_haltOnFailure = value;
   }

   public void setOnHaltTarget(String targetName) {
      this.m_onHaltTarget = targetName;
   }

   public void setFailureProperty(String propertyName) {
      this.m_failurePropertyName = propertyName;
   }

   public void setHaltonskipped(boolean value) {
      this.m_haltOnSkipped = value;
   }

   public void setSkippedProperty(String propertyName) {
      this.m_skippedPropertyName = propertyName;
   }

   public void setHaltonFSP(boolean value) {
      this.m_haltOnFSP = value;
   }

   public void setFSPProperty(String propertyName) {
      this.m_fspPropertyName = propertyName;
   }

   public void setDelegateCommandSystemProperties(boolean value) {
      this.m_delegateCommandSystemProperties = value;
   }

   public void setDumpCommand(boolean verbose) {
      this.m_dump = verbose;
   }

   public void setDumpEnv(boolean verbose) {
      this.m_dumpEnv = verbose;
   }

   public void setDumpSys(boolean verbose) {
      this.m_dumpSys = verbose;
   }

   public void setEnableAssert(boolean flag) {
      this.m_assertEnabled = flag;
   }

   public void setWorkingDir(File workingDir) {
      this.m_workingDir = workingDir;
   }

   public void setJvm(String jvm) {
      this.getJavaCommand().setVm(jvm);
   }

   public void setTimeout(Integer value) {
      this.m_timeout = value;
   }

   public Commandline.Argument createJvmarg() {
      return this.getJavaCommand().createVmArgument();
   }

   public void addSysproperty(Environment.Variable sysp) {
      this.getJavaCommand().addSysproperty(sysp);
   }

   public void addEnv(Environment.Variable var) {
      this.m_environment.addVariable(var);
   }

   public Path createClasspath() {
      return this.getJavaCommand().createClasspath(this.getProject()).createPath();
   }

   public Path createBootclasspath() {
      return this.getJavaCommand().createBootclasspath(this.getProject()).createPath();
   }

   public void setClasspath(Path s) {
      this.createClasspath().append(s);
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void addXmlfileset(FileSet fs) {
      this.m_xmlFilesets.add(fs);
   }

   public void setXmlfilesetRef(Reference ref) {
      this.m_xmlFilesets.add(this.createResourceCollection(ref));
   }

   public void addClassfileset(FileSet fs) {
      this.m_classFilesets.add(this.appendClassSelector(fs));
   }

   public void setClassfilesetRef(Reference ref) {
      this.m_classFilesets.add(this.createResourceCollection(ref));
   }

   public void setTestNames(String testNames) {
      this.m_testNames = testNames;
   }

   public void setSuiteRunnerClass(String s) {
      this.m_mainClass = s;
   }

   public void setSuiteName(String s) {
      this.m_suiteName = s;
   }

   public void setTestName(String s) {
      this.m_testName = s;
   }

   public void setJUnit(boolean value) {
      this.mode = value ? TestNGAntTask.Mode.junit : TestNGAntTask.Mode.testng;
   }

   public void setMode(TestNGAntTask.Mode mode) {
      this.mode = mode;
   }

   public void setOutputDir(File dir) {
      this.m_outputDir = dir;
   }

   public void setTestJar(File s) {
      this.m_testjar = s;
   }

   public void setGroups(String groups) {
      this.m_includedGroups = groups;
   }

   public void setExcludedGroups(String groups) {
      this.m_excludedGroups = groups;
   }

   public void setVerbose(Integer verbose) {
      this.m_verbose = verbose;
   }

   public void setReporter(String listener) {
      this.m_listeners.add(listener);
   }

   public void setObjectFactory(String className) {
      this.m_objectFactory = className;
   }

   public void setTestRunnerFactory(String testRunnerFactory) {
      this.m_testRunnerFactory = testRunnerFactory;
   }

   public void setSuiteThreadPoolSize(Integer n) {
      this.m_suiteThreadPoolSize = n;
   }

   /** @deprecated */
   @Deprecated
   public void setListener(String listener) {
      this.m_listeners.add(listener);
   }

   public void setListeners(String listeners) {
      StringTokenizer st = new StringTokenizer(listeners, " ,");

      while(st.hasMoreTokens()) {
         this.m_listeners.add(st.nextToken());
      }

   }

   public void setMethodSelectors(String methodSelectors) {
      StringTokenizer st = new StringTokenizer(methodSelectors, " ,");

      while(st.hasMoreTokens()) {
         this.m_methodselectors.add(st.nextToken());
      }

   }

   public void setConfigFailurePolicy(String failurePolicy) {
      this.m_configFailurePolicy = failurePolicy;
   }

   public void setRandomizeSuites(Boolean randomizeSuites) {
      this.m_randomizeSuites = randomizeSuites;
   }

   public void setMethods(String methods) {
      this.m_methods = methods;
   }

   public void execute() throws BuildException {
      this.validateOptions();
      CommandlineJava cmd = this.getJavaCommand();
      cmd.setClassname(this.m_mainClass);
      if (this.m_assertEnabled) {
         cmd.createVmArgument().setValue("-ea");
      }

      if (this.m_delegateCommandSystemProperties) {
         this.delegateCommandSystemProperties();
      }

      List<String> argv = this.createArguments();
      String fileName = "";
      FileWriter fw = null;
      BufferedWriter bw = null;

      try {
         File f = File.createTempFile("testng", "");
         fileName = f.getAbsolutePath();
         if (!this.m_dump) {
            f.deleteOnExit();
         }

         fw = new FileWriter(f);
         bw = new BufferedWriter(fw);
         Iterator i$ = argv.iterator();

         while(i$.hasNext()) {
            String arg = (String)i$.next();
            bw.write(arg);
            bw.newLine();
         }

         bw.flush();
      } catch (IOException var17) {
         var17.printStackTrace();
      } finally {
         try {
            if (bw != null) {
               bw.close();
            }

            if (fw != null) {
               fw.close();
            }
         } catch (IOException var16) {
            var16.printStackTrace();
         }

      }

      this.printDebugInfo(fileName);
      this.createClasspath().setLocation(this.findJar());
      cmd.createArgument().setValue("@" + fileName);
      ExecuteWatchdog watchdog = this.createWatchdog();
      boolean wasKilled = false;
      int exitValue = this.executeAsForked(cmd, watchdog);
      if (null != watchdog) {
         wasKilled = watchdog.killedProcess();
      }

      this.actOnResult(exitValue, wasKilled);
   }

   private List<String> createArguments() {
      List<String> argv = Lists.newArrayList();
      this.addBooleanIfTrue(argv, "-junit", this.mode == TestNGAntTask.Mode.junit);
      this.addBooleanIfTrue(argv, "-mixed", this.mode == TestNGAntTask.Mode.mixed);
      this.addBooleanIfTrue(argv, "-skipfailedinvocationcounts", this.m_skipFailedInvocationCounts);
      this.addIntegerIfNotNull(argv, "-log", this.m_verbose);
      this.addDefaultListeners(argv);
      this.addOutputDir(argv);
      this.addFileIfFile(argv, "-testjar", this.m_testjar);
      this.addStringIfNotBlank(argv, "-groups", this.m_includedGroups);
      this.addStringIfNotBlank(argv, "-excludegroups", this.m_excludedGroups);
      this.addFilesOfRCollection(argv, "-testclass", this.m_classFilesets);
      this.addListOfStringIfNotEmpty(argv, "-listener", this.m_listeners);
      this.addListOfStringIfNotEmpty(argv, "-methodselectors", this.m_methodselectors);
      this.addStringIfNotNull(argv, "-objectfactory", this.m_objectFactory);
      this.addStringIfNotNull(argv, "-testrunfactory", this.m_testRunnerFactory);
      this.addStringIfNotNull(argv, "-parallel", this.m_parallelMode);
      this.addStringIfNotNull(argv, "-configfailurepolicy", this.m_configFailurePolicy);
      this.addBooleanIfTrue(argv, "-randomizesuites", this.m_randomizeSuites);
      this.addStringIfNotNull(argv, "-threadcount", this.m_threadCount);
      this.addStringIfNotNull(argv, "-dataproviderthreadcount", this.m_dataproviderthreadCount);
      this.addStringIfNotBlank(argv, "-suitename", this.m_suiteName);
      this.addStringIfNotBlank(argv, "-testname", this.m_testName);
      this.addStringIfNotBlank(argv, "-testnames", this.m_testNames);
      this.addStringIfNotBlank(argv, "-methods", this.m_methods);
      this.addReporterConfigs(argv);
      this.addIntegerIfNotNull(argv, "-suitethreadpoolsize", this.m_suiteThreadPoolSize);
      this.addStringIfNotNull(argv, "-xmlpathinjar", this.m_xmlPathInJar);
      this.addXmlFiles(argv);
      return argv;
   }

   private void addDefaultListeners(List<String> argv) {
      if (this.m_useDefaultListeners != null) {
         String useDefaultListeners = "false";
         if ("yes".equalsIgnoreCase(this.m_useDefaultListeners) || "true".equalsIgnoreCase(this.m_useDefaultListeners)) {
            useDefaultListeners = "true";
         }

         argv.add("-usedefaultlisteners");
         argv.add(useDefaultListeners);
      }

   }

   private void addOutputDir(List<String> argv) {
      if (null != this.m_outputDir) {
         if (!this.m_outputDir.exists()) {
            this.m_outputDir.mkdirs();
         }

         if (!this.m_outputDir.isDirectory()) {
            throw new BuildException("Output directory is not a directory: " + this.m_outputDir);
         }

         argv.add("-d");
         argv.add(this.m_outputDir.getAbsolutePath());
      }

   }

   private void addReporterConfigs(List<String> argv) {
      Iterator i$ = this.reporterConfigs.iterator();

      while(i$.hasNext()) {
         ReporterConfig reporterConfig = (ReporterConfig)i$.next();
         argv.add("-reporter");
         argv.add(reporterConfig.serialize());
      }

   }

   private void addFilesOfRCollection(List<String> argv, String name, List<ResourceCollection> resources) {
      this.addArgumentsIfNotEmpty(argv, name, this.getFiles(resources), ",");
   }

   private void addListOfStringIfNotEmpty(List<String> argv, String name, List<String> arguments) {
      this.addArgumentsIfNotEmpty(argv, name, arguments, ";");
   }

   private void addArgumentsIfNotEmpty(List<String> argv, String name, List<String> arguments, String separator) {
      if (arguments != null && !arguments.isEmpty()) {
         argv.add(name);
         String value = Utils.join(arguments, separator);
         argv.add(value);
      }

   }

   private void addFileIfFile(List<String> argv, String name, File file) {
      if (null != file && file.isFile()) {
         argv.add(name);
         argv.add(file.getAbsolutePath());
      }

   }

   private void addBooleanIfTrue(List<String> argv, String name, Boolean value) {
      if (Boolean.TRUE.equals(value)) {
         argv.add(name);
      }

   }

   private void addIntegerIfNotNull(List<String> argv, String name, Integer value) {
      if (value != null) {
         argv.add(name);
         argv.add(value.toString());
      }

   }

   private void addStringIfNotNull(List<String> argv, String name, String value) {
      if (value != null) {
         argv.add(name);
         argv.add(value);
      }

   }

   private void addStringIfNotBlank(List<String> argv, String name, String value) {
      if (Utils.isStringNotBlank(value)) {
         argv.add(name);
         argv.add(value);
      }

   }

   private void addXmlFiles(List<String> argv) {
      Iterator i$ = this.getSuiteFileNames().iterator();

      while(i$.hasNext()) {
         String file = (String)i$.next();
         argv.add(file);
      }

   }

   protected List<String> getSuiteFileNames() {
      List<String> result = Lists.newArrayList();
      Iterator i$ = this.getFiles(this.m_xmlFilesets).iterator();

      while(i$.hasNext()) {
         String file = (String)i$.next();
         result.add(file);
      }

      return result;
   }

   private void delegateCommandSystemProperties() {
      Iterator i$ = this.getProject().getUserProperties().keySet().iterator();

      while(i$.hasNext()) {
         Object propKey = i$.next();
         String propName = (String)propKey;
         String propVal = this.getProject().getUserProperty(propName);
         if (propName.startsWith("ant.")) {
            this.log("Excluding ant property: " + propName + ": " + propVal, 4);
         } else {
            this.log("Including user property: " + propName + ": " + propVal, 4);
            Environment.Variable var = new Environment.Variable();
            var.setKey(propName);
            var.setValue(propVal);
            this.addSysproperty(var);
         }
      }

   }

   private void printDebugInfo(String fileName) {
      if (this.m_dumpSys) {
         System.out.println("* SYSTEM PROPERTIES *");
         Properties props = System.getProperties();
         Enumeration en = props.propertyNames();

         while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            System.out.println(key + ": " + props.getProperty(key));
         }

         System.out.println("");
      }

      if (this.m_dumpEnv) {
         String[] vars = this.m_environment.getVariables();
         if (null != vars && vars.length > 0) {
            System.out.println("* ENVIRONMENT *");
            String[] arr$ = vars;
            int len$ = vars.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String v = arr$[i$];
               System.out.println(v);
            }

            System.out.println("");
         }
      }

      if (this.m_dump) {
         this.dumpCommand(fileName);
      }

   }

   private void ppp(String string) {
      System.out.println("[TestNGAntTask] " + string);
   }

   protected void actOnResult(int exitValue, boolean wasKilled) {
      if (exitValue == -1) {
         this.executeHaltTarget(exitValue);
         throw new BuildException("an error occured when running TestNG tests");
      } else {
         if ((exitValue & 8) == 8) {
            if (this.m_haltOnFailure) {
               this.executeHaltTarget(exitValue);
               throw new BuildException("No tests were run");
            }

            if (null != this.m_failurePropertyName) {
               this.getProject().setNewProperty(this.m_failurePropertyName, "true");
            }

            this.log("TestNG haven't found any tests to be run", 4);
         }

         boolean failed = (exitValue & 1) == 1 || wasKilled;
         if (failed) {
            String msg = wasKilled ? "The tests timed out and were killed." : "The tests failed.";
            if (this.m_haltOnFailure) {
               this.executeHaltTarget(exitValue);
               throw new BuildException(msg);
            }

            if (null != this.m_failurePropertyName) {
               this.getProject().setNewProperty(this.m_failurePropertyName, "true");
            }

            this.log(msg, 2);
         }

         if ((exitValue & 2) == 2) {
            if (this.m_haltOnSkipped) {
               this.executeHaltTarget(exitValue);
               throw new BuildException("There are TestNG SKIPPED tests");
            }

            if (null != this.m_skippedPropertyName) {
               this.getProject().setNewProperty(this.m_skippedPropertyName, "true");
            }

            this.log("There are TestNG SKIPPED tests", 4);
         }

         if ((exitValue & 4) == 4) {
            if (this.m_haltOnFSP) {
               this.executeHaltTarget(exitValue);
               throw new BuildException("There are TestNG FAILED WITHIN SUCCESS PERCENTAGE tests");
            }

            if (null != this.m_fspPropertyName) {
               this.getProject().setNewProperty(this.m_fspPropertyName, "true");
            }

            this.log("There are TestNG FAILED WITHIN SUCCESS PERCENTAGE tests", 4);
         }

      }
   }

   private void executeHaltTarget(int exitValue) {
      if (this.m_onHaltTarget != null) {
         if (this.m_outputDir != null) {
            this.getProject().setProperty("testng.outputdir", this.m_outputDir.getAbsolutePath());
         }

         this.getProject().setProperty("testng.returncode", String.valueOf(exitValue));
         Target t = (Target)this.getProject().getTargets().get(this.m_onHaltTarget);
         if (t != null) {
            t.execute();
         }
      }

   }

   protected int executeAsForked(CommandlineJava cmd, ExecuteWatchdog watchdog) {
      Execute execute = new Execute(new TestNGAntTask.TestNGLogSH(this, 2, 1, this.m_verbose == null || this.m_verbose < 5), watchdog);
      execute.setCommandline(cmd.getCommandline());
      execute.setAntRun(this.getProject());
      if (this.m_workingDir != null) {
         if (this.m_workingDir.exists() && this.m_workingDir.isDirectory()) {
            execute.setWorkingDirectory(this.m_workingDir);
         } else {
            this.log("Ignoring invalid working directory : " + this.m_workingDir, 1);
         }
      }

      String[] environment = this.m_environment.getVariables();
      if (null != environment) {
         String[] arr$ = environment;
         int len$ = environment.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String envEntry = arr$[i$];
            this.log("Setting environment variable: " + envEntry, 3);
         }
      }

      execute.setEnvironment(environment);
      this.log(cmd.describeCommand(), 3);

      try {
         int retVal = execute.execute();
         return retVal;
      } catch (IOException var9) {
         throw new BuildException("Process fork failed.", var9, this.getLocation());
      }
   }

   protected CommandlineJava getJavaCommand() {
      if (null == this.m_javaCommand) {
         this.m_javaCommand = new CommandlineJava();
      }

      return this.m_javaCommand;
   }

   protected ExecuteWatchdog createWatchdog() {
      return this.m_timeout == null ? null : new ExecuteWatchdog(this.m_timeout.longValue());
   }

   protected void validateOptions() throws BuildException {
      int suiteCount = this.getSuiteFileNames().size();
      if (suiteCount != 0 || this.m_classFilesets.size() != 0 || !Utils.isStringEmpty(this.m_methods) || null != this.m_testjar && this.m_testjar.isFile()) {
         if (null != this.m_includedGroups && this.m_classFilesets.size() == 0 && suiteCount == 0) {
            throw new BuildException("No class filesets or xml file sets specified while using groups");
         } else if (this.m_onHaltTarget != null && !this.getProject().getTargets().containsKey(this.m_onHaltTarget)) {
            throw new BuildException("Target " + this.m_onHaltTarget + " not found in this project");
         }
      } else {
         throw new BuildException("No suites, classes, methods or jar file was specified.");
      }
   }

   private ResourceCollection createResourceCollection(Reference ref) {
      Object o = ref.getReferencedObject();
      if (!(o instanceof ResourceCollection)) {
         throw new BuildException("Only File based ResourceCollections are supported.");
      } else {
         ResourceCollection rc = (ResourceCollection)o;
         if (!rc.isFilesystemOnly()) {
            throw new BuildException("Only ResourceCollections from local file system are supported.");
         } else {
            return rc;
         }
      }
   }

   private FileSet appendClassSelector(FileSet fs) {
      FilenameSelector selector = new FilenameSelector();
      selector.setName("**/*.class");
      selector.setProject(this.getProject());
      fs.appendSelector(selector);
      return fs;
   }

   private File findJar() {
      Class thisClass = this.getClass();
      String resource = thisClass.getName().replace('.', '/') + ".class";
      URL url = thisClass.getClassLoader().getResource(resource);
      if (null != url) {
         String u = url.toString();
         int tail;
         String dirName;
         if (u.startsWith("jar:file:")) {
            tail = u.indexOf("!");
            dirName = u.substring(4, tail);
            return new File(this.fromURI(dirName));
         }

         if (u.startsWith("file:")) {
            tail = u.indexOf(resource);
            dirName = u.substring(0, tail);
            return new File(this.fromURI(dirName));
         }
      }

      return null;
   }

   private String fromURI(String uri) {
      URL url = null;

      try {
         url = new URL(uri);
      } catch (MalformedURLException var13) {
      }

      if (null != url && "file".equals(url.getProtocol())) {
         StringBuffer buf = new StringBuffer(url.getHost());
         if (buf.length() > 0) {
            buf.insert(0, File.separatorChar).insert(0, File.separatorChar);
         }

         String file = url.getFile();
         int queryPos = file.indexOf(63);
         buf.append(queryPos < 0 ? file : file.substring(0, queryPos));
         uri = buf.toString().replace('/', File.separatorChar);
         if (File.pathSeparatorChar == ';' && uri.startsWith("\\") && uri.length() > 2 && Character.isLetter(uri.charAt(1)) && uri.lastIndexOf(58) > -1) {
            uri = uri.substring(1);
         }

         StringBuffer sb = new StringBuffer();
         CharacterIterator iter = new StringCharacterIterator(uri);

         for(char c = iter.first(); c != '\uffff'; c = iter.next()) {
            if (c == '%') {
               char c1 = iter.next();
               if (c1 != '\uffff') {
                  int i1 = Character.digit(c1, 16);
                  char c2 = iter.next();
                  if (c2 != '\uffff') {
                     int i2 = Character.digit(c2, 16);
                     sb.append((char)((i1 << 4) + i2));
                  }
               }
            } else {
               sb.append(c);
            }
         }

         return sb.toString();
      } else {
         throw new IllegalArgumentException("Can only handle valid file: URIs");
      }
   }

   private List<String> getFiles(List<ResourceCollection> resources) throws BuildException {
      List<String> files = Lists.newArrayList();
      Iterator i$ = resources.iterator();

      while(i$.hasNext()) {
         ResourceCollection rc = (ResourceCollection)i$.next();
         Iterator i = rc.iterator();

         while(i.hasNext()) {
            Object o = i.next();
            if (o instanceof FileResource) {
               FileResource fr = (FileResource)o;
               if (fr.isDirectory()) {
                  throw new BuildException("Directory based FileResources are not supported.");
               }

               if (!fr.isExists()) {
                  this.log("'" + fr.toLongString() + "' does not exist", 3);
               }

               files.add(fr.getFile().getAbsolutePath());
            } else {
               this.log("Unsupported Resource type: " + o.toString(), 3);
            }
         }
      }

      return files;
   }

   private List<String> fileset(FileSet fileset) throws BuildException {
      List<String> files = Lists.newArrayList();
      DirectoryScanner ds = fileset.getDirectoryScanner(this.getProject());
      String[] arr$ = ds.getIncludedFiles();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String file = arr$[i$];
         files.add(ds.getBasedir() + File.separator + file);
      }

      return files;
   }

   private static String doubleQuote(String pCommandLineArg) {
      return pCommandLineArg.indexOf(" ") == -1 || pCommandLineArg.startsWith("\"") && pCommandLineArg.endsWith("\"") ? pCommandLineArg : "\"" + pCommandLineArg + '"';
   }

   private String createPathString(Path path, String sep) {
      if (path == null) {
         return null;
      } else {
         StringBuffer buf = new StringBuffer();

         for(int i = 0; i < path.list().length; ++i) {
            File file = this.getProject().resolveFile(path.list()[i]);
            if (!file.exists()) {
               this.log("Classpath entry not found: " + file, 1);
            }

            buf.append(file.getAbsolutePath()).append(sep);
         }

         if (path.list().length > 0) {
            buf.deleteCharAt(buf.length() - 1);
         }

         return buf.toString();
      }
   }

   private void dumpCommand(String fileName) {
      this.ppp("TESTNG PASSED @" + fileName + " WHICH CONTAINS:");
      this.readAndPrintFile(fileName);
   }

   private void readAndPrintFile(String fileName) {
      File file = new File(fileName);
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(file));

         for(String line = br.readLine(); line != null; line = br.readLine()) {
            System.out.println("  " + line);
         }
      } catch (IOException var13) {
         var13.printStackTrace();
      } finally {
         if (br != null) {
            try {
               br.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }

      }

   }

   public void addConfiguredReporter(ReporterConfig reporterConfig) {
      this.reporterConfigs.add(reporterConfig);
   }

   public void setSkipFailedInvocationCounts(boolean skip) {
      this.m_skipFailedInvocationCounts = skip;
   }

   public void setXmlPathInJar(String path) {
      this.m_xmlPathInJar = path;
   }

   public void addConfiguredPropertySet(PropertySet sysPropertySet) {
      Properties properties = sysPropertySet.getProperties();
      this.log(properties.keySet().size() + " properties found in nested propertyset", 3);
      Iterator i$ = properties.keySet().iterator();

      while(i$.hasNext()) {
         Object propKeyObj = i$.next();
         String propKey = (String)propKeyObj;
         Environment.Variable sysProp = new Environment.Variable();
         sysProp.setKey(propKey);
         if (properties.get(propKey) instanceof String) {
            String propVal = (String)properties.get(propKey);
            sysProp.setValue(propVal);
            this.getJavaCommand().addSysproperty(sysProp);
            this.log("Added system property " + propKey + " with value " + propVal, 3);
         } else {
            this.log("Ignoring non-String property " + propKey, 1);
         }
      }

   }

   protected void handleOutput(String output) {
      if (output.startsWith("[VerboseTestNG] ")) {
         this.log(output, this.m_verbose < 5 ? 3 : 2);
      } else {
         super.handleOutput(output);
      }

   }

   protected static class TestNGLogSH extends PumpStreamHandler {
      public TestNGLogSH(Task task, int outlevel, int errlevel, boolean verbose) {
         super(new TestNGAntTask.TestNGLogOS(task, outlevel, verbose), new LogOutputStream(task, errlevel));
      }
   }

   private static class TestNGLogOS extends LogOutputStream {
      private Task task;
      private boolean verbose;

      public TestNGLogOS(Task task, int level, boolean verbose) {
         super(task, level);
         this.task = task;
         this.verbose = verbose;
      }

      protected void processLine(String line, int level) {
         if (line.startsWith("[VerboseTestNG] ")) {
            this.task.log(line, this.verbose ? 3 : 2);
         } else {
            super.processLine(line, level);
         }

      }
   }

   public static enum Mode {
      testng,
      junit,
      mixed;
   }
}
