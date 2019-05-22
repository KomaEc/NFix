package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.Node;
import java.util.Optional;

public interface AssociableToAST<N extends Node> {
   default Optional<N> toAst() {
      throw new UnsupportedOperationException();
   }
}
