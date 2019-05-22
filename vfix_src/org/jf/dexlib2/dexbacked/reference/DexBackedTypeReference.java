package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;

public class DexBackedTypeReference extends BaseTypeReference {
   @Nonnull
   public final DexBackedDexFile dexFile;
   public final int typeIndex;

   public DexBackedTypeReference(@Nonnull DexBackedDexFile dexFile, int typeIndex) {
      this.dexFile = dexFile;
      this.typeIndex = typeIndex;
   }

   @Nonnull
   public String getType() {
      return this.dexFile.getType(this.typeIndex);
   }

   public int getSize() {
      return 4;
   }
}
