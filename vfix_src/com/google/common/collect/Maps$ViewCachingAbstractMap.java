package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;

@GwtCompatible
abstract class Maps$ViewCachingAbstractMap<K, V> extends AbstractMap<K, V> {
   private transient Set<Entry<K, V>> entrySet;
   private transient Set<K> keySet;
   private transient Collection<V> values;

   abstract Set<Entry<K, V>> createEntrySet();

   public Set<Entry<K, V>> entrySet() {
      Set<Entry<K, V>> result = this.entrySet;
      return result == null ? (this.entrySet = this.createEntrySet()) : result;
   }

   public Set<K> keySet() {
      Set<K> result = this.keySet;
      return result == null ? (this.keySet = this.createKeySet()) : result;
   }

   Set<K> createKeySet() {
      return new Maps.KeySet(this);
   }

   public Collection<V> values() {
      Collection<V> result = this.values;
      return result == null ? (this.values = this.createValues()) : result;
   }

   Collection<V> createValues() {
      return new Maps.Values(this);
   }
}
