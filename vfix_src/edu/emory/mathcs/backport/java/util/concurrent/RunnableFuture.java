package edu.emory.mathcs.backport.java.util.concurrent;

public interface RunnableFuture extends Runnable, Future {
   void run();
}
