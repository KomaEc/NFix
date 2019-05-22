package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.function.Consumer;

final class ImmutableMapEntrySet$RegularEntrySet<K, V> extends ImmutableMapEntrySet<K, V> {
   @Weak
   private final transient ImmutableMap<K, V> map;
   private final transient Entry<K, V>[] entries;

   ImmutableMapEntrySet$RegularEntrySet(ImmutableMap<K, V> map, Entry<K, V>[] entries) {
      this.map = map;
      this.entries = entries;
   }

   ImmutableMap<K, V> map() {
      return this.map;
   }

   public UnmodifiableIterator<Entry<K, V>> iterator() {
      return Iterators.forArray(this.entries);
   }

   public Spliterator<Entry<K, V>> spliterator() {
      return Spliterators.spliterator(this.entries, 1297);
   }

   public void forEach(Consumer<? super Entry<K, V>> action) {
      Preconditions.checkNotNull(action);
      Entry[] var2 = this.entries;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Entry<K, V> entry = var2[var4];
         action.accept(entry);
      }

   }

   ImmutableList<Entry<K, V>> createAsList() {
      return new RegularImmutableAsList(this, this.entries);
   }
}
