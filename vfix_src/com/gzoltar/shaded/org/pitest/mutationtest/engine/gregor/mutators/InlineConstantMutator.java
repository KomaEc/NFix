package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.util.PitError;

public class InlineConstantMutator implements MethodMutatorFactory {
   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new InlineConstantMutator.InlineConstantVisitor(context, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String toString() {
      return "INLINE_CONSTANT_MUTATOR";
   }

   public String getName() {
      return this.toString();
   }

   private final class InlineConstantVisitor extends MethodVisitor {
      private final MutationContext context;

      public InlineConstantVisitor(MutationContext context, MethodVisitor delegateVisitor) {
         super(327680, delegateVisitor);
         this.context = context;
      }

      private void mutate(Double constant) {
         Double replacement = constant == 1.0D ? 2.0D : 1.0D;
         if (this.shouldMutate(constant, replacement)) {
            this.translateToByteCode(replacement);
         } else {
            this.translateToByteCode(constant);
         }

      }

      private void mutate(Float constant) {
         Float replacement = constant == 1.0F ? 2.0F : 1.0F;
         if (this.shouldMutate(constant, replacement)) {
            this.translateToByteCode(replacement);
         } else {
            this.translateToByteCode(constant);
         }

      }

      private void mutate(Integer constant) {
         Integer replacement;
         switch(constant) {
         case 1:
            replacement = 0;
            break;
         case 127:
            replacement = -128;
            break;
         case 32767:
            replacement = -32768;
            break;
         default:
            replacement = constant + 1;
         }

         if (this.shouldMutate(constant, replacement)) {
            this.translateToByteCode(replacement);
         } else {
            this.translateToByteCode(constant);
         }

      }

      private void mutate(Long constant) {
         Long replacement = constant + 1L;
         if (this.shouldMutate(constant, replacement)) {
            this.translateToByteCode(replacement);
         } else {
            this.translateToByteCode(constant);
         }

      }

      private void mutate(Number constant) {
         if (constant instanceof Integer) {
            this.mutate((Integer)constant);
         } else if (constant instanceof Long) {
            this.mutate((Long)constant);
         } else if (constant instanceof Float) {
            this.mutate((Float)constant);
         } else {
            if (!(constant instanceof Double)) {
               throw new PitError("Unsupported subtype of Number found:" + constant.getClass());
            }

            this.mutate((Double)constant);
         }

      }

      private <T extends Number> boolean shouldMutate(T constant, T replacement) {
         MutationIdentifier mutationId = this.context.registerMutation(InlineConstantMutator.this, "Substituted " + constant + " with " + replacement);
         return this.context.shouldMutate(mutationId);
      }

      private void translateToByteCode(Double constant) {
         if (constant == 0.0D) {
            super.visitInsn(14);
         } else if (constant == 1.0D) {
            super.visitInsn(15);
         } else {
            super.visitLdcInsn(constant);
         }

      }

      private void translateToByteCode(Float constant) {
         if (constant == 0.0F) {
            super.visitInsn(11);
         } else if (constant == 1.0F) {
            super.visitInsn(12);
         } else if (constant == 2.0F) {
            super.visitInsn(13);
         } else {
            super.visitLdcInsn(constant);
         }

      }

      private void translateToByteCode(Integer constant) {
         switch(constant) {
         case -1:
            super.visitInsn(2);
            break;
         case 0:
            super.visitInsn(3);
            break;
         case 1:
            super.visitInsn(4);
            break;
         case 2:
            super.visitInsn(5);
            break;
         case 3:
            super.visitInsn(6);
            break;
         case 4:
            super.visitInsn(7);
            break;
         case 5:
            super.visitInsn(8);
            break;
         default:
            super.visitLdcInsn(constant);
         }

      }

      private void translateToByteCode(Long constant) {
         if (constant == 0L) {
            super.visitInsn(9);
         } else if (constant == 1L) {
            super.visitInsn(10);
         } else {
            super.visitLdcInsn(constant);
         }

      }

      private Number translateToNumber(int opcode) {
         switch(opcode) {
         case 2:
            return -1;
         case 3:
            return 0;
         case 4:
            return 1;
         case 5:
            return 2;
         case 6:
            return 3;
         case 7:
            return 4;
         case 8:
            return 5;
         case 9:
            return 0L;
         case 10:
            return 1L;
         case 11:
            return 0.0F;
         case 12:
            return 1.0F;
         case 13:
            return 2.0F;
         case 14:
            return 0.0D;
         case 15:
            return 1.0D;
         default:
            return null;
         }
      }

      public void visitInsn(int opcode) {
         Number inlineConstant = this.translateToNumber(opcode);
         if (inlineConstant == null) {
            super.visitInsn(opcode);
         } else {
            this.mutate(inlineConstant);
         }
      }

      public void visitIntInsn(int opcode, int operand) {
         if (opcode != 16 && opcode != 17) {
            super.visitIntInsn(opcode, operand);
         } else {
            this.mutate(operand);
         }

      }

      public void visitLdcInsn(Object constant) {
         if (constant instanceof Number) {
            this.mutate((Number)constant);
         } else {
            super.visitLdcInsn(constant);
         }

      }
   }
}
