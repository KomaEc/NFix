package bsh.classpath;

import bsh.BshClassManager;
import java.util.HashMap;

public class DiscreteFilesClassLoader extends BshClassLoader {
   DiscreteFilesClassLoader.ClassSourceMap map;

   public DiscreteFilesClassLoader(BshClassManager var1, DiscreteFilesClassLoader.ClassSourceMap var2) {
      super(var1);
      this.map = var2;
   }

   public Class findClass(String var1) throws ClassNotFoundException {
      BshClassPath.ClassSource var2 = this.map.get(var1);
      if (var2 != null) {
         byte[] var3 = var2.getCode(var1);
         return this.defineClass(var1, var3, 0, var3.length);
      } else {
         return super.findClass(var1);
      }
   }

   public String toString() {
      return super.toString() + "for files: " + this.map;
   }

   public static class ClassSourceMap extends HashMap {
      public void put(String var1, BshClassPath.ClassSource var2) {
         super.put(var1, var2);
      }

      public BshClassPath.ClassSource get(String var1) {
         return (BshClassPath.ClassSource)super.get(var1);
      }
   }
}
