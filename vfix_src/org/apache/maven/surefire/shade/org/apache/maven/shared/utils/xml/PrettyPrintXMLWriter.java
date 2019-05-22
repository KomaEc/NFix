package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.LinkedList;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.Os;

public class PrettyPrintXMLWriter implements XMLWriter {
   private PrintWriter writer;
   private LinkedList<String> elementStack;
   private boolean processingElement;
   private boolean documentStarted;
   private boolean endOnSameLine;
   private int depth;
   private String lineIndent;
   private String lineSeparator;
   private String encoding;
   private String docType;

   public PrettyPrintXMLWriter(PrintWriter writer, String lineIndent) {
      this((PrintWriter)writer, lineIndent, (String)null, (String)null);
   }

   public PrettyPrintXMLWriter(Writer writer, String lineIndent) {
      this(new PrintWriter(writer), lineIndent);
   }

   public PrettyPrintXMLWriter(PrintWriter writer) {
      this((PrintWriter)writer, (String)null, (String)null);
   }

   public PrettyPrintXMLWriter(Writer writer) {
      this(new PrintWriter(writer));
   }

   public PrettyPrintXMLWriter(PrintWriter writer, String lineIndent, String encoding, String doctype) {
      this(writer, lineIndent, Os.LINE_SEP, encoding, doctype);
   }

   public PrettyPrintXMLWriter(Writer writer, String lineIndent, String encoding, String doctype) {
      this(new PrintWriter(writer), lineIndent, encoding, doctype);
   }

   public PrettyPrintXMLWriter(PrintWriter writer, String encoding, String doctype) {
      this(writer, "  ", encoding, doctype);
   }

   public PrettyPrintXMLWriter(Writer writer, String encoding, String doctype) {
      this(new PrintWriter(writer), encoding, doctype);
   }

   public PrettyPrintXMLWriter(PrintWriter writer, String lineIndent, String lineSeparator, String encoding, String doctype) {
      this.elementStack = new LinkedList();
      this.processingElement = false;
      this.documentStarted = false;
      this.endOnSameLine = false;
      this.depth = 0;
      this.writer = writer;
      this.lineIndent = lineIndent;
      this.lineSeparator = lineSeparator;
      this.encoding = encoding;
      this.docType = doctype;
      this.depth = 0;
   }

   public void addAttribute(String key, String value) {
      if (!this.processingElement) {
         throw new IllegalStateException("currently processing no element");
      } else {
         this.writer.write(32);
         this.writer.write(key);
         this.writer.write(61);
         this.writer.write(XMLEncode.xmlEncodeTextForAttribute(value, '"'));
      }
   }

   public void setEncoding(String encoding) {
      if (this.documentStarted) {
         throw new IllegalStateException("Document headers already written!");
      } else {
         this.encoding = encoding;
      }
   }

   public void setDocType(String docType) {
      if (this.documentStarted) {
         throw new IllegalStateException("Document headers already written!");
      } else {
         this.docType = docType;
      }
   }

   public void setLineSeparator(String lineSeparator) {
      if (this.documentStarted) {
         throw new IllegalStateException("Document headers already written!");
      } else {
         this.lineSeparator = lineSeparator;
      }
   }

   public void setLineIndenter(String lineIndent) {
      if (this.documentStarted) {
         throw new IllegalStateException("Document headers already written!");
      } else {
         this.lineIndent = lineIndent;
      }
   }

   public void startElement(String elementName) {
      boolean firstLine = this.ensureDocumentStarted();
      this.completePreviouslyOpenedElement();
      if (!firstLine) {
         this.newLine();
      }

      this.writer.write(60);
      this.writer.write(elementName);
      this.processingElement = true;
      ++this.depth;
      this.elementStack.addLast(elementName);
   }

   public void writeText(String text) {
      this.ensureDocumentStarted();
      this.completePreviouslyOpenedElement();
      this.writer.write(XMLEncode.xmlEncodeText(text));
      this.endOnSameLine = true;
   }

   public void writeMarkup(String markup) {
      this.ensureDocumentStarted();
      this.completePreviouslyOpenedElement();
      this.writer.write(markup);
   }

   public void endElement() {
      --this.depth;
      if (this.processingElement) {
         this.writer.write("/>");
         this.elementStack.removeLast();
         this.processingElement = false;
      } else {
         if (!this.endOnSameLine) {
            this.newLine();
         }

         this.writer.write("</" + (String)this.elementStack.removeLast() + ">");
      }

      this.endOnSameLine = false;
   }

   private boolean ensureDocumentStarted() {
      if (this.documentStarted) {
         return false;
      } else {
         if (this.docType != null || this.encoding != null) {
            this.writeDocumentHeader();
         }

         this.documentStarted = true;
         return true;
      }
   }

   private void writeDocumentHeader() {
      this.writer.write("<?xml version=\"1.0\"");
      if (this.encoding != null) {
         this.writer.write(" encoding=\"" + this.encoding + "\"");
      }

      this.writer.write("?>");
      this.newLine();
      if (this.docType != null) {
         this.newLine();
         this.writer.write("<!DOCTYPE " + this.docType + ">");
      }

   }

   private void newLine() {
      this.writer.write(this.lineSeparator);

      for(int i = 0; i < this.depth; ++i) {
         this.writer.write(this.lineIndent);
      }

   }

   private void completePreviouslyOpenedElement() {
      if (this.processingElement) {
         this.writer.write(62);
         this.processingElement = false;
      }

   }
}
