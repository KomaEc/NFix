package edu.emory.mathcs.backport.java.util.concurrent;

public interface CompletionService {
   Future submit(Callable var1);

   Future submit(Runnable var1, Object var2);

   Future take() throws InterruptedException;

   Future poll();

   Future poll(long var1, TimeUnit var3) throws InterruptedException;
}
