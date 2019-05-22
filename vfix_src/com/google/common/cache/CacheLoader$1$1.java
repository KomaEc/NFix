package com.google.common.cache;

import java.util.concurrent.Callable;

class CacheLoader$1$1 implements Callable<V> {
   // $FF: synthetic field
   final Object val$key;
   // $FF: synthetic field
   final Object val$oldValue;
   // $FF: synthetic field
   final CacheLoader$1 this$0;

   CacheLoader$1$1(CacheLoader$1 var1, Object var2, Object var3) {
      this.this$0 = var1;
      this.val$key = var2;
      this.val$oldValue = var3;
   }

   public V call() throws Exception {
      return this.this$0.val$loader.reload(this.val$key, this.val$oldValue).get();
   }
}
