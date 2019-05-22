package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;
import java.util.Arrays;

class ArgumentPropagationVisitor extends MethodVisitor {
   private final MethodMutatorFactory factory;
   private final MutationContext context;

   public ArgumentPropagationVisitor(MutationContext context, MethodVisitor writer, MethodMutatorFactory factory) {
      super(327680, writer);
      this.factory = factory;
      this.context = context;
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if (this.hasArgumentMatchingTheReturnType(desc)) {
         MutationIdentifier newId = this.context.registerMutation(this.factory, "replaced call to " + owner + "::" + name + " with argument");
         if (this.context.shouldMutate(newId)) {
            Type returnType = Type.getReturnType(desc);
            this.replaceMethodCallWithArgumentHavingSameTypeAsReturnValue(Type.getArgumentTypes(desc), returnType, opcode);
         } else {
            this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
         }
      } else {
         this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

   }

   private boolean hasArgumentMatchingTheReturnType(String desc) {
      return this.findLastIndexOfArgumentWithSameTypeAsReturnValue(Type.getArgumentTypes(desc), Type.getReturnType(desc)) > -1;
   }

   private void replaceMethodCallWithArgumentHavingSameTypeAsReturnValue(Type[] argTypes, Type returnType, int opcode) {
      int indexOfPropagatedArgument = this.findLastIndexOfArgumentWithSameTypeAsReturnValue(argTypes, returnType);
      this.popArgumentsBeforePropagatedArgument(argTypes, indexOfPropagatedArgument);
      this.popArgumentsFollowingThePropagated(argTypes, returnType, indexOfPropagatedArgument);
      this.removeThisFromStackIfNotStatic(returnType, opcode);
   }

   private int findLastIndexOfArgumentWithSameTypeAsReturnValue(Type[] argTypes, Type returnType) {
      return Arrays.asList(argTypes).lastIndexOf(returnType);
   }

   private void popArgumentsBeforePropagatedArgument(Type[] argTypes, int indexOfPropagatedArgument) {
      Type[] argumentTypesBeforeNewReturnValue = (Type[])Arrays.copyOfRange(argTypes, indexOfPropagatedArgument + 1, argTypes.length);
      this.popArguments(argumentTypesBeforeNewReturnValue);
   }

   private void popArguments(Type[] argumentTypes) {
      for(int i = argumentTypes.length - 1; i >= 0; --i) {
         this.popArgument(argumentTypes[i]);
      }

   }

   private void popArgumentsFollowingThePropagated(Type[] argTypes, Type returnType, int indexOfPropagatedArgument) {
      Type[] argsFollowing = (Type[])Arrays.copyOfRange(argTypes, 0, indexOfPropagatedArgument);

      for(int j = argsFollowing.length - 1; j >= 0; --j) {
         swap(this.mv, returnType, argsFollowing[j]);
         this.popArgument(argsFollowing[j]);
      }

   }

   private void removeThisFromStackIfNotStatic(Type returnType, int opcode) {
      if (isNotStatic(opcode)) {
         swap(this.mv, returnType, Type.getType(Object.class));
         this.mv.visitInsn(87);
      }

   }

   private void popArgument(Type argumentType) {
      if (argumentType.getSize() != 1) {
         this.mv.visitInsn(88);
      } else {
         this.mv.visitInsn(87);
      }

   }

   private static boolean isNotStatic(int opcode) {
      return 184 != opcode;
   }

   private static void swap(MethodVisitor mv, Type stackTop, Type belowTop) {
      if (stackTop.getSize() == 1) {
         if (belowTop.getSize() == 1) {
            mv.visitInsn(95);
         } else {
            mv.visitInsn(91);
            mv.visitInsn(87);
         }
      } else {
         if (belowTop.getSize() == 1) {
            mv.visitInsn(93);
         } else {
            mv.visitInsn(94);
         }

         mv.visitInsn(88);
      }

   }
}
