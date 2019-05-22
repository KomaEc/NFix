package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.AbstractInsnMutator;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import java.util.HashMap;
import java.util.Map;

class ReturnValsMethodVisitor extends AbstractInsnMutator {
   private static final Map<Integer, ZeroOperandMutation> MUTATIONS = new HashMap();

   public ReturnValsMethodVisitor(MethodMutatorFactory factory, MethodInfo methodInfo, MutationContext context, MethodVisitor writer) {
      super(factory, methodInfo, context, writer);
   }

   private static ZeroOperandMutation areturnMutation() {
      return new ZeroOperandMutation() {
         public void apply(int opCode, MethodVisitor mv) {
            Label l1 = new Label();
            mv.visitJumpInsn(199, l1);
            mv.visitTypeInsn(187, "java/lang/RuntimeException");
            mv.visitInsn(89);
            mv.visitMethodInsn(183, "java/lang/RuntimeException", "<init>", "()V", false);
            mv.visitInsn(191);
            mv.visitLabel(l1);
            mv.visitInsn(1);
            mv.visitInsn(176);
         }

         public String decribe(int opCode, MethodInfo methodInfo) {
            return "mutated return of Object value for " + methodInfo.getDescription() + " to ( if (x != null) null else throw new RuntimeException )";
         }
      };
   }

   private static ZeroOperandMutation lreturnMutation() {
      return new ZeroOperandMutation() {
         public void apply(int opcode, MethodVisitor mv) {
            mv.visitInsn(10);
            mv.visitInsn(97);
            mv.visitInsn(opcode);
         }

         public String decribe(int opCode, MethodInfo methodInfo) {
            return "replaced return of long value with value + 1 for " + methodInfo.getDescription();
         }
      };
   }

   private static ZeroOperandMutation freturnMutation() {
      return new ZeroOperandMutation() {
         public void apply(int opcode, MethodVisitor mv) {
            mv.visitInsn(89);
            mv.visitInsn(89);
            mv.visitInsn(150);
            Label l1 = new Label();
            mv.visitJumpInsn(153, l1);
            mv.visitInsn(87);
            mv.visitInsn(11);
            mv.visitLabel(l1);
            mv.visitInsn(12);
            mv.visitInsn(98);
            mv.visitInsn(118);
            mv.visitInsn(174);
         }

         public String decribe(int opCode, MethodInfo methodInfo) {
            return "replaced return of float value with -(x + 1) for " + methodInfo.getDescription();
         }
      };
   }

   private static ZeroOperandMutation dreturnMutation() {
      return new ZeroOperandMutation() {
         public void apply(int opCode, MethodVisitor mv) {
            mv.visitInsn(92);
            mv.visitInsn(92);
            mv.visitInsn(152);
            Label l1 = new Label();
            mv.visitJumpInsn(153, l1);
            mv.visitInsn(88);
            mv.visitInsn(14);
            mv.visitLabel(l1);
            mv.visitInsn(15);
            mv.visitInsn(99);
            mv.visitInsn(119);
            mv.visitInsn(175);
         }

         public String decribe(int opCode, MethodInfo methodInfo) {
            return "replaced return of double value with -(x + 1) for " + methodInfo.getDescription();
         }
      };
   }

   private static ZeroOperandMutation ireturnMutation() {
      return new ZeroOperandMutation() {
         public void apply(int opCode, MethodVisitor mv) {
            Label l1 = new Label();
            mv.visitJumpInsn(153, l1);
            mv.visitInsn(3);
            mv.visitInsn(172);
            mv.visitLabel(l1);
            mv.visitInsn(4);
            mv.visitInsn(172);
         }

         public String decribe(int opCode, MethodInfo methodInfo) {
            return "replaced return of integer sized value with (x == 0 ? 1 : 0)";
         }
      };
   }

   protected Map<Integer, ZeroOperandMutation> getMutations() {
      return MUTATIONS;
   }

   static {
      MUTATIONS.put(172, ireturnMutation());
      MUTATIONS.put(175, dreturnMutation());
      MUTATIONS.put(174, freturnMutation());
      MUTATIONS.put(173, lreturnMutation());
      MUTATIONS.put(176, areturnMutation());
   }
}
