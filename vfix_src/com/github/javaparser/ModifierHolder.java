package com.github.javaparser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.utils.Utils;
import java.util.EnumSet;

class ModifierHolder {
   final EnumSet<Modifier> modifiers;
   final NodeList<AnnotationExpr> annotations;
   final JavaToken begin;

   ModifierHolder(JavaToken begin, EnumSet<Modifier> modifiers, NodeList<AnnotationExpr> annotations) {
      this.begin = begin;
      this.modifiers = (EnumSet)Utils.assertNotNull(modifiers);
      this.annotations = annotations;
   }
}
