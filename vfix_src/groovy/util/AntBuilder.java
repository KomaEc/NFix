package groovy.util;

import groovy.xml.QName;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DemuxInputStream;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.NoBannerLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.dispatch.DispatchUtils;
import org.apache.tools.ant.helper.AntXMLContext;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.codehaus.groovy.ant.FileScanner;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

public class AntBuilder extends BuilderSupport {
   private final Logger log;
   private Project project;
   private final AntXMLContext antXmlContext;
   private final ProjectHelper2.ElementHandler antElementHandler;
   private final ProjectHelper2.TargetHandler antTargetHandler;
   private final Target collectorTarget;
   private final Target implicitTarget;
   private Object lastCompletedNode;
   boolean insideTask;

   public AntBuilder() {
      this(createProject());
   }

   public AntBuilder(Project project) {
      this(project, new Target());
   }

   public AntBuilder(Project project, Target owningTarget) {
      this.log = Logger.getLogger(this.getClass().getName());
      this.antElementHandler = new ProjectHelper2.ElementHandler();
      this.antTargetHandler = new ProjectHelper2.TargetHandler();
      this.project = project;
      this.collectorTarget = owningTarget;
      this.antXmlContext = new AntXMLContext(project);
      this.collectorTarget.setProject(project);
      this.antXmlContext.setCurrentTarget(this.collectorTarget);
      this.antXmlContext.setLocator(new AntBuilderLocator());
      this.antXmlContext.setCurrentTargets(new HashMap());
      this.implicitTarget = new Target();
      this.implicitTarget.setProject(project);
      this.implicitTarget.setName("");
      this.antXmlContext.setImplicitTarget(this.implicitTarget);
      project.addDataTypeDefinition("fileScanner", FileScanner.class);
   }

   public AntBuilder(Task parentTask) {
      this(parentTask.getProject(), parentTask.getOwningTarget());
      UnknownElement ue = new UnknownElement(parentTask.getTaskName());
      ue.setProject(parentTask.getProject());
      ue.setTaskType(parentTask.getTaskType());
      ue.setTaskName(parentTask.getTaskName());
      ue.setLocation(parentTask.getLocation());
      ue.setOwningTarget(parentTask.getOwningTarget());
      ue.setRuntimeConfigurableWrapper(parentTask.getRuntimeConfigurableWrapper());
      parentTask.getRuntimeConfigurableWrapper().setProxy(ue);
      this.antXmlContext.pushWrapper(parentTask.getRuntimeConfigurableWrapper());
   }

   public Project getProject() {
      return this.project;
   }

   public AntXMLContext getAntXmlContext() {
      return this.antXmlContext;
   }

   protected static Project createProject() {
      Project project = new Project();
      ProjectHelper helper = ProjectHelper.getProjectHelper();
      project.addReference("ant.projectHelper", helper);
      helper.getImportStack().addElement("AntBuilder");
      BuildLogger logger = new NoBannerLogger();
      logger.setMessageOutputLevel(2);
      logger.setOutputPrintStream(System.out);
      logger.setErrorPrintStream(System.err);
      project.addBuildListener(logger);
      project.init();
      project.getBaseDir();
      return project;
   }

   protected void setParent(Object parent, Object child) {
   }

   protected Object doInvokeMethod(String methodName, Object name, Object args) {
      super.doInvokeMethod(methodName, name, args);
      return this.lastCompletedNode;
   }

   protected void nodeCompleted(Object parent, Object node) {
      if (parent == null) {
         this.insideTask = false;
      }

      this.antElementHandler.onEndElement((String)null, (String)null, this.antXmlContext);
      this.lastCompletedNode = node;
      if (parent != null && !(parent instanceof Target)) {
         this.log.finest("parent is not null: no perform on nodeCompleted");
      } else {
         if (node instanceof Task) {
            Task task = (Task)node;
            String taskName = task.getTaskName();
            if ("antcall".equals(taskName) && parent == null) {
               throw new BuildException("antcall not supported within AntBuilder, consider using 'ant.project.executeTarget('targetName')' instead.");
            }

            InputStream savedIn = System.in;
            InputStream savedProjectInputStream = this.project.getDefaultInputStream();
            if (!(savedIn instanceof DemuxInputStream)) {
               this.project.setDefaultInputStream(savedIn);
               System.setIn(new DemuxInputStream(this.project));
            }

            try {
               this.lastCompletedNode = this.performTask(task);
            } finally {
               this.project.setDefaultInputStream(savedProjectInputStream);
               System.setIn(savedIn);
            }

            if ("import".equals(taskName)) {
               this.antXmlContext.setCurrentTarget(this.collectorTarget);
            }
         } else if (node instanceof Target) {
            this.antXmlContext.setCurrentTarget(this.collectorTarget);
         } else {
            RuntimeConfigurable r = (RuntimeConfigurable)node;
            r.maybeConfigure(this.project);
         }

      }
   }

   private Object performTask(Task task) {
      Object reason = null;
      boolean var17 = false;

      Object var5;
      try {
         var17 = true;
         Method fireTaskStarted = Project.class.getDeclaredMethod("fireTaskStarted", Task.class);
         fireTaskStarted.setAccessible(true);
         fireTaskStarted.invoke(this.project, task);
         Object realThing = task;
         task.maybeConfigure();
         if (task instanceof UnknownElement) {
            realThing = ((UnknownElement)task).getRealThing();
         }

         DispatchUtils.execute(task);
         var5 = realThing != null ? realThing : task;
         var17 = false;
      } catch (BuildException var20) {
         if (var20.getLocation() == Location.UNKNOWN_LOCATION) {
            var20.setLocation(task.getLocation());
         }

         reason = var20;
         throw var20;
      } catch (Exception var21) {
         reason = var21;
         BuildException be = new BuildException(var21);
         be.setLocation(task.getLocation());
         throw be;
      } catch (Error var22) {
         reason = var22;
         throw var22;
      } finally {
         if (var17) {
            try {
               Method fireTaskFinished = Project.class.getDeclaredMethod("fireTaskFinished", Task.class, Throwable.class);
               fireTaskFinished.setAccessible(true);
               fireTaskFinished.invoke(this.project, task, reason);
            } catch (Exception var18) {
               BuildException be = new BuildException(var18);
               be.setLocation(task.getLocation());
               throw be;
            }
         }
      }

      try {
         Method fireTaskFinished = Project.class.getDeclaredMethod("fireTaskFinished", Task.class, Throwable.class);
         fireTaskFinished.setAccessible(true);
         fireTaskFinished.invoke(this.project, task, reason);
         return var5;
      } catch (Exception var19) {
         BuildException be = new BuildException(var19);
         be.setLocation(task.getLocation());
         throw be;
      }
   }

   protected Object createNode(Object tagName) {
      return this.createNode(tagName, Collections.EMPTY_MAP);
   }

   protected Object createNode(Object name, Object value) {
      Object task = this.createNode(name);
      this.setText(task, value.toString());
      return task;
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      Object task = this.createNode(name, attributes);
      this.setText(task, value.toString());
      return task;
   }

   protected static Attributes buildAttributes(Map attributes) {
      AttributesImpl attr = new AttributesImpl();
      Iterator iter = attributes.entrySet().iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         String attributeName = (String)entry.getKey();
         String attributeValue = String.valueOf(entry.getValue());
         attr.addAttribute((String)null, attributeName, attributeName, "CDATA", attributeValue);
      }

      return attr;
   }

   protected Object createNode(Object name, Map attributes) {
      Attributes attrs = buildAttributes(attributes);
      String tagName = name.toString();
      String ns = "";
      if (name instanceof QName) {
         QName q = (QName)name;
         tagName = q.getLocalPart();
         ns = q.getNamespaceURI();
      }

      if ("import".equals(name)) {
         this.antXmlContext.setCurrentTarget(this.implicitTarget);
      } else if ("target".equals(name) && !this.insideTask) {
         return this.onStartTarget(attrs, tagName, ns);
      }

      try {
         this.antElementHandler.onStartElement(ns, tagName, tagName, attrs, this.antXmlContext);
      } catch (SAXParseException var7) {
         this.log.log(Level.SEVERE, "Caught: " + var7, var7);
      }

      this.insideTask = true;
      RuntimeConfigurable wrapper = (RuntimeConfigurable)this.antXmlContext.getWrapperStack().lastElement();
      return wrapper.getProxy();
   }

   private Target onStartTarget(Attributes attrs, String tagName, String ns) {
      Target target = new Target();
      target.setProject(this.project);
      target.setLocation(new Location(this.antXmlContext.getLocator()));

      try {
         this.antTargetHandler.onStartElement(ns, tagName, tagName, attrs, this.antXmlContext);
         Target newTarget = (Target)this.getProject().getTargets().get(attrs.getValue("name"));
         Vector targets = new Vector();
         Enumeration deps = newTarget.getDependencies();

         while(deps.hasMoreElements()) {
            String targetName = (String)deps.nextElement();
            targets.add(this.project.getTargets().get(targetName));
         }

         this.getProject().executeSortedTargets(targets);
         this.antXmlContext.setCurrentTarget(newTarget);
         return newTarget;
      } catch (SAXParseException var9) {
         this.log.log(Level.SEVERE, "Caught: " + var9, var9);
         return null;
      }
   }

   protected void setText(Object task, String text) {
      char[] characters = text.toCharArray();

      try {
         this.antElementHandler.characters(characters, 0, characters.length, this.antXmlContext);
      } catch (SAXParseException var5) {
         this.log.log(Level.WARNING, "SetText failed: " + task + ". Reason: " + var5, var5);
      }

   }

   public Project getAntProject() {
      return this.project;
   }
}
