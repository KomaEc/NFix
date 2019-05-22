package org.jf.dexlib2.writer.pool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.writer.TypeSection;

public class TypePool extends StringTypeBasePool implements TypeSection<CharSequence, CharSequence, TypeReference> {
   public TypePool(@Nonnull DexPool dexPool) {
      super(dexPool);
   }

   public void intern(@Nonnull CharSequence type) {
      String typeString = type.toString();
      Integer prev = (Integer)this.internedItems.put(typeString, 0);
      if (prev == null) {
         ((StringPool)this.dexPool.stringSection).intern(typeString);
      }

   }

   public void internNullable(@Nullable CharSequence type) {
      if (type != null) {
         this.intern(type);
      }

   }

   public int getItemIndex(@Nonnull TypeReference key) {
      return this.getItemIndex(key.getType());
   }

   @Nonnull
   public CharSequence getString(@Nonnull CharSequence type) {
      return type;
   }
}
