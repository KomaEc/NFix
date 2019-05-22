package org.jboss.util.collection;

import java.util.EnumSet;
import java.util.Set;

public class ConcurrentReferenceHashSet<E> extends MapDelegateSet<E> {
   private static final long serialVersionUID = 1L;

   public ConcurrentReferenceHashSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
      super(new ConcurrentReferenceHashMap(initialCapacity, loadFactor, concurrencyLevel));
   }

   public ConcurrentReferenceHashSet(int initialCapacity, float loadFactor) {
      super(new ConcurrentReferenceHashMap(initialCapacity, loadFactor));
   }

   public ConcurrentReferenceHashSet(int initialCapacity, ConcurrentReferenceHashMap.ReferenceType type) {
      super(new ConcurrentReferenceHashMap(initialCapacity, type, ConcurrentReferenceHashMap.ReferenceType.STRONG));
   }

   public ConcurrentReferenceHashSet(ConcurrentReferenceHashMap.ReferenceType type) {
      super(new ConcurrentReferenceHashMap(type, ConcurrentReferenceHashMap.ReferenceType.STRONG));
   }

   public ConcurrentReferenceHashSet(ConcurrentReferenceHashMap.ReferenceType type, EnumSet<ConcurrentReferenceHashMap.Option> options) {
      super(new ConcurrentReferenceHashMap(type, ConcurrentReferenceHashMap.ReferenceType.STRONG, options));
   }

   public ConcurrentReferenceHashSet(int initialCapacity) {
      super(new ConcurrentReferenceHashMap(initialCapacity));
   }

   public ConcurrentReferenceHashSet() {
      super(new ConcurrentReferenceHashMap());
   }

   public ConcurrentReferenceHashSet(Set<? extends E> s) {
      super(new ConcurrentReferenceHashMap());
      this.addAll(s);
   }
}
