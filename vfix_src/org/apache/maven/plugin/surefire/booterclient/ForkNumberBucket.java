package org.apache.maven.plugin.surefire.booterclient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ForkNumberBucket {
   private static final ForkNumberBucket INSTANCE = new ForkNumberBucket();
   private Queue<Integer> qFree = new ConcurrentLinkedQueue();
   private AtomicInteger highWaterMark = new AtomicInteger(1);

   protected ForkNumberBucket() {
   }

   public static int drawNumber() {
      return getInstance()._drawNumber();
   }

   public static void returnNumber(int number) {
      getInstance()._returnNumber(number);
   }

   private static ForkNumberBucket getInstance() {
      return INSTANCE;
   }

   protected int _drawNumber() {
      Integer nextFree = (Integer)this.qFree.poll();
      return null == nextFree ? this.highWaterMark.getAndIncrement() : nextFree;
   }

   protected int getHighestDrawnNumber() {
      return this.highWaterMark.get() - 1;
   }

   protected void _returnNumber(int number) {
      this.qFree.add(number);
   }
}
