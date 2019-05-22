package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmIndent;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;

public class Added implements DifferenceElement {
   private final CsmElement element;

   Added(CsmElement element) {
      this.element = element;
   }

   public String toString() {
      return "Added{" + this.element + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Added added = (Added)o;
         return this.element.equals(added.element);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.element.hashCode();
   }

   public CsmElement getElement() {
      return this.element;
   }

   public boolean isAdded() {
      return true;
   }

   public boolean isIndent() {
      return this.element instanceof CsmIndent;
   }

   public boolean isUnindent() {
      return this.element instanceof CsmUnindent;
   }

   public TextElement toTextElement() {
      if (this.element instanceof LexicalDifferenceCalculator.CsmChild) {
         return new ChildTextElement(((LexicalDifferenceCalculator.CsmChild)this.element).getChild());
      } else if (this.element instanceof CsmToken) {
         return new TokenTextElement(((CsmToken)this.element).getTokenType(), ((CsmToken)this.element).getContent((Node)null));
      } else {
         throw new UnsupportedOperationException(this.element.getClass().getSimpleName());
      }
   }
}
