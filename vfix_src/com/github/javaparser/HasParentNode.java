package com.github.javaparser;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.Observable;
import java.util.Optional;
import java.util.function.Predicate;

public interface HasParentNode<T> extends Observable {
   Optional<Node> getParentNode();

   T setParentNode(Node parentNode);

   Node getParentNodeForChildren();

   /** @deprecated */
   @Deprecated
   default <N> Optional<N> getAncestorOfType(Class<N> classType) {
      return this.findAncestor(classType);
   }

   /** @deprecated */
   @Deprecated
   default <N> Optional<N> findParent(Class<N> type) {
      return this.findAncestor(type);
   }

   default <N> Optional<N> findAncestor(Class<N> type) {
      return this.findAncestor(type, (x) -> {
         return true;
      });
   }

   default <N> Optional<N> findAncestor(Class<N> type, Predicate<N> predicate) {
      Node parent;
      for(Optional possibleParent = this.getParentNode(); possibleParent.isPresent(); possibleParent = parent.getParentNode()) {
         parent = (Node)possibleParent.get();
         if (type.isAssignableFrom(parent.getClass()) && predicate.test(type.cast(parent))) {
            return Optional.of(type.cast(parent));
         }
      }

      return Optional.empty();
   }
}
