package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;

public class Removed implements DifferenceElement {
   private final CsmElement element;

   Removed(CsmElement element) {
      this.element = element;
   }

   public String toString() {
      return "Removed{" + this.element + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Removed removed = (Removed)o;
         return this.element.equals(removed.element);
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

   public Node getChild() {
      if (this.isChild()) {
         LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)this.element;
         return csmChild.getChild();
      } else {
         throw new IllegalStateException("Removed is not a " + LexicalDifferenceCalculator.CsmChild.class.getSimpleName());
      }
   }

   public int getTokenType() {
      if (this.isToken()) {
         CsmToken csmToken = (CsmToken)this.element;
         return csmToken.getTokenType();
      } else {
         throw new IllegalStateException("Removed is not a " + CsmToken.class.getSimpleName());
      }
   }

   public boolean isAdded() {
      return false;
   }

   public boolean isToken() {
      return this.element instanceof CsmToken;
   }

   public boolean isChild() {
      return this.element instanceof LexicalDifferenceCalculator.CsmChild;
   }

   public boolean isPrimitiveType() {
      if (this.isChild()) {
         LexicalDifferenceCalculator.CsmChild csmChild = (LexicalDifferenceCalculator.CsmChild)this.element;
         return csmChild.getChild() instanceof PrimitiveType;
      } else {
         return false;
      }
   }

   public boolean isWhiteSpace() {
      if (this.isToken()) {
         CsmToken csmToken = (CsmToken)this.element;
         return csmToken.isWhiteSpace();
      } else {
         return false;
      }
   }
}
