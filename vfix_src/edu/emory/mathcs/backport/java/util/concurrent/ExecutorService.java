package edu.emory.mathcs.backport.java.util.concurrent;

import java.util.Collection;
import java.util.List;

public interface ExecutorService extends Executor {
   void shutdown();

   List shutdownNow();

   boolean isShutdown();

   boolean isTerminated();

   boolean awaitTermination(long var1, TimeUnit var3) throws InterruptedException;

   Future submit(Callable var1);

   Future submit(Runnable var1, Object var2);

   Future submit(Runnable var1);

   List invokeAll(Collection var1) throws InterruptedException;

   List invokeAll(Collection var1, long var2, TimeUnit var4) throws InterruptedException;

   Object invokeAny(Collection var1) throws InterruptedException, ExecutionException;

   Object invokeAny(Collection var1, long var2, TimeUnit var4) throws InterruptedException, ExecutionException, TimeoutException;
}
