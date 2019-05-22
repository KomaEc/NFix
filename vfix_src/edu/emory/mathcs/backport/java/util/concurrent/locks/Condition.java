package edu.emory.mathcs.backport.java.util.concurrent.locks;

import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import java.util.Date;

public interface Condition {
   void await() throws InterruptedException;

   void awaitUninterruptibly();

   boolean await(long var1, TimeUnit var3) throws InterruptedException;

   boolean awaitUntil(Date var1) throws InterruptedException;

   void signal();

   void signalAll();
}
