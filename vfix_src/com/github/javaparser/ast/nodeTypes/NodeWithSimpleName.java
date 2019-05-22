package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.utils.Utils;

public interface NodeWithSimpleName<N extends Node> {
   SimpleName getName();

   N setName(SimpleName name);

   default N setName(String name) {
      Utils.assertNonEmpty(name);
      return this.setName(new SimpleName(name));
   }

   default String getNameAsString() {
      return this.getName().getIdentifier();
   }

   default NameExpr getNameAsExpression() {
      return new NameExpr(this.getName());
   }
}
