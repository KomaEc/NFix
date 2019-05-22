package edu.emory.mathcs.backport.java.util.concurrent;

public interface RunnableScheduledFuture extends RunnableFuture, ScheduledFuture {
   boolean isPeriodic();
}
