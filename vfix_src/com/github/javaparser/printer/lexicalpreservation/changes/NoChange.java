package com.github.javaparser.printer.lexicalpreservation.changes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;

public class NoChange implements Change {
   public Object getValue(ObservableProperty property, Node node) {
      return property.getRawValue(node);
   }
}
