package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.SourcePrinter;

public class CsmIndent implements CsmElement {
   public void prettyPrint(Node node, SourcePrinter printer) {
      printer.indent();
   }

   public int hashCode() {
      return 1;
   }

   public boolean equals(Object obj) {
      return obj instanceof CsmIndent;
   }
}
