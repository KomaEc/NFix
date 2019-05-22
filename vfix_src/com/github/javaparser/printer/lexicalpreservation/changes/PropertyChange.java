package com.github.javaparser.printer.lexicalpreservation.changes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.observer.ObservableProperty;

public class PropertyChange implements Change {
   private final ObservableProperty property;
   private final Object oldValue;
   private final Object newValue;

   public ObservableProperty getProperty() {
      return this.property;
   }

   public Object getOldValue() {
      return this.oldValue;
   }

   public Object getNewValue() {
      return this.newValue;
   }

   public PropertyChange(ObservableProperty property, Object oldValue, Object newValue) {
      this.property = property;
      this.oldValue = oldValue;
      this.newValue = newValue;
   }

   public Object getValue(ObservableProperty property, Node node) {
      return property == this.property ? this.newValue : property.getRawValue(node);
   }
}
