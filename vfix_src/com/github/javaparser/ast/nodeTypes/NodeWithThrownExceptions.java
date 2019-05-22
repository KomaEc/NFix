package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ReferenceType;

public interface NodeWithThrownExceptions<N extends Node> {
   N setThrownExceptions(NodeList<ReferenceType> thrownExceptions);

   NodeList<ReferenceType> getThrownExceptions();

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default ReferenceType getThrownException(int i) {
      return (ReferenceType)this.getThrownExceptions().get(i);
   }

   default N addThrownException(ReferenceType throwType) {
      this.getThrownExceptions().add((Node)throwType);
      return (Node)this;
   }

   default N addThrownException(Class<? extends Throwable> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addThrownException((ReferenceType)JavaParser.parseClassOrInterfaceType(clazz.getSimpleName()));
   }

   default boolean isThrown(Class<? extends Throwable> clazz) {
      return this.isThrown(clazz.getSimpleName());
   }

   default boolean isThrown(String throwableName) {
      return this.getThrownExceptions().stream().anyMatch((t) -> {
         return t.toString().equals(throwableName);
      });
   }
}
