package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.PropertySet;
import org.apache.tools.ant.util.FileUtils;

public class Ant extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File dir = null;
   private String antFile = null;
   private String output = null;
   private boolean inheritAll = true;
   private boolean inheritRefs = false;
   private Vector properties = new Vector();
   private Vector references = new Vector();
   private Project newProject;
   private PrintStream out = null;
   private Vector propertySets = new Vector();
   private Vector targets = new Vector();
   private boolean targetAttributeSet = false;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;

   public Ant() {
   }

   public Ant(Task owner) {
      this.bindToOwner(owner);
   }

   public void setInheritAll(boolean value) {
      this.inheritAll = value;
   }

   public void setInheritRefs(boolean value) {
      this.inheritRefs = value;
   }

   public void init() {
      this.newProject = this.getProject().createSubProject();
      this.newProject.setJavaVersionProperty();
   }

   private void reinit() {
      this.init();
   }

   private void initializeProject() {
      this.newProject.setInputHandler(this.getProject().getInputHandler());
      Iterator iter = this.getBuildListeners();

      while(iter.hasNext()) {
         this.newProject.addBuildListener((BuildListener)iter.next());
      }

      if (this.output != null) {
         File outfile = null;
         if (this.dir != null) {
            outfile = FILE_UTILS.resolveFile(this.dir, this.output);
         } else {
            outfile = this.getProject().resolveFile(this.output);
         }

         try {
            this.out = new PrintStream(new FileOutputStream(outfile));
            DefaultLogger logger = new DefaultLogger();
            logger.setMessageOutputLevel(2);
            logger.setOutputPrintStream(this.out);
            logger.setErrorPrintStream(this.out);
            this.newProject.addBuildListener(logger);
         } catch (IOException var4) {
            this.log("Ant: Can't set output to " + this.output);
         }
      }

      this.getProject().copyUserProperties(this.newProject);
      if (!this.inheritAll) {
         this.newProject.setSystemProperties();
      } else {
         this.addAlmostAll(this.getProject().getProperties());
      }

      Enumeration e = this.propertySets.elements();

      while(e.hasMoreElements()) {
         PropertySet ps = (PropertySet)e.nextElement();
         this.addAlmostAll(ps.getProperties());
      }

   }

   public void handleOutput(String outputToHandle) {
      if (this.newProject != null) {
         this.newProject.demuxOutput(outputToHandle, false);
      } else {
         super.handleOutput(outputToHandle);
      }

   }

   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.newProject != null ? this.newProject.demuxInput(buffer, offset, length) : super.handleInput(buffer, offset, length);
   }

   public void handleFlush(String toFlush) {
      if (this.newProject != null) {
         this.newProject.demuxFlush(toFlush, false);
      } else {
         super.handleFlush(toFlush);
      }

   }

   public void handleErrorOutput(String errorOutputToHandle) {
      if (this.newProject != null) {
         this.newProject.demuxOutput(errorOutputToHandle, true);
      } else {
         super.handleErrorOutput(errorOutputToHandle);
      }

   }

   public void handleErrorFlush(String errorOutputToFlush) {
      if (this.newProject != null) {
         this.newProject.demuxFlush(errorOutputToFlush, true);
      } else {
         super.handleErrorFlush(errorOutputToFlush);
      }

   }

   public void execute() throws BuildException {
      File savedDir = this.dir;
      String savedAntFile = this.antFile;
      Vector locals = new Vector(this.targets);
      boolean var20 = false;

      try {
         var20 = true;
         this.getNewProject();
         if (this.dir == null && this.inheritAll) {
            this.dir = this.getProject().getBaseDir();
         }

         this.initializeProject();
         if (this.dir != null) {
            this.newProject.setBaseDir(this.dir);
            if (savedDir != null) {
               this.newProject.setInheritedProperty("basedir", this.dir.getAbsolutePath());
            }
         } else {
            this.dir = this.getProject().getBaseDir();
         }

         this.overrideProperties();
         if (this.antFile == null) {
            this.antFile = "build.xml";
         }

         File file = FILE_UTILS.resolveFile(this.dir, this.antFile);
         this.antFile = file.getAbsolutePath();
         this.log("calling target(s) " + (locals.size() > 0 ? locals.toString() : "[default]") + " in build file " + this.antFile, 3);
         this.newProject.setUserProperty("ant.file", this.antFile);
         String thisAntFile = this.getProject().getProperty("ant.file");
         if (thisAntFile != null && file.equals(this.getProject().resolveFile(thisAntFile)) && this.getOwningTarget() != null && this.getOwningTarget().getName().equals("")) {
            if (this.getTaskName().equals("antcall")) {
               throw new BuildException("antcall must not be used at the top level.");
            }

            throw new BuildException(this.getTaskName() + " task at the" + " top level must not invoke" + " its own build file.");
         }

         try {
            ProjectHelper.configureProject(this.newProject, file);
         } catch (BuildException var31) {
            throw ProjectHelper.addLocationToBuildException(var31, this.getLocation());
         }

         String owningTargetName;
         if (locals.size() == 0) {
            owningTargetName = this.newProject.getDefaultTarget();
            if (owningTargetName != null) {
               locals.add(owningTargetName);
            }
         }

         if (this.newProject.getProperty("ant.file").equals(this.getProject().getProperty("ant.file")) && this.getOwningTarget() != null) {
            owningTargetName = this.getOwningTarget().getName();
            if (locals.contains(owningTargetName)) {
               throw new BuildException(this.getTaskName() + " task calling " + "its own parent target.");
            }

            boolean circular = false;

            Target other;
            for(Iterator it = locals.iterator(); !circular && it.hasNext(); circular |= other != null && other.dependsOn(owningTargetName)) {
               other = (Target)((Target)this.getProject().getTargets().get(it.next()));
            }

            if (circular) {
               throw new BuildException(this.getTaskName() + " task calling a target" + " that depends on" + " its parent target '" + owningTargetName + "'.");
            }
         }

         this.addReferences();
         if (locals.size() > 0) {
            if (locals.size() == 1 && "".equals(locals.get(0))) {
               var20 = false;
            } else {
               BuildException be = null;

               try {
                  this.log("Entering " + this.antFile + "...", 3);
                  this.newProject.fireSubBuildStarted();
                  this.newProject.executeTargets(locals);
               } catch (BuildException var29) {
                  be = ProjectHelper.addLocationToBuildException(var29, this.getLocation());
                  throw be;
               } finally {
                  this.log("Exiting " + this.antFile + ".", 3);
                  this.newProject.fireSubBuildFinished(be);
               }

               var20 = false;
            }
         } else {
            var20 = false;
         }
      } finally {
         if (var20) {
            this.newProject = null;
            Enumeration e = this.properties.elements();

            while(e.hasMoreElements()) {
               Property p = (Property)e.nextElement();
               p.setProject((Project)null);
            }

            if (this.output != null && this.out != null) {
               try {
                  this.out.close();
               } catch (Exception var27) {
               }
            }

            this.dir = savedDir;
            this.antFile = savedAntFile;
         }
      }

      this.newProject = null;
      Enumeration e = this.properties.elements();

      while(e.hasMoreElements()) {
         Property p = (Property)e.nextElement();
         p.setProject((Project)null);
      }

      if (this.output != null && this.out != null) {
         try {
            this.out.close();
         } catch (Exception var28) {
         }
      }

      this.dir = savedDir;
      this.antFile = savedAntFile;
   }

   private void overrideProperties() throws BuildException {
      Set set = new HashSet();

      Property p;
      for(int i = this.properties.size() - 1; i >= 0; --i) {
         p = (Property)this.properties.get(i);
         if (p.getName() != null && !p.getName().equals("")) {
            if (set.contains(p.getName())) {
               this.properties.remove(i);
            } else {
               set.add(p.getName());
            }
         }
      }

      Enumeration e = this.properties.elements();

      while(e.hasMoreElements()) {
         p = (Property)e.nextElement();
         p.setProject(this.newProject);
         p.execute();
      }

      this.getProject().copyInheritedProperties(this.newProject);
   }

   private void addReferences() throws BuildException {
      Hashtable thisReferences = (Hashtable)this.getProject().getReferences().clone();
      Hashtable newReferences = this.newProject.getReferences();
      Enumeration e;
      if (this.references.size() > 0) {
         e = this.references.elements();

         while(e.hasMoreElements()) {
            Ant.Reference ref = (Ant.Reference)e.nextElement();
            String refid = ref.getRefId();
            if (refid == null) {
               throw new BuildException("the refid attribute is required for reference elements");
            }

            if (!thisReferences.containsKey(refid)) {
               this.log("Parent project doesn't contain any reference '" + refid + "'", 1);
            } else {
               thisReferences.remove(refid);
               String toRefid = ref.getToRefid();
               if (toRefid == null) {
                  toRefid = refid;
               }

               this.copyReference(refid, toRefid);
            }
         }
      }

      if (this.inheritRefs) {
         e = thisReferences.keys();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            if (!newReferences.containsKey(key)) {
               this.copyReference(key, key);
               this.newProject.inheritIDReferences(this.getProject());
            }
         }
      }

   }

   private void copyReference(String oldKey, String newKey) {
      Object orig = this.getProject().getReference(oldKey);
      if (orig == null) {
         this.log("No object referenced by " + oldKey + ". Can't copy to " + newKey, 1);
      } else {
         Class c = orig.getClass();
         Object copy = orig;

         Method setProjectM;
         try {
            setProjectM = c.getMethod("clone");
            if (setProjectM != null) {
               copy = setProjectM.invoke(orig);
               this.log("Adding clone of reference " + oldKey, 4);
            }
         } catch (Exception var10) {
         }

         if (copy instanceof ProjectComponent) {
            ((ProjectComponent)copy).setProject(this.newProject);
         } else {
            try {
               setProjectM = c.getMethod("setProject", class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
               if (setProjectM != null) {
                  setProjectM.invoke(copy, this.newProject);
               }
            } catch (NoSuchMethodException var8) {
            } catch (Exception var9) {
               String msg = "Error setting new project instance for reference with id " + oldKey;
               throw new BuildException(msg, var9, this.getLocation());
            }
         }

         this.newProject.addReference(newKey, copy);
      }
   }

   private void addAlmostAll(Hashtable props) {
      Enumeration e = props.keys();

      while(e.hasMoreElements()) {
         String key = e.nextElement().toString();
         if (!"basedir".equals(key) && !"ant.file".equals(key)) {
            String value = props.get(key).toString();
            if (this.newProject.getProperty(key) == null) {
               this.newProject.setNewProperty(key, value);
            }
         }
      }

   }

   public void setDir(File dir) {
      this.dir = dir;
   }

   public void setAntfile(String antFile) {
      this.antFile = antFile;
   }

   public void setTarget(String targetToAdd) {
      if (targetToAdd.equals("")) {
         throw new BuildException("target attribute must not be empty");
      } else {
         this.targets.add(targetToAdd);
         this.targetAttributeSet = true;
      }
   }

   public void setOutput(String outputFile) {
      this.output = outputFile;
   }

   public Property createProperty() {
      Property p = new Property(true, this.getProject());
      p.setProject(this.getNewProject());
      p.setTaskName("property");
      this.properties.addElement(p);
      return p;
   }

   public void addReference(Ant.Reference ref) {
      this.references.addElement(ref);
   }

   public void addConfiguredTarget(Ant.TargetElement t) {
      if (this.targetAttributeSet) {
         throw new BuildException("nested target is incompatible with the target attribute");
      } else {
         String name = t.getName();
         if (name.equals("")) {
            throw new BuildException("target name must not be empty");
         } else {
            this.targets.add(name);
         }
      }
   }

   public void addPropertyset(PropertySet ps) {
      this.propertySets.addElement(ps);
   }

   protected Project getNewProject() {
      if (this.newProject == null) {
         this.reinit();
      }

      return this.newProject;
   }

   private Iterator getBuildListeners() {
      return this.getProject().getBuildListeners().iterator();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class TargetElement {
      private String name;

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }

   public static class Reference extends org.apache.tools.ant.types.Reference {
      private String targetid = null;

      public void setToRefid(String targetid) {
         this.targetid = targetid;
      }

      public String getToRefid() {
         return this.targetid;
      }
   }
}
