package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.metamodel.DerivedProperty;
import java.util.Optional;

public interface NodeWithTypeArguments<N extends Node> {
   Optional<NodeList<Type>> getTypeArguments();

   N setTypeArguments(NodeList<Type> typeArguments);

   @DerivedProperty
   default boolean isUsingDiamondOperator() {
      return this.getTypeArguments().isPresent() && ((NodeList)this.getTypeArguments().get()).isEmpty();
   }

   default N setDiamondOperator() {
      return this.setTypeArguments(new NodeList());
   }

   default N removeTypeArguments() {
      return this.setTypeArguments((NodeList)null);
   }

   default N setTypeArguments(Type... typeArguments) {
      return this.setTypeArguments(NodeList.nodeList((Node[])typeArguments));
   }
}
