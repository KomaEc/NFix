package com.google.common.base;

import java.util.AbstractList;

final class Throwables$1 extends AbstractList<StackTraceElement> {
   // $FF: synthetic field
   final Throwable val$t;

   Throwables$1(Throwable var1) {
      this.val$t = var1;
   }

   public StackTraceElement get(int n) {
      return (StackTraceElement)Throwables.access$200(Throwables.access$000(), Throwables.access$100(), new Object[]{this.val$t, n});
   }

   public int size() {
      return (Integer)Throwables.access$200(Throwables.access$300(), Throwables.access$100(), new Object[]{this.val$t});
   }
}
