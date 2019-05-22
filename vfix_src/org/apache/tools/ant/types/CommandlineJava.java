package org.apache.tools.ant.types;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

public class CommandlineJava implements Cloneable {
   private Commandline vmCommand = new Commandline();
   private Commandline javaCommand = new Commandline();
   private CommandlineJava.SysProperties sysProperties = new CommandlineJava.SysProperties();
   private Path classpath = null;
   private Path bootclasspath = null;
   private String vmVersion;
   private String maxMemory = null;
   private Assertions assertions = null;
   private boolean executeJar = false;
   private boolean cloneVm = false;

   public CommandlineJava() {
      this.setVm(JavaEnvUtils.getJreExecutable("java"));
      this.setVmversion(JavaEnvUtils.getJavaVersion());
   }

   public Commandline.Argument createArgument() {
      return this.javaCommand.createArgument();
   }

   public Commandline.Argument createVmArgument() {
      return this.vmCommand.createArgument();
   }

   public void addSysproperty(Environment.Variable sysp) {
      this.sysProperties.addVariable(sysp);
   }

   public void addSyspropertyset(PropertySet sysp) {
      this.sysProperties.addSyspropertyset(sysp);
   }

   public void addSysproperties(CommandlineJava.SysProperties sysp) {
      this.sysProperties.addSysproperties(sysp);
   }

   public void setVm(String vm) {
      this.vmCommand.setExecutable(vm);
   }

   public void setVmversion(String value) {
      this.vmVersion = value;
   }

   public void setCloneVm(boolean cloneVm) {
      this.cloneVm = cloneVm;
   }

   public Assertions getAssertions() {
      return this.assertions;
   }

   public void setAssertions(Assertions assertions) {
      this.assertions = assertions;
   }

   public void setJar(String jarpathname) {
      this.javaCommand.setExecutable(jarpathname);
      this.executeJar = true;
   }

   public String getJar() {
      return this.executeJar ? this.javaCommand.getExecutable() : null;
   }

   public void setClassname(String classname) {
      this.javaCommand.setExecutable(classname);
      this.executeJar = false;
   }

   public String getClassname() {
      return !this.executeJar ? this.javaCommand.getExecutable() : null;
   }

   public Path createClasspath(Project p) {
      if (this.classpath == null) {
         this.classpath = new Path(p);
      }

      return this.classpath;
   }

   public Path createBootclasspath(Project p) {
      if (this.bootclasspath == null) {
         this.bootclasspath = new Path(p);
      }

      return this.bootclasspath;
   }

   public String getVmversion() {
      return this.vmVersion;
   }

   public String[] getCommandline() {
      List commands = new LinkedList();
      ListIterator listIterator = commands.listIterator();
      this.addCommandsToList(listIterator);
      return (String[])((String[])commands.toArray(new String[commands.size()]));
   }

   private void addCommandsToList(ListIterator listIterator) {
      this.getActualVMCommand().addCommandToList(listIterator);
      this.sysProperties.addDefinitionsToList(listIterator);
      if (this.isCloneVm()) {
         CommandlineJava.SysProperties clonedSysProperties = new CommandlineJava.SysProperties();
         PropertySet ps = new PropertySet();
         PropertySet.BuiltinPropertySetName sys = new PropertySet.BuiltinPropertySetName();
         sys.setValue("system");
         ps.appendBuiltin(sys);
         clonedSysProperties.addSyspropertyset(ps);
         clonedSysProperties.addDefinitionsToList(listIterator);
      }

      Path bcp = this.calculateBootclasspath(true);
      if (bcp.size() > 0) {
         listIterator.add("-Xbootclasspath:" + bcp.toString());
      }

      if (this.haveClasspath()) {
         listIterator.add("-classpath");
         listIterator.add(this.classpath.concatSystemClasspath("ignore").toString());
      }

      if (this.getAssertions() != null) {
         this.getAssertions().applyAssertions(listIterator);
      }

      if (this.executeJar) {
         listIterator.add("-jar");
      }

      this.javaCommand.addCommandToList(listIterator);
   }

   public void setMaxmemory(String max) {
      this.maxMemory = max;
   }

   public String toString() {
      return Commandline.toString(this.getCommandline());
   }

   public String describeCommand() {
      return Commandline.describeCommand(this.getCommandline());
   }

   public String describeJavaCommand() {
      return Commandline.describeCommand(this.getJavaCommand());
   }

   protected Commandline getActualVMCommand() {
      Commandline actualVMCommand = (Commandline)this.vmCommand.clone();
      if (this.maxMemory != null) {
         if (this.vmVersion.startsWith("1.1")) {
            actualVMCommand.createArgument().setValue("-mx" + this.maxMemory);
         } else {
            actualVMCommand.createArgument().setValue("-Xmx" + this.maxMemory);
         }
      }

      return actualVMCommand;
   }

   /** @deprecated */
   public int size() {
      int size = this.getActualVMCommand().size() + this.javaCommand.size() + this.sysProperties.size();
      if (this.isCloneVm()) {
         size += System.getProperties().size();
      }

      if (this.haveClasspath()) {
         size += 2;
      }

      if (this.calculateBootclasspath(true).size() > 0) {
         ++size;
      }

      if (this.executeJar) {
         ++size;
      }

      if (this.getAssertions() != null) {
         size += this.getAssertions().size();
      }

      return size;
   }

   public Commandline getJavaCommand() {
      return this.javaCommand;
   }

   public Commandline getVmCommand() {
      return this.getActualVMCommand();
   }

   public Path getClasspath() {
      return this.classpath;
   }

   public Path getBootclasspath() {
      return this.bootclasspath;
   }

   public void setSystemProperties() throws BuildException {
      this.sysProperties.setSystem();
   }

   public void restoreSystemProperties() throws BuildException {
      this.sysProperties.restoreSystem();
   }

   public CommandlineJava.SysProperties getSystemProperties() {
      return this.sysProperties;
   }

   public Object clone() throws CloneNotSupportedException {
      try {
         CommandlineJava c = (CommandlineJava)super.clone();
         c.vmCommand = (Commandline)this.vmCommand.clone();
         c.javaCommand = (Commandline)this.javaCommand.clone();
         c.sysProperties = (CommandlineJava.SysProperties)this.sysProperties.clone();
         if (this.classpath != null) {
            c.classpath = (Path)this.classpath.clone();
         }

         if (this.bootclasspath != null) {
            c.bootclasspath = (Path)this.bootclasspath.clone();
         }

         if (this.assertions != null) {
            c.assertions = (Assertions)this.assertions.clone();
         }

         return c;
      } catch (CloneNotSupportedException var2) {
         throw new BuildException(var2);
      }
   }

   public void clearJavaArgs() {
      this.javaCommand.clearArgs();
   }

   protected boolean haveClasspath() {
      Path fullClasspath = this.classpath != null ? this.classpath.concatSystemClasspath("ignore") : null;
      return fullClasspath != null && fullClasspath.toString().trim().length() > 0;
   }

   protected boolean haveBootclasspath(boolean log) {
      return this.calculateBootclasspath(log).size() > 0;
   }

   private Path calculateBootclasspath(boolean log) {
      if (this.vmVersion.startsWith("1.1")) {
         if (this.bootclasspath != null && log) {
            this.bootclasspath.log("Ignoring bootclasspath as the target VM doesn't support it.");
         }
      } else {
         if (this.bootclasspath != null) {
            return this.bootclasspath.concatSystemBootClasspath(this.isCloneVm() ? "last" : "ignore");
         }

         if (this.isCloneVm()) {
            return Path.systemBootClasspath;
         }
      }

      return new Path((Project)null);
   }

   private boolean isCloneVm() {
      return this.cloneVm || "true".equals(System.getProperty("ant.build.clonevm"));
   }

   public static class SysProperties extends Environment implements Cloneable {
      Properties sys = null;
      private Vector propertySets = new Vector();

      public String[] getVariables() throws BuildException {
         List definitions = new LinkedList();
         ListIterator list = definitions.listIterator();
         this.addDefinitionsToList(list);
         return definitions.size() == 0 ? null : (String[])((String[])definitions.toArray(new String[definitions.size()]));
      }

      public void addDefinitionsToList(ListIterator listIt) {
         String[] props = super.getVariables();
         if (props != null) {
            for(int i = 0; i < props.length; ++i) {
               listIt.add("-D" + props[i]);
            }
         }

         Properties propertySetProperties = this.mergePropertySets();
         Enumeration e = propertySetProperties.keys();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = propertySetProperties.getProperty(key);
            listIt.add("-D" + key + "=" + value);
         }

      }

      public int size() {
         Properties p = this.mergePropertySets();
         return this.variables.size() + p.size();
      }

      public void setSystem() throws BuildException {
         try {
            this.sys = System.getProperties();
            Properties p = new Properties();
            Enumeration e = this.sys.propertyNames();

            while(e.hasMoreElements()) {
               String name = (String)e.nextElement();
               p.put(name, this.sys.getProperty(name));
            }

            p.putAll(this.mergePropertySets());
            e = this.variables.elements();

            while(e.hasMoreElements()) {
               Environment.Variable v = (Environment.Variable)e.nextElement();
               v.validate();
               p.put(v.getKey(), v.getValue());
            }

            System.setProperties(p);
         } catch (SecurityException var4) {
            throw new BuildException("Cannot modify system properties", var4);
         }
      }

      public void restoreSystem() throws BuildException {
         if (this.sys == null) {
            throw new BuildException("Unbalanced nesting of SysProperties");
         } else {
            try {
               System.setProperties(this.sys);
               this.sys = null;
            } catch (SecurityException var2) {
               throw new BuildException("Cannot modify system properties", var2);
            }
         }
      }

      public Object clone() throws CloneNotSupportedException {
         try {
            CommandlineJava.SysProperties c = (CommandlineJava.SysProperties)super.clone();
            c.variables = (Vector)this.variables.clone();
            c.propertySets = (Vector)this.propertySets.clone();
            return c;
         } catch (CloneNotSupportedException var2) {
            return null;
         }
      }

      public void addSyspropertyset(PropertySet ps) {
         this.propertySets.addElement(ps);
      }

      public void addSysproperties(CommandlineJava.SysProperties ps) {
         this.variables.addAll(ps.variables);
         this.propertySets.addAll(ps.propertySets);
      }

      private Properties mergePropertySets() {
         Properties p = new Properties();
         Enumeration e = this.propertySets.elements();

         while(e.hasMoreElements()) {
            PropertySet ps = (PropertySet)e.nextElement();
            p.putAll(ps.getProperties());
         }

         return p;
      }
   }
}
