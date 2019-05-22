package com.gzoltar.shaded.org.objectweb.asm.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class SAXAdapter {
   private final ContentHandler h;

   protected SAXAdapter(ContentHandler h) {
      this.h = h;
   }

   protected ContentHandler getContentHandler() {
      return this.h;
   }

   protected void addDocumentStart() {
      try {
         this.h.startDocument();
      } catch (SAXException var2) {
         throw new RuntimeException(var2.getMessage(), var2.getException());
      }
   }

   protected void addDocumentEnd() {
      try {
         this.h.endDocument();
      } catch (SAXException var2) {
         throw new RuntimeException(var2.getMessage(), var2.getException());
      }
   }

   protected final void addStart(String name, Attributes attrs) {
      try {
         this.h.startElement("", name, name, attrs);
      } catch (SAXException var4) {
         throw new RuntimeException(var4.getMessage(), var4.getException());
      }
   }

   protected final void addEnd(String name) {
      try {
         this.h.endElement("", name, name);
      } catch (SAXException var3) {
         throw new RuntimeException(var3.getMessage(), var3.getException());
      }
   }

   protected final void addElement(String name, Attributes attrs) {
      this.addStart(name, attrs);
      this.addEnd(name);
   }
}
