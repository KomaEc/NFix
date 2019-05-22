package com.github.javaparser.printer.lexicalpreservation.changes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.concretesyntaxmodel.CsmConditional;
import com.github.javaparser.utils.Utils;

public interface Change {
   default boolean evaluate(CsmConditional csmConditional, Node node) {
      switch(csmConditional.getCondition()) {
      case FLAG:
         return csmConditional.getProperties().stream().anyMatch((p) -> {
            return (Boolean)this.getValue(p, node);
         });
      case IS_NOT_EMPTY:
         return !Utils.valueIsNullOrEmpty(this.getValue(csmConditional.getProperty(), node));
      case IS_EMPTY:
         return Utils.valueIsNullOrEmpty(this.getValue(csmConditional.getProperty(), node));
      case IS_PRESENT:
         return !Utils.valueIsNullOrEmpty(this.getValue(csmConditional.getProperty(), node));
      default:
         throw new UnsupportedOperationException("" + csmConditional.getProperty() + " " + csmConditional.getCondition());
      }
   }

   Object getValue(ObservableProperty property, Node node);
}
