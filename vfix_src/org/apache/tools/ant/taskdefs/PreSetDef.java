package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;

public class PreSetDef extends AntlibDefinition implements TaskContainer {
   private UnknownElement nestedTask;
   private String name;

   public void setName(String name) {
      this.name = name;
   }

   public void addTask(Task nestedTask) {
      if (this.nestedTask != null) {
         throw new BuildException("Only one nested element allowed");
      } else if (!(nestedTask instanceof UnknownElement)) {
         throw new BuildException("addTask called with a task that is not an unknown element");
      } else {
         this.nestedTask = (UnknownElement)nestedTask;
      }
   }

   public void execute() {
      if (this.nestedTask == null) {
         throw new BuildException("Missing nested element");
      } else if (this.name == null) {
         throw new BuildException("Name not specified");
      } else {
         this.name = ProjectHelper.genComponentName(this.getURI(), this.name);
         ComponentHelper helper = ComponentHelper.getComponentHelper(this.getProject());
         String componentName = ProjectHelper.genComponentName(this.nestedTask.getNamespace(), this.nestedTask.getTag());
         AntTypeDefinition def = helper.getDefinition(componentName);
         if (def == null) {
            throw new BuildException("Unable to find typedef " + componentName);
         } else {
            PreSetDef.PreSetDefinition newDef = new PreSetDef.PreSetDefinition(def, this.nestedTask);
            newDef.setName(this.name);
            helper.addDataTypeDefinition(newDef);
            this.log("defining preset " + this.name, 3);
         }
      }
   }

   public static class PreSetDefinition extends AntTypeDefinition {
      private AntTypeDefinition parent;
      private UnknownElement element;

      public PreSetDefinition(AntTypeDefinition parent, UnknownElement el) {
         if (parent instanceof PreSetDef.PreSetDefinition) {
            PreSetDef.PreSetDefinition p = (PreSetDef.PreSetDefinition)parent;
            el.applyPreSet(p.element);
            parent = p.parent;
         }

         this.parent = parent;
         this.element = el;
      }

      public void setClass(Class clazz) {
         throw new BuildException("Not supported");
      }

      public void setClassName(String className) {
         throw new BuildException("Not supported");
      }

      public String getClassName() {
         return this.parent.getClassName();
      }

      public void setAdapterClass(Class adapterClass) {
         throw new BuildException("Not supported");
      }

      public void setAdaptToClass(Class adaptToClass) {
         throw new BuildException("Not supported");
      }

      public void setClassLoader(ClassLoader classLoader) {
         throw new BuildException("Not supported");
      }

      public ClassLoader getClassLoader() {
         return this.parent.getClassLoader();
      }

      public Class getExposedClass(Project project) {
         return this.parent.getExposedClass(project);
      }

      public Class getTypeClass(Project project) {
         return this.parent.getTypeClass(project);
      }

      public void checkClass(Project project) {
         this.parent.checkClass(project);
      }

      public Object createObject(Project project) {
         return this.parent.create(project);
      }

      public UnknownElement getPreSets() {
         return this.element;
      }

      public Object create(Project project) {
         return this;
      }

      public boolean sameDefinition(AntTypeDefinition other, Project project) {
         return other != null && other.getClass() == this.getClass() && this.parent != null && this.parent.sameDefinition(((PreSetDef.PreSetDefinition)other).parent, project) && this.element.similar(((PreSetDef.PreSetDefinition)other).element);
      }

      public boolean similarDefinition(AntTypeDefinition other, Project project) {
         return other != null && other.getClass().getName().equals(this.getClass().getName()) && this.parent != null && this.parent.similarDefinition(((PreSetDef.PreSetDefinition)other).parent, project) && this.element.similar(((PreSetDef.PreSetDefinition)other).element);
      }
   }
}
