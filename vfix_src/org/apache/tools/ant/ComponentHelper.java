package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import org.apache.tools.ant.launch.Launcher;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.Typedef;
import org.apache.tools.ant.util.FileUtils;

public class ComponentHelper {
   private ComponentHelper.AntTypeTable antTypeTable;
   private Hashtable taskClassDefinitions = new Hashtable();
   private boolean rebuildTaskClassDefinitions = true;
   private Hashtable typeClassDefinitions = new Hashtable();
   private boolean rebuildTypeClassDefinitions = true;
   private Set checkedNamespaces = new HashSet();
   private Stack antLibStack = new Stack();
   private String antLibCurrentUri = null;
   private ComponentHelper next;
   private Project project;
   private static final String ERROR_NO_TASK_LIST_LOAD = "Can't load default task list";
   private static final String ERROR_NO_TYPE_LIST_LOAD = "Can't load default type list";
   public static final String COMPONENT_HELPER_REFERENCE = "ant.ComponentHelper";
   private static final String BUILD_SYSCLASSPATH_ONLY = "only";
   private static final String ANT_PROPERTY_TASK = "property";
   private static Properties[] defaultDefinitions = new Properties[2];
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$TaskAdapter;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Task;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$Property;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$ComponentHelper;

   public static ComponentHelper getComponentHelper(Project project) {
      if (project == null) {
         return null;
      } else {
         ComponentHelper ph = (ComponentHelper)project.getReference("ant.ComponentHelper");
         if (ph != null) {
            return ph;
         } else {
            ph = new ComponentHelper();
            ph.setProject(project);
            project.addReference("ant.ComponentHelper", ph);
            return ph;
         }
      }
   }

   protected ComponentHelper() {
   }

   public void setNext(ComponentHelper next) {
      this.next = next;
   }

   public ComponentHelper getNext() {
      return this.next;
   }

   public void setProject(Project project) {
      this.project = project;
      this.antTypeTable = new ComponentHelper.AntTypeTable(project);
   }

   public void initSubProject(ComponentHelper helper) {
      ComponentHelper.AntTypeTable typeTable = helper.antTypeTable;
      Iterator i = typeTable.values().iterator();

      while(i.hasNext()) {
         AntTypeDefinition def = (AntTypeDefinition)i.next();
         this.antTypeTable.put(def.getName(), def);
      }

      i = helper.checkedNamespaces.iterator();

      while(i.hasNext()) {
         this.checkedNamespaces.add(i.next());
      }

   }

   public Object createComponent(UnknownElement ue, String ns, String componentType) throws BuildException {
      Object component = this.createComponent(componentType);
      if (component instanceof Task) {
         Task task = (Task)component;
         task.setLocation(ue.getLocation());
         task.setTaskType(componentType);
         task.setTaskName(ue.getTaskName());
         task.setOwningTarget(ue.getOwningTarget());
         task.init();
      }

      return component;
   }

   public Object createComponent(String componentName) {
      AntTypeDefinition def = this.getDefinition(componentName);
      return def == null ? null : def.create(this.project);
   }

   public Class getComponentClass(String componentName) {
      AntTypeDefinition def = this.getDefinition(componentName);
      return def == null ? null : def.getExposedClass(this.project);
   }

   public AntTypeDefinition getDefinition(String componentName) {
      this.checkNamespace(componentName);
      return this.antTypeTable.getDefinition(componentName);
   }

   public void initDefaultDefinitions() {
      this.initTasks();
      this.initTypes();
   }

   public void addTaskDefinition(String taskName, Class taskClass) {
      this.checkTaskClass(taskClass);
      AntTypeDefinition def = new AntTypeDefinition();
      def.setName(taskName);
      def.setClassLoader(taskClass.getClassLoader());
      def.setClass(taskClass);
      def.setAdapterClass(class$org$apache$tools$ant$TaskAdapter == null ? (class$org$apache$tools$ant$TaskAdapter = class$("org.apache.tools.ant.TaskAdapter")) : class$org$apache$tools$ant$TaskAdapter);
      def.setClassName(taskClass.getName());
      def.setAdaptToClass(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task);
      this.updateDataTypeDefinition(def);
   }

   public void checkTaskClass(Class taskClass) throws BuildException {
      String message;
      if (!Modifier.isPublic(taskClass.getModifiers())) {
         message = taskClass + " is not public";
         this.project.log(message, 0);
         throw new BuildException(message);
      } else if (Modifier.isAbstract(taskClass.getModifiers())) {
         message = taskClass + " is abstract";
         this.project.log(message, 0);
         throw new BuildException(message);
      } else {
         try {
            taskClass.getConstructor((Class[])null);
         } catch (NoSuchMethodException var4) {
            String message = "No public no-arg constructor in " + taskClass;
            this.project.log(message, 0);
            throw new BuildException(message);
         }

         if (!(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(taskClass)) {
            TaskAdapter.checkTaskClass(taskClass, this.project);
         }

      }
   }

   public Hashtable getTaskDefinitions() {
      synchronized(this.taskClassDefinitions) {
         synchronized(this.antTypeTable) {
            if (this.rebuildTaskClassDefinitions) {
               this.taskClassDefinitions.clear();
               Iterator i = this.antTypeTable.keySet().iterator();

               while(true) {
                  if (!i.hasNext()) {
                     this.rebuildTaskClassDefinitions = false;
                     break;
                  }

                  String name = (String)i.next();
                  Class clazz = this.antTypeTable.getExposedClass(name);
                  if (clazz != null && (class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(clazz)) {
                     this.taskClassDefinitions.put(name, this.antTypeTable.getTypeClass(name));
                  }
               }
            }
         }
      }

      return this.taskClassDefinitions;
   }

   public Hashtable getDataTypeDefinitions() {
      synchronized(this.typeClassDefinitions) {
         synchronized(this.antTypeTable) {
            if (this.rebuildTypeClassDefinitions) {
               this.typeClassDefinitions.clear();
               Iterator i = this.antTypeTable.keySet().iterator();

               while(true) {
                  if (!i.hasNext()) {
                     this.rebuildTypeClassDefinitions = false;
                     break;
                  }

                  String name = (String)i.next();
                  Class clazz = this.antTypeTable.getExposedClass(name);
                  if (clazz != null && !(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(clazz)) {
                     this.typeClassDefinitions.put(name, this.antTypeTable.getTypeClass(name));
                  }
               }
            }
         }
      }

      return this.typeClassDefinitions;
   }

   public void addDataTypeDefinition(String typeName, Class typeClass) {
      AntTypeDefinition def = new AntTypeDefinition();
      def.setName(typeName);
      def.setClass(typeClass);
      this.updateDataTypeDefinition(def);
      this.project.log(" +User datatype: " + typeName + "     " + typeClass.getName(), 4);
   }

   public void addDataTypeDefinition(AntTypeDefinition def) {
      this.updateDataTypeDefinition(def);
   }

   public Hashtable getAntTypeTable() {
      return this.antTypeTable;
   }

   public Task createTask(String taskType) throws BuildException {
      Task task = this.createNewTask(taskType);
      if (task == null && taskType.equals("property")) {
         this.addTaskDefinition("property", class$org$apache$tools$ant$taskdefs$Property == null ? (class$org$apache$tools$ant$taskdefs$Property = class$("org.apache.tools.ant.taskdefs.Property")) : class$org$apache$tools$ant$taskdefs$Property);
         task = this.createNewTask(taskType);
      }

      return task;
   }

   private Task createNewTask(String taskType) throws BuildException {
      Class c = this.getComponentClass(taskType);
      if (c != null && (class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(c)) {
         Object obj = this.createComponent(taskType);
         if (obj == null) {
            return null;
         } else if (!(obj instanceof Task)) {
            throw new BuildException("Expected a Task from '" + taskType + "' but got an instance of " + obj.getClass().getName() + " instead");
         } else {
            Task task = (Task)obj;
            task.setTaskType(taskType);
            task.setTaskName(taskType);
            this.project.log("   +Task: " + taskType, 4);
            return task;
         }
      } else {
         return null;
      }
   }

   public Object createDataType(String typeName) throws BuildException {
      return this.createComponent(typeName);
   }

   public String getElementName(Object element) {
      return this.getElementName(element, false);
   }

   public String getElementName(Object o, boolean brief) {
      Class elementClass = o.getClass();
      String elementClassname = elementClass.getName();
      Iterator i = this.antTypeTable.values().iterator();

      AntTypeDefinition def;
      do {
         if (!i.hasNext()) {
            return getUnmappedElementName(o.getClass(), brief);
         }

         def = (AntTypeDefinition)i.next();
      } while(!elementClassname.equals(def.getClassName()) || elementClass != def.getExposedClass(this.project));

      String name = def.getName();
      return brief ? name : "The <" + name + "> type";
   }

   public static String getElementName(Project p, Object o, boolean brief) {
      if (p == null) {
         p = getProject(o);
      }

      return p == null ? getUnmappedElementName(o.getClass(), brief) : getComponentHelper(p).getElementName(o, brief);
   }

   private static String getUnmappedElementName(Class c, boolean brief) {
      if (brief) {
         String name = c.getName();
         return name.substring(name.lastIndexOf(46) + 1);
      } else {
         return c.toString();
      }
   }

   private static Project getProject(Object o) {
      if (o instanceof ProjectComponent) {
         return ((ProjectComponent)o).getProject();
      } else {
         try {
            Method m = o.getClass().getMethod("getProject", (Class[])null);
            if ((class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project) == m.getReturnType()) {
               return (Project)m.invoke(o, (Object[])null);
            }
         } catch (Exception var2) {
         }

         return null;
      }
   }

   private boolean validDefinition(AntTypeDefinition def) {
      return def.getTypeClass(this.project) != null && def.getExposedClass(this.project) != null;
   }

   private boolean sameDefinition(AntTypeDefinition def, AntTypeDefinition old) {
      boolean defValid = this.validDefinition(def);
      boolean sameValidity = defValid == this.validDefinition(old);
      return sameValidity && (!defValid || def.sameDefinition(old, this.project));
   }

   private void updateDataTypeDefinition(AntTypeDefinition def) {
      String name = def.getName();
      synchronized(this.antTypeTable) {
         this.rebuildTaskClassDefinitions = true;
         this.rebuildTypeClassDefinitions = true;
         AntTypeDefinition old = this.antTypeTable.getDefinition(name);
         if (old != null) {
            if (this.sameDefinition(def, old)) {
               return;
            }

            Class oldClass = this.antTypeTable.getExposedClass(name);
            boolean isTask = oldClass != null && (class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task).isAssignableFrom(oldClass);
            this.project.log("Trying to override old definition of " + (isTask ? "task " : "datatype ") + name, def.similarDefinition(old, this.project) ? 3 : 1);
         }

         this.project.log(" +Datatype " + name + " " + def.getClassName(), 4);
         this.antTypeTable.put(name, def);
      }
   }

   public void enterAntLib(String uri) {
      this.antLibCurrentUri = uri;
      this.antLibStack.push(uri);
   }

   public String getCurrentAntlibUri() {
      return this.antLibCurrentUri;
   }

   public void exitAntLib() {
      this.antLibStack.pop();
      this.antLibCurrentUri = this.antLibStack.size() == 0 ? null : (String)this.antLibStack.peek();
   }

   private void initTasks() {
      ClassLoader classLoader = this.getClassLoader((ClassLoader)null);
      Properties props = getDefaultDefinitions(false);
      Enumeration e = props.propertyNames();

      while(e.hasMoreElements()) {
         String name = (String)e.nextElement();
         String className = props.getProperty(name);
         AntTypeDefinition def = new AntTypeDefinition();
         def.setName(name);
         def.setClassName(className);
         def.setClassLoader(classLoader);
         def.setAdaptToClass(class$org$apache$tools$ant$Task == null ? (class$org$apache$tools$ant$Task = class$("org.apache.tools.ant.Task")) : class$org$apache$tools$ant$Task);
         def.setAdapterClass(class$org$apache$tools$ant$TaskAdapter == null ? (class$org$apache$tools$ant$TaskAdapter = class$("org.apache.tools.ant.TaskAdapter")) : class$org$apache$tools$ant$TaskAdapter);
         this.antTypeTable.put(name, def);
      }

   }

   private ClassLoader getClassLoader(ClassLoader classLoader) {
      String buildSysclasspath = this.project.getProperty("build.sysclasspath");
      if (this.project.getCoreLoader() != null && !"only".equals(buildSysclasspath)) {
         classLoader = this.project.getCoreLoader();
      }

      return classLoader;
   }

   private static synchronized Properties getDefaultDefinitions(boolean type) throws BuildException {
      int idx = type ? 1 : 0;
      if (defaultDefinitions[idx] == null) {
         String resource = type ? "/org/apache/tools/ant/types/defaults.properties" : "/org/apache/tools/ant/taskdefs/defaults.properties";
         String errorString = type ? "Can't load default type list" : "Can't load default task list";
         InputStream in = null;

         try {
            in = (class$org$apache$tools$ant$ComponentHelper == null ? (class$org$apache$tools$ant$ComponentHelper = class$("org.apache.tools.ant.ComponentHelper")) : class$org$apache$tools$ant$ComponentHelper).getResourceAsStream(resource);
            if (in == null) {
               throw new BuildException(errorString);
            }

            Properties p = new Properties();
            p.load(in);
            defaultDefinitions[idx] = p;
         } catch (IOException var9) {
            throw new BuildException(errorString, var9);
         } finally {
            FileUtils.close(in);
         }
      }

      return defaultDefinitions[idx];
   }

   private void initTypes() {
      ClassLoader classLoader = this.getClassLoader((ClassLoader)null);
      Properties props = getDefaultDefinitions(true);
      Enumeration e = props.propertyNames();

      while(e.hasMoreElements()) {
         String name = (String)e.nextElement();
         String className = props.getProperty(name);
         AntTypeDefinition def = new AntTypeDefinition();
         def.setName(name);
         def.setClassName(className);
         def.setClassLoader(classLoader);
         this.antTypeTable.put(name, def);
      }

   }

   private synchronized void checkNamespace(String componentName) {
      String uri = ProjectHelper.extractUriFromComponentName(componentName);
      if ("".equals(uri)) {
         uri = "antlib:org.apache.tools.ant";
      }

      if (uri.startsWith("antlib:")) {
         if (!this.checkedNamespaces.contains(uri)) {
            this.checkedNamespaces.add(uri);
            Typedef definer = new Typedef();
            definer.setProject(this.project);
            definer.init();
            definer.setURI(uri);
            definer.setTaskName(uri);
            definer.setResource(Definer.makeResourceFromURI(uri));
            definer.setOnError(new Definer.OnError("ignore"));
            definer.execute();
         }
      }
   }

   public String diagnoseCreationFailure(String componentName, String type) {
      StringWriter errorText = new StringWriter();
      PrintWriter out = new PrintWriter(errorText);
      out.println("Problem: failed to create " + type + " " + componentName);
      boolean lowlevel = false;
      boolean jars = false;
      boolean definitions = false;
      String home = System.getProperty("user.home");
      File libDir = new File(home, Launcher.USER_LIBDIR);
      boolean probablyIDE = false;
      String anthome = System.getProperty("ant.home");
      String antHomeLib;
      if (anthome != null) {
         File antHomeLibDir = new File(anthome, "lib");
         antHomeLib = antHomeLibDir.getAbsolutePath();
      } else {
         probablyIDE = true;
         antHomeLib = "ANT_HOME" + File.separatorChar + "lib";
      }

      StringBuffer dirListingText = new StringBuffer();
      String tab = "        -";
      dirListingText.append("        -");
      dirListingText.append(antHomeLib);
      dirListingText.append('\n');
      if (probablyIDE) {
         dirListingText.append("        -");
         dirListingText.append("the IDE Ant configuration dialogs");
      } else {
         dirListingText.append("        -");
         dirListingText.append(libDir);
         dirListingText.append('\n');
         dirListingText.append("        -");
         dirListingText.append("a directory added on the command line with the -lib argument");
      }

      String dirListing = dirListingText.toString();
      AntTypeDefinition def = this.getDefinition(componentName);
      if (def == null) {
         boolean isAntlib = componentName.indexOf("antlib:") == 0;
         out.println("Cause: The name is undefined.");
         out.println("Action: Check the spelling.");
         out.println("Action: Check that any custom tasks/types have been declared.");
         out.println("Action: Check that any <presetdef>/<macrodef> declarations have taken place.");
         if (isAntlib) {
            out.println();
            out.println("This appears to be an antlib declaration. ");
            out.println("Action: Check that the implementing library exists in one of:");
            out.println(dirListing);
         }

         definitions = true;
      } else {
         String classname = def.getClassName();
         boolean antTask = classname.startsWith("org.apache.tools.ant.");
         boolean optional = classname.startsWith("org.apache.tools.ant.taskdefs.optional");
         optional |= classname.startsWith("org.apache.tools.ant.types.optional");
         Class clazz = null;

         try {
            clazz = def.innerGetTypeClass();
         } catch (ClassNotFoundException var28) {
            out.println("Cause: the class " + classname + " was not found.");
            jars = true;
            if (optional) {
               out.println("        This looks like one of Ant's optional components.");
               out.println("Action: Check that the appropriate optional JAR exists in");
               out.println(dirListing);
            } else {
               out.println("Action: Check that the component has been correctly declared");
               out.println("        and that the implementing JAR is in one of:");
               out.println(dirListing);
               definitions = true;
            }
         } catch (NoClassDefFoundError var29) {
            jars = true;
            out.println("Cause: Could not load a dependent class " + var29.getMessage());
            if (optional) {
               out.println("       It is not enough to have Ant's optional JARs");
               out.println("       you need the JAR files that the optional tasks depend upon.");
               out.println("       Ant's optional task dependencies are listed in the manual.");
            } else {
               out.println("       This class may be in a separate JAR that is not installed.");
            }

            out.println("Action: Determine what extra JAR files are needed, and place them in one of:");
            out.println(dirListing);
         }

         if (clazz != null) {
            try {
               def.innerCreateAndSet(clazz, this.project);
               out.println("The component could be instantiated.");
            } catch (NoSuchMethodException var23) {
               lowlevel = true;
               out.println("Cause: The class " + classname + " has no compatible constructor.");
            } catch (InstantiationException var24) {
               lowlevel = true;
               out.println("Cause: The class " + classname + " is abstract and cannot be instantiated.");
            } catch (IllegalAccessException var25) {
               lowlevel = true;
               out.println("Cause: The constructor for " + classname + " is private and cannot be invoked.");
            } catch (InvocationTargetException var26) {
               lowlevel = true;
               Throwable t = var26.getTargetException();
               out.println("Cause: The constructor threw the exception");
               out.println(t.toString());
               t.printStackTrace(out);
            } catch (NoClassDefFoundError var27) {
               jars = true;
               out.println("Cause:  A class needed by class " + classname + " cannot be found: ");
               out.println("       " + var27.getMessage());
               out.println("Action: Determine what extra JAR files are needed, and place them in:");
               out.println(dirListing);
            }
         }

         out.println();
         out.println("Do not panic, this is a common problem.");
         if (definitions) {
            out.println("It may just be a typographical error in the build file or the task/type declaration.");
         }

         if (jars) {
            out.println("The commonest cause is a missing JAR.");
         }

         if (lowlevel) {
            out.println("This is quite a low level problem, which may need consultation with the author of the task.");
            if (antTask) {
               out.println("This may be the Ant team. Please file a defect or contact the developer team.");
            } else {
               out.println("This does not appear to be a task bundled with Ant.");
               out.println("Please take it up with the supplier of the third-party " + type + ".");
               out.println("If you have written it yourself, you probably have a bug to fix.");
            }
         } else {
            out.println();
            out.println("This is not a bug; it is a configuration problem");
         }
      }

      out.flush();
      out.close();
      return errorText.toString();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private static class AntTypeTable extends Hashtable {
      private Project project;

      AntTypeTable(Project project) {
         this.project = project;
      }

      AntTypeDefinition getDefinition(String key) {
         return (AntTypeDefinition)((AntTypeDefinition)super.get(key));
      }

      public Object get(Object key) {
         return this.getTypeClass((String)key);
      }

      Object create(String name) {
         AntTypeDefinition def = this.getDefinition(name);
         return def == null ? null : def.create(this.project);
      }

      Class getTypeClass(String name) {
         AntTypeDefinition def = this.getDefinition(name);
         return def == null ? null : def.getTypeClass(this.project);
      }

      Class getExposedClass(String name) {
         AntTypeDefinition def = this.getDefinition(name);
         return def == null ? null : def.getExposedClass(this.project);
      }

      public boolean contains(Object clazz) {
         boolean found = false;
         if (clazz instanceof Class) {
            for(Iterator i = this.values().iterator(); i.hasNext() && !found; found |= ((AntTypeDefinition)((AntTypeDefinition)i.next())).getExposedClass(this.project) == clazz) {
            }
         }

         return found;
      }

      public boolean containsValue(Object value) {
         return this.contains(value);
      }
   }
}
