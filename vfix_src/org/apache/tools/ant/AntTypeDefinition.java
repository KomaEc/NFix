package org.apache.tools.ant;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AntTypeDefinition {
   private String name;
   private Class clazz;
   private Class adapterClass;
   private Class adaptToClass;
   private String className;
   private ClassLoader classLoader;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$Project;

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setClass(Class clazz) {
      this.clazz = clazz;
      if (clazz != null) {
         this.classLoader = this.classLoader == null ? clazz.getClassLoader() : this.classLoader;
         this.className = this.className == null ? clazz.getName() : this.className;
      }
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getClassName() {
      return this.className;
   }

   public void setAdapterClass(Class adapterClass) {
      this.adapterClass = adapterClass;
   }

   public void setAdaptToClass(Class adaptToClass) {
      this.adaptToClass = adaptToClass;
   }

   public void setClassLoader(ClassLoader classLoader) {
      this.classLoader = classLoader;
   }

   public ClassLoader getClassLoader() {
      return this.classLoader;
   }

   public Class getExposedClass(Project project) {
      if (this.adaptToClass != null) {
         Class z = this.getTypeClass(project);
         if (z == null || this.adaptToClass.isAssignableFrom(z)) {
            return z;
         }
      }

      return this.adapterClass == null ? this.getTypeClass(project) : this.adapterClass;
   }

   public Class getTypeClass(Project project) {
      try {
         return this.innerGetTypeClass();
      } catch (NoClassDefFoundError var3) {
         project.log("Could not load a dependent class (" + var3.getMessage() + ") for type " + this.name, 4);
      } catch (ClassNotFoundException var4) {
         project.log("Could not load class (" + this.className + ") for type " + this.name, 4);
      }

      return null;
   }

   public Class innerGetTypeClass() throws ClassNotFoundException {
      if (this.clazz != null) {
         return this.clazz;
      } else {
         if (this.classLoader == null) {
            this.clazz = Class.forName(this.className);
         } else {
            this.clazz = this.classLoader.loadClass(this.className);
         }

         return this.clazz;
      }
   }

   public Object create(Project project) {
      return this.icreate(project);
   }

   private Object icreate(Project project) {
      Class c = this.getTypeClass(project);
      if (c == null) {
         return null;
      } else {
         Object o = this.createAndSet(project, c);
         if (o != null && this.adapterClass != null) {
            if (this.adaptToClass != null && this.adaptToClass.isAssignableFrom(o.getClass())) {
               return o;
            } else {
               TypeAdapter adapterObject = (TypeAdapter)this.createAndSet(project, this.adapterClass);
               if (adapterObject == null) {
                  return null;
               } else {
                  adapterObject.setProxy(o);
                  return adapterObject;
               }
            }
         } else {
            return o;
         }
      }
   }

   public void checkClass(Project project) {
      if (this.clazz == null) {
         this.clazz = this.getTypeClass(project);
         if (this.clazz == null) {
            throw new BuildException("Unable to create class for " + this.getName());
         }
      }

      if (this.adapterClass != null && (this.adaptToClass == null || !this.adaptToClass.isAssignableFrom(this.clazz))) {
         TypeAdapter adapter = (TypeAdapter)this.createAndSet(project, this.adapterClass);
         if (adapter == null) {
            throw new BuildException("Unable to create adapter object");
         }

         adapter.checkProxyClass(this.clazz);
      }

   }

   private Object createAndSet(Project project, Class c) {
      try {
         Object o = this.innerCreateAndSet(c, project);
         return o;
      } catch (InvocationTargetException var5) {
         Throwable t = var5.getTargetException();
         throw new BuildException("Could not create type " + this.name + " due to " + t, t);
      } catch (NoClassDefFoundError var6) {
         String msg = "Type " + this.name + ": A class needed by class " + c + " cannot be found: " + var6.getMessage();
         throw new BuildException(msg, var6);
      } catch (NoSuchMethodException var7) {
         throw new BuildException("Could not create type " + this.name + " as the class " + c + " has no compatible constructor");
      } catch (InstantiationException var8) {
         throw new BuildException("Could not create type " + this.name + " as the class " + c + " is abstract");
      } catch (IllegalAccessException var9) {
         throw new BuildException("Could not create type " + this.name + " as the constructor " + c + " is not accessible");
      } catch (Throwable var10) {
         throw new BuildException("Could not create type " + this.name + " due to " + var10, var10);
      }
   }

   public Object innerCreateAndSet(Class newclass, Project project) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Constructor ctor = null;
      boolean noArg = false;

      try {
         ctor = newclass.getConstructor();
         noArg = true;
      } catch (NoSuchMethodException var6) {
         ctor = newclass.getConstructor(class$org$apache$tools$ant$Project == null ? (class$org$apache$tools$ant$Project = class$("org.apache.tools.ant.Project")) : class$org$apache$tools$ant$Project);
         noArg = false;
      }

      Object o = ctor.newInstance(noArg ? new Object[0] : new Object[]{project});
      project.setProjectReference(o);
      return o;
   }

   public boolean sameDefinition(AntTypeDefinition other, Project project) {
      return other != null && other.getClass() == this.getClass() && other.getTypeClass(project).equals(this.getTypeClass(project)) && other.getExposedClass(project).equals(this.getExposedClass(project)) && other.adapterClass == this.adapterClass && other.adaptToClass == this.adaptToClass;
   }

   public boolean similarDefinition(AntTypeDefinition other, Project project) {
      if (other != null && this.getClass() == other.getClass() && this.getClassName().equals(other.getClassName()) && this.extractClassname(this.adapterClass).equals(this.extractClassname(other.adapterClass)) && this.extractClassname(this.adaptToClass).equals(this.extractClassname(other.adaptToClass))) {
         ClassLoader oldLoader = other.getClassLoader();
         ClassLoader newLoader = this.getClassLoader();
         return oldLoader == newLoader || oldLoader instanceof AntClassLoader && newLoader instanceof AntClassLoader && ((AntClassLoader)oldLoader).getClasspath().equals(((AntClassLoader)newLoader).getClasspath());
      } else {
         return false;
      }
   }

   private String extractClassname(Class c) {
      return c == null ? "<null>" : c.getClass().getName();
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
