package com.gzoltar.shaded.org.jacoco.report.internal.xml;

import java.io.IOException;
import java.io.Writer;

public class XMLElement {
   private static final char SPACE = ' ';
   private static final char EQ = '=';
   private static final char LT = '<';
   private static final char GT = '>';
   private static final char QUOT = '"';
   private static final char AMP = '&';
   private static final char SLASH = '/';
   protected final Writer writer;
   private final String name;
   private boolean openTagDone;
   private boolean closed;
   private XMLElement lastchild;

   protected XMLElement(Writer writer, String name) {
      this.writer = writer;
      this.name = name;
      this.openTagDone = false;
      this.closed = false;
      this.lastchild = null;
   }

   protected void beginOpenTag() throws IOException {
      this.writer.write(60);
      this.writer.write(this.name);
   }

   private void finishOpenTag() throws IOException {
      if (!this.openTagDone) {
         this.writer.append('>');
         this.openTagDone = true;
      }

   }

   protected void addChildElement(XMLElement child) throws IOException {
      if (this.closed) {
         throw new IOException(String.format("Element %s already closed.", this.name));
      } else {
         this.finishOpenTag();
         if (this.lastchild != null) {
            this.lastchild.close();
         }

         child.beginOpenTag();
         this.lastchild = child;
      }
   }

   private void quote(String text) throws IOException {
      int len = text.length();

      for(int i = 0; i < len; ++i) {
         char c = text.charAt(i);
         switch(c) {
         case '"':
            this.writer.write("&quot;");
            break;
         case '&':
            this.writer.write("&amp;");
            break;
         case '<':
            this.writer.write("&lt;");
            break;
         case '>':
            this.writer.write("&gt;");
            break;
         default:
            this.writer.write(c);
         }
      }

   }

   public XMLElement attr(String name, String value) throws IOException {
      if (value == null) {
         return this;
      } else if (!this.closed && !this.openTagDone) {
         this.writer.write(32);
         this.writer.write(name);
         this.writer.write(61);
         this.writer.write(34);
         this.quote(value);
         this.writer.write(34);
         return this;
      } else {
         throw new IOException(String.format("Element %s already closed.", this.name));
      }
   }

   public XMLElement attr(String name, int value) throws IOException {
      return this.attr(name, String.valueOf(value));
   }

   public XMLElement attr(String name, long value) throws IOException {
      return this.attr(name, String.valueOf(value));
   }

   public XMLElement text(String text) throws IOException {
      if (this.closed) {
         throw new IOException(String.format("Element %s already closed.", this.name));
      } else {
         this.finishOpenTag();
         if (this.lastchild != null) {
            this.lastchild.close();
         }

         this.quote(text);
         return this;
      }
   }

   public XMLElement element(String name) throws IOException {
      XMLElement element = new XMLElement(this.writer, name);
      this.addChildElement(element);
      return element;
   }

   public void close() throws IOException {
      if (!this.closed) {
         if (this.lastchild != null) {
            this.lastchild.close();
         }

         if (this.openTagDone) {
            this.writer.write(60);
            this.writer.write(47);
            this.writer.write(this.name);
         } else {
            this.writer.write(47);
         }

         this.writer.write(62);
         this.closed = true;
         this.openTagDone = true;
      }

   }
}
