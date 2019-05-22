package com.github.javaparser.printer.lexicalpreservation.changes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.observer.ObservableProperty;
import java.util.Collection;

public class ListRemovalChange implements Change {
   private final ObservableProperty observableProperty;
   private final int index;

   public ListRemovalChange(ObservableProperty observableProperty, int index) {
      this.observableProperty = observableProperty;
      this.index = index;
   }

   public Object getValue(ObservableProperty property, Node node) {
      if (property == this.observableProperty) {
         NodeList<Node> nodeList = new NodeList();
         Object currentRawValue = (new NoChange()).getValue(property, node);
         if (!(currentRawValue instanceof NodeList)) {
            throw new IllegalStateException("Expected NodeList, found " + currentRawValue.getClass().getCanonicalName());
         } else {
            NodeList<?> currentNodeList = (NodeList)currentRawValue;
            nodeList.addAll((Collection)currentNodeList);
            nodeList.remove(this.index);
            return nodeList;
         }
      } else {
         return (new NoChange()).getValue(property, node);
      }
   }
}
