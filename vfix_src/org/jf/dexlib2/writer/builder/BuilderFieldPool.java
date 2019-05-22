package org.jf.dexlib2.writer.builder;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.writer.FieldSection;

public class BuilderFieldPool extends BaseBuilderPool implements FieldSection<BuilderStringReference, BuilderTypeReference, BuilderFieldReference, BuilderField> {
   @Nonnull
   private final ConcurrentMap<FieldReference, BuilderFieldReference> internedItems = Maps.newConcurrentMap();

   public BuilderFieldPool(@Nonnull DexBuilder dexBuilder) {
      super(dexBuilder);
   }

   @Nonnull
   BuilderFieldReference internField(@Nonnull String definingClass, String name, String type) {
      ImmutableFieldReference fieldReference = new ImmutableFieldReference(definingClass, name, type);
      return this.internField(fieldReference);
   }

   @Nonnull
   public BuilderFieldReference internField(@Nonnull FieldReference fieldReference) {
      BuilderFieldReference ret = (BuilderFieldReference)this.internedItems.get(fieldReference);
      if (ret != null) {
         return ret;
      } else {
         BuilderFieldReference dexPoolFieldReference = new BuilderFieldReference(((BuilderTypePool)this.dexBuilder.typeSection).internType(fieldReference.getDefiningClass()), ((BuilderStringPool)this.dexBuilder.stringSection).internString(fieldReference.getName()), ((BuilderTypePool)this.dexBuilder.typeSection).internType(fieldReference.getType()));
         ret = (BuilderFieldReference)this.internedItems.putIfAbsent(dexPoolFieldReference, dexPoolFieldReference);
         return ret == null ? dexPoolFieldReference : ret;
      }
   }

   @Nonnull
   public BuilderTypeReference getDefiningClass(@Nonnull BuilderFieldReference key) {
      return key.definingClass;
   }

   @Nonnull
   public BuilderTypeReference getFieldType(@Nonnull BuilderFieldReference key) {
      return key.fieldType;
   }

   @Nonnull
   public BuilderStringReference getName(@Nonnull BuilderFieldReference key) {
      return key.name;
   }

   public int getFieldIndex(@Nonnull BuilderField builderField) {
      return builderField.fieldReference.getIndex();
   }

   public int getItemIndex(@Nonnull BuilderFieldReference key) {
      return key.index;
   }

   @Nonnull
   public Collection<? extends Entry<? extends BuilderFieldReference, Integer>> getItems() {
      return new BuilderMapEntryCollection<BuilderFieldReference>(this.internedItems.values()) {
         protected int getValue(@Nonnull BuilderFieldReference key) {
            return key.index;
         }

         protected int setValue(@Nonnull BuilderFieldReference key, int value) {
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
