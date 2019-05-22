package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.MethodInsnNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.VarInsnNode;

public final class PrivateEmptyNoArgConstructorFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if ((methodNode.access & 2) != 0 && "<init>".equals(methodNode.name) && "()V".equals(methodNode.desc) && (new PrivateEmptyNoArgConstructorFilter.Matcher()).match(methodNode, superClassName)) {
         output.ignore(methodNode.instructions.getFirst(), methodNode.instructions.getLast());
      }

   }

   private static class Matcher extends AbstractMatcher {
      private Matcher() {
      }

      private boolean match(MethodNode methodNode, String superClassName) {
         this.cursor = methodNode.instructions.getFirst();
         this.skipNonOpcodes();
         if (this.cursor.getOpcode() == 25 && ((VarInsnNode)this.cursor).var == 0) {
            this.nextIs(183);
            MethodInsnNode m = (MethodInsnNode)this.cursor;
            if (m != null && superClassName.equals(m.owner) && "<init>".equals(m.name) && "()V".equals(m.desc)) {
               this.nextIs(177);
               return this.cursor != null;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      Matcher(Object x0) {
         this();
      }
   }
}
