package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.WrappedCheckedException;

final class ClassLoaderUtils {
   static Class<?> loadClass(ClassLoader classLoader, Class<?> classToLoad) {
      try {
         return classLoader.loadClass(classToLoad.getName());
      } catch (ClassNotFoundException var3) {
         throw new WrappedCheckedException(var3);
      }
   }

   private ClassLoaderUtils() {
      throw new UnsupportedOperationException("Not instantiable");
   }
}
