package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class StringEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      this.setValue(text);
   }
}
