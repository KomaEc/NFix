package edu.emory.mathcs.backport.java.util.concurrent;

public interface Delayed extends Comparable {
   long getDelay(TimeUnit var1);
}
