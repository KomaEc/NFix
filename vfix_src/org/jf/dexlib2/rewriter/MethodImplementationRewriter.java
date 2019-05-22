package org.jf.dexlib2.rewriter;

import java.util.List;
import javax.annotation.Nonnull;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.TryBlock;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.instruction.Instruction;

public class MethodImplementationRewriter implements Rewriter<MethodImplementation> {
   @Nonnull
   protected final Rewriters rewriters;

   public MethodImplementationRewriter(@Nonnull Rewriters rewriters) {
      this.rewriters = rewriters;
   }

   @Nonnull
   public MethodImplementation rewrite(@Nonnull MethodImplementation methodImplementation) {
      return new MethodImplementationRewriter.RewrittenMethodImplementation(methodImplementation);
   }

   protected class RewrittenMethodImplementation implements MethodImplementation {
      @Nonnull
      protected MethodImplementation methodImplementation;

      public RewrittenMethodImplementation(@Nonnull MethodImplementation methodImplementation) {
         this.methodImplementation = methodImplementation;
      }

      public int getRegisterCount() {
         return this.methodImplementation.getRegisterCount();
      }

      @Nonnull
      public Iterable<? extends Instruction> getInstructions() {
         return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getInstructionRewriter(), this.methodImplementation.getInstructions());
      }

      @Nonnull
      public List<? extends TryBlock<? extends ExceptionHandler>> getTryBlocks() {
         return RewriterUtils.rewriteList(MethodImplementationRewriter.this.rewriters.getTryBlockRewriter(), this.methodImplementation.getTryBlocks());
      }

      @Nonnull
      public Iterable<? extends DebugItem> getDebugItems() {
         return RewriterUtils.rewriteIterable(MethodImplementationRewriter.this.rewriters.getDebugItemRewriter(), this.methodImplementation.getDebugItems());
      }
   }
}
