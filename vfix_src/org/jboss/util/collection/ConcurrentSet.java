package org.jboss.util.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentSet<E> extends MapDelegateSet<E> implements Serializable {
   private static final long serialVersionUID = 1L;

   public ConcurrentSet() {
      super(new ConcurrentHashMap());
   }

   public ConcurrentSet(Collection<? extends E> c) {
      super(new ConcurrentHashMap(Math.max((int)((float)c.size() / 0.75F) + 1, 16)));
      this.addAll(c);
   }

   public ConcurrentSet(int initialCapacity, float loadFactor, int concurrencyLevel) {
      super(new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel));
   }

   public ConcurrentSet(int initialCapacity) {
      super(new ConcurrentHashMap(initialCapacity));
   }
}
