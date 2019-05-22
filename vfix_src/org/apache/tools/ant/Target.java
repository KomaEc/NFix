package org.apache.tools.ant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.tools.ant.util.CollectionUtils;

public class Target implements TaskContainer {
   private String name;
   private String ifCondition = "";
   private String unlessCondition = "";
   private List dependencies = null;
   private List children = new ArrayList();
   private Location location;
   private Project project;
   private String description;

   public Target() {
      this.location = Location.UNKNOWN_LOCATION;
      this.description = null;
   }

   public Target(Target other) {
      this.location = Location.UNKNOWN_LOCATION;
      this.description = null;
      this.name = other.name;
      this.ifCondition = other.ifCondition;
      this.unlessCondition = other.unlessCondition;
      this.dependencies = other.dependencies;
      this.location = other.location;
      this.project = other.project;
      this.description = other.description;
      this.children = other.children;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public Project getProject() {
      return this.project;
   }

   public void setLocation(Location location) {
      this.location = location;
   }

   public Location getLocation() {
      return this.location;
   }

   public void setDepends(String depS) {
      if (depS.length() > 0) {
         StringTokenizer tok = new StringTokenizer(depS, ",", true);

         while(tok.hasMoreTokens()) {
            String token = tok.nextToken().trim();
            if ("".equals(token) || ",".equals(token)) {
               throw new BuildException("Syntax Error: depends attribute of target \"" + this.getName() + "\" has an empty string as dependency.");
            }

            this.addDependency(token);
            if (tok.hasMoreTokens()) {
               token = tok.nextToken();
               if (!tok.hasMoreTokens() || !",".equals(token)) {
                  throw new BuildException("Syntax Error: Depend attribute for target \"" + this.getName() + "\" ends with a , character");
               }
            }
         }
      }

   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void addTask(Task task) {
      this.children.add(task);
   }

   public void addDataType(RuntimeConfigurable r) {
      this.children.add(r);
   }

   public Task[] getTasks() {
      List tasks = new ArrayList(this.children.size());
      Iterator it = this.children.iterator();

      while(it.hasNext()) {
         Object o = it.next();
         if (o instanceof Task) {
            tasks.add(o);
         }
      }

      return (Task[])((Task[])tasks.toArray(new Task[tasks.size()]));
   }

   public void addDependency(String dependency) {
      if (this.dependencies == null) {
         this.dependencies = new ArrayList(2);
      }

      this.dependencies.add(dependency);
   }

   public Enumeration getDependencies() {
      return (Enumeration)(this.dependencies != null ? Collections.enumeration(this.dependencies) : new CollectionUtils.EmptyEnumeration());
   }

   public boolean dependsOn(String other) {
      Project p = this.getProject();
      Hashtable t = p == null ? null : p.getTargets();
      return p != null && p.topoSort(this.getName(), t, false).contains(t.get(other));
   }

   public void setIf(String property) {
      this.ifCondition = property == null ? "" : property;
   }

   public String getIf() {
      return "".equals(this.ifCondition) ? null : this.ifCondition;
   }

   public void setUnless(String property) {
      this.unlessCondition = property == null ? "" : property;
   }

   public String getUnless() {
      return "".equals(this.unlessCondition) ? null : this.unlessCondition;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }

   public String toString() {
      return this.name;
   }

   public void execute() throws BuildException {
      if (this.testIfCondition() && this.testUnlessCondition()) {
         for(int taskPosition = 0; taskPosition < this.children.size(); ++taskPosition) {
            Object o = this.children.get(taskPosition);
            if (o instanceof Task) {
               Task task = (Task)o;
               task.perform();
            } else {
               RuntimeConfigurable r = (RuntimeConfigurable)o;
               r.maybeConfigure(this.project);
            }
         }
      } else if (!this.testIfCondition()) {
         this.project.log((Target)this, (String)("Skipped because property '" + this.project.replaceProperties(this.ifCondition) + "' not set."), 3);
      } else {
         this.project.log((Target)this, (String)("Skipped because property '" + this.project.replaceProperties(this.unlessCondition) + "' set."), 3);
      }

   }

   public final void performTasks() {
      RuntimeException thrown = null;
      this.project.fireTargetStarted(this);

      try {
         this.execute();
      } catch (RuntimeException var6) {
         thrown = var6;
         throw var6;
      } finally {
         this.project.fireTargetFinished(this, thrown);
      }

   }

   void replaceChild(Task el, RuntimeConfigurable o) {
      int index;
      while((index = this.children.indexOf(el)) >= 0) {
         this.children.set(index, o);
      }

   }

   void replaceChild(Task el, Task o) {
      int index;
      while((index = this.children.indexOf(el)) >= 0) {
         this.children.set(index, o);
      }

   }

   private boolean testIfCondition() {
      if ("".equals(this.ifCondition)) {
         return true;
      } else {
         String test = this.project.replaceProperties(this.ifCondition);
         return this.project.getProperty(test) != null;
      }
   }

   private boolean testUnlessCondition() {
      if ("".equals(this.unlessCondition)) {
         return true;
      } else {
         String test = this.project.replaceProperties(this.unlessCondition);
         return this.project.getProperty(test) == null;
      }
   }
}
