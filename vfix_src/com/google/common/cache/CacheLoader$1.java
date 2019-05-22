package com.google.common.cache;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import java.util.Map;
import java.util.concurrent.Executor;

final class CacheLoader$1 extends CacheLoader<K, V> {
   // $FF: synthetic field
   final CacheLoader val$loader;
   // $FF: synthetic field
   final Executor val$executor;

   CacheLoader$1(CacheLoader var1, Executor var2) {
      this.val$loader = var1;
      this.val$executor = var2;
   }

   public V load(K key) throws Exception {
      return this.val$loader.load(key);
   }

   public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
      ListenableFutureTask<V> task = ListenableFutureTask.create(new CacheLoader$1$1(this, key, oldValue));
      this.val$executor.execute(task);
      return task;
   }

   public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
      return this.val$loader.loadAll(keys);
   }
}
