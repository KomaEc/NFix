package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class FloatEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      Object newValue = Float.valueOf(text);
      this.setValue(newValue);
   }
}
