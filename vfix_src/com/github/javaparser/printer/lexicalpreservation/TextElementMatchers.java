package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Node;

class TextElementMatchers {
   static TextElementMatcher byTokenType(int tokenType) {
      return (textElement) -> {
         return textElement.isToken(tokenType);
      };
   }

   static TextElementMatcher byNode(final Node node) {
      return new TextElementMatcher() {
         public boolean match(TextElement textElement) {
            return textElement.isNode(node);
         }

         public String toString() {
            return "match node " + node;
         }
      };
   }
}
