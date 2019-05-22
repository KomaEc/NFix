package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.writer.FieldSection;

public class FieldPool extends BaseIndexPool<FieldReference> implements FieldSection<CharSequence, CharSequence, FieldReference, Field> {
   public FieldPool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull FieldReference field) {
      Integer prev = (Integer)this.internedItems.put(field, 0);
      if (prev == null) {
         ((TypePool)this.dexPool.typeSection).intern(field.getDefiningClass());
         ((StringPool)this.dexPool.stringSection).intern(field.getName());
         ((TypePool)this.dexPool.typeSection).intern(field.getType());
      }

   }

   @Nonnull
   public CharSequence getDefiningClass(@Nonnull FieldReference fieldReference) {
      return fieldReference.getDefiningClass();
   }

   @Nonnull
   public CharSequence getFieldType(@Nonnull FieldReference fieldReference) {
      return fieldReference.getType();
   }

   @Nonnull
   public CharSequence getName(@Nonnull FieldReference fieldReference) {
      return fieldReference.getName();
   }

   public int getFieldIndex(@Nonnull Field field) {
      return this.getItemIndex(field);
   }
}
