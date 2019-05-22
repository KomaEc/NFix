package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;

public class DexBackedTypedExceptionHandler extends DexBackedExceptionHandler {
   @Nonnull
   private final DexBackedDexFile dexFile;
   private final int typeId;
   private final int handlerCodeAddress;

   public DexBackedTypedExceptionHandler(@Nonnull DexReader reader) {
      this.dexFile = (DexBackedDexFile)reader.dexBuf;
      this.typeId = reader.readSmallUleb128();
      this.handlerCodeAddress = reader.readSmallUleb128();
   }

   @Nonnull
   public String getExceptionType() {
      return this.dexFile.getType(this.typeId);
   }

   public int getHandlerCodeAddress() {
      return this.handlerCodeAddress;
   }
}
