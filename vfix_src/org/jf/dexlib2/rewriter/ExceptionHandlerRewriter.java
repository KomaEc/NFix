package org.jf.dexlib2.rewriter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.base.BaseExceptionHandler;
import org.jf.dexlib2.iface.ExceptionHandler;

public class ExceptionHandlerRewriter implements Rewriter<ExceptionHandler> {
   @Nonnull
   protected final Rewriters rewriters;

   public ExceptionHandlerRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public ExceptionHandler rewrite(@Nonnull ExceptionHandler value) {
      return new ExceptionHandlerRewriter.RewrittenExceptionHandler(value);
   }

   protected class RewrittenExceptionHandler extends BaseExceptionHandler {
      @Nonnull
      protected ExceptionHandler exceptionHandler;

      public RewrittenExceptionHandler(@Nonnull ExceptionHandler exceptionHandler) {
         this.exceptionHandler = exceptionHandler;
      }

      @Nullable
      public String getExceptionType() {
         return (String)RewriterUtils.rewriteNullable(ExceptionHandlerRewriter.this.rewriters.getTypeRewriter(), this.exceptionHandler.getExceptionType());
      }

      public int getHandlerCodeAddress() {
         return this.exceptionHandler.getHandlerCodeAddress();
      }
   }
}
