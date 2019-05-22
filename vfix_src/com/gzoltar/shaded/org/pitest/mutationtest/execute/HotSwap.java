package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.boot.HotSwapAgent;
import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.F3;
import com.gzoltar.shaded.org.pitest.util.Unchecked;

class HotSwap implements F3<ClassName, ClassLoader, byte[], Boolean> {
   private final ClassByteArraySource byteSource;
   private byte[] lastClassPreMutation;
   private ClassName lastMutatedClass;
   private ClassLoader lastUsedLoader;

   HotSwap(ClassByteArraySource byteSource) {
      this.byteSource = byteSource;
   }

   public Boolean apply(ClassName clazzName, ClassLoader loader, byte[] b) {
      try {
         this.restoreLastClass(this.byteSource, clazzName, loader);
         this.lastUsedLoader = loader;
         Class<?> clazz = Class.forName(clazzName.asJavaName(), false, loader);
         return HotSwapAgent.hotSwap(clazz, b);
      } catch (ClassNotFoundException var6) {
         throw Unchecked.translateCheckedException(var6);
      }
   }

   private void restoreLastClass(ClassByteArraySource byteSource, ClassName clazzName, ClassLoader loader) throws ClassNotFoundException {
      if (this.lastMutatedClass != null && !this.lastMutatedClass.equals(clazzName)) {
         this.restoreForLoader(this.lastUsedLoader);
         this.restoreForLoader(loader);
      }

      if (this.lastMutatedClass == null || !this.lastMutatedClass.equals(clazzName)) {
         this.lastClassPreMutation = (byte[])byteSource.getBytes(clazzName.asJavaName()).value();
      }

      this.lastMutatedClass = clazzName;
   }

   private void restoreForLoader(ClassLoader loader) throws ClassNotFoundException {
      Class<?> clazz = Class.forName(this.lastMutatedClass.asJavaName(), false, loader);
      HotSwapAgent.hotSwap(clazz, this.lastClassPreMutation);
   }
}
