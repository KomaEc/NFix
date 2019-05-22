package soot.shimple.internal;

import java.util.LinkedHashSet;
import java.util.Set;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class SHashMultiMap<K, V> extends HashMultiMap<K, V> {
   private static final long serialVersionUID = -860669798578291979L;

   public SHashMultiMap() {
   }

   public SHashMultiMap(MultiMap<K, V> m) {
      super(m);
   }

   protected Set<V> newSet() {
      return new LinkedHashSet(4);
   }
}
