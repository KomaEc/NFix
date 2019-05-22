package org.codehaus.groovy.reflection;

import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.GroovySunClassLoader;

public class ClassLoaderForClassArtifacts extends ClassLoader {
   public final SoftReference<Class> klazz;
   private final Set<String> allocatedNames = new HashSet();

   public ClassLoaderForClassArtifacts(Class klazz) {
      super(klazz.getClassLoader());
      this.klazz = new SoftReference(klazz);
   }

   public Class define(String name, byte[] bytes) {
      Class cls = this.defineClass(name, bytes, 0, bytes.length, ((Class)this.klazz.get()).getProtectionDomain());
      this.resolveClass(cls);
      return cls;
   }

   public Class loadClass(String name) throws ClassNotFoundException {
      Class cls = this.findLoadedClass(name);
      if (cls != null) {
         return cls;
      } else {
         if (GroovySunClassLoader.sunVM != null) {
            cls = GroovySunClassLoader.sunVM.doesKnow(name);
            if (cls != null) {
               return cls;
            }
         }

         return super.loadClass(name);
      }
   }

   public synchronized String createClassName(Method method) {
      String clsName = ((Class)this.klazz.get()).getName();
      String name;
      if (clsName.startsWith("java.")) {
         name = clsName.replace('.', '_') + "$" + method.getName();
      } else {
         name = clsName + "$" + method.getName();
      }

      if (!this.allocatedNames.contains(name)) {
         this.allocatedNames.add(name);
         return name;
      } else {
         int i = 0;

         while(true) {
            String newName = name + "$" + i;
            if (!this.allocatedNames.contains(newName)) {
               this.allocatedNames.add(newName);
               return newName;
            }

            ++i;
         }
      }
   }

   public Constructor defineClassAndGetConstructor(final String name, final byte[] bytes) {
      Class cls = (Class)AccessController.doPrivileged(new PrivilegedAction<Class>() {
         public Class run() {
            return ClassLoaderForClassArtifacts.this.define(name, bytes);
         }
      });
      if (cls != null) {
         try {
            return cls.getConstructor(CallSite.class, MetaClassImpl.class, MetaMethod.class, Class[].class);
         } catch (NoSuchMethodException var5) {
         }
      }

      return null;
   }
}
