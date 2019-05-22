package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NodeWithConstructors<N extends Node> extends NodeWithSimpleName<N>, NodeWithMembers<N> {
   default Optional<ConstructorDeclaration> getDefaultConstructor() {
      return this.getMembers().stream().filter((bd) -> {
         return bd instanceof ConstructorDeclaration;
      }).map((bd) -> {
         return (ConstructorDeclaration)bd;
      }).filter((cd) -> {
         return cd.getParameters().isEmpty();
      }).findFirst();
   }

   default ConstructorDeclaration addConstructor(Modifier... modifiers) {
      ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration();
      constructorDeclaration.setModifiers((EnumSet)Arrays.stream(modifiers).collect(Collectors.toCollection(() -> {
         return EnumSet.noneOf(Modifier.class);
      })));
      constructorDeclaration.setName(this.getName());
      this.getMembers().add((Node)constructorDeclaration);
      return constructorDeclaration;
   }

   default List<ConstructorDeclaration> getConstructors() {
      return Collections.unmodifiableList((List)this.getMembers().stream().filter((m) -> {
         return m instanceof ConstructorDeclaration;
      }).map((m) -> {
         return (ConstructorDeclaration)m;
      }).collect(Collectors.toList()));
   }

   default Optional<ConstructorDeclaration> getConstructorByParameterTypes(String... paramTypes) {
      return this.getConstructors().stream().filter((m) -> {
         return m.hasParametersOfType(paramTypes);
      }).findFirst();
   }

   default Optional<ConstructorDeclaration> getConstructorByParameterTypes(Class<?>... paramTypes) {
      return this.getConstructors().stream().filter((m) -> {
         return m.hasParametersOfType(paramTypes);
      }).findFirst();
   }
}
