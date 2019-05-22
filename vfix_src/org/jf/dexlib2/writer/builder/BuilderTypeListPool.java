package org.jf.dexlib2.writer.builder;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.writer.TypeListSection;

class BuilderTypeListPool extends BaseBuilderPool implements TypeListSection<BuilderTypeReference, BuilderTypeList> {
   @Nonnull
   private final ConcurrentMap<List<? extends CharSequence>, BuilderTypeList> internedItems = Maps.newConcurrentMap();

   public BuilderTypeListPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   public BuilderTypeList internTypeList(@Nullable List<? extends CharSequence> types) {
      if (types != null && types.size() != 0) {
         BuilderTypeList ret = (BuilderTypeList)this.internedItems.get(types);
         if (ret != null) {
            return ret;
         } else {
            BuilderTypeList typeList = new BuilderTypeList(ImmutableList.copyOf(Iterables.transform(types, new Function<CharSequence, BuilderTypeReference>() {
               @Nonnull
               public BuilderTypeReference apply(CharSequence input) {
                  return ((BuilderTypePool)BuilderTypeListPool.this.dexBuilder.typeSection).internType(input.toString());
               }
            })));
            ret = (BuilderTypeList)this.internedItems.putIfAbsent(typeList, typeList);
            return ret == null ? typeList : ret;
         }
      } else {
         return BuilderTypeList.EMPTY;
      }
   }

   public int getNullableItemOffset(@Nullable BuilderTypeList key) {
      return key != null && key.size() != 0 ? key.offset : 0;
   }

   @Nonnull
   public Collection<? extends BuilderTypeReference> getTypes(@Nullable BuilderTypeList key) {
      return (Collection)(key == null ? BuilderTypeList.EMPTY : key.types);
   }

   public int getItemOffset(@Nonnull BuilderTypeList key) {
      return key.offset;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderTypeList, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderTypeList>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderTypeList key) {
            return key.offset;
         }

         protected int setValue(@Nonnull BuilderTypeList key, int value) {
            int prev = key.offset;
            key.offset = value;
            return prev;
         }
      };
   }
}
