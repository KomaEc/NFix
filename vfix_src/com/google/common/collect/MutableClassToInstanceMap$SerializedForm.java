package com.google.common.collect;

import java.io.Serializable;
import java.util.Map;

final class MutableClassToInstanceMap$SerializedForm<B> implements Serializable {
   private final Map<Class<? extends B>, B> backingMap;
   private static final long serialVersionUID = 0L;

   MutableClassToInstanceMap$SerializedForm(Map<Class<? extends B>, B> backingMap) {
      this.backingMap = backingMap;
   }

   Object readResolve() {
      return MutableClassToInstanceMap.create(this.backingMap);
   }
}
