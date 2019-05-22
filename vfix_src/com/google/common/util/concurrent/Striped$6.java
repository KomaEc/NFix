package com.google.common.util.concurrent;

import com.google.common.base.Supplier;
import java.util.concurrent.locks.ReadWriteLock;

final class Striped$6 implements Supplier<ReadWriteLock> {
   public ReadWriteLock get() {
      return new Striped$WeakSafeReadWriteLock();
   }
}
