package org.jf.dexlib2.dexbacked.value;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.dexbacked.util.VariableSizeList;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;

public class DexBackedArrayEncodedValue extends BaseArrayEncodedValue implements ArrayEncodedValue {
   @Nonnull
   public final DexBackedDexFile dexFile;
   private final int elementCount;
   private final int encodedArrayOffset;

   public DexBackedArrayEncodedValue(@Nonnull DexReader reader) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.elementCount = reader.readSmallUleb128();
      this.encodedArrayOffset = reader.getOffset();
      skipElementsFrom(reader, this.elementCount);
   }

   public static void skipFrom(@Nonnull DexReader reader) {
      int elementCount = reader.readSmallUleb128();
      skipElementsFrom(reader, elementCount);
   }

   private static void skipElementsFrom(@Nonnull DexReader reader, int elementCount) {
      for(int i = 0; i < elementCount; ++i) {
         DexBackedEncodedValue.skipFrom(reader);
      }

   }

   @Nonnull
   public List<? extends EncodedValue> getValue() {
      return new VariableSizeList<EncodedValue>(this.dexFile, this.encodedArrayOffset, this.elementCount) {
         @Nonnull
         protected EncodedValue readNextItem(@Nonnull DexReader dexReader, int index) {
            return DexBackedEncodedValue.readFrom(dexReader);
         }
      };
   }
}
