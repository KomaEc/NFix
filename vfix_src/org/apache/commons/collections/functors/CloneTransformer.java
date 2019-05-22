package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Transformer;

public class CloneTransformer implements Transformer, Serializable {
   private static final long serialVersionUID = -8188742709499652567L;
   public static final Transformer INSTANCE = new CloneTransformer();

   public static Transformer getInstance() {
      return INSTANCE;
   }

   private CloneTransformer() {
   }

   public Object transform(Object input) {
      return input == null ? null : PrototypeFactory.getInstance(input).create();
   }
}
