package com.github.javaparser.ast.nodeTypes.modifiers;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

public interface NodeWithPrivateModifier<N extends Node> extends NodeWithModifiers<N> {
   default boolean isPrivate() {
      return this.getModifiers().contains(Modifier.PRIVATE);
   }

   default N setPrivate(boolean set) {
      return this.setModifier(Modifier.PRIVATE, set);
   }
}
