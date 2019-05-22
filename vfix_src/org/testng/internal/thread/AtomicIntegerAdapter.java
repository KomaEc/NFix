package org.testng.internal.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerAdapter implements IAtomicInteger {
   private static final long serialVersionUID = -6295904797532558594L;
   private AtomicInteger m_atomicInteger;

   public AtomicIntegerAdapter(int initialValue) {
      this.m_atomicInteger = new AtomicInteger(initialValue);
   }

   public int get() {
      return this.m_atomicInteger.get();
   }

   public int incrementAndGet() {
      return this.m_atomicInteger.incrementAndGet();
   }
}
