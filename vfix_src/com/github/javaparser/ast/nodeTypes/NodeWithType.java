package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.utils.Utils;

public interface NodeWithType<N extends Node, T extends Type> {
   T getType();

   N setType(T type);

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default N setType(Class<?> typeClass) {
      this.tryAddImportToParentCompilationUnit(typeClass);
      return this.setType(JavaParser.parseType(typeClass.getSimpleName()));
   }

   default N setType(final String typeString) {
      Utils.assertNonEmpty(typeString);
      return this.setType(JavaParser.parseType(typeString));
   }

   default String getTypeAsString() {
      return this.getType().asString();
   }
}
