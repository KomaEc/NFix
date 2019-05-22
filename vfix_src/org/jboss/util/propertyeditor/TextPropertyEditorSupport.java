package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class TextPropertyEditorSupport extends PropertyEditorSupport {
   protected TextPropertyEditorSupport(Object source) {
      super(source);
   }

   protected TextPropertyEditorSupport() {
   }

   public void setAsText(String text) {
      this.setValue(text);
   }
}
