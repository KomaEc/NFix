package org.apache.tools.ant.util;

import java.io.File;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class ScriptRunnerHelper {
   private ClasspathUtils.Delegate cpDelegate = null;
   private File srcFile;
   private String manager = "auto";
   private String language;
   private String text;
   private boolean setBeans = true;
   private ProjectComponent projectComponent;
   private ClassLoader scriptLoader = null;

   public void setProjectComponent(ProjectComponent component) {
      this.projectComponent = component;
   }

   public ScriptRunnerBase getScriptRunner() {
      ScriptRunnerBase runner = this.getRunner();
      if (this.srcFile != null) {
         runner.setSrc(this.srcFile);
      }

      if (this.text != null) {
         runner.addText(this.text);
      }

      if (this.setBeans) {
         runner.bindToComponent(this.projectComponent);
      } else {
         runner.bindToComponentMinimum(this.projectComponent);
      }

      return runner;
   }

   public Path createClasspath() {
      return this.getClassPathDelegate().createClasspath();
   }

   public void setClasspath(Path classpath) {
      this.getClassPathDelegate().setClasspath(classpath);
   }

   public void setClasspathRef(Reference r) {
      this.getClassPathDelegate().setClasspathref(r);
   }

   public void setSrc(File file) {
      this.srcFile = file;
   }

   public void addText(String text) {
      this.text = text;
   }

   public void setManager(String manager) {
      this.manager = manager;
   }

   public void setLanguage(String language) {
      this.language = language;
   }

   public String getLanguage() {
      return this.language;
   }

   public void setSetBeans(boolean setBeans) {
      this.setBeans = setBeans;
   }

   public void setClassLoader(ClassLoader loader) {
      this.scriptLoader = loader;
   }

   private ClassLoader generateClassLoader() {
      if (this.scriptLoader != null) {
         return this.scriptLoader;
      } else if (this.cpDelegate == null) {
         this.scriptLoader = this.getClass().getClassLoader();
         return this.scriptLoader;
      } else {
         this.scriptLoader = this.cpDelegate.getClassLoader();
         return this.scriptLoader;
      }
   }

   private ClasspathUtils.Delegate getClassPathDelegate() {
      if (this.cpDelegate == null) {
         this.cpDelegate = ClasspathUtils.getDelegate(this.projectComponent);
      }

      return this.cpDelegate;
   }

   private ScriptRunnerBase getRunner() {
      return (new ScriptRunnerCreator(this.projectComponent.getProject())).createRunner(this.manager, this.language, this.generateClassLoader());
   }
}
