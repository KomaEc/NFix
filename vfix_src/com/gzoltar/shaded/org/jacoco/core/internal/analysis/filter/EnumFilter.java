package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;

public final class EnumFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if (this.isMethodFiltered(className, superClassName, methodNode.name, methodNode.desc)) {
         output.ignore(methodNode.instructions.getFirst(), methodNode.instructions.getLast());
      }

   }

   private boolean isMethodFiltered(String className, String superClassName, String methodName, String methodDesc) {
      if ("java/lang/Enum".equals(superClassName)) {
         if ("values".equals(methodName) && ("()[L" + className + ";").equals(methodDesc)) {
            return true;
         }

         if ("valueOf".equals(methodName) && ("(Ljava/lang/String;)L" + className + ";").equals(methodDesc)) {
            return true;
         }
      }

      return false;
   }
}
