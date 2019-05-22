package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.utils.Utils;
import java.util.Optional;

public interface NodeWithOptionalLabel<T extends Node> {
   Optional<SimpleName> getLabel();

   T setLabel(SimpleName label);

   T removeLabel();

   default T setLabel(String label) {
      Utils.assertNonEmpty(label);
      return this.setLabel(new SimpleName(label));
   }

   default Optional<String> getLabelAsString() {
      return this.getLabel().flatMap((l) -> {
         return Optional.of(l.getIdentifier());
      });
   }
}
