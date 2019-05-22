package bsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Hashtable;

public class BshClassManager {
   private static Object NOVALUE = new Object();
   private Interpreter declaringInterpreter;
   protected ClassLoader externalClassLoader;
   protected transient Hashtable absoluteClassCache = new Hashtable();
   protected transient Hashtable absoluteNonClasses = new Hashtable();
   protected transient Hashtable resolvedObjectMethods = new Hashtable();
   protected transient Hashtable resolvedStaticMethods = new Hashtable();
   protected transient Hashtable definingClasses = new Hashtable();
   protected transient Hashtable definingClassesBaseNames = new Hashtable();
   // $FF: synthetic field
   static Class class$bsh$Interpreter;

   public static BshClassManager createClassManager(Interpreter var0) {
      BshClassManager var2;
      if (Capabilities.classExists("java.lang.ref.WeakReference") && Capabilities.classExists("java.util.HashMap") && Capabilities.classExists("bsh.classpath.ClassManagerImpl")) {
         try {
            Class var1 = Class.forName("bsh.classpath.ClassManagerImpl");
            var2 = (BshClassManager)var1.newInstance();
         } catch (Exception var3) {
            throw new InterpreterError("Error loading classmanager: " + var3);
         }
      } else {
         var2 = new BshClassManager();
      }

      if (var0 == null) {
         var0 = new Interpreter();
      }

      var2.declaringInterpreter = var0;
      return var2;
   }

   public boolean classExists(String var1) {
      return this.classForName(var1) != null;
   }

   public Class classForName(String var1) {
      if (this.isClassBeingDefined(var1)) {
         throw new InterpreterError("Attempting to load class in the process of being defined: " + var1);
      } else {
         Class var2 = null;

         try {
            var2 = this.plainClassForName(var1);
         } catch (ClassNotFoundException var4) {
         }

         if (var2 == null) {
            var2 = this.loadSourceClass(var1);
         }

         return var2;
      }
   }

   protected Class loadSourceClass(String var1) {
      String var2 = "/" + var1.replace('.', '/') + ".java";
      InputStream var3 = this.getResourceAsStream(var2);
      if (var3 == null) {
         return null;
      } else {
         try {
            System.out.println("Loading class from source file: " + var2);
            this.declaringInterpreter.eval((Reader)(new InputStreamReader(var3)));
         } catch (EvalError var6) {
            System.err.println(var6);
         }

         try {
            return this.plainClassForName(var1);
         } catch (ClassNotFoundException var5) {
            System.err.println("Class not found in source file: " + var1);
            return null;
         }
      }
   }

   public Class plainClassForName(String var1) throws ClassNotFoundException {
      Class var2 = null;

      try {
         if (this.externalClassLoader != null) {
            var2 = this.externalClassLoader.loadClass(var1);
         } else {
            var2 = Class.forName(var1);
         }

         this.cacheClassInfo(var1, var2);
         return var2;
      } catch (NoClassDefFoundError var4) {
         throw noClassDefFound(var1, var4);
      }
   }

   public URL getResource(String var1) {
      URL var2 = null;
      if (this.externalClassLoader != null) {
         var2 = this.externalClassLoader.getResource(var1.substring(1));
      }

      if (var2 == null) {
         var2 = (class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter).getResource(var1);
      }

      return var2;
   }

   public InputStream getResourceAsStream(String var1) {
      InputStream var2 = null;
      if (this.externalClassLoader != null) {
         var2 = this.externalClassLoader.getResourceAsStream(var1.substring(1));
      }

      if (var2 == null) {
         var2 = (class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter).getResourceAsStream(var1);
      }

      return var2;
   }

   public void cacheClassInfo(String var1, Class var2) {
      if (var2 != null) {
         this.absoluteClassCache.put(var1, var2);
      } else {
         this.absoluteNonClasses.put(var1, NOVALUE);
      }

   }

   public void cacheResolvedMethod(Class var1, Class[] var2, Method var3) {
      if (Interpreter.DEBUG) {
         Interpreter.debug("cacheResolvedMethod putting: " + var1 + " " + var3);
      }

      BshClassManager.SignatureKey var4 = new BshClassManager.SignatureKey(var1, var3.getName(), var2);
      if (Modifier.isStatic(var3.getModifiers())) {
         this.resolvedStaticMethods.put(var4, var3);
      } else {
         this.resolvedObjectMethods.put(var4, var3);
      }

   }

   protected Method getResolvedMethod(Class var1, String var2, Class[] var3, boolean var4) {
      BshClassManager.SignatureKey var5 = new BshClassManager.SignatureKey(var1, var2, var3);
      Method var6 = (Method)this.resolvedStaticMethods.get(var5);
      if (var6 == null && !var4) {
         var6 = (Method)this.resolvedObjectMethods.get(var5);
      }

      if (Interpreter.DEBUG) {
         if (var6 == null) {
            Interpreter.debug("getResolvedMethod cache MISS: " + var1 + " - " + var2);
         } else {
            Interpreter.debug("getResolvedMethod cache HIT: " + var1 + " - " + var6);
         }
      }

      return var6;
   }

   protected void clearCaches() {
      this.absoluteNonClasses = new Hashtable();
      this.absoluteClassCache = new Hashtable();
      this.resolvedObjectMethods = new Hashtable();
      this.resolvedStaticMethods = new Hashtable();
   }

   public void setClassLoader(ClassLoader var1) {
      this.externalClassLoader = var1;
      this.classLoaderChanged();
   }

   public void addClassPath(URL var1) throws IOException {
   }

   public void reset() {
      this.clearCaches();
   }

   public void setClassPath(URL[] var1) throws UtilEvalError {
      throw cmUnavailable();
   }

   public void reloadAllClasses() throws UtilEvalError {
      throw cmUnavailable();
   }

   public void reloadClasses(String[] var1) throws UtilEvalError {
      throw cmUnavailable();
   }

   public void reloadPackage(String var1) throws UtilEvalError {
      throw cmUnavailable();
   }

   protected void doSuperImport() throws UtilEvalError {
      throw cmUnavailable();
   }

   protected boolean hasSuperImport() {
      return false;
   }

   protected String getClassNameByUnqName(String var1) throws UtilEvalError {
      throw cmUnavailable();
   }

   public void addListener(BshClassManager.Listener var1) {
   }

   public void removeListener(BshClassManager.Listener var1) {
   }

   public void dump(PrintWriter var1) {
      var1.println("BshClassManager: no class manager.");
   }

   protected void definingClass(String var1) {
      String var2 = Name.suffix(var1, 1);
      int var3 = var2.indexOf("$");
      if (var3 != -1) {
         var2 = var2.substring(var3 + 1);
      }

      String var4 = (String)this.definingClassesBaseNames.get(var2);
      if (var4 != null) {
         throw new InterpreterError("Defining class problem: " + var1 + ": BeanShell cannot yet simultaneously define two or more " + "dependant classes of the same name.  Attempt to define: " + var1 + " while defining: " + var4);
      } else {
         this.definingClasses.put(var1, NOVALUE);
         this.definingClassesBaseNames.put(var2, var1);
      }
   }

   protected boolean isClassBeingDefined(String var1) {
      return this.definingClasses.get(var1) != null;
   }

   protected String getClassBeingDefined(String var1) {
      String var2 = Name.suffix(var1, 1);
      return (String)this.definingClassesBaseNames.get(var2);
   }

   protected void doneDefiningClass(String var1) {
      String var2 = Name.suffix(var1, 1);
      this.definingClasses.remove(var1);
      this.definingClassesBaseNames.remove(var2);
   }

   public Class defineClass(String var1, byte[] var2) {
      throw new InterpreterError("Can't create class (" + var1 + ") without class manager package.");
   }

   protected void classLoaderChanged() {
   }

   protected static Error noClassDefFound(String var0, Error var1) {
      return new NoClassDefFoundError("A class required by class: " + var0 + " could not be loaded:\n" + var1.toString());
   }

   protected static UtilEvalError cmUnavailable() {
      return new Capabilities.Unavailable("ClassLoading features unavailable.");
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static class SignatureKey {
      Class clas;
      Class[] types;
      String methodName;
      int hashCode = 0;

      SignatureKey(Class var1, String var2, Class[] var3) {
         this.clas = var1;
         this.methodName = var2;
         this.types = var3;
      }

      public int hashCode() {
         if (this.hashCode == 0) {
            this.hashCode = this.clas.hashCode() * this.methodName.hashCode();
            if (this.types == null) {
               return this.hashCode;
            }

            for(int var1 = 0; var1 < this.types.length; ++var1) {
               int var2 = this.types[var1] == null ? 21 : this.types[var1].hashCode();
               this.hashCode = this.hashCode * (var1 + 1) + var2;
            }
         }

         return this.hashCode;
      }

      public boolean equals(Object var1) {
         BshClassManager.SignatureKey var2 = (BshClassManager.SignatureKey)var1;
         if (this.types == null) {
            return var2.types == null;
         } else if (this.clas != var2.clas) {
            return false;
         } else if (!this.methodName.equals(var2.methodName)) {
            return false;
         } else if (this.types.length != var2.types.length) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.types.length; ++var3) {
               if (this.types[var3] == null) {
                  if (var2.types[var3] != null) {
                     return false;
                  }
               } else if (!this.types[var3].equals(var2.types[var3])) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public interface Listener {
      void classLoaderChanged();
   }
}
