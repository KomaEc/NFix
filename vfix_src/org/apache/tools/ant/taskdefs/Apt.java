package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.JavaEnvUtils;

public class Apt extends Javac {
   private boolean compile = true;
   private String factory;
   private Path factoryPath;
   private Vector options = new Vector();
   private File preprocessDir;
   public static final String EXECUTABLE_NAME = "apt";
   public static final String ERROR_IGNORING_COMPILER_OPTION = "Ignoring compiler attribute for the APT task, as it is fixed";
   public static final String ERROR_WRONG_JAVA_VERSION = "Apt task requires Java 1.5+";
   public static final String WARNING_IGNORING_FORK = "Apt only runs in its own JVM; fork=false option ignored";
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$compilers$AptExternalCompilerAdapter;

   public Apt() {
      super.setCompiler((class$org$apache$tools$ant$taskdefs$compilers$AptExternalCompilerAdapter == null ? (class$org$apache$tools$ant$taskdefs$compilers$AptExternalCompilerAdapter = class$("org.apache.tools.ant.taskdefs.compilers.AptExternalCompilerAdapter")) : class$org$apache$tools$ant$taskdefs$compilers$AptExternalCompilerAdapter).getName());
      this.setFork(true);
   }

   public String getAptExecutable() {
      return JavaEnvUtils.getJdkExecutable("apt");
   }

   public void setCompiler(String compiler) {
      this.log("Ignoring compiler attribute for the APT task, as it is fixed", 1);
   }

   public void setFork(boolean fork) {
      if (!fork) {
         this.log("Apt only runs in its own JVM; fork=false option ignored", 1);
      }

   }

   public String getCompiler() {
      return super.getCompiler();
   }

   public boolean isCompile() {
      return this.compile;
   }

   public void setCompile(boolean compile) {
      this.compile = compile;
   }

   public String getFactory() {
      return this.factory;
   }

   public void setFactory(String factory) {
      this.factory = factory;
   }

   public void setFactoryPathRef(Reference ref) {
      this.createFactoryPath().setRefid(ref);
   }

   public Path createFactoryPath() {
      if (this.factoryPath == null) {
         this.factoryPath = new Path(this.getProject());
      }

      return this.factoryPath.createPath();
   }

   public Path getFactoryPath() {
      return this.factoryPath;
   }

   public Apt.Option createOption() {
      Apt.Option opt = new Apt.Option();
      this.options.add(opt);
      return opt;
   }

   public Vector getOptions() {
      return this.options;
   }

   public File getPreprocessDir() {
      return this.preprocessDir;
   }

   public void setPreprocessDir(File preprocessDir) {
      this.preprocessDir = preprocessDir;
   }

   public void execute() throws BuildException {
      super.execute();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static final class Option {
      private String name;
      private String value;

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }
   }
}
