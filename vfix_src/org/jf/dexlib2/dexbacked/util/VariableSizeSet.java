package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractSet;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public abstract class VariableSizeSet<T> extends AbstractSet<T> {
   @Nonnull
   private final DexBackedDexFile dexFile;
   private final int offset;
   private final int size;

   public VariableSizeSet(@Nonnull DexBackedDexFile dexFile, int offset, int size) {
      this.dexFile = dexFile;
      this.offset = offset;
      this.size = size;
   }

   protected abstract T readNextItem(@Nonnull DexReader var1, int var2);

   @Nonnull
   public VariableSizeIterator<T> iterator() {
      return new VariableSizeIterator<T>(this.dexFile, this.offset, this.size) {
         protected T readNextItem(@Nonnull DexReader reader, int index) {
            return VariableSizeSet.this.readNextItem(reader, index);
         }
      };
   }

   public int size() {
      return this.size;
   }
}
