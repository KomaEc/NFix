package org.jf.dexlib2.builder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
import org.jf.dexlib2.iface.reference.TypeReference;

public abstract class BuilderExceptionHandler extends BaseExceptionHandler {
   @Nonnull
   protected final Label handler;

   private BuilderExceptionHandler(@Nonnull Label handler) {
      this.handler = handler;
   }

   @Nonnull
   public Label getHandler() {
      return this.handler;
   }

   static BuilderExceptionHandler newExceptionHandler(@Nullable final TypeReference exceptionType, @Nonnull Label handler) {
      return exceptionType == null ? newExceptionHandler(handler) : new BuilderExceptionHandler(handler) {
         @Nullable
         public String getExceptionType() {
            return exceptionType.getType();
         }

         public int getHandlerCodeAddress() {
            return this.handler.getCodeAddress();
         }

         @Nullable
         public TypeReference getExceptionTypeReference() {
            return exceptionType;
         }
      };
   }

   static BuilderExceptionHandler newExceptionHandler(@Nonnull Label handler) {
      return new BuilderExceptionHandler(handler) {
         @Nullable
         public String getExceptionType() {
            return null;
         }

         public int getHandlerCodeAddress() {
            return this.handler.getCodeAddress();
         }
      };
   }

   static BuilderExceptionHandler newExceptionHandler(@Nullable final String exceptionType, @Nonnull Label handler) {
      return exceptionType == null ? newExceptionHandler(handler) : new BuilderExceptionHandler(handler) {
         @Nullable
         public String getExceptionType() {
            return exceptionType;
         }

         public int getHandlerCodeAddress() {
            return this.handler.getCodeAddress();
         }
      };
   }

   // $FF: synthetic method
   BuilderExceptionHandler(Label x0, Object x1) {
      this(x0);
   }
}
