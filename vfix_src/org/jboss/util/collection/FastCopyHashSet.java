package org.jboss.util.collection;

import java.io.Serializable;
import java.util.Collection;

public class FastCopyHashSet<E> extends MapDelegateSet<E> implements Serializable {
   private static final long serialVersionUID = 1L;

   public FastCopyHashSet() {
      super(new FastCopyHashMap());
   }

   public FastCopyHashSet(Collection<? extends E> c) {
      super(new FastCopyHashMap(Math.max((int)((float)c.size() / 0.75F) + 1, 16)));
      this.addAll(c);
   }

   public FastCopyHashSet(int initialCapacity, float loadFactor) {
      super(new FastCopyHashMap(initialCapacity, loadFactor));
   }

   public FastCopyHashSet(int initialCapacity) {
      super(new FastCopyHashMap(initialCapacity));
   }
}
