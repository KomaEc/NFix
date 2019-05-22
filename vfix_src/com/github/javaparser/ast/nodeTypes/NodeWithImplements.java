package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public interface NodeWithImplements<N extends Node> {
   NodeList<ClassOrInterfaceType> getImplementedTypes();

   default ClassOrInterfaceType getImplementedTypes(int i) {
      return (ClassOrInterfaceType)this.getImplementedTypes().get(i);
   }

   N setImplementedTypes(NodeList<ClassOrInterfaceType> implementsList);

   void tryAddImportToParentCompilationUnit(Class<?> clazz);

   default N setImplementedType(int i, ClassOrInterfaceType implement) {
      this.getImplementedTypes().set(i, (Node)implement);
      return (Node)this;
   }

   default N addImplementedType(ClassOrInterfaceType implement) {
      this.getImplementedTypes().add((Node)implement);
      return (Node)this;
   }

   /** @deprecated */
   default N addImplements(String name) {
      return this.addImplementedType(name);
   }

   /** @deprecated */
   default N addImplements(Class<?> clazz) {
      return this.addImplementedType(clazz);
   }

   default N addImplementedType(String name) {
      this.getImplementedTypes().add((Node)JavaParser.parseClassOrInterfaceType(name));
      return (Node)this;
   }

   default N addImplementedType(Class<?> clazz) {
      this.tryAddImportToParentCompilationUnit(clazz);
      return this.addImplementedType(clazz.getSimpleName());
   }
}
