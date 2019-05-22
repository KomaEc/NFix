package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseStringReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public class DexBackedStringReference extends BaseStringReference {
   @Nonnull
   public final DexBackedDexFile dexFile;
   public final int stringIndex;

   public DexBackedStringReference(@Nonnull DexBackedDexFile dexBuf, int stringIndex) {
      this.dexFile = dexBuf;
      this.stringIndex = stringIndex;
   }

   @Nonnull
   public String getString() {
      return this.dexFile.getString(this.stringIndex);
   }

   public int getSize() {
      int size = 4;
      int stringOffset = this.dexFile.getStringIdItemOffset(this.stringIndex);
      int stringDataOffset = this.dexFile.readSmallUint(stringOffset);
      DexReader reader = this.dexFile.readerAt(stringDataOffset);
      int size = size + reader.peekSmallUleb128Size();
      int utf16Length = reader.readSmallUleb128();
      size += reader.peekStringLength(utf16Length);
      return size;
   }
}
