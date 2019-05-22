package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@GwtIncompatible
class RegularImmutableMap$KeySet$SerializedForm<K> implements Serializable {
   final ImmutableMap<K, ?> map;
   private static final long serialVersionUID = 0L;

   RegularImmutableMap$KeySet$SerializedForm(ImmutableMap<K, ?> map) {
      this.map = map;
   }

   Object readResolve() {
      return this.map.keySet();
   }
}
