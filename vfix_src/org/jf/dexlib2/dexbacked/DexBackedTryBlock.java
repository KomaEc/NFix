package org.jf.dexlib2.dexbacked;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.dexbacked.util.VariableSizeList;

public class DexBackedTryBlock extends BaseTryBlock<DexBackedExceptionHandler> {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int tryItemOffset;
   private final int handlersStartOffset;

   public DexBackedTryBlock(@Nonnull DexBackedDexFile dexFile, int tryItemOffset, int handlersStartOffset) {
      this.dexFile = dexFile;
      this.tryItemOffset = tryItemOffset;
      this.handlersStartOffset = handlersStartOffset;
   }

   public int getStartCodeAddress() {
      return this.dexFile.readSmallUint(this.tryItemOffset + 0);
   }

   public int getCodeUnitCount() {
      return this.dexFile.readUshort(this.tryItemOffset + 4);
   }

   @Nonnull
   public List<? extends DexBackedExceptionHandler> getExceptionHandlers() {
      DexReader reader = this.dexFile.readerAt(this.handlersStartOffset + this.dexFile.readUshort(this.tryItemOffset + 6));
      int encodedSize = reader.readSleb128();
      if (encodedSize > 0) {
         return new VariableSizeList<DexBackedTypedExceptionHandler>(this.dexFile, reader.getOffset(), encodedSize) {
            @Nonnull
            protected DexBackedTypedExceptionHandler readNextItem(@Nonnull DexReader reader, int index) {
               return new DexBackedTypedExceptionHandler(reader);
            }
         };
      } else {
         final int sizeWithCatchAll = -1 * encodedSize + 1;
         return new VariableSizeList<DexBackedExceptionHandler>(this.dexFile, reader.getOffset(), sizeWithCatchAll) {
            @Nonnull
            protected DexBackedExceptionHandler readNextItem(@Nonnull DexReader dexReader, int index) {
               return (DexBackedExceptionHandler)(index == sizeWithCatchAll - 1 ? new DexBackedCatchAllExceptionHandler(dexReader) : new DexBackedTypedExceptionHandler(dexReader));
            }
         };
      }
   }
}
