package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class LongEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else {
         Object newValue = Long.valueOf(text);
         this.setValue(newValue);
      }
   }
}
