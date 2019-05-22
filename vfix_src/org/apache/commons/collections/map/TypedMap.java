package org.apache.commons.collections.map;

import java.util.Map;
import org.apache.commons.collections.functors.InstanceofPredicate;

public class TypedMap {
   public static Map decorate(Map map, Class keyType, Class valueType) {
      return new PredicatedMap(map, InstanceofPredicate.getInstance(keyType), InstanceofPredicate.getInstance(valueType));
   }

   protected TypedMap() {
   }
}
