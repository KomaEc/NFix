package org.codehaus.groovy.runtime.metaclass;

import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.runtime.Reflector;

public class ReflectorLoader extends ClassLoader {
   private boolean inDefine = false;
   private final Map loadedClasses = new HashMap();
   private final ClassLoader delegatationLoader = this.getClass().getClassLoader();
   private static final String REFLECTOR = Reflector.class.getName();

   protected Class findClass(String name) throws ClassNotFoundException {
      return this.delegatationLoader == null ? super.findClass(name) : this.delegatationLoader.loadClass(name);
   }

   protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
      return this.inDefine && name.equals(REFLECTOR) ? Reflector.class : super.loadClass(name, resolve);
   }

   public synchronized Class defineClass(String name, byte[] bytecode, ProtectionDomain domain) {
      this.inDefine = true;
      Class c = this.defineClass(name, bytecode, 0, bytecode.length, domain);
      this.loadedClasses.put(name, c);
      this.resolveClass(c);
      this.inDefine = false;
      return c;
   }

   public ReflectorLoader(ClassLoader parent) {
      super(parent);
   }

   public synchronized Class getLoadedClass(String name) {
      return (Class)this.loadedClasses.get(name);
   }

   static String getReflectorName(Class theClass) {
      String className = theClass.getName();
      String name;
      if (className.startsWith("java.")) {
         name = "gjdk.";
         String name = name + className + "_GroovyReflector";
         if (theClass.isArray()) {
            Class clazz = theClass;

            int level;
            for(level = 0; clazz.isArray(); ++level) {
               clazz = clazz.getComponentType();
            }

            String componentName = clazz.getName();
            name = name + componentName + "_GroovyReflectorArray";
            if (level > 1) {
               name = name + level;
            }
         }

         return name;
      } else {
         name = className.replace('$', '_') + "_GroovyReflector";
         if (theClass.isArray()) {
            Class clazz = theClass;

            int level;
            for(level = 0; clazz.isArray(); ++level) {
               clazz = clazz.getComponentType();
            }

            String componentName = clazz.getName();
            name = componentName.replace('$', '_') + "_GroovyReflectorArray";
            if (level > 1) {
               name = name + level;
            }
         }

         return name;
      }
   }
}
