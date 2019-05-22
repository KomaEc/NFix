package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class Classloader extends Task {
   public static final String SYSTEM_LOADER_REF = "ant.coreLoader";
   private String name = null;
   private Path classpath;
   private boolean reset = false;
   private boolean parentFirst = true;
   private String parentName = null;

   public void setName(String name) {
      this.name = name;
   }

   public void setReset(boolean b) {
      this.reset = b;
   }

   public void setReverse(boolean b) {
      this.parentFirst = !b;
   }

   public void setParentFirst(boolean b) {
      this.parentFirst = b;
   }

   public void setParentName(String name) {
      this.parentName = name;
   }

   public void setClasspathRef(Reference pathRef) throws BuildException {
      this.classpath = (Path)pathRef.getReferencedObject(this.getProject());
   }

   public void setClasspath(Path classpath) {
      if (this.classpath == null) {
         this.classpath = classpath;
      } else {
         this.classpath.append(classpath);
      }

   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path((Project)null);
      }

      return this.classpath.createPath();
   }

   public void execute() {
      try {
         if ("only".equals(this.getProject().getProperty("build.sysclasspath")) && (this.name == null || "ant.coreLoader".equals(this.name))) {
            this.log("Changing the system loader is disabled by build.sysclasspath=only", 1);
            return;
         }

         String loaderName = this.name == null ? "ant.coreLoader" : this.name;
         Object obj = this.getProject().getReference(loaderName);
         if (this.reset) {
            obj = null;
         }

         if (obj != null && !(obj instanceof AntClassLoader)) {
            this.log("Referenced object is not an AntClassLoader", 0);
            return;
         }

         AntClassLoader acl = (AntClassLoader)obj;
         if (acl == null) {
            Object parent = null;
            if (this.parentName != null) {
               parent = this.getProject().getReference(this.parentName);
               if (!(parent instanceof ClassLoader)) {
                  parent = null;
               }
            }

            if (parent == null) {
               parent = this.getClass().getClassLoader();
            }

            if (this.name == null) {
            }

            this.getProject().log("Setting parent loader " + this.name + " " + parent + " " + this.parentFirst, 4);
            acl = new AntClassLoader((ClassLoader)parent, this.getProject(), this.classpath, this.parentFirst);
            this.getProject().addReference(loaderName, acl);
            if (this.name == null) {
               acl.addLoaderPackageRoot("org.apache.tools.ant.taskdefs.optional");
               this.getProject().setCoreLoader(acl);
            }
         }

         if (this.classpath != null) {
            String[] list = this.classpath.list();

            for(int i = 0; i < list.length; ++i) {
               File f = new File(list[i]);
               if (f.exists()) {
                  acl.addPathElement(f.getAbsolutePath());
                  this.log("Adding to class loader " + acl + " " + f.getAbsolutePath(), 4);
               }
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
