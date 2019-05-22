package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class ByteEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else {
         Object newValue = Byte.decode(text);
         this.setValue(newValue);
      }
   }
}
