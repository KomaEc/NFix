package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import java.util.Optional;

class ChildTextElement extends TextElement {
   private final Node child;

   ChildTextElement(Node child) {
      this.child = child;
   }

   String expand() {
      return LexicalPreservingPrinter.print(this.child);
   }

   Node getChild() {
      return this.child;
   }

   boolean isToken(int tokenKind) {
      return false;
   }

   boolean isNode(Node node) {
      return node == this.child;
   }

   NodeText getNodeTextForWrappedNode() {
      return LexicalPreservingPrinter.getOrCreateNodeText(this.child);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ChildTextElement that = (ChildTextElement)o;
         return this.child.equals(that.child);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.child.hashCode();
   }

   public String toString() {
      return "ChildTextElement{" + this.child + '}';
   }

   public boolean isWhiteSpace() {
      return false;
   }

   public boolean isSpaceOrTab() {
      return false;
   }

   public boolean isNewline() {
      return false;
   }

   public boolean isComment() {
      return this.child instanceof Comment;
   }

   public boolean isChildOfClass(Class<? extends Node> nodeClass) {
      return nodeClass.isInstance(this.child);
   }

   Optional<Range> getRange() {
      return this.child.getRange();
   }
}
