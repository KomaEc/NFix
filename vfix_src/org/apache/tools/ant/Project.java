package org.apache.tools.ant;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.WeakHashMap;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.launch.Locator;
import org.apache.tools.ant.types.Description;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.StringUtils;

public class Project implements ResourceFactory {
   private static final String LINE_SEP = System.getProperty("line.separator");
   public static final int MSG_ERR = 0;
   public static final int MSG_WARN = 1;
   public static final int MSG_INFO = 2;
   public static final int MSG_VERBOSE = 3;
   public static final int MSG_DEBUG = 4;
   private static final String VISITING = "VISITING";
   private static final String VISITED = "VISITED";
   /** @deprecated */
   public static final String JAVA_1_0 = "1.0";
   /** @deprecated */
   public static final String JAVA_1_1 = "1.1";
   /** @deprecated */
   public static final String JAVA_1_2 = "1.2";
   /** @deprecated */
   public static final String JAVA_1_3 = "1.3";
   /** @deprecated */
   public static final String JAVA_1_4 = "1.4";
   public static final String TOKEN_START = "@";
   public static final String TOKEN_END = "@";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private String name;
   private String description;
   private Hashtable references = new Project.AntRefTable();
   private HashMap idReferences = new HashMap();
   private Project parentIdProject = null;
   private String defaultTarget;
   private Hashtable targets = new Hashtable();
   private FilterSet globalFilterSet = new FilterSet();
   private FilterSetCollection globalFilters;
   private File baseDir;
   private Vector listeners;
   private ClassLoader coreLoader;
   private Map threadTasks;
   private Map threadGroupTasks;
   private InputHandler inputHandler;
   private InputStream defaultInputStream;
   private boolean keepGoingMode;
   private boolean loggingMessage;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Task;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$helper$DefaultExecutor;

   public void setInputHandler(InputHandler handler) {
      this.inputHandler = handler;
   }

   public void setDefaultInputStream(InputStream defaultInputStream) {
      this.defaultInputStream = defaultInputStream;
   }

   public InputStream getDefaultInputStream() {
      return this.defaultInputStream;
   }

   public InputHandler getInputHandler() {
      return this.inputHandler;
   }

   public Project() {
      this.globalFilterSet.setProject(this);
      this.globalFilters = new FilterSetCollection(this.globalFilterSet);
      this.listeners = new Vector();
      this.coreLoader = null;
      this.threadTasks = Collections.synchronizedMap(new WeakHashMap());
      this.threadGroupTasks = Collections.synchronizedMap(new WeakHashMap());
      this.inputHandler = null;
      this.defaultInputStream = null;
      this.keepGoingMode = false;
      this.loggingMessage = false;
      this.inputHandler = new DefaultInputHandler();
   }

   public Project createSubProject() {
      Project subProject = null;

      try {
         subProject = (Project)((Project)this.getClass().newInstance());
      } catch (Exception var3) {
         subProject = new Project();
      }

      this.initSubProject(subProject);
      return subProject;
   }

   public void initSubProject(Project subProject) {
      ComponentHelper.getComponentHelper(subProject).initSubProject(ComponentHelper.getComponentHelper(this));
      subProject.setDefaultInputStream(this.getDefaultInputStream());
      subProject.setKeepGoingMode(this.isKeepGoingMode());
      subProject.setExecutor(this.getExecutor().getSubProjectExecutor());
   }

   public void init() throws BuildException {
      this.initProperties();
      ComponentHelper.getComponentHelper(this).initDefaultDefinitions();
   }

   public void initProperties() throws BuildException {
      this.setJavaVersionProperty();
      this.setSystemProperties();
      this.setPropertyInternal("ant.version", Main.getAntVersion());
      this.setAntLib();
   }

   private void setAntLib() {
      File antlib = Locator.getClassSource(class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
      if (antlib != null) {
         this.setPropertyInternal("ant.core.lib", antlib.getAbsolutePath());
      }

   }

   public AntClassLoader createClassLoader(Path path) {
      return new AntClassLoader(this.getClass().getClassLoader(), this, path);
   }

   public AntClassLoader createClassLoader(ClassLoader parent, Path path) {
      return new AntClassLoader(parent, this, path);
   }

   public void setCoreLoader(ClassLoader coreLoader) {
      this.coreLoader = coreLoader;
   }

   public ClassLoader getCoreLoader() {
      return this.coreLoader;
   }

   public synchronized void addBuildListener(BuildListener listener) {
      if (!this.listeners.contains(listener)) {
         Vector newListeners = this.getBuildListeners();
         newListeners.addElement(listener);
         this.listeners = newListeners;
      }
   }

   public synchronized void removeBuildListener(BuildListener listener) {
      Vector newListeners = this.getBuildListeners();
      newListeners.removeElement(listener);
      this.listeners = newListeners;
   }

   public Vector getBuildListeners() {
      return (Vector)this.listeners.clone();
   }

   public void log(String message) {
      this.log(message, 2);
   }

   public void log(String message, int msgLevel) {
      this.log((String)message, (Throwable)null, msgLevel);
   }

   public void log(String message, Throwable throwable, int msgLevel) {
      this.fireMessageLogged(this, message, throwable, msgLevel);
   }

   public void log(Task task, String message, int msgLevel) {
      this.fireMessageLogged((Task)task, message, (Throwable)null, msgLevel);
   }

   public void log(Task task, String message, Throwable throwable, int msgLevel) {
      this.fireMessageLogged(task, message, throwable, msgLevel);
   }

   public void log(Target target, String message, int msgLevel) {
      this.log((Target)target, message, (Throwable)null, msgLevel);
   }

   public void log(Target target, String message, Throwable throwable, int msgLevel) {
      this.fireMessageLogged(target, message, throwable, msgLevel);
   }

   public FilterSet getGlobalFilterSet() {
      return this.globalFilterSet;
   }

   public void setProperty(String name, String value) {
      PropertyHelper.getPropertyHelper(this).setProperty((String)null, name, value, true);
   }

   public void setNewProperty(String name, String value) {
      PropertyHelper.getPropertyHelper(this).setNewProperty((String)null, name, value);
   }

   public void setUserProperty(String name, String value) {
      PropertyHelper.getPropertyHelper(this).setUserProperty((String)null, name, value);
   }

   public void setInheritedProperty(String name, String value) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      ph.setInheritedProperty((String)null, name, value);
   }

   private void setPropertyInternal(String name, String value) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      ph.setProperty((String)null, name, value, false);
   }

   public String getProperty(String propertyName) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      return (String)ph.getProperty((String)null, propertyName);
   }

   public String replaceProperties(String value) throws BuildException {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      return ph.replaceProperties((String)null, value, (Hashtable)null);
   }

   public String getUserProperty(String propertyName) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      return (String)ph.getUserProperty((String)null, propertyName);
   }

   public Hashtable getProperties() {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      return ph.getProperties();
   }

   public Hashtable getUserProperties() {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      return ph.getUserProperties();
   }

   public void copyUserProperties(Project other) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      ph.copyUserProperties(other);
   }

   public void copyInheritedProperties(Project other) {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(this);
      ph.copyInheritedProperties(other);
   }

   /** @deprecated */
   public void setDefaultTarget(String defaultTarget) {
      this.defaultTarget = defaultTarget;
   }

   public String getDefaultTarget() {
      return this.defaultTarget;
   }

   public void setDefault(String defaultTarget) {
      this.defaultTarget = defaultTarget;
   }

   public void setName(String name) {
      this.setUserProperty("ant.project.name", name);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      if (this.description == null) {
         this.description = Description.getDescription(this);
      }

      return this.description;
   }

   /** @deprecated */
   public void addFilter(String token, String value) {
      if (token != null) {
         this.globalFilterSet.addFilter(new FilterSet.Filter(token, value));
      }
   }

   /** @deprecated */
   public Hashtable getFilters() {
      return this.globalFilterSet.getFilterHash();
   }

   public void setBasedir(String baseD) throws BuildException {
      this.setBaseDir(new File(baseD));
   }

   public void setBaseDir(File baseDir) throws BuildException {
      baseDir = FILE_UTILS.normalize(baseDir.getAbsolutePath());
      if (!baseDir.exists()) {
         throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " does not exist");
      } else if (!baseDir.isDirectory()) {
         throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " is not a directory");
      } else {
         this.baseDir = baseDir;
         this.setPropertyInternal("basedir", this.baseDir.getPath());
         String msg = "Project base dir set to: " + this.baseDir;
         this.log(msg, 3);
      }
   }

   public File getBaseDir() {
      if (this.baseDir == null) {
         try {
            this.setBasedir(".");
         } catch (BuildException var2) {
            var2.printStackTrace();
         }
      }

      return this.baseDir;
   }

   public void setKeepGoingMode(boolean keepGoingMode) {
      this.keepGoingMode = keepGoingMode;
   }

   public boolean isKeepGoingMode() {
      return this.keepGoingMode;
   }

   /** @deprecated */
   public static String getJavaVersion() {
      return JavaEnvUtils.getJavaVersion();
   }

   public void setJavaVersionProperty() throws BuildException {
      String javaVersion = JavaEnvUtils.getJavaVersion();
      this.setPropertyInternal("ant.java.version", javaVersion);
      if (!JavaEnvUtils.isJavaVersion("1.0") && !JavaEnvUtils.isJavaVersion("1.1")) {
         this.log("Detected Java version: " + javaVersion + " in: " + System.getProperty("java.home"), 3);
         this.log("Detected OS: " + System.getProperty("os.name"), 3);
      } else {
         throw new BuildException("Ant cannot work on Java 1.0 / 1.1");
      }
   }

   public void setSystemProperties() {
      Properties systemP = System.getProperties();
      Enumeration e = systemP.propertyNames();

      while(e.hasMoreElements()) {
         String propertyName = (String)e.nextElement();
         String value = systemP.getProperty(propertyName);
         this.setPropertyInternal(propertyName, value);
      }

   }

   public void addTaskDefinition(String taskName, Class taskClass) throws BuildException {
      ComponentHelper.getComponentHelper(this).addTaskDefinition(taskName, taskClass);
   }

   public void checkTaskClass(Class taskClass) throws BuildException {
      ComponentHelper.getComponentHelper(this).checkTaskClass(taskClass);
      String message;
      if (!Modifier.isPublic(taskClass.getModifiers())) {
         message = taskClass + " is not public";
         this.log(message, 0);
         throw new BuildException(message);
      } else if (Modifier.isAbstract(taskClass.getModifiers())) {
         message = taskClass + " is abstract";
         this.log(message, 0);
         throw new BuildException(message);
      } else {
         String message;
         try {
            taskClass.getConstructor((Class[])null);
         } catch (NoSuchMethodException var4) {
            message = "No public no-arg constructor in " + taskClass;
            this.log(message, 0);
            throw new BuildException(message);
         } catch (LinkageError var5) {
            message = "Could not load " + taskClass + ": " + var5;
            this.log(message, 0);
            throw new BuildException(message, var5);
         }

         if (!(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(taskClass)) {
            TaskAdapter.checkTaskClass(taskClass, this);
         }

      }
   }

   public Hashtable getTaskDefinitions() {
      return ComponentHelper.getComponentHelper(this).getTaskDefinitions();
   }

   public void addDataTypeDefinition(String typeName, Class typeClass) {
      ComponentHelper.getComponentHelper(this).addDataTypeDefinition(typeName, typeClass);
   }

   public Hashtable getDataTypeDefinitions() {
      return ComponentHelper.getComponentHelper(this).getDataTypeDefinitions();
   }

   public void addTarget(Target target) throws BuildException {
      this.addTarget(target.getName(), target);
   }

   public void addTarget(String targetName, Target target) throws BuildException {
      if (this.targets.get(targetName) != null) {
         throw new BuildException("Duplicate target: `" + targetName + "'");
      } else {
         this.addOrReplaceTarget(targetName, target);
      }
   }

   public void addOrReplaceTarget(Target target) {
      this.addOrReplaceTarget(target.getName(), target);
   }

   public void addOrReplaceTarget(String targetName, Target target) {
      String msg = " +Target: " + targetName;
      this.log(msg, 4);
      target.setProject(this);
      this.targets.put(targetName, target);
   }

   public Hashtable getTargets() {
      return this.targets;
   }

   public Task createTask(String taskType) throws BuildException {
      return ComponentHelper.getComponentHelper(this).createTask(taskType);
   }

   public Object createDataType(String typeName) throws BuildException {
      return ComponentHelper.getComponentHelper(this).createDataType(typeName);
   }

   public void setExecutor(Executor e) {
      this.addReference("ant.executor", e);
   }

   public Executor getExecutor() {
      Object o = this.getReference("ant.executor");
      if (o == null) {
         String classname = this.getProperty("ant.executor.class");
         if (classname == null) {
            classname = (class$org$apache$tools$ant$helper$DefaultExecutor == null ? (class$org$apache$tools$ant$helper$DefaultExecutor = class$("org.apache.tools.ant.helper.DefaultExecutor")) : class$org$apache$tools$ant$helper$DefaultExecutor).getName();
         }

         this.log("Attempting to create object of type " + classname, 4);

         try {
            o = Class.forName(classname, true, this.coreLoader).newInstance();
         } catch (ClassNotFoundException var6) {
            try {
               o = Class.forName(classname).newInstance();
            } catch (Exception var5) {
               this.log(var5.toString(), 0);
            }
         } catch (Exception var7) {
            this.log(var7.toString(), 0);
         }

         if (o == null) {
            throw new BuildException("Unable to obtain a Target Executor instance.");
         }

         this.setExecutor((Executor)o);
      }

      return (Executor)o;
   }

   public void executeTargets(Vector names) throws BuildException {
      this.getExecutor().executeTargets(this, (String[])((String[])names.toArray(new String[names.size()])));
   }

   public void demuxOutput(String output, boolean isWarning) {
      Task task = this.getThreadTask(Thread.currentThread());
      if (task == null) {
         this.log(output, isWarning ? 1 : 2);
      } else if (isWarning) {
         task.handleErrorOutput(output);
      } else {
         task.handleOutput(output);
      }

   }

   public int defaultInput(byte[] buffer, int offset, int length) throws IOException {
      if (this.defaultInputStream != null) {
         System.out.flush();
         return this.defaultInputStream.read(buffer, offset, length);
      } else {
         throw new EOFException("No input provided for project");
      }
   }

   public int demuxInput(byte[] buffer, int offset, int length) throws IOException {
      Task task = this.getThreadTask(Thread.currentThread());
      return task == null ? this.defaultInput(buffer, offset, length) : task.handleInput(buffer, offset, length);
   }

   public void demuxFlush(String output, boolean isError) {
      Task task = this.getThreadTask(Thread.currentThread());
      if (task == null) {
         this.fireMessageLogged(this, output, isError ? 0 : 2);
      } else if (isError) {
         task.handleErrorFlush(output);
      } else {
         task.handleFlush(output);
      }

   }

   public void executeTarget(String targetName) throws BuildException {
      if (targetName == null) {
         String msg = "No target specified";
         throw new BuildException(msg);
      } else {
         this.executeSortedTargets(this.topoSort(targetName, this.targets, false));
      }
   }

   public void executeSortedTargets(Vector sortedTargets) throws BuildException {
      Set succeededTargets = new HashSet();
      BuildException buildException = null;
      Enumeration iter = sortedTargets.elements();

      while(true) {
         Target curtarget;
         boolean canExecute;
         do {
            if (!iter.hasMoreElements()) {
               if (buildException != null) {
                  throw buildException;
               }

               return;
            }

            curtarget = (Target)iter.nextElement();
            canExecute = true;
            Enumeration depIter = curtarget.getDependencies();

            while(depIter.hasMoreElements()) {
               String dependencyName = (String)depIter.nextElement();
               if (!succeededTargets.contains(dependencyName)) {
                  canExecute = false;
                  this.log((Target)curtarget, (String)("Cannot execute '" + curtarget.getName() + "' - '" + dependencyName + "' failed or was not executed."), 0);
                  break;
               }
            }
         } while(!canExecute);

         Object thrownException = null;

         try {
            curtarget.performTasks();
            succeededTargets.add(curtarget.getName());
         } catch (RuntimeException var9) {
            if (!this.keepGoingMode) {
               throw var9;
            }

            thrownException = var9;
         } catch (Throwable var10) {
            if (!this.keepGoingMode) {
               throw new BuildException(var10);
            }

            thrownException = var10;
         }

         if (thrownException != null) {
            if (thrownException instanceof BuildException) {
               this.log((Target)curtarget, (String)("Target '" + curtarget.getName() + "' failed with message '" + ((Throwable)thrownException).getMessage() + "'."), 0);
               if (buildException == null) {
                  buildException = (BuildException)thrownException;
               }
            } else {
               this.log((Target)curtarget, (String)("Target '" + curtarget.getName() + "' failed with message '" + ((Throwable)thrownException).getMessage() + "'."), 0);
               ((Throwable)thrownException).printStackTrace(System.err);
               if (buildException == null) {
                  buildException = new BuildException((Throwable)thrownException);
               }
            }
         }
      }
   }

   /** @deprecated */
   public File resolveFile(String fileName, File rootDir) {
      return FILE_UTILS.resolveFile(rootDir, fileName);
   }

   public File resolveFile(String fileName) {
      return FILE_UTILS.resolveFile(this.baseDir, fileName);
   }

   /** @deprecated */
   public static String translatePath(String toProcess) {
      return FileUtils.translatePath(toProcess);
   }

   /** @deprecated */
   public void copyFile(String sourceFile, String destFile) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile);
   }

   /** @deprecated */
   public void copyFile(String sourceFile, String destFile, boolean filtering) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
   }

   /** @deprecated */
   public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
   }

   /** @deprecated */
   public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
   }

   /** @deprecated */
   public void copyFile(File sourceFile, File destFile) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile);
   }

   /** @deprecated */
   public void copyFile(File sourceFile, File destFile, boolean filtering) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null);
   }

   /** @deprecated */
   public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite);
   }

   /** @deprecated */
   public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
      FILE_UTILS.copyFile(sourceFile, destFile, filtering ? this.globalFilters : null, overwrite, preserveLastModified);
   }

   /** @deprecated */
   public void setFileLastModified(File file, long time) throws BuildException {
      FILE_UTILS.setFileLastModified(file, time);
      this.log("Setting modification time for " + file, 3);
   }

   public static boolean toBoolean(String s) {
      return "on".equalsIgnoreCase(s) || "true".equalsIgnoreCase(s) || "yes".equalsIgnoreCase(s);
   }

   public final Vector topoSort(String root, Hashtable targetTable) throws BuildException {
      return this.topoSort(new String[]{root}, targetTable, true);
   }

   public final Vector topoSort(String root, Hashtable targetTable, boolean returnAll) throws BuildException {
      return this.topoSort(new String[]{root}, targetTable, returnAll);
   }

   public final Vector topoSort(String[] root, Hashtable targetTable, boolean returnAll) throws BuildException {
      Vector ret = new Vector();
      Hashtable state = new Hashtable();
      Stack visiting = new Stack();

      for(int i = 0; i < root.length; ++i) {
         String st = (String)((String)state.get(root[i]));
         if (st == null) {
            this.tsort(root[i], targetTable, state, visiting, ret);
         } else if (st == "VISITING") {
            throw new RuntimeException("Unexpected node in visiting state: " + root[i]);
         }
      }

      StringBuffer buf = new StringBuffer("Build sequence for target(s)");

      for(int j = 0; j < root.length; ++j) {
         buf.append(j == 0 ? " `" : ", `").append(root[j]).append('\'');
      }

      buf.append(" is " + ret);
      this.log(buf.toString(), 3);
      Vector complete = returnAll ? ret : new Vector(ret);
      Enumeration en = targetTable.keys();

      while(en.hasMoreElements()) {
         String curTarget = (String)en.nextElement();
         String st = (String)state.get(curTarget);
         if (st == null) {
            this.tsort(curTarget, targetTable, state, visiting, complete);
         } else if (st == "VISITING") {
            throw new RuntimeException("Unexpected node in visiting state: " + curTarget);
         }
      }

      this.log("Complete build sequence is " + complete, 3);
      return ret;
   }

   private void tsort(String root, Hashtable targetTable, Hashtable state, Stack visiting, Vector ret) throws BuildException {
      state.put(root, "VISITING");
      visiting.push(root);
      Target target = (Target)targetTable.get(root);
      String cur;
      if (target == null) {
         StringBuffer sb = new StringBuffer("Target \"");
         sb.append(root);
         sb.append("\" does not exist in the project \"");
         sb.append(this.name);
         sb.append("\". ");
         visiting.pop();
         if (!visiting.empty()) {
            cur = (String)visiting.peek();
            sb.append("It is used from target \"");
            sb.append(cur);
            sb.append("\".");
         }

         throw new BuildException(new String(sb));
      } else {
         Enumeration en = target.getDependencies();

         while(en.hasMoreElements()) {
            cur = (String)en.nextElement();
            String m = (String)state.get(cur);
            if (m == null) {
               this.tsort(cur, targetTable, state, visiting, ret);
            } else if (m == "VISITING") {
               throw makeCircularException(cur, visiting);
            }
         }

         String p = (String)visiting.pop();
         if (root != p) {
            throw new RuntimeException("Unexpected internal error: expected to pop " + root + " but got " + p);
         } else {
            state.put(root, "VISITED");
            ret.addElement(target);
         }
      }
   }

   private static BuildException makeCircularException(String end, Stack stk) {
      StringBuffer sb = new StringBuffer("Circular dependency: ");
      sb.append(end);

      String c;
      do {
         c = (String)stk.pop();
         sb.append(" <- ");
         sb.append(c);
      } while(!c.equals(end));

      return new BuildException(new String(sb));
   }

   public void inheritIDReferences(Project parent) {
      this.parentIdProject = parent;
   }

   private Object resolveIdReference(String key, Project callerProject) {
      UnknownElement origUE = (UnknownElement)this.idReferences.get(key);
      if (origUE == null) {
         return this.parentIdProject == null ? null : this.parentIdProject.resolveIdReference(key, callerProject);
      } else {
         callerProject.log("Warning: Reference " + key + " has not been set at runtime," + " but was found during" + LINE_SEP + "build file parsing, attempting to resolve." + " Future versions of Ant may support" + LINE_SEP + " referencing ids defined in non-executed targets.", 1);
         UnknownElement copyUE = origUE.copy(callerProject);
         copyUE.maybeConfigure();
         return copyUE.getRealThing();
      }
   }

   public void addIdReference(String id, Object value) {
      this.idReferences.put(id, value);
   }

   public void addReference(String referenceName, Object value) {
      synchronized(this.references) {
         Object old = ((Project.AntRefTable)this.references).getReal(referenceName);
         if (old != value) {
            if (old != null && !(old instanceof UnknownElement)) {
               this.log("Overriding previous definition of reference to " + referenceName, 3);
            }

            this.log("Adding reference: " + referenceName, 4);
            this.references.put(referenceName, value);
         }
      }
   }

   public Hashtable getReferences() {
      return this.references;
   }

   public Object getReference(String key) {
      Object ret = this.references.get(key);
      if (ret != null) {
         return ret;
      } else {
         ret = this.resolveIdReference(key, this);
         if (ret == null && !key.equals("ant.PropertyHelper")) {
            Vector p = new Vector();
            PropertyHelper.getPropertyHelper(this).parsePropertyString(key, new Vector(), p);
            if (p.size() == 1) {
               this.log("Unresolvable reference " + key + " might be a misuse of property expansion syntax.", 1);
            }
         }

         return ret;
      }
   }

   public String getElementName(Object element) {
      return ComponentHelper.getComponentHelper(this).getElementName(element);
   }

   public void fireBuildStarted() {
      BuildEvent event = new BuildEvent(this);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.buildStarted(event);
      }

   }

   public void fireBuildFinished(Throwable exception) {
      BuildEvent event = new BuildEvent(this);
      event.setException(exception);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.buildFinished(event);
      }

      IntrospectionHelper.clearCache();
   }

   public void fireSubBuildStarted() {
      BuildEvent event = new BuildEvent(this);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         Object listener = iter.next();
         if (listener instanceof SubBuildListener) {
            ((SubBuildListener)listener).subBuildStarted(event);
         }
      }

   }

   public void fireSubBuildFinished(Throwable exception) {
      BuildEvent event = new BuildEvent(this);
      event.setException(exception);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         Object listener = iter.next();
         if (listener instanceof SubBuildListener) {
            ((SubBuildListener)listener).subBuildFinished(event);
         }
      }

   }

   protected void fireTargetStarted(Target target) {
      BuildEvent event = new BuildEvent(target);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.targetStarted(event);
      }

   }

   protected void fireTargetFinished(Target target, Throwable exception) {
      BuildEvent event = new BuildEvent(target);
      event.setException(exception);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.targetFinished(event);
      }

   }

   protected void fireTaskStarted(Task task) {
      this.registerThreadTask(Thread.currentThread(), task);
      BuildEvent event = new BuildEvent(task);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.taskStarted(event);
      }

   }

   protected void fireTaskFinished(Task task, Throwable exception) {
      this.registerThreadTask(Thread.currentThread(), (Task)null);
      System.out.flush();
      System.err.flush();
      BuildEvent event = new BuildEvent(task);
      event.setException(exception);
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         BuildListener listener = (BuildListener)iter.next();
         listener.taskFinished(event);
      }

   }

   private void fireMessageLoggedEvent(BuildEvent event, String message, int priority) {
      if (message.endsWith(StringUtils.LINE_SEP)) {
         int endIndex = message.length() - StringUtils.LINE_SEP.length();
         event.setMessage(message.substring(0, endIndex), priority);
      } else {
         event.setMessage(message, priority);
      }

      synchronized(this) {
         if (!this.loggingMessage) {
            try {
               this.loggingMessage = true;
               Iterator iter = this.listeners.iterator();

               while(iter.hasNext()) {
                  BuildListener listener = (BuildListener)iter.next();
                  listener.messageLogged(event);
               }
            } finally {
               this.loggingMessage = false;
            }

         }
      }
   }

   protected void fireMessageLogged(Project project, String message, int priority) {
      this.fireMessageLogged((Project)project, message, (Throwable)null, priority);
   }

   protected void fireMessageLogged(Project project, String message, Throwable throwable, int priority) {
      BuildEvent event = new BuildEvent(project);
      event.setException(throwable);
      this.fireMessageLoggedEvent(event, message, priority);
   }

   protected void fireMessageLogged(Target target, String message, int priority) {
      this.fireMessageLogged((Target)target, message, (Throwable)null, priority);
   }

   protected void fireMessageLogged(Target target, String message, Throwable throwable, int priority) {
      BuildEvent event = new BuildEvent(target);
      event.setException(throwable);
      this.fireMessageLoggedEvent(event, message, priority);
   }

   protected void fireMessageLogged(Task task, String message, int priority) {
      this.fireMessageLogged((Task)task, message, (Throwable)null, priority);
   }

   protected void fireMessageLogged(Task task, String message, Throwable throwable, int priority) {
      BuildEvent event = new BuildEvent(task);
      event.setException(throwable);
      this.fireMessageLoggedEvent(event, message, priority);
   }

   public synchronized void registerThreadTask(Thread thread, Task task) {
      if (task != null) {
         this.threadTasks.put(thread, task);
         this.threadGroupTasks.put(thread.getThreadGroup(), task);
      } else {
         this.threadTasks.remove(thread);
         this.threadGroupTasks.remove(thread.getThreadGroup());
      }

   }

   public Task getThreadTask(Thread thread) {
      Task task = (Task)this.threadTasks.get(thread);
      if (task == null) {
         for(ThreadGroup group = thread.getThreadGroup(); task == null && group != null; group = group.getParent()) {
            task = (Task)this.threadGroupTasks.get(group);
         }
      }

      return task;
   }

   public final void setProjectReference(Object obj) {
      if (obj instanceof ProjectComponent) {
         ((ProjectComponent)obj).setProject(this);
      } else {
         try {
            Method method = obj.getClass().getMethod("setProject", class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
            if (method != null) {
               method.invoke(obj, this);
            }
         } catch (Throwable var3) {
         }

      }
   }

   public Resource getResource(String name) {
      return new FileResource(this.getBaseDir(), name);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static class AntRefTable extends Hashtable {
      AntRefTable() {
      }

      private Object getReal(Object key) {
         return super.get(key);
      }

      public Object get(Object key) {
         Object o = this.getReal(key);
         if (o instanceof UnknownElement) {
            UnknownElement ue = (UnknownElement)o;
            ue.maybeConfigure();
            o = ue.getRealThing();
         }

         return o;
      }
   }
}
