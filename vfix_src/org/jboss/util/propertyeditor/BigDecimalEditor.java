package org.jboss.util.propertyeditor;

import java.math.BigDecimal;

public class BigDecimalEditor extends TextPropertyEditorSupport {
   public Object getValue() {
      return new BigDecimal(this.getAsText());
   }
}
