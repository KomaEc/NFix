package com.github.javaparser.printer.lexicalpreservation;

public interface TextElementMatcher {
   boolean match(TextElement textElement);

   default TextElementMatcher and(TextElementMatcher textElementMatcher) {
      return (textElement) -> {
         return this.match(textElement) && textElementMatcher.match(textElement);
      };
   }
}
