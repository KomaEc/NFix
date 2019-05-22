package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.utils.Utils;

public interface NodeWithName<N extends Node> {
   Name getName();

   N setName(Name name);

   default N setName(String name) {
      Utils.assertNonEmpty(name);
      return this.setName(JavaParser.parseName(name));
   }

   default String getNameAsString() {
      return this.getName().asString();
   }
}
