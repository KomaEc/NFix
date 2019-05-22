package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AbstractInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.TryCatchBlockNode;
import java.util.Iterator;

public final class SynchronizedFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      Iterator i$ = methodNode.tryCatchBlocks.iterator();

      while(i$.hasNext()) {
         TryCatchBlockNode tryCatch = (TryCatchBlockNode)i$.next();
         if (tryCatch.type == null && tryCatch.start != tryCatch.handler) {
            AbstractInsnNode to = (new SynchronizedFilter.Matcher(tryCatch.handler)).match();
            if (to != null) {
               output.ignore(tryCatch.handler, to);
            }
         }
      }

   }

   private static class Matcher extends AbstractMatcher {
      private final AbstractInsnNode start;

      private Matcher(AbstractInsnNode start) {
         this.start = start;
      }

      private AbstractInsnNode match() {
         return !this.nextIsEcj() && !this.nextIsJavac() ? null : this.cursor;
      }

      private boolean nextIsJavac() {
         this.cursor = this.start;
         this.nextIsVar(58, "t");
         this.nextIs(25);
         this.nextIs(195);
         this.nextIsVar(25, "t");
         this.nextIs(191);
         return this.cursor != null;
      }

      private boolean nextIsEcj() {
         this.cursor = this.start;
         this.nextIs(25);
         this.nextIs(195);
         this.nextIs(191);
         return this.cursor != null;
      }

      // $FF: synthetic method
      Matcher(AbstractInsnNode x0, Object x1) {
         this(x0);
      }
   }
}
