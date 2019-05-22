package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@GwtIncompatible
class RegularImmutableMap$Values$SerializedForm<V> implements Serializable {
   final ImmutableMap<?, V> map;
   private static final long serialVersionUID = 0L;

   RegularImmutableMap$Values$SerializedForm(ImmutableMap<?, V> map) {
      this.map = map;
   }

   Object readResolve() {
      return this.map.values();
   }
}
