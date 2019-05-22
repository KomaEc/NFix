package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

class HashBiMap$1$MapEntry extends AbstractMapEntry<K, V> {
   HashBiMap.BiEntry<K, V> delegate;
   // $FF: synthetic field
   final <undefinedtype> this$1;

   HashBiMap$1$MapEntry(Object this$1, HashBiMap.BiEntry entry) {
      this.this$1 = this$1;
      this.delegate = entry;
   }

   public K getKey() {
      return this.delegate.key;
   }

   public V getValue() {
      return this.delegate.value;
   }

   public V setValue(V value) {
      V oldValue = this.delegate.value;
      int valueHash = Hashing.smearedHash(value);
      if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
         return value;
      } else {
         Preconditions.checkArgument(HashBiMap.access$400(this.this$1.this$0, value, valueHash) == null, "value already present: %s", value);
         HashBiMap.access$200(this.this$1.this$0, this.delegate);
         HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry(this.delegate.key, this.delegate.keyHash, value, valueHash);
         HashBiMap.access$500(this.this$1.this$0, newEntry, this.delegate);
         this.delegate.prevInKeyInsertionOrder = null;
         this.delegate.nextInKeyInsertionOrder = null;
         this.this$1.expectedModCount = HashBiMap.access$100(this.this$1.this$0);
         if (this.this$1.toRemove == this.delegate) {
            this.this$1.toRemove = newEntry;
         }

         this.delegate = newEntry;
         return oldValue;
      }
   }
}
