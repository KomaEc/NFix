package com.github.javaparser.ast.nodeTypes.modifiers;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

public interface NodeWithStrictfpModifier<N extends Node> extends NodeWithModifiers<N> {
   default boolean isStrictfp() {
      return this.getModifiers().contains(Modifier.STRICTFP);
   }

   default N setStrictfp(boolean set) {
      return this.setModifier(Modifier.STRICTFP, set);
   }
}
