package com.github.javaparser.printer.lexicalpreservation;

import com.github.javaparser.printer.concretesyntaxmodel.CsmElement;

public interface DifferenceElement {
   static DifferenceElement added(CsmElement element) {
      return new Added(element);
   }

   static DifferenceElement removed(CsmElement element) {
      return new Removed(element);
   }

   static DifferenceElement kept(CsmElement element) {
      return new Kept(element);
   }

   CsmElement getElement();

   boolean isAdded();
}
