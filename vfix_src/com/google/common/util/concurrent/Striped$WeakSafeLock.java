package com.google.common.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

final class Striped$WeakSafeLock extends ForwardingLock {
   private final Lock delegate;
   private final Striped$WeakSafeReadWriteLock strongReference;

   Striped$WeakSafeLock(Lock delegate, Striped$WeakSafeReadWriteLock strongReference) {
      this.delegate = delegate;
      this.strongReference = strongReference;
   }

   Lock delegate() {
      return this.delegate;
   }

   public Condition newCondition() {
      return new Striped$WeakSafeCondition(this.delegate.newCondition(), this.strongReference);
   }
}
