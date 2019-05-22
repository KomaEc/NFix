package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseAnnotationElement;
import org.jf.dexlib2.dexbacked.value.DexBackedEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public class DexBackedAnnotationElement extends BaseAnnotationElement {
   @Nonnull
   private final DexBackedDexFile dexFile;
   public final int nameIndex;
   @Nonnull
   public final EncodedValue value;

   public DexBackedAnnotationElement(@Nonnull DexReader reader) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.nameIndex = reader.readSmallUleb128();
      this.value = DexBackedEncodedValue.readFrom(reader);
   }

   @Nonnull
   public String getName() {
      return this.dexFile.getString(this.nameIndex);
   }

   @Nonnull
   public EncodedValue getValue() {
      return this.value;
   }
}
