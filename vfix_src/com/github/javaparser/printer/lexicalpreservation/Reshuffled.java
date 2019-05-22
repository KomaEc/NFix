package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.printer.concretesyntaxmodel.CsmMix;

public class Reshuffled implements DifferenceElement {
   private final CsmMix previousOrder;
   private final CsmMix nextOrder;

   Reshuffled(CsmMix previousOrder, CsmMix nextOrder) {
      this.previousOrder = previousOrder;
      this.nextOrder = nextOrder;
   }

   public String toString() {
      return "Reshuffled{" + this.nextOrder + ", previous=" + this.previousOrder + '}';
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Reshuffled that = (Reshuffled)o;
         return !this.previousOrder.equals(that.previousOrder) ? false : this.nextOrder.equals(that.nextOrder);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.previousOrder.hashCode();
      result = 31 * result + this.nextOrder.hashCode();
      return result;
   }

   public CsmMix getElement() {
      return this.nextOrder;
   }

   public CsmMix getPreviousOrder() {
      return this.previousOrder;
   }

   public CsmMix getNextOrder() {
      return this.nextOrder;
   }

   public boolean isAdded() {
      return false;
   }
}
