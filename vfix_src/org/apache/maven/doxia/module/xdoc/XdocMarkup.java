package org.apache.maven.doxia.module.xdoc;

import javax.swing.text.html.HTML.Tag;
import org.apache.maven.doxia.markup.XmlMarkup;

public interface XdocMarkup extends XmlMarkup {
   Tag AUTHOR_TAG = new Tag() {
      public String toString() {
         return "author";
      }
   };
   Tag DATE_TAG = new Tag() {
      public String toString() {
         return "date";
      }
   };
   Tag DOCUMENT_TAG = new Tag() {
      public String toString() {
         return "document";
      }
   };
   Tag MACRO_TAG = new Tag() {
      public String toString() {
         return "macro";
      }
   };
   Tag PROPERTIES_TAG = new Tag() {
      public String toString() {
         return "properties";
      }
   };
   Tag SECTION_TAG = new Tag() {
      public String toString() {
         return "section";
      }
   };
   Tag SOURCE_TAG = new Tag() {
      public String toString() {
         return "source";
      }
   };
   Tag SUBSECTION_TAG = new Tag() {
      public String toString() {
         return "subsection";
      }
   };
}
