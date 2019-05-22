package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else {
         this.setValue(new AtomicInteger(Integer.parseInt(text)));
      }

   }
}
