package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.RedirectorElement;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.JavaEnvUtils;

public abstract class AbstractJarSignerTask extends Task {
   protected File jar;
   protected String alias;
   protected String keystore;
   protected String storepass;
   protected String storetype;
   protected String keypass;
   protected boolean verbose;
   protected String maxMemory;
   protected Vector filesets = new Vector();
   protected static final String JARSIGNER_COMMAND = "jarsigner";
   private RedirectorElement redirector;
   private Environment sysProperties = new Environment();
   public static final String ERROR_NO_SOURCE = "jar must be set through jar attribute or nested filesets";
   private Path path = null;

   public void setMaxmemory(String max) {
      this.maxMemory = max;
   }

   public void setJar(File jar) {
      this.jar = jar;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public void setKeystore(String keystore) {
      this.keystore = keystore;
   }

   public void setStorepass(String storepass) {
      this.storepass = storepass;
   }

   public void setStoretype(String storetype) {
      this.storetype = storetype;
   }

   public void setKeypass(String keypass) {
      this.keypass = keypass;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void addFileset(FileSet set) {
      this.filesets.addElement(set);
   }

   public void addSysproperty(Environment.Variable sysp) {
      this.sysProperties.addVariable(sysp);
   }

   public Path createPath() {
      if (this.path == null) {
         this.path = new Path(this.getProject());
      }

      return this.path.createPath();
   }

   protected void beginExecution() {
      this.redirector = this.createRedirector();
   }

   protected void endExecution() {
      this.redirector = null;
   }

   private RedirectorElement createRedirector() {
      RedirectorElement result = new RedirectorElement();
      if (this.storepass != null) {
         StringBuffer input = (new StringBuffer(this.storepass)).append('\n');
         if (this.keypass != null) {
            input.append(this.keypass).append('\n');
         }

         result.setInputString(input.toString());
         result.setLogInputString(false);
      }

      return result;
   }

   public RedirectorElement getRedirector() {
      return this.redirector;
   }

   protected void setCommonOptions(ExecTask cmd) {
      if (this.maxMemory != null) {
         this.addValue(cmd, "-J-Xmx" + this.maxMemory);
      }

      if (this.verbose) {
         this.addValue(cmd, "-verbose");
      }

      Vector props = this.sysProperties.getVariablesVector();
      Enumeration e = props.elements();

      while(e.hasMoreElements()) {
         Environment.Variable variable = (Environment.Variable)e.nextElement();
         this.declareSysProperty(cmd, variable);
      }

   }

   protected void declareSysProperty(ExecTask cmd, Environment.Variable property) throws BuildException {
      this.addValue(cmd, "-J-D" + property.getContent());
   }

   protected void bindToKeystore(ExecTask cmd) {
      if (null != this.keystore) {
         this.addValue(cmd, "-keystore");
         File keystoreFile = this.getProject().resolveFile(this.keystore);
         String loc;
         if (keystoreFile.exists()) {
            loc = keystoreFile.getPath();
         } else {
            loc = this.keystore;
         }

         this.addValue(cmd, loc);
      }

      if (null != this.storetype) {
         this.addValue(cmd, "-storetype");
         this.addValue(cmd, this.storetype);
      }

   }

   protected ExecTask createJarSigner() {
      ExecTask cmd = new ExecTask(this);
      cmd.setExecutable(JavaEnvUtils.getJdkExecutable("jarsigner"));
      cmd.setTaskType("jarsigner");
      cmd.setFailonerror(true);
      cmd.addConfiguredRedirector(this.redirector);
      return cmd;
   }

   protected Vector createUnifiedSources() {
      Vector sources = (Vector)this.filesets.clone();
      if (this.jar != null) {
         FileSet sourceJar = new FileSet();
         sourceJar.setProject(this.getProject());
         sourceJar.setFile(this.jar);
         sourceJar.setDir(this.jar.getParentFile());
         sources.add(sourceJar);
      }

      return sources;
   }

   protected Path createUnifiedSourcePath() {
      Path p = this.path == null ? new Path(this.getProject()) : (Path)this.path.clone();
      Vector s = this.createUnifiedSources();
      Enumeration e = s.elements();

      while(e.hasMoreElements()) {
         p.add((ResourceCollection)((FileSet)e.nextElement()));
      }

      return p;
   }

   protected boolean hasResources() {
      return this.path != null || this.filesets.size() > 0;
   }

   protected void addValue(ExecTask cmd, String value) {
      cmd.createArg().setValue(value);
   }
}
