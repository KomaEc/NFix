package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import java.util.Optional;

public interface NodeWithScope<N extends Node> extends NodeWithTraversableScope {
   Expression getScope();

   N setScope(Expression scope);

   default Optional<Expression> traverseScope() {
      return Optional.of(this.getScope());
   }
}
