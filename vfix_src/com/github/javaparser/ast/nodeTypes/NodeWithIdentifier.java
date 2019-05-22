package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.utils.Utils;

public interface NodeWithIdentifier<N extends Node> {
   String getIdentifier();

   N setIdentifier(String identifier);

   default String getId() {
      return this.getIdentifier();
   }

   default N setId(String identifier) {
      Utils.assertNonEmpty(identifier);
      return this.setIdentifier(identifier);
   }
}
