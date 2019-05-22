package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;

public class CharacterEditor extends PropertyEditorSupport {
   public void setAsText(String text) {
      if (PropertyEditors.isNull(text)) {
         this.setValue((Object)null);
      } else if (text.length() != 1) {
         throw new IllegalArgumentException("Too many (" + text.length() + ") characters: '" + text + "'");
      } else {
         Object newValue = new Character(text.charAt(0));
         this.setValue(newValue);
      }
   }
}
