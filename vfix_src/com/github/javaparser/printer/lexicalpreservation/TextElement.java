package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import java.util.Optional;

public abstract class TextElement implements TextElementMatcher {
   abstract String expand();

   abstract boolean isToken(int tokenKind);

   final boolean isCommentToken() {
      return this.isToken(8) || this.isToken(5) || this.isToken(9);
   }

   public boolean match(TextElement textElement) {
      return this.equals(textElement);
   }

   abstract boolean isNode(Node node);

   public abstract boolean isWhiteSpace();

   public abstract boolean isSpaceOrTab();

   public abstract boolean isNewline();

   public abstract boolean isComment();

   public final boolean isWhiteSpaceOrComment() {
      return this.isWhiteSpace() || this.isComment();
   }

   public abstract boolean isChildOfClass(Class<? extends Node> nodeClass);

   abstract Optional<Range> getRange();

   TextElementMatcher matchByRange() {
      return (textElement) -> {
         Optional<Range> range1 = this.getRange();
         Optional<Range> range2 = textElement.getRange();
         return range1.isPresent() && range2.isPresent() ? ((Range)range1.get()).equals(range2.get()) : false;
      };
   }
}
