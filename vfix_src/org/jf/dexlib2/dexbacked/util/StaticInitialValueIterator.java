package org.jf.dexlib2.dexbacked.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public abstract class StaticInitialValueIterator {
   public static final StaticInitialValueIterator EMPTY = new StaticInitialValueIterator() {
      @Nullable
      public EncodedValue getNextOrNull() {
         return null;
      }

      public void skipNext() {
      }

      public int getReaderOffset() {
         return 0;
      }
   };

   @Nullable
   public abstract EncodedValue getNextOrNull();

   public abstract void skipNext();

   public abstract int getReaderOffset();

   @Nonnull
   public static StaticInitialValueIterator newOrEmpty(@Nonnull DexBackedDexFile dexFile, int offset) {
      return (StaticInitialValueIterator)(offset == 0 ? EMPTY : new StaticInitialValueIterator.StaticInitialValueIteratorImpl(dexFile, offset));
   }

   private static class StaticInitialValueIteratorImpl extends StaticInitialValueIterator {
      @Nonnull
      private final DexReader reader;
      private final int size;
      private int index = 0;

      public StaticInitialValueIteratorImpl(@Nonnull DexBackedDexFile dexFile, int offset) {
         this.reader = dexFile.readerAt(offset);
         this.size = this.reader.readSmallUleb128();
      }

      @Nullable
      public EncodedValue getNextOrNull() {
         if (this.index < this.size) {
            ++this.index;
            return DexBackedEncodedValue.readFrom(this.reader);
         } else {
            return null;
         }
      }

      public void skipNext() {
         if (this.index < this.size) {
            ++this.index;
            DexBackedEncodedValue.skipFrom(this.reader);
         }

      }

      public int getReaderOffset() {
         return this.reader.getOffset();
      }
   }
}
