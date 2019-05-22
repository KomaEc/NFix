package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import java.util.Optional;

class TokenTextElement extends TextElement {
   private final JavaToken token;

   TokenTextElement(JavaToken token) {
      this.token = token;
   }

   TokenTextElement(int tokenKind, String text) {
      this(new JavaToken(tokenKind, text));
   }

   TokenTextElement(int tokenKind) {
      this(new JavaToken(tokenKind));
   }

   String expand() {
      return this.token.getText();
   }

   String getText() {
      return this.token.getText();
   }

   int getTokenKind() {
      return this.token.getKind();
   }

   public JavaToken getToken() {
      return this.token;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TokenTextElement that = (TokenTextElement)o;
         return this.token.equals(that.token);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.token.hashCode();
   }

   public String toString() {
      return this.token.toString();
   }

   boolean isToken(int tokenKind) {
      return this.token.getKind() == tokenKind;
   }

   boolean isNode(Node node) {
      return false;
   }

   public boolean isWhiteSpace() {
      return this.token.getCategory().isWhitespace();
   }

   public boolean isSpaceOrTab() {
      return this.token.getCategory().isWhitespaceButNotEndOfLine();
   }

   public boolean isComment() {
      return this.token.getCategory().isComment();
   }

   public boolean isNewline() {
      return this.token.getCategory().isEndOfLine();
   }

   public boolean isChildOfClass(Class<? extends Node> nodeClass) {
      return false;
   }

   Optional<Range> getRange() {
      return this.token.getRange();
   }
}
