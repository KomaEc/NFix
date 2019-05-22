package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

class HashBiMap$Inverse$1$InverseEntry extends AbstractMapEntry<V, K> {
   HashBiMap.BiEntry<K, V> delegate;
   // $FF: synthetic field
   final <undefinedtype> this$2;

   HashBiMap$Inverse$1$InverseEntry(Object this$2, HashBiMap.BiEntry entry) {
      this.this$2 = this$2;
      this.delegate = entry;
   }

   public V getKey() {
      return this.delegate.value;
   }

   public K getValue() {
      return this.delegate.key;
   }

   public K setValue(K key) {
      K oldKey = this.delegate.key;
      int keyHash = Hashing.smearedHash(key);
      if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
         return key;
      } else {
         Preconditions.checkArgument(HashBiMap.access$300(this.this$2.this$1.this$0, key, keyHash) == null, "value already present: %s", key);
         HashBiMap.access$200(this.this$2.this$1.this$0, this.delegate);
         HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry(key, keyHash, this.delegate.value, this.delegate.valueHash);
         this.delegate = newEntry;
         HashBiMap.access$500(this.this$2.this$1.this$0, newEntry, (HashBiMap.BiEntry)null);
         this.this$2.expectedModCount = HashBiMap.access$100(this.this$2.this$1.this$0);
         return oldKey;
      }
   }
}
