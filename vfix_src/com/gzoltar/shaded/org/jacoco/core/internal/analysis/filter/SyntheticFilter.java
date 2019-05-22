package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;

public final class SyntheticFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if ((methodNode.access & 4096) != 0 && !methodNode.name.startsWith("lambda$")) {
         output.ignore(methodNode.instructions.getFirst(), methodNode.instructions.getLast());
      }

   }
}
