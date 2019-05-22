package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;

class AbstractBiMap$BiMapEntry extends ForwardingMapEntry<K, V> {
   private final Entry<K, V> delegate;
   // $FF: synthetic field
   final AbstractBiMap this$0;

   AbstractBiMap$BiMapEntry(AbstractBiMap this$0, Entry delegate) {
      this.this$0 = this$0;
      this.delegate = delegate;
   }

   protected Entry<K, V> delegate() {
      return this.delegate;
   }

   public V setValue(V value) {
      this.this$0.checkValue(value);
      Preconditions.checkState(this.this$0.entrySet().contains(this), "entry no longer in map");
      if (Objects.equal(value, this.getValue())) {
         return value;
      } else {
         Preconditions.checkArgument(!this.this$0.containsValue(value), "value already present: %s", value);
         V oldValue = this.delegate.setValue(value);
         Preconditions.checkState(Objects.equal(value, this.this$0.get(this.getKey())), "entry no longer in map");
         AbstractBiMap.access$500(this.this$0, this.getKey(), true, oldValue, value);
         return oldValue;
      }
   }
}
