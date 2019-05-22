package bsh.classpath;

import bsh.BshClassManager;
import bsh.ClassPathException;
import bsh.Interpreter;
import bsh.InterpreterError;
import bsh.UtilEvalError;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ClassManagerImpl extends BshClassManager {
   static final String BSH_PACKAGE = "bsh";
   private BshClassPath baseClassPath;
   private boolean superImport;
   private BshClassPath fullClassPath;
   private Vector listeners = new Vector();
   private ReferenceQueue refQueue = new ReferenceQueue();
   private BshClassLoader baseLoader;
   private Map loaderMap;
   // $FF: synthetic field
   static Class class$bsh$Interpreter;

   public ClassManagerImpl() {
      this.reset();
   }

   public Class classForName(String var1) {
      Class var2 = (Class)this.absoluteClassCache.get(var1);
      if (var2 != null) {
         return var2;
      } else if (this.absoluteNonClasses.get(var1) != null) {
         if (Interpreter.DEBUG) {
            Interpreter.debug("absoluteNonClass list hit: " + var1);
         }

         return null;
      } else {
         if (Interpreter.DEBUG) {
            Interpreter.debug("Trying to load class: " + var1);
         }

         ClassLoader var3 = this.getLoaderForClass(var1);
         if (var3 != null) {
            try {
               var2 = var3.loadClass(var1);
            } catch (Exception var12) {
            } catch (NoClassDefFoundError var13) {
               throw BshClassManager.noClassDefFound(var1, var13);
            }
         }

         if (var2 == null && var1.startsWith("bsh")) {
            try {
               var2 = (class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter).getClassLoader().loadClass(var1);
            } catch (ClassNotFoundException var11) {
            }
         }

         if (var2 == null && this.baseLoader != null) {
            try {
               var2 = this.baseLoader.loadClass(var1);
            } catch (ClassNotFoundException var10) {
            }
         }

         if (var2 == null && this.externalClassLoader != null) {
            try {
               var2 = this.externalClassLoader.loadClass(var1);
            } catch (ClassNotFoundException var9) {
            }
         }

         if (var2 == null) {
            try {
               ClassLoader var4 = Thread.currentThread().getContextClassLoader();
               if (var4 != null) {
                  var2 = Class.forName(var1, true, var4);
               }
            } catch (ClassNotFoundException var7) {
            } catch (SecurityException var8) {
            }
         }

         if (var2 == null) {
            try {
               var2 = this.plainClassForName(var1);
            } catch (ClassNotFoundException var6) {
            }
         }

         if (var2 == null) {
            var2 = this.loadSourceClass(var1);
         }

         this.cacheClassInfo(var1, var2);
         return var2;
      }
   }

   public URL getResource(String var1) {
      URL var2 = null;
      if (this.baseLoader != null) {
         var2 = this.baseLoader.getResource(var1.substring(1));
      }

      if (var2 == null) {
         var2 = super.getResource(var1);
      }

      return var2;
   }

   public InputStream getResourceAsStream(String var1) {
      InputStream var2 = null;
      if (this.baseLoader != null) {
         var2 = this.baseLoader.getResourceAsStream(var1.substring(1));
      }

      if (var2 == null) {
         var2 = super.getResourceAsStream(var1);
      }

      return var2;
   }

   ClassLoader getLoaderForClass(String var1) {
      return (ClassLoader)this.loaderMap.get(var1);
   }

   public void addClassPath(URL var1) throws IOException {
      if (this.baseLoader == null) {
         this.setClassPath(new URL[]{var1});
      } else {
         this.baseLoader.addURL(var1);
         this.baseClassPath.add(var1);
         this.classLoaderChanged();
      }

   }

   public void reset() {
      this.baseClassPath = new BshClassPath("baseClassPath");
      this.baseLoader = null;
      this.loaderMap = new HashMap();
      this.classLoaderChanged();
   }

   public void setClassPath(URL[] var1) {
      this.baseClassPath.setPath(var1);
      this.initBaseLoader();
      this.loaderMap = new HashMap();
      this.classLoaderChanged();
   }

   public void reloadAllClasses() throws ClassPathException {
      BshClassPath var1 = new BshClassPath("temp");
      var1.addComponent(this.baseClassPath);
      var1.addComponent(BshClassPath.getUserClassPath());
      this.setClassPath(var1.getPathComponents());
   }

   private void initBaseLoader() {
      this.baseLoader = new BshClassLoader(this, this.baseClassPath);
   }

   public void reloadClasses(String[] var1) throws ClassPathException {
      if (this.baseLoader == null) {
         this.initBaseLoader();
      }

      DiscreteFilesClassLoader.ClassSourceMap var2 = new DiscreteFilesClassLoader.ClassSourceMap();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3];
         BshClassPath.ClassSource var5 = this.baseClassPath.getClassSource(var4);
         if (var5 == null) {
            BshClassPath.getUserClassPath().insureInitialized();
            var5 = BshClassPath.getUserClassPath().getClassSource(var4);
         }

         if (var5 == null) {
            throw new ClassPathException("Nothing known about class: " + var4);
         }

         if (var5 instanceof BshClassPath.JarClassSource) {
            throw new ClassPathException("Cannot reload class: " + var4 + " from source: " + var5);
         }

         var2.put(var4, var5);
      }

      DiscreteFilesClassLoader var7 = new DiscreteFilesClassLoader(this, var2);
      Iterator var6 = var2.keySet().iterator();

      while(var6.hasNext()) {
         this.loaderMap.put((String)var6.next(), var7);
      }

      this.classLoaderChanged();
   }

   public void reloadPackage(String var1) throws ClassPathException {
      Set var2 = this.baseClassPath.getClassesForPackage(var1);
      if (var2 == null) {
         var2 = BshClassPath.getUserClassPath().getClassesForPackage(var1);
      }

      if (var2 == null) {
         throw new ClassPathException("No classes found for package: " + var1);
      } else {
         this.reloadClasses((String[])var2.toArray(new String[0]));
      }
   }

   public BshClassPath getClassPath() throws ClassPathException {
      if (this.fullClassPath != null) {
         return this.fullClassPath;
      } else {
         this.fullClassPath = new BshClassPath("BeanShell Full Class Path");
         this.fullClassPath.addComponent(BshClassPath.getUserClassPath());

         try {
            this.fullClassPath.addComponent(BshClassPath.getBootClassPath());
         } catch (ClassPathException var2) {
            System.err.println("Warning: can't get boot class path");
         }

         this.fullClassPath.addComponent(this.baseClassPath);
         return this.fullClassPath;
      }
   }

   public void doSuperImport() throws UtilEvalError {
      try {
         this.getClassPath().insureInitialized();
         this.getClassNameByUnqName("");
      } catch (ClassPathException var2) {
         throw new UtilEvalError("Error importing classpath " + var2);
      }

      this.superImport = true;
   }

   protected boolean hasSuperImport() {
      return this.superImport;
   }

   public String getClassNameByUnqName(String var1) throws ClassPathException {
      return this.getClassPath().getClassNameByUnqName(var1);
   }

   public void addListener(BshClassManager.Listener var1) {
      this.listeners.addElement(new WeakReference(var1, this.refQueue));

      Reference var2;
      while((var2 = this.refQueue.poll()) != null) {
         boolean var3 = this.listeners.removeElement(var2);
         if (!var3 && Interpreter.DEBUG) {
            Interpreter.debug("tried to remove non-existent weak ref: " + var2);
         }
      }

   }

   public void removeListener(BshClassManager.Listener var1) {
      throw new Error("unimplemented");
   }

   public ClassLoader getBaseLoader() {
      return this.baseLoader;
   }

   public Class defineClass(String var1, byte[] var2) {
      this.baseClassPath.setClassSource(var1, new BshClassPath.GeneratedClassSource(var2));

      try {
         this.reloadClasses(new String[]{var1});
      } catch (ClassPathException var4) {
         throw new InterpreterError("defineClass: " + var4);
      }

      return this.classForName(var1);
   }

   protected void classLoaderChanged() {
      this.clearCaches();
      Vector var1 = new Vector();
      Enumeration var2 = this.listeners.elements();

      while(var2.hasMoreElements()) {
         WeakReference var3 = (WeakReference)var2.nextElement();
         BshClassManager.Listener var4 = (BshClassManager.Listener)var3.get();
         if (var4 == null) {
            var1.add(var3);
         } else {
            var4.classLoaderChanged();
         }
      }

      Enumeration var5 = var1.elements();

      while(var5.hasMoreElements()) {
         this.listeners.removeElement(var5.nextElement());
      }

   }

   public void dump(PrintWriter var1) {
      var1.println("Bsh Class Manager Dump: ");
      var1.println("----------------------- ");
      var1.println("baseLoader = " + this.baseLoader);
      var1.println("loaderMap= " + this.loaderMap);
      var1.println("----------------------- ");
      var1.println("baseClassPath = " + this.baseClassPath);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
