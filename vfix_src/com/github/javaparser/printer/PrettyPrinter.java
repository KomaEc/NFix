package com.github.javaparser.printer;

import com.github.javaparser.ast.Node;

public class PrettyPrinter {
   private final PrettyPrinterConfiguration configuration;

   public PrettyPrinter() {
      this(new PrettyPrinterConfiguration());
   }

   public PrettyPrinter(PrettyPrinterConfiguration configuration) {
      this.configuration = configuration;
   }

   public String print(Node node) {
      PrettyPrintVisitor visitor = (PrettyPrintVisitor)this.configuration.getVisitorFactory().apply(this.configuration);
      node.accept(visitor, (Object)null);
      return visitor.getSource();
   }
}
