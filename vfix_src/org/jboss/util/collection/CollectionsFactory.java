package org.jboss.util.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class CollectionsFactory {
   public static final <K, V> Map<K, V> createLazyMap() {
      return new LazyMap();
   }

   public static final <T> List<T> createLazyList() {
      return new LazyList();
   }

   public static final <T> Set<T> createLazySet() {
      return new LazySet();
   }

   public static final <K, V> Map<K, V> createConcurrentReaderMap() {
      return new ConcurrentHashMap();
   }

   public static final <T> List<T> createCopyOnWriteList() {
      return new CopyOnWriteArrayList();
   }

   public static final <T> Set<T> createCopyOnWriteSet() {
      return new CopyOnWriteArraySet();
   }

   public static final <T> Set<T> createConcurrentSet() {
      return new ConcurrentSet();
   }

   public static final <T> Set<T> createConcurrentReferenceSet() {
      return new ConcurrentReferenceHashSet();
   }
}
