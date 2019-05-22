package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.StringSection;

class BuilderStringPool implements StringSection<BuilderStringReference, BuilderStringReference> {
   @Nonnull
   private final ConcurrentMap<String, BuilderStringReference> internedItems = Maps.newConcurrentMap();

   @Nonnull
   BuilderStringReference internString(@Nonnull String string) {
      BuilderStringReference ret = (BuilderStringReference)this.internedItems.get(string);
      if (ret != null) {
         return ret;
      } else {
         BuilderStringReference stringReference = new BuilderStringReference(string);
         ret = (BuilderStringReference)this.internedItems.putIfAbsent(string, stringReference);
         return ret == null ? stringReference : ret;
      }
   }

   @Nullable
   BuilderStringReference internNullableString(@Nullable String string) {
      return string == null ? null : this.internString(string);
   }

   public int getNullableItemIndex(@Nullable BuilderStringReference key) {
      return key == null ? -1 : key.index;
   }

   public int getItemIndex(@Nonnull BuilderStringReference key) {
      return key.index;
   }

   public boolean hasJumboIndexes() {
      return this.internedItems.size() > 65536;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderStringReference, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderStringReference>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderStringReference key) {
            return key.index;
         }

         protected int setValue(@Nonnull BuilderStringReference key, int value) {
            int prev = key.index;
            key.index = value;
            return prev;
         }
      };
   }

   public int getItemCount() {
      return this.internedItems.size();
   }
}
