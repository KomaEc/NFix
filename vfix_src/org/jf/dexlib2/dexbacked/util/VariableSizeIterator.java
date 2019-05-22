package org.jf.dexlib2.dexbacked.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public abstract class VariableSizeIterator<T> implements Iterator<T> {
   @Nonnull
   private final DexReader reader;
   protected final int size;
   private int index;

   protected VariableSizeIterator(@Nonnull DexBackedDexFile dexFile, int offset, int size) {
      this.reader = dexFile.readerAt(offset);
      this.size = size;
   }

   protected VariableSizeIterator(@Nonnull DexReader reader, int size) {
      this.reader = reader;
      this.size = size;
   }

   protected abstract T readNextItem(@Nonnull DexReader var1, int var2);

   public int getReaderOffset() {
      return this.reader.getOffset();
   }

   public boolean hasNext() {
      return this.index < this.size;
   }

   public T next() {
      if (this.index >= this.size) {
         throw new NoSuchElementException();
      } else {
         return this.readNextItem(this.reader, this.index++);
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }
}
