package org.testng.internal.thread.graph;

import java.util.concurrent.ThreadFactory;

class TestNGThreadPoolFactory implements ThreadFactory {
   private int m_count = 0;

   public Thread newThread(Runnable r) {
      Thread result = new Thread(r);
      result.setName("TestNG-" + this.m_count++);
      return result;
   }
}
