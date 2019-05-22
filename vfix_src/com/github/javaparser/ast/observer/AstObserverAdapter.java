package com.github.javaparser.ast.observer;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;

public abstract class AstObserverAdapter implements AstObserver {
   public void propertyChange(Node observedNode, ObservableProperty property, Object oldValue, Object newValue) {
   }

   public void parentChange(Node observedNode, Node previousParent, Node newParent) {
   }

   public void listChange(NodeList observedNode, AstObserver.ListChangeType type, int index, Node nodeAddedOrRemoved) {
   }

   public void listReplacement(NodeList observedNode, int index, Node oldNode, Node newNode) {
   }
}
