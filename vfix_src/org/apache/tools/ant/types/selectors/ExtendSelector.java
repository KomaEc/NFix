package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class ExtendSelector extends BaseSelector {
   private String classname = null;
   private FileSelector dynselector = null;
   private Vector paramVec = new Vector();
   private Path classpath = null;

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public void selectorCreate() {
      if (this.classname != null && this.classname.length() > 0) {
         try {
            Class c = null;
            if (this.classpath == null) {
               c = Class.forName(this.classname);
            } else {
               AntClassLoader al = this.getProject().createClassLoader(this.classpath);
               c = Class.forName(this.classname, true, al);
            }

            this.dynselector = (FileSelector)c.newInstance();
            Project p = this.getProject();
            if (p != null) {
               p.setProjectReference(this.dynselector);
            }
         } catch (ClassNotFoundException var3) {
            this.setError("Selector " + this.classname + " not initialized, no such class");
         } catch (InstantiationException var4) {
            this.setError("Selector " + this.classname + " not initialized, could not create class");
         } catch (IllegalAccessException var5) {
            this.setError("Selector " + this.classname + " not initialized, class not accessible");
         }
      } else {
         this.setError("There is no classname specified");
      }

   }

   public void addParam(Parameter p) {
      this.paramVec.addElement(p);
   }

   public final void setClasspath(Path classpath) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (this.classpath == null) {
            this.classpath = classpath;
         } else {
            this.classpath.append(classpath);
         }

      }
   }

   public final Path createClasspath() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
         }

         return this.classpath.createPath();
      }
   }

   public final Path getClasspath() {
      return this.classpath;
   }

   public void setClasspathref(Reference r) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createClasspath().setRefid(r);
      }
   }

   public void verifySettings() {
      if (this.dynselector == null) {
         this.selectorCreate();
      }

      if (this.classname != null && this.classname.length() >= 1) {
         if (this.dynselector == null) {
            this.setError("Internal Error: The custom selector was not created");
         } else if (!(this.dynselector instanceof ExtendFileSelector) && this.paramVec.size() > 0) {
            this.setError("Cannot set parameters on custom selector that does not implement ExtendFileSelector");
         }
      } else {
         this.setError("The classname attribute is required");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) throws BuildException {
      this.validate();
      if (this.paramVec.size() > 0 && this.dynselector instanceof ExtendFileSelector) {
         Parameter[] paramArray = new Parameter[this.paramVec.size()];
         this.paramVec.copyInto(paramArray);
         ((ExtendFileSelector)this.dynselector).setParameters(paramArray);
      }

      return this.dynselector.isSelected(basedir, filename, file);
   }
}
