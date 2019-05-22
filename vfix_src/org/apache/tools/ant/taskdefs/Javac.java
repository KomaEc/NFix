package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapter;
import org.apache.tools.ant.taskdefs.compilers.CompilerAdapterFactory;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;

public class Javac extends MatchingTask {
   private static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";
   private static final String JAVAC16 = "javac1.6";
   private static final String JAVAC15 = "javac1.5";
   private static final String JAVAC14 = "javac1.4";
   private static final String JAVAC13 = "javac1.3";
   private static final String JAVAC12 = "javac1.2";
   private static final String JAVAC11 = "javac1.1";
   private static final String MODERN = "modern";
   private static final String CLASSIC = "classic";
   private static final String EXTJAVAC = "extJavac";
   private Path src;
   private File destDir;
   private Path compileClasspath;
   private Path compileSourcepath;
   private String encoding;
   private boolean debug = false;
   private boolean optimize = false;
   private boolean deprecation = false;
   private boolean depend = false;
   private boolean verbose = false;
   private String targetAttribute;
   private Path bootclasspath;
   private Path extdirs;
   private boolean includeAntRuntime = true;
   private boolean includeJavaRuntime = false;
   private boolean fork = false;
   private String forkedExecutable = null;
   private boolean nowarn = false;
   private String memoryInitialSize;
   private String memoryMaximumSize;
   private FacadeTaskHelper facade = null;
   protected boolean failOnError = true;
   protected boolean listFiles = false;
   protected File[] compileList = new File[0];
   private String source;
   private String debugLevel;
   private File tmpDir;

   public Javac() {
      this.facade = new FacadeTaskHelper(this.assumedJavaVersion());
   }

   private String assumedJavaVersion() {
      if (JavaEnvUtils.isJavaVersion("1.2")) {
         return "javac1.2";
      } else if (JavaEnvUtils.isJavaVersion("1.3")) {
         return "javac1.3";
      } else if (JavaEnvUtils.isJavaVersion("1.4")) {
         return "javac1.4";
      } else if (JavaEnvUtils.isJavaVersion("1.5")) {
         return "javac1.5";
      } else {
         return JavaEnvUtils.isJavaVersion("1.6") ? "javac1.6" : "classic";
      }
   }

   public String getDebugLevel() {
      return this.debugLevel;
   }

   public void setDebugLevel(String v) {
      this.debugLevel = v;
   }

   public String getSource() {
      return this.source != null ? this.source : this.getProject().getProperty("ant.build.javac.source");
   }

   public void setSource(String v) {
      this.source = v;
   }

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

   public void setBootclasspath(Path bootclasspath) {
      if (this.bootclasspath == null) {
         this.bootclasspath = bootclasspath;
      } else {
         this.bootclasspath.append(bootclasspath);
      }

   }

   public Path getBootclasspath() {
      return this.bootclasspath;
   }

   public Path createBootclasspath() {
      if (this.bootclasspath == null) {
         this.bootclasspath = new Path(this.getProject());
      }

      return this.bootclasspath.createPath();
   }

   public void setBootClasspathRef(Reference r) {
      this.createBootclasspath().setRefid(r);
   }

   public void setExtdirs(Path extdirs) {
      if (this.extdirs == null) {
         this.extdirs = extdirs;
      } else {
         this.extdirs.append(extdirs);
      }

   }

   public Path getExtdirs() {
      return this.extdirs;
   }

   public Path createExtdirs() {
      if (this.extdirs == null) {
         this.extdirs = new Path(this.getProject());
      }

      return this.extdirs.createPath();
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

   public void setDeprecation(boolean deprecation) {
      this.deprecation = deprecation;
   }

   public boolean getDeprecation() {
      return this.deprecation;
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

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public boolean getDebug() {
      return this.debug;
   }

   public void setOptimize(boolean optimize) {
      this.optimize = optimize;
   }

   public boolean getOptimize() {
      return this.optimize;
   }

   public void setDepend(boolean depend) {
      this.depend = depend;
   }

   public boolean getDepend() {
      return this.depend;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public boolean getVerbose() {
      return this.verbose;
   }

   public void setTarget(String target) {
      this.targetAttribute = target;
   }

   public String getTarget() {
      return this.targetAttribute != null ? this.targetAttribute : this.getProject().getProperty("ant.build.javac.target");
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

   public void setExecutable(String forkExec) {
      this.forkedExecutable = forkExec;
   }

   public String getExecutable() {
      return this.forkedExecutable;
   }

   public boolean isForkedJavac() {
      return this.fork || "extJavac".equals(this.getCompiler());
   }

   public String getJavacExecutable() {
      if (this.forkedExecutable == null && this.isForkedJavac()) {
         this.forkedExecutable = this.getSystemJavac();
      } else if (this.forkedExecutable != null && !this.isForkedJavac()) {
         this.forkedExecutable = null;
      }

      return this.forkedExecutable;
   }

   public void setNowarn(boolean flag) {
      this.nowarn = flag;
   }

   public boolean getNowarn() {
      return this.nowarn;
   }

   public Javac.ImplementationSpecificArgument createCompilerArg() {
      Javac.ImplementationSpecificArgument arg = new Javac.ImplementationSpecificArgument();
      this.facade.addImplementationArgument(arg);
      return arg;
   }

   public String[] getCurrentCompilerArgs() {
      String chosen = this.facade.getExplicitChoice();

      String[] var5;
      try {
         String appliedCompiler = this.getCompiler();
         this.facade.setImplementation(appliedCompiler);
         String[] result = this.facade.getArgs();
         String altCompilerName = this.getAltCompilerName(this.facade.getImplementation());
         if (result.length == 0 && altCompilerName != null) {
            this.facade.setImplementation(altCompilerName);
            result = this.facade.getArgs();
         }

         var5 = result;
      } finally {
         this.facade.setImplementation(chosen);
      }

      return var5;
   }

   private String getAltCompilerName(String anImplementation) {
      if (!"javac1.6".equalsIgnoreCase(anImplementation) && !"javac1.5".equalsIgnoreCase(anImplementation) && !"javac1.4".equalsIgnoreCase(anImplementation) && !"javac1.3".equalsIgnoreCase(anImplementation)) {
         if (!"javac1.2".equalsIgnoreCase(anImplementation) && !"javac1.1".equalsIgnoreCase(anImplementation)) {
            if ("modern".equalsIgnoreCase(anImplementation)) {
               String nextSelected = this.assumedJavaVersion();
               if ("javac1.6".equalsIgnoreCase(nextSelected) || "javac1.5".equalsIgnoreCase(nextSelected) || "javac1.4".equalsIgnoreCase(nextSelected) || "javac1.3".equalsIgnoreCase(nextSelected)) {
                  return nextSelected;
               }
            }

            if ("classic".equals(anImplementation)) {
               return this.assumedJavaVersion();
            } else {
               return "extJavac".equalsIgnoreCase(anImplementation) ? this.assumedJavaVersion() : null;
            }
         } else {
            return "classic";
         }
      } else {
         return "modern";
      }
   }

   public void setTempdir(File tmpDir) {
      this.tmpDir = tmpDir;
   }

   public File getTempdir() {
      return this.tmpDir;
   }

   public void execute() throws BuildException {
      this.checkParameters();
      this.resetFileLists();
      String[] list = this.src.list();

      for(int i = 0; i < list.length; ++i) {
         File srcDir = this.getProject().resolveFile(list[i]);
         if (!srcDir.exists()) {
            throw new BuildException("srcdir \"" + srcDir.getPath() + "\" does not exist!", this.getLocation());
         }

         DirectoryScanner ds = this.getDirectoryScanner(srcDir);
         String[] files = ds.getIncludedFiles();
         this.scanDir(srcDir, this.destDir != null ? this.destDir : srcDir, files);
      }

      this.compile();
   }

   protected void resetFileLists() {
      this.compileList = new File[0];
   }

   protected void scanDir(File srcDir, File destDir, String[] files) {
      GlobPatternMapper m = new GlobPatternMapper();
      m.setFrom("*.java");
      m.setTo("*.class");
      SourceFileScanner sfs = new SourceFileScanner(this);
      File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, m);
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

   protected boolean isJdkCompiler(String compilerImpl) {
      return "modern".equals(compilerImpl) || "classic".equals(compilerImpl) || "javac1.6".equals(compilerImpl) || "javac1.5".equals(compilerImpl) || "javac1.4".equals(compilerImpl) || "javac1.3".equals(compilerImpl) || "javac1.2".equals(compilerImpl) || "javac1.1".equals(compilerImpl);
   }

   protected String getSystemJavac() {
      return JavaEnvUtils.getJdkExecutable("javac");
   }

   public void setCompiler(String compiler) {
      this.facade.setImplementation(compiler);
   }

   public String getCompiler() {
      String compilerImpl = this.getCompilerVersion();
      if (this.fork) {
         if (this.isJdkCompiler(compilerImpl)) {
            compilerImpl = "extJavac";
         } else {
            this.log("Since compiler setting isn't classic or modern,ignoring fork setting.", 1);
         }
      }

      return compilerImpl;
   }

   public String getCompilerVersion() {
      this.facade.setMagicValue(this.getProject().getProperty("build.compiler"));
      return this.facade.getImplementation();
   }

   protected void checkParameters() throws BuildException {
      if (this.src == null) {
         throw new BuildException("srcdir attribute must be set!", this.getLocation());
      } else if (this.src.size() == 0) {
         throw new BuildException("srcdir attribute must be set!", this.getLocation());
      } else if (this.destDir != null && !this.destDir.isDirectory()) {
         throw new BuildException("destination directory \"" + this.destDir + "\" does not exist " + "or is not a directory", this.getLocation());
      }
   }

   protected void compile() {
      String compilerImpl = this.getCompiler();
      if (this.compileList.length > 0) {
         this.log("Compiling " + this.compileList.length + " source file" + (this.compileList.length == 1 ? "" : "s") + (this.destDir != null ? " to " + this.destDir : ""));
         if (this.listFiles) {
            for(int i = 0; i < this.compileList.length; ++i) {
               String filename = this.compileList[i].getAbsolutePath();
               this.log(filename);
            }
         }

         CompilerAdapter adapter = CompilerAdapterFactory.getCompiler(compilerImpl, this);
         adapter.setJavac(this);
         if (!adapter.execute()) {
            if (this.failOnError) {
               throw new BuildException("Compile failed; see the compiler error output for details.", this.getLocation());
            }

            this.log("Compile failed; see the compiler error output for details.", 0);
         }
      }

   }

   public class ImplementationSpecificArgument extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument {
      public void setCompiler(String impl) {
         super.setImplementation(impl);
      }
   }
}
