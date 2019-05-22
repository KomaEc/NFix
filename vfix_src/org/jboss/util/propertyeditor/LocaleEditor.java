package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import org.jboss.util.Strings;

public class LocaleEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      this.setValue(Strings.parseLocaleString(text));
   }

   public String getAsText() {
      Object value = this.getValue();
      return value != null ? value.toString() : "";
   }
}
