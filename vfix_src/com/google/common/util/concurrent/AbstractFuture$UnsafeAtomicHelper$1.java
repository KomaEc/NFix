package com.google.common.util.concurrent;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;

final class AbstractFuture$UnsafeAtomicHelper$1 implements PrivilegedExceptionAction<Unsafe> {
   public Unsafe run() throws Exception {
      Class<Unsafe> k = Unsafe.class;
      Field[] var2 = k.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field f = var2[var4];
         f.setAccessible(true);
         Object x = f.get((Object)null);
         if (k.isInstance(x)) {
            return (Unsafe)k.cast(x);
         }
      }

      throw new NoSuchFieldError("the Unsafe");
   }
}
