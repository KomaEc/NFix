package org.testng.internal.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownAdapter implements ICountDown {
   protected CountDownLatch m_doneLatch;

   public CountDownAdapter(int count) {
      this.m_doneLatch = new CountDownLatch(count);
   }

   public void await() throws InterruptedException {
      this.m_doneLatch.await();
   }

   public boolean await(long timeout) throws InterruptedException {
      return this.m_doneLatch.await(timeout, TimeUnit.MILLISECONDS);
   }

   public void countDown() {
      this.m_doneLatch.countDown();
   }
}
