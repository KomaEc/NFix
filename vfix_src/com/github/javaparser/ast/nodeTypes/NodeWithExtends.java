package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public interface NodeWithExtends<N extends Node> {
   NodeList<ClassOrInterfaceType> getExtendedTypes();

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default ClassOrInterfaceType getExtendedTypes(int i) {
      return (ClassOrInterfaceType)this.getExtendedTypes().get(i);
   }

   N setExtendedTypes(NodeList<ClassOrInterfaceType> extendsList);

   default N setExtendedType(int i, ClassOrInterfaceType extend) {
      this.getExtendedTypes().set(i, (Node)extend);
      return (Node)this;
   }

   default N addExtendedType(ClassOrInterfaceType extend) {
      this.getExtendedTypes().add((Node)extend);
      return (Node)this;
   }

   /** @deprecated */
   default N addExtends(Class<?> clazz) {
      return this.addExtendedType(clazz);
   }

   /** @deprecated */
   default N addExtends(String name) {
      return this.addExtendedType(name);
   }

   default N addExtendedType(Class<?> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addExtendedType(clazz.getSimpleName());
   }

   default N addExtendedType(String name) {
      this.getExtendedTypes().add((Node)JavaParser.parseClassOrInterfaceType(name));
      return (Node)this;
   }
}
