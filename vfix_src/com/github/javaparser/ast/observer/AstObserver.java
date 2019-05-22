package com.github.javaparser.ast.observer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

public interface AstObserver {
   void propertyChange(Node observedNode, ObservableProperty property, Object oldValue, Object newValue);

   void parentChange(Node observedNode, Node previousParent, Node newParent);

   void listChange(NodeList observedNode, AstObserver.ListChangeType type, int index, Node nodeAddedOrRemoved);

   void listReplacement(NodeList observedNode, int index, Node oldNode, Node newNode);

   public static enum ListChangeType {
      ADDITION,
      REMOVAL;
   }
}
