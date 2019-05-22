package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public class DexBackedTypeEncodedValue extends BaseTypeEncodedValue {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int typeIndex;

   public DexBackedTypeEncodedValue(@Nonnull DexReader reader, int valueArg) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.typeIndex = reader.readSizedSmallUint(valueArg + 1);
   }

   @Nonnull
   public String getValue() {
      return this.dexFile.getType(this.typeIndex);
   }
}
