package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.expr.Expression;
import java.util.Optional;

public interface NodeWithTraversableScope {
   Optional<Expression> traverseScope();
}
