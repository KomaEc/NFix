package org.jboss.util.propertyeditor;

import org.w3c.dom.Document;

public class ElementEditor extends DocumentEditor {
   public void setAsText(String text) {
      Document d = this.getAsDocument(text);
      this.setValue(d.getDocumentElement());
   }
}
