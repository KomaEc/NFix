package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.GeneratedJavaParserConstants;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.SourcePrinter;

public class CsmAttribute implements CsmElement {
   private final ObservableProperty property;

   public ObservableProperty getProperty() {
      return this.property;
   }

   public CsmAttribute(ObservableProperty property) {
      this.property = property;
   }

   public void prettyPrint(Node node, SourcePrinter printer) {
      Object value = this.property.getRawValue(node);
      printer.print(PrintingHelper.printToString(value));
   }

   public int getTokenType(Node node, String text, String tokenText) {
      String expectedImage;
      int i;
      switch(this.property) {
      case IDENTIFIER:
         return 89;
      case TYPE:
         expectedImage = "\"" + text.toLowerCase() + "\"";

         for(i = 0; i < GeneratedJavaParserConstants.tokenImage.length; ++i) {
            if (GeneratedJavaParserConstants.tokenImage[i].equals(expectedImage)) {
               return i;
            }
         }

         throw new RuntimeException("Attribute 'type' does not corresponding to any expected value. Text: " + text);
      case OPERATOR:
         expectedImage = "\"" + tokenText.toLowerCase() + "\"";

         for(i = 0; i < GeneratedJavaParserConstants.tokenImage.length; ++i) {
            if (GeneratedJavaParserConstants.tokenImage[i].equals(expectedImage)) {
               return i;
            }
         }

         throw new RuntimeException("Attribute 'operator' does not corresponding to any expected value. Text: " + tokenText);
      case VALUE:
         if (node instanceof IntegerLiteralExpr) {
            return 75;
         }
      case NAME:
         return 89;
      default:
         throw new UnsupportedOperationException("getTokenType does not know how to handle property " + this.property + " with text: " + text);
      }
   }
}
