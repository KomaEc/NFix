package org.jf.dexlib2.dexbacked.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.reference.DexBackedMethodReference;
import org.jf.dexlib2.iface.reference.MethodReference;

public class DexBackedMethodEncodedValue extends BaseMethodEncodedValue {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int MethodIndex;

   public DexBackedMethodEncodedValue(@Nonnull DexReader reader, int valueArg) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.MethodIndex = reader.readSizedSmallUint(valueArg + 1);
   }

   @Nonnull
   public MethodReference getValue() {
      return new DexBackedMethodReference(this.dexFile, this.MethodIndex);
   }
}
