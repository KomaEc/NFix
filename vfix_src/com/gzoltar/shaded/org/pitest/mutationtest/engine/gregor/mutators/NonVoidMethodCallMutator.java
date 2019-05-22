package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public enum NonVoidMethodCallMutator implements MethodMutatorFactory {
   NON_VOID_METHOD_CALL_MUTATOR;

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new MethodCallMethodVisitor(methodInfo, context, methodVisitor, this, this.nonVoidMethods());
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   public String getName() {
      return this.name();
   }

   private F2<String, String, Boolean> nonVoidMethods() {
      return new F2<String, String, Boolean>() {
         public Boolean apply(String name, String desc) {
            return !MethodInfo.isVoid(desc);
         }
      };
   }
}
