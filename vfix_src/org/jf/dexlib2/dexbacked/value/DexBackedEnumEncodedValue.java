package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseEnumEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.reference.DexBackedFieldReference;
import org.jf.dexlib2.iface.reference.FieldReference;

public class DexBackedEnumEncodedValue extends BaseEnumEncodedValue {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int fieldIndex;

   public DexBackedEnumEncodedValue(@Nonnull DexReader reader, int valueArg) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.fieldIndex = reader.readSizedSmallUint(valueArg + 1);
   }

   @Nonnull
   public FieldReference getValue() {
      return new DexBackedFieldReference(this.dexFile, this.fieldIndex);
   }
}
