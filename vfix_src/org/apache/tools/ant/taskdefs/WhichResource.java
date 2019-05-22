package org.apache.tools.ant.taskdefs;

import java.net.URL;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;

public class WhichResource extends Task {
   private Path classpath;
   private String classname;
   private String resource;
   private String property;

   public void setClasspath(Path cp) {
      if (this.classpath == null) {
         this.classpath = cp;
      } else {
         this.classpath.append(cp);
      }

   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   private void validate() {
      int setcount = 0;
      if (this.classname != null) {
         ++setcount;
      }

      if (this.resource != null) {
         ++setcount;
      }

      if (setcount == 0) {
         throw new BuildException("One of classname or resource must be specified");
      } else if (setcount > 1) {
         throw new BuildException("Only one of classname or resource can be specified");
      } else if (this.property == null) {
         throw new BuildException("No property defined");
      }
   }

   public void execute() throws BuildException {
      this.validate();
      if (this.classpath != null) {
         this.getProject().log("using user supplied classpath: " + this.classpath, 4);
         this.classpath = this.classpath.concatSystemClasspath("ignore");
      } else {
         this.classpath = new Path(this.getProject());
         this.classpath = this.classpath.concatSystemClasspath("only");
         this.getProject().log("using system classpath: " + this.classpath, 4);
      }

      AntClassLoader loader = new AntClassLoader(this.getProject().getCoreLoader(), this.getProject(), this.classpath, false);
      String loc = null;
      if (this.classname != null) {
         this.resource = this.classname.replace('.', '/') + ".class";
      }

      if (this.resource == null) {
         throw new BuildException("One of class or resource is required");
      } else {
         if (this.resource.startsWith("/")) {
            this.resource = this.resource.substring(1);
         }

         this.log("Searching for " + this.resource, 3);
         URL url = loader.getResource(this.resource);
         if (url != null) {
            loc = url.toExternalForm();
            this.getProject().setNewProperty(this.property, loc);
         }

      }
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public void setClass(String classname) {
      this.classname = classname;
   }

   public void setProperty(String property) {
      this.property = property;
   }
}
