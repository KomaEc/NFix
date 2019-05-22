package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.TypeSection;

class BuilderTypePool extends BaseBuilderPool implements TypeSection<BuilderStringReference, BuilderTypeReference, BuilderTypeReference> {
   @Nonnull
   private final ConcurrentMap<String, BuilderTypeReference> internedItems = Maps.newConcurrentMap();

   public BuilderTypePool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderTypeReference internType(@Nonnull String type) {
      BuilderTypeReference ret = (BuilderTypeReference)this.internedItems.get(type);
      if (ret != null) {
         return ret;
      } else {
         BuilderStringReference stringRef = ((BuilderStringPool)this.dexBuilder.stringSection).internString(type);
         BuilderTypeReference typeReference = new BuilderTypeReference(stringRef);
         ret = (BuilderTypeReference)this.internedItems.putIfAbsent(type, typeReference);
         return ret == null ? typeReference : ret;
      }
   }

   @Nullable
   public BuilderTypeReference internNullableType(@Nullable String type) {
      return type == null ? null : this.internType(type);
   }

   @Nonnull
   public BuilderStringReference getString(@Nonnull BuilderTypeReference key) {
      return key.stringReference;
   }

   public int getNullableItemIndex(@Nullable BuilderTypeReference key) {
      return key == null ? -1 : key.index;
   }

   public int getItemIndex(@Nonnull BuilderTypeReference key) {
      return key.getIndex();
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderTypeReference, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderTypeReference>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderTypeReference key) {
            return key.index;
         }

         protected int setValue(@Nonnull BuilderTypeReference key, int value) {
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
