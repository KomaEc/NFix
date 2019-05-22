package com.gzoltar.shaded.org.jacoco.core.internal.analysis.filter;

import com.gzoltar.shaded.org.objectweb.asm.tree.AnnotationNode;
import com.gzoltar.shaded.org.objectweb.asm.tree.MethodNode;
import java.util.Iterator;
import java.util.List;

public class LombokGeneratedFilter implements IFilter {
   public void filter(String className, String superClassName, MethodNode methodNode, IFilterOutput output) {
      if (this.hasLombokGeneratedAnnotation(methodNode)) {
         output.ignore(methodNode.instructions.getFirst(), methodNode.instructions.getLast());
      }

   }

   private boolean hasLombokGeneratedAnnotation(MethodNode methodNode) {
      List<AnnotationNode> runtimeInvisibleAnnotations = methodNode.invisibleAnnotations;
      if (runtimeInvisibleAnnotations != null) {
         Iterator i$ = runtimeInvisibleAnnotations.iterator();

         while(i$.hasNext()) {
            AnnotationNode annotation = (AnnotationNode)i$.next();
            if ("Llombok/Generated;".equals(annotation.desc)) {
               return true;
            }
         }
      }

      return false;
   }
}
