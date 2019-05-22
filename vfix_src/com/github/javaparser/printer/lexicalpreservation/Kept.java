package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.TokenTypes;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;
import com.github.javaparser.printer.concretesyntaxmodel.CsmIndent;
import com.github.javaparser.printer.concretesyntaxmodel.CsmToken;
import com.github.javaparser.printer.concretesyntaxmodel.CsmUnindent;

public class Kept implements DifferenceElement {
   private final CsmElement element;

   Kept(CsmElement element) {
      this.element = element;
   }

   public String toString() {
      return "Kept{" + this.element + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Kept kept = (Kept)o;
         return this.element.equals(kept.element);
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

   public int getTokenType() {
      if (this.isToken()) {
         CsmToken csmToken = (CsmToken)this.element;
         return csmToken.getTokenType();
      } else {
         throw new IllegalStateException("Kept is not a " + CsmToken.class.getSimpleName());
      }
   }

   public boolean isAdded() {
      return false;
   }

   public boolean isIndent() {
      return this.element instanceof CsmIndent;
   }

   public boolean isUnindent() {
      return this.element instanceof CsmUnindent;
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

   public boolean isWhiteSpaceOrComment() {
      if (this.isToken()) {
         CsmToken csmToken = (CsmToken)this.element;
         return TokenTypes.isWhitespaceOrComment(csmToken.getTokenType());
      } else {
         return false;
      }
   }
}
