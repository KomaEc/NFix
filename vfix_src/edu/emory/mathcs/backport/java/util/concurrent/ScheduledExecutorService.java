package edu.emory.mathcs.backport.java.util.concurrent;

public interface ScheduledExecutorService extends ExecutorService {
   ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4);

   ScheduledFuture schedule(Callable var1, long var2, TimeUnit var4);

   ScheduledFuture scheduleAtFixedRate(Runnable var1, long var2, long var4, TimeUnit var6);

   ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6);
}
