package com.google.common.collect;

import javax.annotation.Nullable;

interface MapMakerInternalMap$InternalEntryHelper<K, V, E extends MapMakerInternalMap$InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>> {
   MapMakerInternalMap.Strength keyStrength();

   MapMakerInternalMap.Strength valueStrength();

   S newSegment(MapMakerInternalMap<K, V, E, S> var1, int var2, int var3);

   E newEntry(S var1, K var2, int var3, @Nullable E var4);

   E copy(S var1, E var2, @Nullable E var3);

   void setValue(S var1, E var2, V var3);
}
