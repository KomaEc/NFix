package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Transformer;

public class ConstantTransformer implements Transformer, Serializable {
   private static final long serialVersionUID = 6374440726369055124L;
   public static final Transformer NULL_INSTANCE = new ConstantTransformer((Object)null);
   private final Object iConstant;

   public static Transformer getInstance(Object constantToReturn) {
      return (Transformer)(constantToReturn == null ? NULL_INSTANCE : new ConstantTransformer(constantToReturn));
   }

   public ConstantTransformer(Object constantToReturn) {
      this.iConstant = constantToReturn;
   }

   public Object transform(Object input) {
      return this.iConstant;
   }

   public Object getConstant() {
      return this.iConstant;
   }
}
