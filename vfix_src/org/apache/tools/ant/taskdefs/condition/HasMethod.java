package org.apache.tools.ant.taskdefs.condition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class HasMethod extends ProjectComponent implements Condition {
   private String classname;
   private String method;
   private String field;
   private Path classpath;
   private AntClassLoader loader;
   private boolean ignoreSystemClasses = false;

   public void setClasspath(Path classpath) {
      this.createClasspath().append(classpath);
   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public void setField(String field) {
      this.field = field;
   }

   public void setIgnoreSystemClasses(boolean ignoreSystemClasses) {
      this.ignoreSystemClasses = ignoreSystemClasses;
   }

   private Class loadClass(String classname) {
      try {
         if (this.ignoreSystemClasses) {
            this.loader = this.getProject().createClassLoader(this.classpath);
            this.loader.setParentFirst(false);
            this.loader.addJavaLibraries();
            if (this.loader != null) {
               try {
                  return this.loader.findClass(classname);
               } catch (SecurityException var3) {
                  return null;
               }
            } else {
               return null;
            }
         } else if (this.loader != null) {
            return this.loader.loadClass(classname);
         } else {
            ClassLoader l = this.getClass().getClassLoader();
            return l != null ? Class.forName(classname, true, l) : Class.forName(classname);
         }
      } catch (ClassNotFoundException var4) {
         throw new BuildException("class \"" + classname + "\" was not found");
      } catch (NoClassDefFoundError var5) {
         throw new BuildException("Could not load dependent class \"" + var5.getMessage() + "\" for class \"" + classname + "\"");
      }
   }

   public boolean eval() throws BuildException {
      if (this.classname == null) {
         throw new BuildException("No classname defined");
      } else {
         Class clazz = this.loadClass(this.classname);
         if (this.method != null) {
            return this.isMethodFound(clazz);
         } else if (this.field != null) {
            return this.isFieldFound(clazz);
         } else {
            throw new BuildException("Neither method nor field defined");
         }
      }
   }

   private boolean isFieldFound(Class clazz) {
      Field[] fields = clazz.getDeclaredFields();

      for(int i = 0; i < fields.length; ++i) {
         Field fieldEntry = fields[i];
         if (fieldEntry.getName().equals(this.field)) {
            return true;
         }
      }

      return false;
   }

   private boolean isMethodFound(Class clazz) {
      Method[] methods = clazz.getDeclaredMethods();

      for(int i = 0; i < methods.length; ++i) {
         Method methodEntry = methods[i];
         if (methodEntry.getName().equals(this.method)) {
            return true;
         }
      }

      return false;
   }
}
