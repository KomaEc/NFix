package com.google.inject.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public abstract class FailableCache<K, V> {
   private final LoadingCache<K, Object> delegate = CacheBuilder.newBuilder().build(new CacheLoader<K, Object>() {
      public Object load(K key) {
         Errors errors = new Errors();
         Object result = null;

         try {
            result = FailableCache.this.create(key, errors);
         } catch (ErrorsException var5) {
            errors.merge(var5.getErrors());
         }

         return errors.hasErrors() ? errors : result;
      }
   });

   protected abstract V create(K var1, Errors var2) throws ErrorsException;

   public V get(K key, Errors errors) throws ErrorsException {
      Object resultOrError = this.delegate.getUnchecked(key);
      if (resultOrError instanceof Errors) {
         errors.merge((Errors)resultOrError);
         throw errors.toException();
      } else {
         return resultOrError;
      }
   }

   boolean remove(K key) {
      return this.delegate.asMap().remove(key) != null;
   }
}
