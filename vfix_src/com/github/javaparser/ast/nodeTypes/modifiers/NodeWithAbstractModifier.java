package com.github.javaparser.ast.nodeTypes.modifiers;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

public interface NodeWithAbstractModifier<N extends Node> extends NodeWithModifiers<N> {
   default boolean isAbstract() {
      return this.getModifiers().contains(Modifier.ABSTRACT);
   }

   default N setAbstract(boolean set) {
      return this.setModifier(Modifier.ABSTRACT, set);
   }
}
