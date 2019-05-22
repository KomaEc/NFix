package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else {
         this.setValue(new AtomicLong(Long.parseLong(text)));
      }

   }
}
