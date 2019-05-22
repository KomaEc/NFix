package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractSequentialList;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public abstract class VariableSizeList<T> extends AbstractSequentialList<T> {
   @Nonnull
   private final DexBackedDexFile dexFile;
   private final int offset;
   private final int size;

   public VariableSizeList(@Nonnull DexBackedDexFile dexFile, int offset, int size) {
      this.dexFile = dexFile;
      this.offset = offset;
      this.size = size;
   }

   protected abstract T readNextItem(@Nonnull DexReader var1, int var2);

   @Nonnull
   public VariableSizeListIterator<T> listIterator() {
      return this.listIterator(0);
   }

   public int size() {
      return this.size;
   }

   @Nonnull
   public VariableSizeListIterator<T> listIterator(int index) {
      VariableSizeListIterator<T> iterator = new VariableSizeListIterator<T>(this.dexFile, this.offset, this.size) {
         protected T readNextItem(@Nonnull DexReader reader, int index) {
            return VariableSizeList.this.readNextItem(reader, index);
         }
      };

      for(int i = 0; i < index; ++i) {
         iterator.next();
      }

      return iterator;
   }
}
