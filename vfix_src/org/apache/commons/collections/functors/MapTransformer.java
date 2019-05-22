package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections.Transformer;

public final class MapTransformer implements Transformer, Serializable {
   private static final long serialVersionUID = 862391807045468939L;
   private final Map iMap;

   public static Transformer getInstance(Map map) {
      return (Transformer)(map == null ? ConstantTransformer.NULL_INSTANCE : new MapTransformer(map));
   }

   private MapTransformer(Map map) {
      this.iMap = map;
   }

   public Object transform(Object input) {
      return this.iMap.get(input);
   }

   public Map getMap() {
      return this.iMap;
   }
}
