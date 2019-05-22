package org.apache.tools.ant.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;

public abstract class ScriptRunnerBase {
   private boolean keepEngine = false;
   private String language;
   private String script = "";
   private Project project;
   private ClassLoader scriptLoader;
   private Map beans = new HashMap();

   public void addBeans(Map dictionary) {
      Iterator i = dictionary.keySet().iterator();

      while(i.hasNext()) {
         String key = (String)i.next();

         try {
            Object val = dictionary.get(key);
            this.addBean(key, val);
         } catch (BuildException var5) {
         }
      }

   }

   public void addBean(String key, Object bean) {
      boolean isValid = key.length() > 0 && Character.isJavaIdentifierStart(key.charAt(0));

      for(int i = 1; isValid && i < key.length(); ++i) {
         isValid = Character.isJavaIdentifierPart(key.charAt(i));
      }

      if (isValid) {
         this.beans.put(key, bean);
      }

   }

   protected Map getBeans() {
      return this.beans;
   }

   public abstract void executeScript(String var1);

   public abstract Object evaluateScript(String var1);

   public abstract boolean supportsLanguage();

   public abstract String getManagerName();

   public void setLanguage(String language) {
      this.language = language;
   }

   public String getLanguage() {
      return this.language;
   }

   public void setScriptClassLoader(ClassLoader classLoader) {
      this.scriptLoader = classLoader;
   }

   protected ClassLoader getScriptClassLoader() {
      return this.scriptLoader;
   }

   public void setKeepEngine(boolean keepEngine) {
      this.keepEngine = keepEngine;
   }

   public boolean getKeepEngine() {
      return this.keepEngine;
   }

   public void setSrc(File file) {
      if (!file.exists()) {
         throw new BuildException("file " + file.getPath() + " not found.");
      } else {
         BufferedReader in = null;

         try {
            in = new BufferedReader(new FileReader(file));
            this.script = this.script + FileUtils.readFully(in);
         } catch (IOException var7) {
            throw new BuildException(var7);
         } finally {
            FileUtils.close((Reader)in);
         }

      }
   }

   public void addText(String text) {
      this.script = this.script + text;
   }

   public String getScript() {
      return this.script;
   }

   public void clearScript() {
      this.script = "";
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public Project getProject() {
      return this.project;
   }

   public void bindToComponent(ProjectComponent component) {
      this.project = component.getProject();
      this.addBeans(this.project.getProperties());
      this.addBeans(this.project.getUserProperties());
      this.addBeans(this.project.getTargets());
      this.addBeans(this.project.getReferences());
      this.addBean("project", this.project);
      this.addBean("self", component);
   }

   public void bindToComponentMinimum(ProjectComponent component) {
      this.project = component.getProject();
      this.addBean("project", this.project);
      this.addBean("self", component);
   }

   protected void checkLanguage() {
      if (this.language == null) {
         throw new BuildException("script language must be specified");
      }
   }

   protected ClassLoader replaceContextLoader() {
      ClassLoader origContextClassLoader = Thread.currentThread().getContextClassLoader();
      if (this.getScriptClassLoader() == null) {
         this.setScriptClassLoader(this.getClass().getClassLoader());
      }

      Thread.currentThread().setContextClassLoader(this.getScriptClassLoader());
      return origContextClassLoader;
   }

   protected void restoreContextLoader(ClassLoader origLoader) {
      Thread.currentThread().setContextClassLoader(origLoader);
   }
}
