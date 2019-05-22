package com.google.common.util.concurrent;

import java.util.concurrent.locks.Condition;

final class Striped$WeakSafeCondition extends ForwardingCondition {
   private final Condition delegate;
   private final Striped$WeakSafeReadWriteLock strongReference;

   Striped$WeakSafeCondition(Condition delegate, Striped$WeakSafeReadWriteLock strongReference) {
      this.delegate = delegate;
      this.strongReference = strongReference;
   }

   Condition delegate() {
      return this.delegate;
   }
}
