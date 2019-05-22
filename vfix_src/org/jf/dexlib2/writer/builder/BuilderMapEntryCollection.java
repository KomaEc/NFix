package org.jf.dexlib2.writer.builder;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public abstract class BuilderMapEntryCollection<Key> extends AbstractCollection<Entry<Key, Integer>> {
   @Nonnull
   private final Collection<Key> keys;

   public BuilderMapEntryCollection(@Nonnull Collection<Key> keys) {
      this.keys = keys;
   }

   @Nonnull
   public Iterator<Entry<Key, Integer>> iterator() {
      final Iterator<Key> iter = this.keys.iterator();
      return new Iterator<Entry<Key, Integer>>() {
         public boolean hasNext() {
            return iter.hasNext();
         }

         public Entry<Key, Integer> next() {
            BuilderMapEntryCollection<Key>.MapEntry entry = BuilderMapEntryCollection.this.new MapEntry();
            entry.key = iter.next();
            return entry;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return this.keys.size();
   }

   protected abstract int getValue(@Nonnull Key var1);

   protected abstract int setValue(@Nonnull Key var1, int var2);

   private class MapEntry implements Entry<Key, Integer> {
      @Nonnull
      private Key key;

      private MapEntry() {
      }

      @Nonnull
      public Key getKey() {
         return this.key;
      }

      public Integer getValue() {
         return BuilderMapEntryCollection.this.getValue(this.key);
      }

      public Integer setValue(Integer value) {
         return BuilderMapEntryCollection.this.setValue(this.key, value);
      }

      // $FF: synthetic method
      MapEntry(Object x1) {
         this();
      }
   }
}
