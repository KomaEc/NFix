package org.apache.commons.collections.set;

import java.util.Set;
import org.apache.commons.collections.functors.InstanceofPredicate;

public class TypedSet {
   public static Set decorate(Set set, Class type) {
      return new PredicatedSet(set, InstanceofPredicate.getInstance(type));
   }

   protected TypedSet() {
   }
}
