package com.github.javaparser.printer.concretesyntaxmodel;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.observer.ObservableProperty;
import com.github.javaparser.printer.SourcePrinter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CsmConditional implements CsmElement {
   private final CsmConditional.Condition condition;
   private final List<ObservableProperty> properties;
   private final CsmElement thenElement;
   private final CsmElement elseElement;

   public CsmConditional.Condition getCondition() {
      return this.condition;
   }

   public ObservableProperty getProperty() {
      if (this.properties.size() > 1) {
         throw new IllegalStateException();
      } else {
         return (ObservableProperty)this.properties.get(0);
      }
   }

   public List<ObservableProperty> getProperties() {
      return this.properties;
   }

   public CsmElement getThenElement() {
      return this.thenElement;
   }

   public CsmElement getElseElement() {
      return this.elseElement;
   }

   public CsmConditional(ObservableProperty property, CsmConditional.Condition condition, CsmElement thenElement, CsmElement elseElement) {
      this.properties = Arrays.asList(property);
      this.condition = condition;
      this.thenElement = thenElement;
      this.elseElement = elseElement;
   }

   public CsmConditional(List<ObservableProperty> properties, CsmConditional.Condition condition, CsmElement thenElement, CsmElement elseElement) {
      if (properties.size() < 1) {
         throw new IllegalArgumentException();
      } else {
         this.properties = properties;
         this.condition = condition;
         this.thenElement = thenElement;
         this.elseElement = elseElement;
      }
   }

   public CsmConditional(ObservableProperty property, CsmConditional.Condition condition, CsmElement thenElement) {
      this((ObservableProperty)property, condition, thenElement, new CsmNone());
   }

   public void prettyPrint(Node node, SourcePrinter printer) {
      boolean test = false;

      ObservableProperty prop;
      for(Iterator var4 = this.properties.iterator(); var4.hasNext(); test = test || this.condition.evaluate(node, prop)) {
         prop = (ObservableProperty)var4.next();
      }

      if (test) {
         this.thenElement.prettyPrint(node, printer);
      } else {
         this.elseElement.prettyPrint(node, printer);
      }

   }

   public static enum Condition {
      IS_EMPTY,
      IS_NOT_EMPTY,
      IS_PRESENT,
      FLAG;

      boolean evaluate(Node node, ObservableProperty property) {
         if (this == IS_PRESENT) {
            return !property.isNullOrNotPresent(node);
         } else if (this == FLAG) {
            return property.getValueAsBooleanAttribute(node);
         } else {
            NodeList value;
            if (this == IS_EMPTY) {
               value = property.getValueAsMultipleReference(node);
               return value == null || value.isEmpty();
            } else if (this != IS_NOT_EMPTY) {
               throw new UnsupportedOperationException(this.name());
            } else {
               value = property.getValueAsMultipleReference(node);
               return value != null && !value.isEmpty();
            }
         }
      }
   }
}
