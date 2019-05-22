package edu.emory.mathcs.backport.java.util.concurrent;

public interface Future {
   boolean cancel(boolean var1);

   boolean isCancelled();

   boolean isDone();

   Object get() throws InterruptedException, ExecutionException;

   Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException;
}
