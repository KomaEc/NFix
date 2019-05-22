package org.jf.dexlib2.immutable;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.util.ImmutableConverter;

public class ImmutableExceptionHandler extends BaseExceptionHandler implements ExceptionHandler {
   @Nullable
   protected final String exceptionType;
   protected final int handlerCodeAddress;
   private static final ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler> CONVERTER = new ImmutableConverter<ImmutableExceptionHandler, ExceptionHandler>() {
      protected boolean isImmutable(@Nonnull ExceptionHandler item) {
         return item instanceof ImmutableExceptionHandler;
      }

      @Nonnull
      protected ImmutableExceptionHandler makeImmutable(@Nonnull ExceptionHandler item) {
         return ImmutableExceptionHandler.of(item);
      }
   };

   public ImmutableExceptionHandler(@Nullable String exceptionType, int handlerCodeAddress) {
      this.exceptionType = exceptionType;
      this.handlerCodeAddress = handlerCodeAddress;
   }

   public static ImmutableExceptionHandler of(ExceptionHandler exceptionHandler) {
      return exceptionHandler instanceof ImmutableExceptionHandler ? (ImmutableExceptionHandler)exceptionHandler : new ImmutableExceptionHandler(exceptionHandler.getExceptionType(), exceptionHandler.getHandlerCodeAddress());
   }

   @Nullable
   public String getExceptionType() {
      return this.exceptionType;
   }

   public int getHandlerCodeAddress() {
      return this.handlerCodeAddress;
   }

   @Nonnull
   public static ImmutableList<ImmutableExceptionHandler> immutableListOf(@Nullable Iterable<? extends ExceptionHandler> list) {
      return CONVERTER.toList(list);
   }
}
