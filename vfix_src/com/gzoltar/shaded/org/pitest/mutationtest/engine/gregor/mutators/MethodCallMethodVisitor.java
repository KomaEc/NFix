package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;
import com.gzoltar.shaded.org.pitest.reloc.asm.Type;
import java.util.HashMap;
import java.util.Map;

class MethodCallMethodVisitor extends MethodVisitor {
   private static final Map<Type, Integer> RETURN_TYPE_MAP = new HashMap();
   private final F2<String, String, Boolean> filter;
   private final MethodMutatorFactory factory;
   private final MutationContext context;
   private final MethodInfo methodInfo;

   public MethodCallMethodVisitor(MethodInfo methodInfo, MutationContext context, MethodVisitor writer, MethodMutatorFactory factory, F2<String, String, Boolean> filter) {
      super(327680, writer);
      this.factory = factory;
      this.filter = filter;
      this.context = context;
      this.methodInfo = methodInfo;
   }

   public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
      if ((Boolean)this.filter.apply(name, desc) && !this.isCallToSuperOrOwnConstructor(name, owner)) {
         MutationIdentifier newId = this.context.registerMutation(this.factory, "removed call to " + owner + "::" + name);
         if (this.context.shouldMutate(newId)) {
            this.popStack(desc, name);
            this.popThisIfNotStatic(opcode);
            this.putReturnValueOnStack(desc, name);
         } else {
            this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
         }
      } else {
         this.mv.visitMethodInsn(opcode, owner, name, desc, itf);
      }

   }

   private boolean isCallToSuperOrOwnConstructor(String name, String owner) {
      return this.methodInfo.isConstructor() && MethodInfo.isConstructor(name) && (owner.equals(this.context.getClassInfo().getName()) || this.context.getClassInfo().getSuperName().equals(owner));
   }

   private void popThisIfNotStatic(int opcode) {
      if (!isStatic(opcode)) {
         this.mv.visitInsn(87);
      }

   }

   private void popStack(String desc, String name) {
      Type[] argTypes = Type.getArgumentTypes(desc);

      for(int i = argTypes.length - 1; i >= 0; --i) {
         Type argumentType = argTypes[i];
         if (argumentType.getSize() != 1) {
            this.mv.visitInsn(88);
         } else {
            this.mv.visitInsn(87);
         }
      }

      if (MethodInfo.isConstructor(name)) {
         this.mv.visitInsn(87);
      }

   }

   private static boolean isStatic(int opcode) {
      return 184 == opcode;
   }

   private void putReturnValueOnStack(String desc, String name) {
      Type returnType = Type.getReturnType(desc);
      if (!returnType.equals(Type.VOID_TYPE)) {
         Integer opCode = (Integer)RETURN_TYPE_MAP.get(returnType);
         if (opCode == null) {
            this.mv.visitInsn(1);
         } else {
            this.mv.visitInsn(opCode);
         }
      } else if (MethodInfo.isConstructor(name)) {
         this.mv.visitInsn(1);
      }

   }

   static {
      RETURN_TYPE_MAP.put(Type.INT_TYPE, 3);
      RETURN_TYPE_MAP.put(Type.BOOLEAN_TYPE, 3);
      RETURN_TYPE_MAP.put(Type.BYTE_TYPE, 3);
      RETURN_TYPE_MAP.put(Type.CHAR_TYPE, 3);
      RETURN_TYPE_MAP.put(Type.SHORT_TYPE, 3);
      RETURN_TYPE_MAP.put(Type.LONG_TYPE, 9);
      RETURN_TYPE_MAP.put(Type.FLOAT_TYPE, 11);
      RETURN_TYPE_MAP.put(Type.DOUBLE_TYPE, 14);
   }
}
