package bsh.classpath;

import bsh.BshClassManager;
import java.net.URL;
import java.net.URLClassLoader;

public class BshClassLoader extends URLClassLoader {
   BshClassManager classManager;
   // $FF: synthetic field
   static Class class$bsh$Interpreter;

   public BshClassLoader(BshClassManager var1, URL[] var2) {
      super(var2);
      this.classManager = var1;
   }

   public BshClassLoader(BshClassManager var1, BshClassPath var2) {
      this(var1, var2.getPathComponents());
   }

   protected BshClassLoader(BshClassManager var1) {
      this(var1, new URL[0]);
   }

   public void addURL(URL var1) {
      super.addURL(var1);
   }

   public Class loadClass(String var1, boolean var2) throws ClassNotFoundException {
      Class var3 = null;
      var3 = this.findLoadedClass(var1);
      if (var3 != null) {
         return var3;
      } else {
         if (var1.startsWith("bsh")) {
            try {
               return (class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter).getClassLoader().loadClass(var1);
            } catch (ClassNotFoundException var6) {
            }
         }

         try {
            var3 = this.findClass(var1);
         } catch (ClassNotFoundException var5) {
         }

         if (var3 == null) {
            throw new ClassNotFoundException("here in loaClass");
         } else {
            if (var2) {
               this.resolveClass(var3);
            }

            return var3;
         }
      }
   }

   protected Class findClass(String var1) throws ClassNotFoundException {
      ClassManagerImpl var2 = (ClassManagerImpl)this.getClassManager();
      ClassLoader var3 = var2.getLoaderForClass(var1);
      if (var3 != null && var3 != this) {
         try {
            return var3.loadClass(var1);
         } catch (ClassNotFoundException var5) {
            throw new ClassNotFoundException("Designated loader could not find class: " + var5);
         }
      } else {
         if (this.getURLs().length > 0) {
            try {
               return super.findClass(var1);
            } catch (ClassNotFoundException var7) {
            }
         }

         var3 = var2.getBaseLoader();
         if (var3 != null && var3 != this) {
            try {
               return var3.loadClass(var1);
            } catch (ClassNotFoundException var6) {
            }
         }

         return var2.plainClassForName(var1);
      }
   }

   BshClassManager getClassManager() {
      return this.classManager;
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
