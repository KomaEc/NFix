package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.reference.BaseFieldReference;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;

public class DexBackedFieldReference extends BaseFieldReference {
   @Nonnull
   public final DexBackedDexFile dexFile;
   public final int fieldIdItemOffset;

   public DexBackedFieldReference(@Nonnull DexBackedDexFile dexFile, int fieldIndex) {
      this.dexFile = dexFile;
      this.fieldIdItemOffset = dexFile.getFieldIdItemOffset(fieldIndex);
   }

   @Nonnull
   public String getDefiningClass() {
      return this.dexFile.getType(this.dexFile.readUshort(this.fieldIdItemOffset + 0));
   }

   @Nonnull
   public String getName() {
      return this.dexFile.getString(this.dexFile.readSmallUint(this.fieldIdItemOffset + 4));
   }

   @Nonnull
   public String getType() {
      return this.dexFile.getType(this.dexFile.readUshort(this.fieldIdItemOffset + 2));
   }

   public int getSize() {
      return 8;
   }
}
