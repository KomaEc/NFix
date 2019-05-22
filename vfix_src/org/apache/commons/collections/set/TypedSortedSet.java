package org.apache.commons.collections.set;

import java.util.SortedSet;
import org.apache.commons.collections.functors.InstanceofPredicate;

public class TypedSortedSet {
   public static SortedSet decorate(SortedSet set, Class type) {
      return new PredicatedSortedSet(set, InstanceofPredicate.getInstance(type));
   }

   protected TypedSortedSet() {
   }
}
