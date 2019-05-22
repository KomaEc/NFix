package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;

public interface NodeWithArguments<N extends Node> {
   N setArguments(NodeList<Expression> arguments);

   NodeList<Expression> getArguments();

   default Expression getArgument(int i) {
      return (Expression)this.getArguments().get(i);
   }

   default N addArgument(String arg) {
      return this.addArgument(JavaParser.parseExpression(arg));
   }

   default N addArgument(Expression arg) {
      this.getArguments().add((Node)arg);
      return (Node)this;
   }

   default N setArgument(int i, Expression arg) {
      this.getArguments().set(i, (Node)arg);
      return (Node)this;
   }
}
