package org.jf.dexlib2.dexbacked.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.reference.Reference;
import org.jf.util.ExceptionWithContext;

public abstract class DexBackedReference {
   public static Reference makeReference(@Nonnull DexBackedDexFile dexFile, int referenceType, int referenceIndex) {
      switch(referenceType) {
      case 0:
         return new DexBackedStringReference(dexFile, referenceIndex);
      case 1:
         return new DexBackedTypeReference(dexFile, referenceIndex);
      case 2:
         return new DexBackedFieldReference(dexFile, referenceIndex);
      case 3:
         return new DexBackedMethodReference(dexFile, referenceIndex);
      case 4:
         return new DexBackedMethodProtoReference(dexFile, referenceIndex);
      default:
         throw new ExceptionWithContext("Invalid reference type: %d", new Object[]{referenceType});
      }
   }
}
