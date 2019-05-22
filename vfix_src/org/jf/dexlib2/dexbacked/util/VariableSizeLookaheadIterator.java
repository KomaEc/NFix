package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public abstract class VariableSizeLookaheadIterator<T> extends AbstractIterator<T> implements Iterator<T> {
   @Nonnull
   private final DexReader reader;

   protected VariableSizeLookaheadIterator(@Nonnull DexBackedDexFile dexFile, int offset) {
      this.reader = dexFile.readerAt(offset);
   }

   @Nullable
   protected abstract T readNextItem(@Nonnull DexReader var1);

   protected T computeNext() {
      return this.readNextItem(this.reader);
   }

   public final int getReaderOffset() {
      return this.reader.getOffset();
   }
}
