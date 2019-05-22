package org.jf.dexlib2.dexbacked;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DexBackedCatchAllExceptionHandler extends DexBackedExceptionHandler {
   private final int handlerCodeAddress;

   public DexBackedCatchAllExceptionHandler(@Nonnull DexReader reader) {
      this.handlerCodeAddress = reader.readSmallUleb128();
   }

   @Nullable
   public String getExceptionType() {
      return null;
   }

   public int getHandlerCodeAddress() {
      return this.handlerCodeAddress;
   }
}
