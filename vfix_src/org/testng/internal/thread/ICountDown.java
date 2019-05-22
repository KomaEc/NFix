package org.testng.internal.thread;

public interface ICountDown {
   void await() throws InterruptedException;

   boolean await(long var1) throws InterruptedException;

   void countDown();
}
