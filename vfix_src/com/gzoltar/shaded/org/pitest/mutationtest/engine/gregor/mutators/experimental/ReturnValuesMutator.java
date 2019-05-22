package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators.experimental;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.Label;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;

public class ReturnValuesMutator implements MethodMutatorFactory {
   private static final ReturnValuesMutator.ObjectMutationMethod OBJECT_MUTATION_METHOD = new ReturnValuesMutator.ObjectMutationMethod();
   private static final ReturnValuesMutator.ObjectReferenceReplacer SINGLETON_REPLACER = new ReturnValuesMutator.ObjectReferenceReplacer();

   public static Object mutateObjectInstance(Object object, Class<?> clazz) {
      return SINGLETON_REPLACER.replaceObjectInstance(object, clazz);
   }

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new ReturnValuesMutator.ReturnValuesMethodVisitor(context, methodInfo, methodVisitor);
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String getName() {
      return "EXPERIMENTAL_RETURN_VALUES_MUTATOR";
   }

   private final class ReturnValuesMethodVisitor extends MethodVisitor {
      private static final String DESCRIPTION_MESSAGE_PATTERN = "replaced return of %s value with %s";
      private final MutationContext context;
      private final MethodInfo methodInfo;

      private ReturnValuesMethodVisitor(MutationContext context, MethodInfo methodInfo, MethodVisitor delegateVisitor) {
         super(327680, delegateVisitor);
         this.context = context;
         this.methodInfo = methodInfo;
      }

      private void mutateObjectReferenceReturn() {
         if (this.shouldMutate("object reference", "[see docs for details]")) {
            Type returnType = this.methodInfo.getReturnType();
            super.visitLdcInsn(returnType);
            super.visitMethodInsn(184, ReturnValuesMutator.OBJECT_MUTATION_METHOD.getClassName(), ReturnValuesMutator.OBJECT_MUTATION_METHOD.getMethodName(), ReturnValuesMutator.OBJECT_MUTATION_METHOD.getMethodDescriptor(), false);
            super.visitTypeInsn(192, returnType.getInternalName());
         }

         super.visitInsn(176);
      }

      private void mutatePrimitiveDoubleReturn() {
         if (this.shouldMutate("primitive double", "(x != NaN)? -(x + 1) : -1 ")) {
            Label label = new Label();
            super.visitInsn(92);
            super.visitInsn(92);
            super.visitInsn(152);
            super.visitJumpInsn(153, label);
            super.visitInsn(88);
            super.visitInsn(14);
            super.visitLabel(label);
            super.visitInsn(15);
            super.visitInsn(99);
            super.visitInsn(119);
            super.visitInsn(175);
         }

      }

      private void mutatePrimitiveFloatReturn() {
         if (this.shouldMutate("primitive float", "(x != NaN)? -(x + 1) : -1 ")) {
            Label label = new Label();
            super.visitInsn(89);
            super.visitInsn(89);
            super.visitInsn(150);
            super.visitJumpInsn(153, label);
            super.visitInsn(87);
            super.visitInsn(11);
            super.visitLabel(label);
            super.visitInsn(12);
            super.visitInsn(98);
            super.visitInsn(118);
            super.visitInsn(174);
         }

      }

      private void mutatePrimitiveIntegerReturn() {
         if (this.shouldMutate("primitive boolean/byte/short/integer", "(x == 1) ? 0 : x + 1")) {
            Label label = new Label();
            super.visitInsn(89);
            super.visitInsn(4);
            super.visitJumpInsn(159, label);
            super.visitInsn(4);
            super.visitInsn(96);
            super.visitInsn(172);
            super.visitLabel(label);
            super.visitInsn(3);
            super.visitInsn(172);
         }

      }

      private void mutatePrimitiveLongReturn() {
         if (this.shouldMutate("primitive long", "x + 1")) {
            super.visitInsn(10);
            super.visitInsn(97);
            super.visitInsn(173);
         }

      }

      private boolean shouldMutate(String type, String replacement) {
         String description = String.format("replaced return of %s value with %s", type, replacement);
         MutationIdentifier mutationId = this.context.registerMutation(ReturnValuesMutator.this, description);
         return this.context.shouldMutate(mutationId);
      }

      public void visitInsn(int opcode) {
         switch(opcode) {
         case 172:
            this.mutatePrimitiveIntegerReturn();
            break;
         case 173:
            this.mutatePrimitiveLongReturn();
            break;
         case 174:
            this.mutatePrimitiveFloatReturn();
            break;
         case 175:
            this.mutatePrimitiveDoubleReturn();
            break;
         case 176:
            this.mutateObjectReferenceReturn();
            break;
         default:
            super.visitInsn(opcode);
         }

      }

      // $FF: synthetic method
      ReturnValuesMethodVisitor(MutationContext x1, MethodInfo x2, MethodVisitor x3, Object x4) {
         this(x1, x2, x3);
      }
   }

   private static final class ObjectReferenceReplacer {
      private ObjectReferenceReplacer() {
      }

      private Object replaceObjectInstance(Object object, Class<?> declaredReturnType) {
         if (Boolean.class == declaredReturnType) {
            return Boolean.TRUE.equals(object) ? Boolean.FALSE : Boolean.TRUE;
         } else if (Integer.class == declaredReturnType) {
            Integer intValue = (Integer)object;
            if (intValue == null) {
               return 1;
            } else {
               return intValue == 1 ? 0 : intValue + 1;
            }
         } else if (Long.class == declaredReturnType) {
            Long longValue = (Long)object;
            return longValue == null ? 1L : longValue + 1L;
         } else if (Object.class == declaredReturnType) {
            return object != null ? null : new Object();
         } else if (object == null) {
            throw new RuntimeException("Mutated return of null object to throwing a runtime exception");
         } else {
            return null;
         }
      }

      // $FF: synthetic method
      ObjectReferenceReplacer(Object x0) {
         this();
      }
   }

   private static final class ObjectMutationMethod {
      private final String mutatorMethodName;
      private final String mutatorInternalName;
      private final String mutationMethodDescriptor;

      public ObjectMutationMethod() {
         Type mutatorType = Type.getType(ReturnValuesMutator.class);
         this.mutatorInternalName = mutatorType.getInternalName();
         this.mutatorMethodName = "mutateObjectInstance";
         Type objectType = Type.getType(Object.class);
         Type classType = Type.getType(Class.class);
         this.mutationMethodDescriptor = Type.getMethodDescriptor(objectType, objectType, classType);
      }

      public String getClassName() {
         return this.mutatorInternalName;
      }

      public String getMethodDescriptor() {
         return this.mutationMethodDescriptor;
      }

      public String getMethodName() {
         return this.mutatorMethodName;
      }
   }
}
