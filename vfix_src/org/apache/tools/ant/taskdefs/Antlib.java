package org.apache.tools.ant.taskdefs;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.helper.ProjectHelper2;

public class Antlib extends Task implements TaskContainer {
   public static final String TAG = "antlib";
   private ClassLoader classLoader;
   private String uri = "";
   private List tasks = new ArrayList();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$Antlib;

   public static Antlib createAntlib(Project project, URL antlibUrl, String uri) {
      try {
         antlibUrl.openConnection().connect();
      } catch (IOException var11) {
         throw new BuildException("Unable to find " + antlibUrl, var11);
      }

      ComponentHelper helper = ComponentHelper.getComponentHelper(project);
      helper.enterAntLib(uri);

      Antlib var7;
      try {
         ProjectHelper2 parser = new ProjectHelper2();
         UnknownElement ue = parser.parseUnknownElement(project, antlibUrl);
         if (!ue.getTag().equals("antlib")) {
            throw new BuildException("Unexpected tag " + ue.getTag() + " expecting " + "antlib", ue.getLocation());
         }

         Antlib antlib = new Antlib();
         antlib.setProject(project);
         antlib.setLocation(ue.getLocation());
         antlib.setTaskName("antlib");
         antlib.init();
         ue.configure(antlib);
         var7 = antlib;
      } finally {
         helper.exitAntLib();
      }

      return var7;
   }

   protected void setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   protected void setURI(String uri) {
      this.uri = uri;
   }

   private ClassLoader getClassLoader() {
      if (this.classLoader == null) {
         this.classLoader = (class$org$apache$tools$ant$taskdefs$Antlib == null ? (class$org$apache$tools$ant$taskdefs$Antlib = class$("org.apache.tools.ant.taskdefs.Antlib")) : class$org$apache$tools$ant$taskdefs$Antlib).getClassLoader();
      }

      return this.classLoader;
   }

   public void addTask(Task nestedTask) {
      this.tasks.add(nestedTask);
   }

   public void execute() {
      Iterator i = this.tasks.iterator();

      while(i.hasNext()) {
         UnknownElement ue = (UnknownElement)i.next();
         this.setLocation(ue.getLocation());
         ue.maybeConfigure();
         Object configuredObject = ue.getRealThing();
         if (configuredObject != null) {
            if (!(configuredObject instanceof AntlibDefinition)) {
               throw new BuildException("Invalid task in antlib " + ue.getTag() + " " + configuredObject.getClass() + " does not " + "extend org.apache.tools.ant.taskdefs.AntlibDefinition");
            }

            AntlibDefinition def = (AntlibDefinition)configuredObject;
            def.setURI(this.uri);
            def.setAntlibClassLoader(this.getClassLoader());
            def.init();
            def.execute();
         }
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
