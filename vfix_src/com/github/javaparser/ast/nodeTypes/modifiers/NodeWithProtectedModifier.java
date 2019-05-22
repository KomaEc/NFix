package com.github.javaparser.ast.nodeTypes.modifiers;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

public interface NodeWithProtectedModifier<N extends Node> extends NodeWithModifiers<N> {
   default boolean isProtected() {
      return this.getModifiers().contains(Modifier.PROTECTED);
   }

   default N setProtected(boolean set) {
      return this.setModifier(Modifier.PROTECTED, set);
   }
}
