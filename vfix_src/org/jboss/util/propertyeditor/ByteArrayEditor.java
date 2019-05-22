package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class ByteArrayEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text, false, false)) {
         this.setValue((Object)null);
      } else {
         Object newValue = text.getBytes();
         this.setValue(newValue);
      }
   }
}
