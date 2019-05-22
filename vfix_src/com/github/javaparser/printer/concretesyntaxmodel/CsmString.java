package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.SourcePrinter;

public class CsmString implements CsmElement {
   private final ObservableProperty property;

   public CsmString(ObservableProperty property) {
      this.property = property;
   }

   public void prettyPrint(Node node, SourcePrinter printer) {
      printer.print("\"");
      printer.print(this.property.getValueAsStringAttribute(node));
      printer.print("\"");
   }

   public String toString() {
      return String.format("CsmString(property:%s)", this.property);
   }
}
