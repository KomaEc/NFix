package org.apache.tools.ant.taskdefs.rmic;

import java.io.File;
import java.util.Random;
import java.util.Vector;
import org.apache.tools.ant.taskdefs.Rmic;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileNameMapper;

public abstract class DefaultRmicAdapter implements RmicAdapter {
   private Rmic attributes;
   private FileNameMapper mapper;
   private static final Random RAND = new Random();
   public static final String RMI_STUB_SUFFIX = "_Stub";
   public static final String RMI_SKEL_SUFFIX = "_Skel";
   public static final String RMI_TIE_SUFFIX = "_Tie";
   public static final String STUB_COMPAT = "-vcompat";
   public static final String STUB_1_1 = "-v1.1";
   public static final String STUB_1_2 = "-v1.2";

   public void setRmic(Rmic attributes) {
      this.attributes = attributes;
      this.mapper = new DefaultRmicAdapter.RmicFileNameMapper();
   }

   public Rmic getRmic() {
      return this.attributes;
   }

   protected String getStubClassSuffix() {
      return "_Stub";
   }

   protected String getSkelClassSuffix() {
      return "_Skel";
   }

   protected String getTieClassSuffix() {
      return "_Tie";
   }

   public FileNameMapper getMapper() {
      return this.mapper;
   }

   public Path getClasspath() {
      return this.getCompileClasspath();
   }

   protected Path getCompileClasspath() {
      Path classpath = new Path(this.attributes.getProject());
      classpath.setLocation(this.attributes.getBase());
      Path cp = this.attributes.getClasspath();
      if (cp == null) {
         cp = new Path(this.attributes.getProject());
      }

      if (this.attributes.getIncludeantruntime()) {
         classpath.addExisting(cp.concatSystemClasspath("last"));
      } else {
         classpath.addExisting(cp.concatSystemClasspath("ignore"));
      }

      if (this.attributes.getIncludejavaruntime()) {
         classpath.addJavaRuntime();
      }

      return classpath;
   }

   protected Commandline setupRmicCommand() {
      return this.setupRmicCommand((String[])null);
   }

   protected Commandline setupRmicCommand(String[] options) {
      Commandline cmd = new Commandline();
      if (options != null) {
         for(int i = 0; i < options.length; ++i) {
            cmd.createArgument().setValue(options[i]);
         }
      }

      Path classpath = this.getCompileClasspath();
      cmd.createArgument().setValue("-d");
      cmd.createArgument().setFile(this.attributes.getBase());
      if (this.attributes.getExtdirs() != null) {
         cmd.createArgument().setValue("-extdirs");
         cmd.createArgument().setPath(this.attributes.getExtdirs());
      }

      cmd.createArgument().setValue("-classpath");
      cmd.createArgument().setPath(classpath);
      String stubVersion = this.attributes.getStubVersion();
      String stubOption = null;
      if (null != stubVersion) {
         if ("1.1".equals(stubVersion)) {
            stubOption = "-v1.1";
         } else if ("1.2".equals(stubVersion)) {
            stubOption = "-v1.2";
         } else if ("compat".equals(stubVersion)) {
            stubOption = "-vcompat";
         } else {
            this.attributes.log("Unknown stub option " + stubVersion);
         }
      }

      if (stubOption == null && !this.attributes.getIiop() && !this.attributes.getIdl()) {
         stubOption = "-vcompat";
      }

      if (stubOption != null) {
         cmd.createArgument().setValue(stubOption);
      }

      if (null != this.attributes.getSourceBase()) {
         cmd.createArgument().setValue("-keepgenerated");
      }

      if (this.attributes.getIiop()) {
         this.attributes.log("IIOP has been turned on.", 2);
         cmd.createArgument().setValue("-iiop");
         if (this.attributes.getIiopopts() != null) {
            this.attributes.log("IIOP Options: " + this.attributes.getIiopopts(), 2);
            cmd.createArgument().setValue(this.attributes.getIiopopts());
         }
      }

      if (this.attributes.getIdl()) {
         cmd.createArgument().setValue("-idl");
         this.attributes.log("IDL has been turned on.", 2);
         if (this.attributes.getIdlopts() != null) {
            cmd.createArgument().setValue(this.attributes.getIdlopts());
            this.attributes.log("IDL Options: " + this.attributes.getIdlopts(), 2);
         }
      }

      if (this.attributes.getDebug()) {
         cmd.createArgument().setValue("-g");
      }

      cmd.addArguments(this.attributes.getCurrentCompilerArgs());
      this.logAndAddFilesToCompile(cmd);
      return cmd;
   }

   protected void logAndAddFilesToCompile(Commandline cmd) {
      Vector compileList = this.attributes.getCompileList();
      this.attributes.log("Compilation " + cmd.describeArguments(), 3);
      StringBuffer niceSourceList = new StringBuffer("File");
      int cListSize = compileList.size();
      if (cListSize != 1) {
         niceSourceList.append("s");
      }

      niceSourceList.append(" to be compiled:");

      for(int i = 0; i < cListSize; ++i) {
         String arg = (String)compileList.elementAt(i);
         cmd.createArgument().setValue(arg);
         niceSourceList.append("    ");
         niceSourceList.append(arg);
      }

      this.attributes.log(niceSourceList.toString(), 3);
   }

   private class RmicFileNameMapper implements FileNameMapper {
      RmicFileNameMapper() {
      }

      public void setFrom(String s) {
      }

      public void setTo(String s) {
      }

      public String[] mapFileName(String name) {
         if (name != null && name.endsWith(".class") && !name.endsWith(DefaultRmicAdapter.this.getStubClassSuffix() + ".class") && !name.endsWith(DefaultRmicAdapter.this.getSkelClassSuffix() + ".class") && !name.endsWith(DefaultRmicAdapter.this.getTieClassSuffix() + ".class")) {
            String base = name.substring(0, name.length() - 6);
            String classname = base.replace(File.separatorChar, '.');
            if (DefaultRmicAdapter.this.attributes.getVerify() && !DefaultRmicAdapter.this.attributes.isValidRmiRemote(classname)) {
               return null;
            } else {
               String[] target = new String[]{name + ".tmp." + DefaultRmicAdapter.RAND.nextLong()};
               if (!DefaultRmicAdapter.this.attributes.getIiop() && !DefaultRmicAdapter.this.attributes.getIdl()) {
                  if ("1.2".equals(DefaultRmicAdapter.this.attributes.getStubVersion())) {
                     target = new String[]{base + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"};
                  } else {
                     target = new String[]{base + DefaultRmicAdapter.this.getStubClassSuffix() + ".class", base + DefaultRmicAdapter.this.getSkelClassSuffix() + ".class"};
                  }
               } else if (!DefaultRmicAdapter.this.attributes.getIdl()) {
                  int lastSlash = base.lastIndexOf(File.separatorChar);
                  String dirname = "";
                  int index = true;
                  int indexx;
                  if (lastSlash == -1) {
                     indexx = 0;
                  } else {
                     indexx = lastSlash + 1;
                     dirname = base.substring(0, indexx);
                  }

                  String filename = base.substring(indexx);

                  try {
                     Class c = DefaultRmicAdapter.this.attributes.getLoader().loadClass(classname);
                     if (c.isInterface()) {
                        target = new String[]{dirname + "_" + filename + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"};
                     } else {
                        Class interf = DefaultRmicAdapter.this.attributes.getRemoteInterface(c);
                        String iName = interf.getName();
                        String iDir = "";
                        int iIndex = true;
                        int lastDot = iName.lastIndexOf(".");
                        int iIndexx;
                        if (lastDot == -1) {
                           iIndexx = 0;
                        } else {
                           iIndexx = lastDot + 1;
                           iDir = iName.substring(0, iIndexx);
                           iDir = iDir.replace('.', File.separatorChar);
                        }

                        target = new String[]{dirname + "_" + filename + DefaultRmicAdapter.this.getTieClassSuffix() + ".class", iDir + "_" + iName.substring(iIndexx) + DefaultRmicAdapter.this.getStubClassSuffix() + ".class"};
                     }
                  } catch (ClassNotFoundException var15) {
                     DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". It could not be found.", 1);
                  } catch (NoClassDefFoundError var16) {
                     DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". It is not defined.", 1);
                  } catch (Throwable var17) {
                     DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". Loading caused Exception: " + var17.getMessage(), 1);
                  }
               }

               return target;
            }
         } else {
            return null;
         }
      }
   }
}
