package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.Serializable;
import java.util.Map;

@GwtCompatible(
   emulated = true
)
public abstract class CacheLoader<K, V> {
   protected CacheLoader() {
   }

   public abstract V load(K var1) throws Exception;

   @GwtIncompatible("Futures")
   public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(oldValue);
      return Futures.immediateFuture(this.load(key));
   }

   public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
      throw new CacheLoader.UnsupportedLoadingOperationException();
   }

   @Beta
   public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
      return new CacheLoader.FunctionToCacheLoader(function);
   }

   @Beta
   public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
      return new CacheLoader.SupplierToCacheLoader(supplier);
   }

   public static final class InvalidCacheLoadException extends RuntimeException {
      public InvalidCacheLoadException(String message) {
         super(message);
      }
   }

   static final class UnsupportedLoadingOperationException extends UnsupportedOperationException {
   }

   private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable {
      private final Supplier<V> computingSupplier;
      private static final long serialVersionUID = 0L;

      public SupplierToCacheLoader(Supplier<V> computingSupplier) {
         this.computingSupplier = (Supplier)Preconditions.checkNotNull(computingSupplier);
      }

      public V load(Object key) {
         Preconditions.checkNotNull(key);
         return this.computingSupplier.get();
      }
   }

   private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
      private final Function<K, V> computingFunction;
      private static final long serialVersionUID = 0L;

      public FunctionToCacheLoader(Function<K, V> computingFunction) {
         this.computingFunction = (Function)Preconditions.checkNotNull(computingFunction);
      }

      public V load(K key) {
         return this.computingFunction.apply(Preconditions.checkNotNull(key));
      }
   }
}
