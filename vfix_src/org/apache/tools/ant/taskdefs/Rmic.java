package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapter;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;

public class Rmic extends MatchingTask {
   public static final String ERROR_RMIC_FAILED = "Rmic failed; see the compiler error output for details.";
   private File baseDir;
   private String classname;
   private File sourceBase;
   private String stubVersion;
   private Path compileClasspath;
   private Path extDirs;
   private boolean verify = false;
   private boolean filtering = false;
   private boolean iiop = false;
   private String iiopOpts;
   private boolean idl = false;
   private String idlOpts;
   private boolean debug = false;
   private boolean includeAntRuntime = true;
   private boolean includeJavaRuntime = false;
   private Vector compileList = new Vector();
   private ClassLoader loader = null;
   private FacadeTaskHelper facade = new FacadeTaskHelper("default");
   public static final String ERROR_UNABLE_TO_VERIFY_CLASS = "Unable to verify class ";
   public static final String ERROR_NOT_FOUND = ". It could not be found.";
   public static final String ERROR_NOT_DEFINED = ". It is not defined.";
   public static final String ERROR_LOADING_CAUSED_EXCEPTION = ". Loading caused Exception: ";
   public static final String ERROR_NO_BASE_EXISTS = "base does not exist: ";
   public static final String ERROR_NOT_A_DIR = "base is not a directory:";
   public static final String ERROR_BASE_NOT_SET = "base attribute must be set!";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   // $FF: synthetic field
   static Class class$java$rmi$Remote;

   public void setBase(File base) {
      this.baseDir = base;
   }

   public File getBase() {
      return this.baseDir;
   }

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public String getClassname() {
      return this.classname;
   }

   public void setSourceBase(File sourceBase) {
      this.sourceBase = sourceBase;
   }

   public File getSourceBase() {
      return this.sourceBase;
   }

   public void setStubVersion(String stubVersion) {
      this.stubVersion = stubVersion;
   }

   public String getStubVersion() {
      return this.stubVersion;
   }

   public void setFiltering(boolean filter) {
      this.filtering = filter;
   }

   public boolean getFiltering() {
      return this.filtering;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public boolean getDebug() {
      return this.debug;
   }

   public void setClasspath(Path classpath) {
      if (this.compileClasspath == null) {
         this.compileClasspath = classpath;
      } else {
         this.compileClasspath.append(classpath);
      }

   }

   public Path createClasspath() {
      if (this.compileClasspath == null) {
         this.compileClasspath = new Path(this.getProject());
      }

      return this.compileClasspath.createPath();
   }

   public void setClasspathRef(Reference pathRef) {
      this.createClasspath().setRefid(pathRef);
   }

   public Path getClasspath() {
      return this.compileClasspath;
   }

   public void setVerify(boolean verify) {
      this.verify = verify;
   }

   public boolean getVerify() {
      return this.verify;
   }

   public void setIiop(boolean iiop) {
      this.iiop = iiop;
   }

   public boolean getIiop() {
      return this.iiop;
   }

   public void setIiopopts(String iiopOpts) {
      this.iiopOpts = iiopOpts;
   }

   public String getIiopopts() {
      return this.iiopOpts;
   }

   public void setIdl(boolean idl) {
      this.idl = idl;
   }

   public boolean getIdl() {
      return this.idl;
   }

   public void setIdlopts(String idlOpts) {
      this.idlOpts = idlOpts;
   }

   public String getIdlopts() {
      return this.idlOpts;
   }

   public Vector getFileList() {
      return this.compileList;
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

   public void setExtdirs(Path extDirs) {
      if (this.extDirs == null) {
         this.extDirs = extDirs;
      } else {
         this.extDirs.append(extDirs);
      }

   }

   public Path createExtdirs() {
      if (this.extDirs == null) {
         this.extDirs = new Path(this.getProject());
      }

      return this.extDirs.createPath();
   }

   public Path getExtdirs() {
      return this.extDirs;
   }

   public Vector getCompileList() {
      return this.compileList;
   }

   public void setCompiler(String compiler) {
      if (compiler.length() > 0) {
         this.facade.setImplementation(compiler);
      }

   }

   public String getCompiler() {
      this.facade.setMagicValue(this.getProject().getProperty("build.rmic"));
      return this.facade.getImplementation();
   }

   public Rmic.ImplementationSpecificArgument createCompilerArg() {
      Rmic.ImplementationSpecificArgument arg = new Rmic.ImplementationSpecificArgument();
      this.facade.addImplementationArgument(arg);
      return arg;
   }

   public String[] getCurrentCompilerArgs() {
      this.getCompiler();
      return this.facade.getArgs();
   }

   public void execute() throws BuildException {
      if (this.baseDir == null) {
         throw new BuildException("base attribute must be set!", this.getLocation());
      } else if (!this.baseDir.exists()) {
         throw new BuildException("base does not exist: " + this.baseDir, this.getLocation());
      } else if (!this.baseDir.isDirectory()) {
         throw new BuildException("base is not a directory:" + this.baseDir, this.getLocation());
      } else {
         if (this.verify) {
            this.log("Verify has been turned on.", 3);
         }

         RmicAdapter adapter = RmicAdapterFactory.getRmic(this.getCompiler(), this);
         adapter.setRmic(this);
         Path classpath = adapter.getClasspath();
         this.loader = this.getProject().createClassLoader(classpath);

         try {
            if (this.classname == null) {
               DirectoryScanner ds = this.getDirectoryScanner(this.baseDir);
               String[] files = ds.getIncludedFiles();
               this.scanDir(this.baseDir, files, adapter.getMapper());
            } else {
               this.scanDir(this.baseDir, new String[]{this.classname.replace('.', File.separatorChar) + ".class"}, adapter.getMapper());
            }

            int fileCount = this.compileList.size();
            if (fileCount > 0) {
               this.log("RMI Compiling " + fileCount + " class" + (fileCount > 1 ? "es" : "") + " to " + this.baseDir, 2);
               if (!adapter.execute()) {
                  throw new BuildException("Rmic failed; see the compiler error output for details.", this.getLocation());
               }
            }

            if (null != this.sourceBase && !this.baseDir.equals(this.sourceBase) && fileCount > 0) {
               if (this.idl) {
                  this.log("Cannot determine sourcefiles in idl mode, ", 1);
                  this.log("sourcebase attribute will be ignored.", 1);
               } else {
                  for(int j = 0; j < fileCount; ++j) {
                     this.moveGeneratedFile(this.baseDir, this.sourceBase, (String)this.compileList.elementAt(j), adapter);
                  }
               }
            }
         } finally {
            this.compileList.removeAllElements();
         }

      }
   }

   private void moveGeneratedFile(File baseDir, File sourceBaseFile, String classname, RmicAdapter adapter) throws BuildException {
      String classFileName = classname.replace('.', File.separatorChar) + ".class";
      String[] generatedFiles = adapter.getMapper().mapFileName(classFileName);

      for(int i = 0; i < generatedFiles.length; ++i) {
         String generatedFile = generatedFiles[i];
         if (generatedFile.endsWith(".class")) {
            int pos = generatedFile.length() - ".class".length();
            String sourceFileName = generatedFile.substring(0, pos) + ".java";
            File oldFile = new File(baseDir, sourceFileName);
            if (oldFile.exists()) {
               File newFile = new File(sourceBaseFile, sourceFileName);

               try {
                  if (this.filtering) {
                     FILE_UTILS.copyFile(oldFile, newFile, new FilterSetCollection(this.getProject().getGlobalFilterSet()));
                  } else {
                     FILE_UTILS.copyFile(oldFile, newFile);
                  }

                  oldFile.delete();
               } catch (IOException var15) {
                  String msg = "Failed to copy " + oldFile + " to " + newFile + " due to " + var15.getMessage();
                  throw new BuildException(msg, var15, this.getLocation());
               }
            }
         }
      }

   }

   protected void scanDir(File baseDir, String[] files, FileNameMapper mapper) {
      String[] newFiles = files;
      if (this.idl) {
         this.log("will leave uptodate test to rmic implementation in idl mode.", 3);
      } else if (this.iiop && this.iiopOpts != null && this.iiopOpts.indexOf("-always") > -1) {
         this.log("no uptodate test as -always option has been specified", 3);
      } else {
         SourceFileScanner sfs = new SourceFileScanner(this);
         newFiles = sfs.restrict(files, baseDir, baseDir, mapper);
      }

      for(int i = 0; i < newFiles.length; ++i) {
         String name = newFiles[i].replace(File.separatorChar, '.');
         name = name.substring(0, name.lastIndexOf(".class"));
         this.compileList.addElement(name);
      }

   }

   public boolean isValidRmiRemote(String classname) {
      try {
         Class testClass = this.loader.loadClass(classname);
         if (testClass.isInterface() && !this.iiop && !this.idl) {
            return false;
         }

         return this.isValidRmiRemote(testClass);
      } catch (ClassNotFoundException var3) {
         this.log("Unable to verify class " + classname + ". It could not be found.", 1);
      } catch (NoClassDefFoundError var4) {
         this.log("Unable to verify class " + classname + ". It is not defined.", 1);
      } catch (Throwable var5) {
         this.log("Unable to verify class " + classname + ". Loading caused Exception: " + var5.getMessage(), 1);
      }

      return false;
   }

   public Class getRemoteInterface(Class testClass) {
      if ((class$java$rmi$Remote == null ? (class$java$rmi$Remote = class$("java.rmi.Remote")) : class$java$rmi$Remote).isAssignableFrom(testClass)) {
         Class[] interfaces = testClass.getInterfaces();
         if (interfaces != null) {
            for(int i = 0; i < interfaces.length; ++i) {
               if ((class$java$rmi$Remote == null ? (class$java$rmi$Remote = class$("java.rmi.Remote")) : class$java$rmi$Remote).isAssignableFrom(interfaces[i])) {
                  return interfaces[i];
               }
            }
         }
      }

      return null;
   }

   private boolean isValidRmiRemote(Class testClass) {
      return this.getRemoteInterface(testClass) != null;
   }

   public ClassLoader getLoader() {
      return this.loader;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public class ImplementationSpecificArgument extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument {
      public void setCompiler(String impl) {
         super.setImplementation(impl);
      }
   }
}
