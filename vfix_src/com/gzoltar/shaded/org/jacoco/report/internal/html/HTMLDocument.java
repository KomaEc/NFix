package com.gzoltar.shaded.org.jacoco.report.internal.html;

import com.gzoltar.shaded.org.jacoco.report.internal.xml.XMLDocument;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class HTMLDocument extends XMLDocument {
   private static final String ROOT = "html";
   private static final String PUBID = "-//W3C//DTD XHTML 1.0 Strict//EN";
   private static final String SYSTEM = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
   private static final String XMLNS = "xmlns";
   private static final String XHTML_NAMESPACE_URL = "http://www.w3.org/1999/xhtml";

   public HTMLDocument(Writer writer, String encoding) throws IOException {
      super("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", encoding, false, writer);
      this.attr("xmlns", "http://www.w3.org/1999/xhtml");
   }

   public HTMLDocument(OutputStream output, String encoding) throws IOException {
      super("html", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", encoding, false, output);
      this.attr("xmlns", "http://www.w3.org/1999/xhtml");
   }

   public HTMLElement element(String name) throws IOException {
      HTMLElement element = new HTMLElement(this.writer, name);
      this.addChildElement(element);
      return element;
   }

   public HTMLElement head() throws IOException {
      return this.element("head");
   }

   public HTMLElement body() throws IOException {
      return this.element("body");
   }
}
