package org.apache.maven.doxia.module.xhtml;

import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.markup.XmlMarkup;

public interface XhtmlMarkup extends XmlMarkup {
   Tag TBODY_TAG = new Tag() {
      public String toString() {
         return "tbody";
      }
   };
}
