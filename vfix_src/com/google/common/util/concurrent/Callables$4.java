package com.google.common.util.concurrent;

import com.google.common.base.Supplier;

final class Callables$4 implements Runnable {
   // $FF: synthetic field
   final Supplier val$nameSupplier;
   // $FF: synthetic field
   final Runnable val$task;

   Callables$4(Supplier var1, Runnable var2) {
      this.val$nameSupplier = var1;
      this.val$task = var2;
   }

   public void run() {
      Thread currentThread = Thread.currentThread();
      String oldName = currentThread.getName();
      boolean restoreName = Callables.access$000((String)this.val$nameSupplier.get(), currentThread);
      boolean var8 = false;

      try {
         var8 = true;
         this.val$task.run();
         var8 = false;
      } finally {
         if (var8) {
            if (restoreName) {
               Callables.access$000(oldName, currentThread);
            }

         }
      }

      if (restoreName) {
         Callables.access$000(oldName, currentThread);
      }

   }
}
