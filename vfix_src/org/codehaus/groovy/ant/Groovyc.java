package org.codehaus.groovy.ant;

import groovy.lang.GroovyClassLoader;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.Options;
import groovyjarjarcommonscli.PosixParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceExtensionHandler;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.tools.ErrorReporter;
import org.codehaus.groovy.tools.FileSystemCompiler;
import org.codehaus.groovy.tools.RootLoader;
import org.codehaus.groovy.tools.javac.JavaAwareCompilationUnit;

public class Groovyc extends MatchingTask {
   private final LoggingHelper log = new LoggingHelper(this);
   private Path src;
   private File destDir;
   private Path compileClasspath;
   private Path compileSourcepath;
   private String encoding;
   private boolean stacktrace = false;
   private boolean verbose = false;
   private boolean includeAntRuntime = true;
   private boolean includeJavaRuntime = false;
   private boolean fork = false;
   private File forkJDK;
   private String memoryInitialSize;
   private String memoryMaximumSize;
   private String scriptExtension = "*.groovy";
   private String targetBytecode = null;
   protected boolean failOnError = true;
   protected boolean listFiles = false;
   protected File[] compileList = new File[0];
   private String updatedProperty;
   private String errorProperty;
   private boolean taskSuccess = true;
   private boolean includeDestClasses = true;
   protected CompilerConfiguration configuration;
   private Javac javac;
   private boolean jointCompilation;
   private List<File> temporaryFiles = new ArrayList(2);
   private File stubDir;
   private boolean keepStubs;
   private Set<String> scriptExtensions = new LinkedHashSet();

   public Path createSrc() {
      if (this.src == null) {
         this.src = new Path(this.getProject());
      }

      return this.src.createPath();
   }

   protected Path recreateSrc() {
      this.src = null;
      return this.createSrc();
   }

   public void setSrcdir(Path srcDir) {
      if (this.src == null) {
         this.src = srcDir;
      } else {
         this.src.append(srcDir);
      }

   }

   public Path getSrcdir() {
      return this.src;
   }

   public void setScriptExtension(String scriptExtension) {
      if (scriptExtension.startsWith("*.")) {
         this.scriptExtension = scriptExtension;
      } else if (scriptExtension.startsWith(".")) {
         this.scriptExtension = "*" + scriptExtension;
      } else {
         this.scriptExtension = "*." + scriptExtension;
      }

   }

   public String getScriptExtension() {
      return this.scriptExtension;
   }

   public void setTargetBytecode(String version) {
      if ("1.4".equals(version) || "1.5".equals(version)) {
         this.targetBytecode = version;
      }

   }

   public String getTargetBytecode() {
      return this.targetBytecode;
   }

   public void setDestdir(File destDir) {
      this.destDir = destDir;
   }

   public File getDestdir() {
      return this.destDir;
   }

   public void setSourcepath(Path sourcepath) {
      if (this.compileSourcepath == null) {
         this.compileSourcepath = sourcepath;
      } else {
         this.compileSourcepath.append(sourcepath);
      }

   }

   public Path getSourcepath() {
      return this.compileSourcepath;
   }

   public Path createSourcepath() {
      if (this.compileSourcepath == null) {
         this.compileSourcepath = new Path(this.getProject());
      }

      return this.compileSourcepath.createPath();
   }

   public void setSourcepathRef(Reference r) {
      this.createSourcepath().setRefid(r);
   }

   public void setClasspath(Path classpath) {
      if (this.compileClasspath == null) {
         this.compileClasspath = classpath;
      } else {
         this.compileClasspath.append(classpath);
      }

   }

   public Path getClasspath() {
      return this.compileClasspath;
   }

   public Path createClasspath() {
      if (this.compileClasspath == null) {
         this.compileClasspath = new Path(this.getProject());
      }

      return this.compileClasspath.createPath();
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void setListfiles(boolean list) {
      this.listFiles = list;
   }

   public boolean getListfiles() {
      return this.listFiles;
   }

   public void setFailonerror(boolean fail) {
      this.failOnError = fail;
   }

   public void setProceed(boolean proceed) {
      this.failOnError = !proceed;
   }

   public boolean getFailonerror() {
      return this.failOnError;
   }

   public void setMemoryInitialSize(String memoryInitialSize) {
      this.memoryInitialSize = memoryInitialSize;
   }

   public String getMemoryInitialSize() {
      return this.memoryInitialSize;
   }

   public void setMemoryMaximumSize(String memoryMaximumSize) {
      this.memoryMaximumSize = memoryMaximumSize;
   }

   public String getMemoryMaximumSize() {
      return this.memoryMaximumSize;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public boolean getVerbose() {
      return this.verbose;
   }

   public void setIncludeantruntime(boolean include) {
      this.includeAntRuntime = include;
   }

   public boolean getIncludeantruntime() {
      return this.includeAntRuntime;
   }

   public void setIncludejavaruntime(boolean include) {
      this.includeJavaRuntime = include;
   }

   public boolean getIncludejavaruntime() {
      return this.includeJavaRuntime;
   }

   public void setFork(boolean f) {
      this.fork = f;
   }

   public void setJavaHome(File home) {
      this.forkJDK = home;
   }

   public void setUpdatedProperty(String updatedProperty) {
      this.updatedProperty = updatedProperty;
   }

   public void setErrorProperty(String errorProperty) {
      this.errorProperty = errorProperty;
   }

   public void setIncludeDestClasses(boolean includeDestClasses) {
      this.includeDestClasses = includeDestClasses;
   }

   public boolean isIncludeDestClasses() {
      return this.includeDestClasses;
   }

   public boolean getTaskSuccess() {
      return this.taskSuccess;
   }

   public void addConfiguredJavac(Javac javac) {
      this.javac = javac;
      this.jointCompilation = true;
   }

   public void setStacktrace(boolean stacktrace) {
      this.stacktrace = stacktrace;
   }

   public void execute() throws BuildException {
      this.checkParameters();
      this.resetFileLists();
      this.loadRegisteredScriptExtensions();
      if (this.javac != null) {
         this.jointCompilation = true;
      }

      String[] list = this.src.list();
      String[] arr$ = list;
      int len$ = list.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String filename = arr$[i$];
         File file = this.getProject().resolveFile(filename);
         if (!file.exists()) {
            throw new BuildException("srcdir \"" + file.getPath() + "\" does not exist!", this.getLocation());
         }

         DirectoryScanner ds = this.getDirectoryScanner(file);
         String[] files = ds.getIncludedFiles();
         this.scanDir(file, this.destDir != null ? this.destDir : file, files);
      }

      this.compile();
      if (this.updatedProperty != null && this.taskSuccess && this.compileList.length != 0) {
         this.getProject().setNewProperty(this.updatedProperty, "true");
      }

   }

   protected void resetFileLists() {
      this.compileList = new File[0];
      this.scriptExtensions = new LinkedHashSet();
   }

   protected void scanDir(File srcDir, File destDir, String[] files) {
      GlobPatternMapper m = new GlobPatternMapper();
      SourceFileScanner sfs = new SourceFileScanner(this);
      Iterator i$ = this.getScriptExtensions().iterator();

      File[] newFiles;
      while(i$.hasNext()) {
         String extension = (String)i$.next();
         m.setFrom("*." + extension);
         m.setTo("*.class");
         newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
         this.addToCompileList(newFiles);
      }

      if (this.jointCompilation) {
         m.setFrom("*.java");
         m.setTo("*.class");
         newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
         this.addToCompileList(newFiles);
      }

   }

   protected void addToCompileList(File[] newFiles) {
      if (newFiles.length > 0) {
         File[] newCompileList = new File[this.compileList.length + newFiles.length];
         System.arraycopy(this.compileList, 0, newCompileList, 0, this.compileList.length);
         System.arraycopy(newFiles, 0, newCompileList, this.compileList.length, newFiles.length);
         this.compileList = newCompileList;
      }

   }

   public File[] getFileList() {
      return this.compileList;
   }

   protected void checkParameters() throws BuildException {
      if (this.src == null) {
         throw new BuildException("srcdir attribute must be set!", this.getLocation());
      } else if (this.src.size() == 0) {
         throw new BuildException("srcdir attribute must be set!", this.getLocation());
      } else if (this.destDir != null && !this.destDir.isDirectory()) {
         throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", this.getLocation());
      } else if (this.encoding != null && !Charset.isSupported(this.encoding)) {
         throw new BuildException("encoding \"" + this.encoding + "\" not supported.");
      }
   }

   protected void compile() {
      boolean var23 = false;

      try {
         var23 = true;
         if (this.compileList.length > 0) {
            this.log("Compiling " + this.compileList.length + " source file" + (this.compileList.length == 1 ? "" : "s") + (this.destDir != null ? " to " + this.destDir : ""));
            if (this.listFiles) {
               File[] arr$ = this.compileList;
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  File srcFile = arr$[i$];
                  this.log(srcFile.getAbsolutePath());
               }
            }

            Path classpath = this.getClasspath() != null ? this.getClasspath() : new Path(this.getProject());
            List<String> jointOptions = new ArrayList();
            String key;
            String tmpExtension;
            Iterator i$;
            if (this.jointCompilation) {
               RuntimeConfigurable rc = this.javac.getRuntimeConfigurableWrapper();
               Iterator i = rc.getAttributeMap().entrySet().iterator();

               label679:
               while(true) {
                  String key;
                  while(i.hasNext()) {
                     Entry e = (Entry)i.next();
                     key = e.getKey().toString();
                     tmpExtension = this.getProject().replaceProperties(e.getValue().toString());
                     if (key.contains("debug")) {
                        key = "";
                        if (this.javac.getDebugLevel() != null) {
                           key = ":" + this.javac.getDebugLevel();
                        }

                        jointOptions.add("-Fg" + key);
                     } else if (!key.contains("debugLevel")) {
                        if (!key.contains("nowarn") && !key.contains("verbose") && !key.contains("deprecation")) {
                           if (key.contains("classpath")) {
                              classpath.add(this.javac.getClasspath());
                           } else if (!key.contains("depend") && !key.contains("extdirs") && !key.contains("encoding") && !key.contains("source") && !key.contains("target") && !key.contains("verbose")) {
                              this.log("The option " + key + " cannot be set on the contained <javac> element. The option will be ignored", 1);
                           } else {
                              jointOptions.add("-J" + key + "=" + tmpExtension);
                           }
                        } else if ("on".equalsIgnoreCase(tmpExtension) || "true".equalsIgnoreCase(tmpExtension) || "yes".equalsIgnoreCase("value")) {
                           jointOptions.add("-F" + key);
                        }
                     }
                  }

                  Enumeration children = rc.getChildren();

                  label651:
                  while(true) {
                     RuntimeConfigurable childrc;
                     do {
                        if (!children.hasMoreElements()) {
                           break label679;
                        }

                        childrc = (RuntimeConfigurable)children.nextElement();
                     } while(!childrc.getElementTag().equals("compilerarg"));

                     i$ = childrc.getAttributeMap().entrySet().iterator();

                     while(true) {
                        Entry e;
                        do {
                           if (!i$.hasNext()) {
                              continue label651;
                           }

                           e = (Entry)i$.next();
                           key = e.getKey().toString();
                        } while(!key.equals("value"));

                        String value = this.getProject().replaceProperties(e.getValue().toString());
                        StringTokenizer st = new StringTokenizer(value, " ");

                        while(st.hasMoreTokens()) {
                           String optionStr = st.nextToken();
                           jointOptions.add(optionStr.replace("-X", "-FX"));
                        }
                     }
                  }
               }
            }

            String separator = System.getProperty("file.separator");
            List<String> commandLineList = new ArrayList();
            if (this.fork) {
               String javaHome;
               if (this.forkJDK != null) {
                  javaHome = this.forkJDK.getPath();
               } else {
                  javaHome = System.getProperty("java.home");
               }

               if (this.includeAntRuntime) {
                  classpath.addExisting((new Path(this.getProject())).concatSystemClasspath("last"));
               }

               if (this.includeJavaRuntime) {
                  classpath.addJavaRuntime();
               }

               commandLineList.add(javaHome + separator + "bin" + separator + "java");
               commandLineList.add("-classpath");
               commandLineList.add(classpath.toString());
               key = System.getProperty("file.encoding");
               if (key != null && !key.equals("")) {
                  commandLineList.add("-Dfile.encoding=" + key);
               }

               if (this.targetBytecode != null) {
                  commandLineList.add("-Dgroovy.target.bytecode=" + this.targetBytecode);
               }

               if (this.memoryInitialSize != null && !this.memoryInitialSize.equals("")) {
                  commandLineList.add("-Xms" + this.memoryInitialSize);
               }

               if (this.memoryMaximumSize != null && !this.memoryMaximumSize.equals("")) {
                  commandLineList.add("-Xmx" + this.memoryMaximumSize);
               }

               if (!"*.groovy".equals(this.getScriptExtension())) {
                  tmpExtension = this.getScriptExtension();
                  if (tmpExtension.startsWith("*.")) {
                     tmpExtension = tmpExtension.substring(1);
                  }

                  commandLineList.add("-Dgroovy.default.scriptExtension=" + tmpExtension);
               }

               commandLineList.add("org.codehaus.groovy.tools.FileSystemCompiler");
            }

            commandLineList.add("--classpath");
            commandLineList.add(classpath.toString());
            if (this.jointCompilation) {
               commandLineList.add("-j");
               commandLineList.addAll(jointOptions);
            }

            commandLineList.add("-d");
            commandLineList.add(this.destDir.getPath());
            if (this.encoding != null) {
               commandLineList.add("--encoding");
               commandLineList.add(this.encoding);
            }

            if (this.stacktrace) {
               commandLineList.add("-e");
            }

            int count = 0;
            File[] arr$;
            int i;
            int returnCode;
            File srcFile;
            if (this.fork) {
               arr$ = this.compileList;
               i = arr$.length;

               for(returnCode = 0; returnCode < i; ++returnCode) {
                  srcFile = arr$[returnCode];
                  count += srcFile.getPath().length();
               }

               Object commandLineArg;
               for(i$ = commandLineList.iterator(); i$.hasNext(); count += commandLineArg.toString().length()) {
                  commandLineArg = i$.next();
               }

               count += this.compileList.length;
               count += commandLineList.size();
            }

            if (this.fork && count > 32767) {
               try {
                  File tempFile = File.createTempFile("groovyc-files-", ".txt");
                  this.temporaryFiles.add(tempFile);
                  PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
                  File[] arr$ = this.compileList;
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     File srcFile = arr$[i$];
                     pw.println(srcFile.getPath());
                  }

                  pw.close();
                  commandLineList.add("@" + tempFile.getPath());
               } catch (IOException var28) {
                  this.log("Error creating file list", var28, 0);
               }
            } else {
               arr$ = this.compileList;
               i = arr$.length;

               for(returnCode = 0; returnCode < i; ++returnCode) {
                  srcFile = arr$[returnCode];
                  commandLineList.add(srcFile.getPath());
               }
            }

            String[] commandLine = new String[commandLineList.size()];

            for(i = 0; i < commandLine.length; ++i) {
               commandLine[i] = (String)commandLineList.get(i);
            }

            this.log("Compilation arguments:", 3);
            this.log(DefaultGroovyMethods.join((Object[])commandLine, "\n"), 3);
            if (this.fork) {
               Execute executor = new Execute();
               executor.setAntRun(this.getProject());
               executor.setWorkingDirectory(this.getProject().getBaseDir());
               executor.setCommandline(commandLine);

               try {
                  executor.execute();
               } catch (IOException var26) {
                  throw new BuildException("Error running forked groovyc.", var26);
               }

               returnCode = executor.getExitValue();
               if (returnCode != 0) {
                  if (this.failOnError) {
                     throw new BuildException("Forked groovyc returned error code: " + returnCode);
                  }

                  this.log("Forked groovyc returned error code: " + returnCode, 0);
                  var23 = false;
               } else {
                  var23 = false;
               }
            } else {
               String message;
               try {
                  Options options = FileSystemCompiler.createCompilationOptions();
                  PosixParser cliParser = new PosixParser();
                  CommandLine cli = cliParser.parse(options, commandLine);
                  this.configuration = FileSystemCompiler.generateCompilerConfigurationFromOptions(cli);
                  this.configuration.setScriptExtensions(this.getScriptExtensions());
                  message = this.getScriptExtension();
                  if (message.startsWith("*.")) {
                     message = message.substring(1);
                  }

                  this.configuration.setDefaultScriptExtension(message);
                  String[] filenames = FileSystemCompiler.generateFileNamesFromOptions(cli);
                  boolean fileNameErrors = filenames == null;
                  fileNameErrors = fileNameErrors && !FileSystemCompiler.validateFiles(filenames);
                  if (this.targetBytecode != null) {
                     this.configuration.setTargetBytecode(this.targetBytecode);
                  }

                  if (!fileNameErrors) {
                     FileSystemCompiler.doCompilation(this.configuration, this.makeCompileUnit(), filenames);
                     var23 = false;
                  } else {
                     var23 = false;
                  }
               } catch (Exception var27) {
                  Throwable t = var27;
                  if (var27.getClass() == RuntimeException.class && var27.getCause() != null) {
                     t = var27.getCause();
                  }

                  StringWriter writer = new StringWriter();
                  (new ErrorReporter((Throwable)t, false)).write(new PrintWriter(writer));
                  message = writer.toString();
                  if (this.failOnError) {
                     this.log(message, 2);
                     throw new BuildException("Compilation Failed", (Throwable)t, this.getLocation());
                  }

                  this.log(message, 0);
                  var23 = false;
               }
            }
         } else {
            var23 = false;
         }
      } finally {
         if (var23) {
            Iterator i$ = this.temporaryFiles.iterator();

            while(i$.hasNext()) {
               File temporaryFile = (File)i$.next();

               try {
                  FileSystemCompiler.deleteRecursive(temporaryFile);
               } catch (Throwable var24) {
                  System.err.println("error: could not delete temp files - " + temporaryFile.getPath());
               }
            }

         }
      }

      Iterator i$ = this.temporaryFiles.iterator();

      while(i$.hasNext()) {
         File temporaryFile = (File)i$.next();

         try {
            FileSystemCompiler.deleteRecursive(temporaryFile);
         } catch (Throwable var25) {
            System.err.println("error: could not delete temp files - " + temporaryFile.getPath());
         }
      }

   }

   protected CompilationUnit makeCompileUnit() {
      Map<String, Object> options = this.configuration.getJointCompilationOptions();
      if (options != null) {
         if (this.keepStubs) {
            options.put("keepStubs", Boolean.TRUE);
         }

         if (this.stubDir != null) {
            options.put("stubDir", this.stubDir);
         } else {
            try {
               File tempStubDir = FileSystemCompiler.createTempDir();
               this.temporaryFiles.add(tempStubDir);
               options.put("stubDir", tempStubDir);
            } catch (IOException var3) {
               throw new BuildException(var3);
            }
         }

         return new JavaAwareCompilationUnit(this.configuration, this.buildClassLoaderFor());
      } else {
         return new CompilationUnit(this.configuration, (CodeSource)null, this.buildClassLoaderFor());
      }
   }

   protected GroovyClassLoader buildClassLoaderFor() {
      ClassLoader parent = this.getIncludeantruntime() ? this.getClass().getClassLoader() : new AntClassLoader(new RootLoader(new URL[0], (ClassLoader)null), this.getProject(), this.getClasspath());
      if (parent instanceof AntClassLoader) {
         AntClassLoader antLoader = (AntClassLoader)parent;
         String[] pathElm = antLoader.getClasspath().split(File.pathSeparator);
         List<String> classpath = this.configuration.getClasspath();
         Iterator i$ = classpath.iterator();

         while(i$.hasNext()) {
            String cpEntry = (String)i$.next();
            boolean found = false;
            String[] arr$ = pathElm;
            int len$ = pathElm.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               String path = arr$[i$];
               if (cpEntry.equals(path)) {
                  found = true;
                  break;
               }
            }

            if (!found && (new File(cpEntry)).exists()) {
               antLoader.addPathElement(cpEntry);
            }
         }
      }

      return new GroovyClassLoader((ClassLoader)parent, this.configuration);
   }

   public void setStubdir(File stubDir) {
      this.jointCompilation = true;
      this.stubDir = stubDir;
   }

   public File getStubdir() {
      return this.stubDir;
   }

   public void setKeepStubs(boolean keepStubs) {
      this.keepStubs = keepStubs;
   }

   public boolean getKeepStubs() {
      return this.keepStubs;
   }

   private Set<String> getScriptExtensions() {
      return this.scriptExtensions;
   }

   private void loadRegisteredScriptExtensions() {
      if (this.scriptExtensions.isEmpty()) {
         this.scriptExtensions.add(this.getScriptExtension().substring(2));
         Path classpath = this.getClasspath() != null ? this.getClasspath() : new Path(this.getProject());
         String[] pe = classpath.list();
         GroovyClassLoader loader = new GroovyClassLoader(this.getClass().getClassLoader());
         String[] arr$ = pe;
         int len$ = pe.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String file = arr$[i$];
            loader.addClasspath(file);
         }

         this.scriptExtensions.addAll(SourceExtensionHandler.getRegisteredExtensions(loader));
      }

   }
}
