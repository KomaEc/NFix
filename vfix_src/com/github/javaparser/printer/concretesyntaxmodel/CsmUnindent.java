package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.SourcePrinter;

public class CsmUnindent implements CsmElement {
   public void prettyPrint(Node node, SourcePrinter printer) {
      printer.unindent();
   }

   public int hashCode() {
      return 2;
   }

   public boolean equals(Object obj) {
      return obj instanceof CsmUnindent;
   }
}
