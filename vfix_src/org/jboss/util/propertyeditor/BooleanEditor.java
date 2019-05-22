package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class BooleanEditor extends PropertyEditorSupport {
   private static final String[] BOOLEAN_TAGS = new String[]{"true", "false"};

   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else {
         Object newValue = Boolean.valueOf(text);
         this.setValue(newValue);
      }
   }

   public String[] getTags() {
      return BOOLEAN_TAGS;
   }
}
