package org.jf.dexlib2.rewriter;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.BaseTryBlock;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.TryBlock;

public class TryBlockRewriter implements Rewriter<TryBlock<? extends ExceptionHandler>> {
   @Nonnull
   protected final Rewriters rewriters;

   public TryBlockRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public TryBlock<? extends ExceptionHandler> rewrite(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
      return new TryBlockRewriter.RewrittenTryBlock(tryBlock);
   }

   protected class RewrittenTryBlock extends BaseTryBlock<ExceptionHandler> {
      @Nonnull
      protected TryBlock<? extends ExceptionHandler> tryBlock;

      public RewrittenTryBlock(@Nonnull TryBlock<? extends ExceptionHandler> tryBlock) {
         this.tryBlock = tryBlock;
      }

      public int getStartCodeAddress() {
         return this.tryBlock.getStartCodeAddress();
      }

      public int getCodeUnitCount() {
         return this.tryBlock.getCodeUnitCount();
      }

      @Nonnull
      public List<? extends ExceptionHandler> getExceptionHandlers() {
         return RewriterUtils.rewriteList(TryBlockRewriter.this.rewriters.getExceptionHandlerRewriter(), this.tryBlock.getExceptionHandlers());
      }
   }
}
