package org.jf.dexlib2.writer.builder;

import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;

public class BuilderExceptionHandler extends BaseExceptionHandler {
   @Nullable
   final BuilderTypeReference exceptionType;
   final int handlerCodeAddress;

   BuilderExceptionHandler(@Nullable BuilderTypeReference exceptionType, int handlerCodeAddress) {
      this.exceptionType = exceptionType;
      this.handlerCodeAddress = handlerCodeAddress;
   }

   @Nullable
   public String getExceptionType() {
      return this.exceptionType == null ? null : this.exceptionType.getType();
   }

   public int getHandlerCodeAddress() {
      return this.handlerCodeAddress;
   }
}
