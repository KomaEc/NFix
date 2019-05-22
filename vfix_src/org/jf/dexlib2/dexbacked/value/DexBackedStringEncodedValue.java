package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;

public class DexBackedStringEncodedValue extends BaseStringEncodedValue {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int stringIndex;

   public DexBackedStringEncodedValue(@Nonnull DexReader reader, int valueArg) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.stringIndex = reader.readSizedSmallUint(valueArg + 1);
   }

   @Nonnull
   public String getValue() {
      return this.dexFile.getString(this.stringIndex);
   }
}
