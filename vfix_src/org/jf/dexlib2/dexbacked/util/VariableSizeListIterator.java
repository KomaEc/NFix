package org.jf.dexlib2.dexbacked.util;

import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public abstract class VariableSizeListIterator<T> implements ListIterator<T> {
   @Nonnull
   private DexReader reader;
   protected final int size;
   private final int startOffset;
   private int index;

   protected VariableSizeListIterator(@Nonnull DexBackedDexFile dexFile, int offset, int size) {
      this.reader = dexFile.readerAt(offset);
      this.startOffset = offset;
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

   public boolean hasPrevious() {
      return this.index > 0;
   }

   public T previous() {
      int targetIndex = this.index - 1;
      this.reader.setOffset(this.startOffset);
      this.index = 0;

      while(this.index < targetIndex) {
         this.readNextItem(this.reader, this.index++);
      }

      return this.readNextItem(this.reader, this.index++);
   }

   public int nextIndex() {
      return this.index;
   }

   public int previousIndex() {
      return this.index - 1;
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public void set(T t) {
      throw new UnsupportedOperationException();
   }

   public void add(T t) {
      throw new UnsupportedOperationException();
   }
}
