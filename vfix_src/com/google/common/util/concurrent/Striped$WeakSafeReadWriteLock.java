package com.google.common.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class Striped$WeakSafeReadWriteLock implements ReadWriteLock {
   private final ReadWriteLock delegate = new ReentrantReadWriteLock();

   public Lock readLock() {
      return new Striped$WeakSafeLock(this.delegate.readLock(), this);
   }

   public Lock writeLock() {
      return new Striped$WeakSafeLock(this.delegate.writeLock(), this);
   }
}
