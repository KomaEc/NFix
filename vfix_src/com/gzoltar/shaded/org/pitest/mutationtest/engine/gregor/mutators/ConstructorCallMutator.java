package com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.mutators;

import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodInfo;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.gregor.MutationContext;
import com.gzoltar.shaded.org.pitest.reloc.asm.MethodVisitor;

public enum ConstructorCallMutator implements MethodMutatorFactory {
   CONSTRUCTOR_CALL_MUTATOR;

   public MethodVisitor create(MutationContext context, MethodInfo methodInfo, MethodVisitor methodVisitor) {
      return new MethodCallMethodVisitor(methodInfo, context, methodVisitor, this, constructors());
   }

   public String getGloballyUniqueId() {
      return this.getClass().getName();
   }

   private static F2<String, String, Boolean> constructors() {
      return new F2<String, String, Boolean>() {
         public Boolean apply(String name, String desc) {
            return MethodInfo.isConstructor(name);
         }
      };
   }

   public String getName() {
      return this.name();
   }
}
