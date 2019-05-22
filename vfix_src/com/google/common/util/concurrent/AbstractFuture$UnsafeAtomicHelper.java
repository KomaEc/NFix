package com.google.common.util.concurrent;

import com.google.common.base.Throwables;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import sun.misc.Unsafe;

final class AbstractFuture$UnsafeAtomicHelper extends AbstractFuture$AtomicHelper {
   static final Unsafe UNSAFE;
   static final long LISTENERS_OFFSET;
   static final long WAITERS_OFFSET;
   static final long VALUE_OFFSET;
   static final long WAITER_THREAD_OFFSET;
   static final long WAITER_NEXT_OFFSET;

   private AbstractFuture$UnsafeAtomicHelper() {
      super((AbstractFuture$1)null);
   }

   void putThread(AbstractFuture$Waiter waiter, Thread newValue) {
      UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
   }

   void putNext(AbstractFuture$Waiter waiter, AbstractFuture$Waiter newValue) {
      UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
   }

   boolean casWaiters(AbstractFuture<?> future, AbstractFuture$Waiter expect, AbstractFuture$Waiter update) {
      return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
   }

   boolean casListeners(AbstractFuture<?> future, AbstractFuture$Listener expect, AbstractFuture$Listener update) {
      return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
   }

   boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
      return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
   }

   // $FF: synthetic method
   AbstractFuture$UnsafeAtomicHelper(AbstractFuture$1 x0) {
      this();
   }

   static {
      Unsafe unsafe = null;

      try {
         unsafe = Unsafe.getUnsafe();
      } catch (SecurityException var5) {
         try {
            unsafe = (Unsafe)AccessController.doPrivileged(new AbstractFuture$UnsafeAtomicHelper$1());
         } catch (PrivilegedActionException var4) {
            throw new RuntimeException("Could not initialize intrinsics", var4.getCause());
         }
      }

      try {
         Class<?> abstractFuture = AbstractFuture.class;
         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture$Waiter.class.getDeclaredField("thread"));
         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture$Waiter.class.getDeclaredField("next"));
         UNSAFE = unsafe;
      } catch (Exception var3) {
         Throwables.throwIfUnchecked(var3);
         throw new RuntimeException(var3);
      }
   }
}
