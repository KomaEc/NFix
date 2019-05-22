package com.gzoltar.shaded.org.objectweb.asm.xml;

import com.gzoltar.shaded.org.objectweb.asm.ClassReader;
import com.gzoltar.shaded.org.objectweb.asm.ClassWriter;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Processor {
   public static final int BYTECODE = 1;
   public static final int MULTI_XML = 2;
   public static final int SINGLE_XML = 3;
   private static final String SINGLE_XML_NAME = "classes.xml";
   private final int inRepresentation;
   private final int outRepresentation;
   private final InputStream input;
   private final OutputStream output;
   private final Source xslt;
   private int n = 0;

   public Processor(int inRepresenation, int outRepresentation, InputStream input, OutputStream output, Source xslt) {
      this.inRepresentation = inRepresenation;
      this.outRepresentation = outRepresentation;
      this.input = input;
      this.output = output;
      this.xslt = xslt;
   }

   public int process() throws TransformerException, IOException, SAXException {
      ZipInputStream zis = new ZipInputStream(this.input);
      ZipOutputStream zos = new ZipOutputStream(this.output);
      OutputStreamWriter osw = new OutputStreamWriter(zos);
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      TransformerFactory tf = TransformerFactory.newInstance();
      if (tf.getFeature("http://javax.xml.transform.sax.SAXSource/feature") && tf.getFeature("http://javax.xml.transform.sax.SAXResult/feature")) {
         SAXTransformerFactory saxtf = (SAXTransformerFactory)tf;
         Templates templates = null;
         if (this.xslt != null) {
            templates = saxtf.newTemplates(this.xslt);
         }

         Processor.EntryElement entryElement = this.getEntryElement(zos);
         ContentHandler outDocHandler = null;
         switch(this.outRepresentation) {
         case 1:
            outDocHandler = new Processor.OutputSlicingHandler(new Processor.ASMContentHandlerFactory(zos), entryElement, false);
            break;
         case 2:
            outDocHandler = new Processor.OutputSlicingHandler(new Processor.SAXWriterFactory(osw, true), entryElement, true);
            break;
         case 3:
            ZipEntry outputEntry = new ZipEntry("classes.xml");
            zos.putNextEntry(outputEntry);
            outDocHandler = new Processor.SAXWriter(osw, false);
         }

         Object inDocHandler;
         if (templates == null) {
            inDocHandler = outDocHandler;
         } else {
            inDocHandler = new Processor.InputSlicingHandler("class", (ContentHandler)outDocHandler, new Processor.TransformerHandlerFactory(saxtf, templates, (ContentHandler)outDocHandler));
         }

         Processor.ContentHandlerFactory inDocHandlerFactory = new Processor.SubdocumentHandlerFactory((ContentHandler)inDocHandler);
         if (inDocHandler != null && this.inRepresentation != 3) {
            ((ContentHandler)inDocHandler).startDocument();
            ((ContentHandler)inDocHandler).startElement("", "classes", "classes", new AttributesImpl());
         }

         int i;
         ZipEntry ze;
         for(i = 0; (ze = zis.getNextEntry()) != null; ++i) {
            this.update(ze.getName(), this.n++);
            if (this.isClassEntry(ze)) {
               this.processEntry(zis, ze, inDocHandlerFactory);
            } else {
               OutputStream os = entryElement.openEntry(this.getName(ze));
               this.copyEntry(zis, os);
               entryElement.closeEntry();
            }
         }

         if (inDocHandler != null && this.inRepresentation != 3) {
            ((ContentHandler)inDocHandler).endElement("", "classes", "classes");
            ((ContentHandler)inDocHandler).endDocument();
         }

         if (this.outRepresentation == 3) {
            zos.closeEntry();
         }

         zos.flush();
         zos.close();
         return i;
      } else {
         return 0;
      }
   }

   private void copyEntry(InputStream is, OutputStream os) throws IOException {
      if (this.outRepresentation != 3) {
         byte[] buff = new byte[2048];

         int i;
         while((i = is.read(buff)) != -1) {
            os.write(buff, 0, i);
         }

      }
   }

   private boolean isClassEntry(ZipEntry ze) {
      String name = ze.getName();
      return this.inRepresentation == 3 && name.equals("classes.xml") || name.endsWith(".class") || name.endsWith(".class.xml");
   }

   private void processEntry(ZipInputStream zis, ZipEntry ze, Processor.ContentHandlerFactory handlerFactory) {
      ContentHandler handler = handlerFactory.createContentHandler();

      try {
         boolean singleInputDocument = this.inRepresentation == 3;
         if (this.inRepresentation == 1) {
            ClassReader cr = new ClassReader(readEntry(zis, ze));
            cr.accept(new SAXClassAdapter(handler, singleInputDocument), 0);
         } else {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource((InputStream)(singleInputDocument ? new Processor.ProtectedInputStream(zis) : new ByteArrayInputStream(readEntry(zis, ze)))));
         }
      } catch (Exception var7) {
         this.update(ze.getName(), 0);
         this.update(var7, 0);
      }

   }

   private Processor.EntryElement getEntryElement(ZipOutputStream zos) {
      return (Processor.EntryElement)(this.outRepresentation == 3 ? new Processor.SingleDocElement(zos) : new Processor.ZipEntryElement(zos));
   }

   private String getName(ZipEntry ze) {
      String name = ze.getName();
      if (this.isClassEntry(ze)) {
         if (this.inRepresentation != 1 && this.outRepresentation == 1) {
            name = name.substring(0, name.length() - 4);
         } else if (this.inRepresentation == 1 && this.outRepresentation != 1) {
            name = name + ".xml";
         }
      }

      return name;
   }

   private static byte[] readEntry(InputStream zis, ZipEntry ze) throws IOException {
      long size = ze.getSize();
      int n;
      if (size > -1L) {
         byte[] buff = new byte[(int)size];

         for(int k = 0; (n = zis.read(buff, k, buff.length - k)) > 0; k += n) {
         }

         return buff;
      } else {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         byte[] buff = new byte[4096];

         while((n = zis.read(buff)) != -1) {
            bos.write(buff, 0, n);
         }

         return bos.toByteArray();
      }
   }

   protected void update(Object arg, int n) {
      if (arg instanceof Throwable) {
         ((Throwable)arg).printStackTrace();
      } else if (n % 100 == 0) {
         System.err.println(n + " " + arg);
      }

   }

   public static void main(String[] args) throws Exception {
      if (args.length < 2) {
         showUsage();
      } else {
         int inRepresentation = getRepresentation(args[0]);
         int outRepresentation = getRepresentation(args[1]);
         InputStream is = System.in;
         OutputStream os = new BufferedOutputStream(System.out);
         Source xslt = null;

         for(int i = 2; i < args.length; ++i) {
            if ("-in".equals(args[i])) {
               ++i;
               is = new FileInputStream(args[i]);
            } else if ("-out".equals(args[i])) {
               ++i;
               os = new BufferedOutputStream(new FileOutputStream(args[i]));
            } else {
               if (!"-xslt".equals(args[i])) {
                  showUsage();
                  return;
               }

               ++i;
               xslt = new StreamSource(new FileInputStream(args[i]));
            }
         }

         if (inRepresentation != 0 && outRepresentation != 0) {
            Processor m = new Processor(inRepresentation, outRepresentation, (InputStream)is, os, xslt);
            long l1 = System.currentTimeMillis();
            int n = m.process();
            long l2 = System.currentTimeMillis();
            System.err.println(n);
            System.err.println(l2 - l1 + "ms  " + 1000.0F * (float)n / (float)(l2 - l1) + " resources/sec");
         } else {
            showUsage();
         }
      }
   }

   private static int getRepresentation(String s) {
      if ("code".equals(s)) {
         return 1;
      } else if ("xml".equals(s)) {
         return 2;
      } else {
         return "singlexml".equals(s) ? 3 : 0;
      }
   }

   private static void showUsage() {
      System.err.println("Usage: Main <in format> <out format> [-in <input jar>] [-out <output jar>] [-xslt <xslt fiel>]");
      System.err.println("  when -in or -out is omitted sysin and sysout would be used");
      System.err.println("  <in format> and <out format> - code | xml | singlexml");
   }

   private static final class ZipEntryElement implements Processor.EntryElement {
      private ZipOutputStream zos;

      ZipEntryElement(ZipOutputStream zos) {
         this.zos = zos;
      }

      public OutputStream openEntry(String name) throws IOException {
         ZipEntry entry = new ZipEntry(name);
         this.zos.putNextEntry(entry);
         return this.zos;
      }

      public void closeEntry() throws IOException {
         this.zos.flush();
         this.zos.closeEntry();
      }
   }

   private static final class SingleDocElement implements Processor.EntryElement {
      private final OutputStream os;

      SingleDocElement(OutputStream os) {
         this.os = os;
      }

      public OutputStream openEntry(String name) throws IOException {
         return this.os;
      }

      public void closeEntry() throws IOException {
         this.os.flush();
      }
   }

   private interface EntryElement {
      OutputStream openEntry(String var1) throws IOException;

      void closeEntry() throws IOException;
   }

   private static final class OutputSlicingHandler extends DefaultHandler {
      private final String subdocumentRoot = "class";
      private Processor.ContentHandlerFactory subdocumentHandlerFactory;
      private final Processor.EntryElement entryElement;
      private boolean isXml;
      private boolean subdocument = false;
      private ContentHandler subdocumentHandler;

      OutputSlicingHandler(Processor.ContentHandlerFactory subdocumentHandlerFactory, Processor.EntryElement entryElement, boolean isXml) {
         this.subdocumentHandlerFactory = subdocumentHandlerFactory;
         this.entryElement = entryElement;
         this.isXml = isXml;
      }

      public final void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
         } else if (localName.equals(this.subdocumentRoot)) {
            String name = list.getValue("name");
            if (name == null || name.length() == 0) {
               throw new SAXException("Class element without name attribute.");
            }

            try {
               this.entryElement.openEntry(this.isXml ? name + ".class.xml" : name + ".class");
            } catch (IOException var7) {
               throw new SAXException(var7.toString(), var7);
            }

            this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler();
            this.subdocumentHandler.startDocument();
            this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
            this.subdocument = true;
         }

      }

      public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.endElement(namespaceURI, localName, qName);
            if (localName.equals(this.subdocumentRoot)) {
               this.subdocumentHandler.endDocument();
               this.subdocument = false;

               try {
                  this.entryElement.closeEntry();
               } catch (IOException var5) {
                  throw new SAXException(var5.toString(), var5);
               }
            }
         }

      }

      public final void startDocument() throws SAXException {
      }

      public final void endDocument() throws SAXException {
      }

      public final void characters(char[] buff, int offset, int size) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.characters(buff, offset, size);
         }

      }
   }

   private static final class InputSlicingHandler extends DefaultHandler {
      private String subdocumentRoot;
      private final ContentHandler rootHandler;
      private Processor.ContentHandlerFactory subdocumentHandlerFactory;
      private boolean subdocument = false;
      private ContentHandler subdocumentHandler;

      InputSlicingHandler(String subdocumentRoot, ContentHandler rootHandler, Processor.ContentHandlerFactory subdocumentHandlerFactory) {
         this.subdocumentRoot = subdocumentRoot;
         this.rootHandler = rootHandler;
         this.subdocumentHandlerFactory = subdocumentHandlerFactory;
      }

      public final void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
         } else if (localName.equals(this.subdocumentRoot)) {
            this.subdocumentHandler = this.subdocumentHandlerFactory.createContentHandler();
            this.subdocumentHandler.startDocument();
            this.subdocumentHandler.startElement(namespaceURI, localName, qName, list);
            this.subdocument = true;
         } else if (this.rootHandler != null) {
            this.rootHandler.startElement(namespaceURI, localName, qName, list);
         }

      }

      public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.endElement(namespaceURI, localName, qName);
            if (localName.equals(this.subdocumentRoot)) {
               this.subdocumentHandler.endDocument();
               this.subdocument = false;
            }
         } else if (this.rootHandler != null) {
            this.rootHandler.endElement(namespaceURI, localName, qName);
         }

      }

      public final void startDocument() throws SAXException {
         if (this.rootHandler != null) {
            this.rootHandler.startDocument();
         }

      }

      public final void endDocument() throws SAXException {
         if (this.rootHandler != null) {
            this.rootHandler.endDocument();
         }

      }

      public final void characters(char[] buff, int offset, int size) throws SAXException {
         if (this.subdocument) {
            this.subdocumentHandler.characters(buff, offset, size);
         } else if (this.rootHandler != null) {
            this.rootHandler.characters(buff, offset, size);
         }

      }
   }

   private static final class SAXWriter extends DefaultHandler implements LexicalHandler {
      private static final char[] OFF = "                                                                                                        ".toCharArray();
      private Writer w;
      private final boolean optimizeEmptyElements;
      private boolean openElement = false;
      private int ident = 0;

      SAXWriter(Writer w, boolean optimizeEmptyElements) {
         this.w = w;
         this.optimizeEmptyElements = optimizeEmptyElements;
      }

      public final void startElement(String ns, String localName, String qName, Attributes atts) throws SAXException {
         try {
            this.closeElement();
            this.writeIdent();
            this.w.write('<' + qName);
            if (atts != null && atts.getLength() > 0) {
               this.writeAttributes(atts);
            }

            if (this.optimizeEmptyElements) {
               this.openElement = true;
            } else {
               this.w.write(">\n");
            }

            this.ident += 2;
         } catch (IOException var6) {
            throw new SAXException(var6);
         }
      }

      public final void endElement(String ns, String localName, String qName) throws SAXException {
         this.ident -= 2;

         try {
            if (this.openElement) {
               this.w.write("/>\n");
               this.openElement = false;
            } else {
               this.writeIdent();
               this.w.write("</" + qName + ">\n");
            }

         } catch (IOException var5) {
            throw new SAXException(var5);
         }
      }

      public final void endDocument() throws SAXException {
         try {
            this.w.flush();
         } catch (IOException var2) {
            throw new SAXException(var2);
         }
      }

      public final void comment(char[] ch, int off, int len) throws SAXException {
         try {
            this.closeElement();
            this.writeIdent();
            this.w.write("<!-- ");
            this.w.write(ch, off, len);
            this.w.write(" -->\n");
         } catch (IOException var5) {
            throw new SAXException(var5);
         }
      }

      public final void startDTD(String arg0, String arg1, String arg2) throws SAXException {
      }

      public final void endDTD() throws SAXException {
      }

      public final void startEntity(String arg0) throws SAXException {
      }

      public final void endEntity(String arg0) throws SAXException {
      }

      public final void startCDATA() throws SAXException {
      }

      public final void endCDATA() throws SAXException {
      }

      private final void writeAttributes(Attributes atts) throws IOException {
         StringBuilder sb = new StringBuilder();
         int len = atts.getLength();

         for(int i = 0; i < len; ++i) {
            sb.append(' ').append(atts.getLocalName(i)).append("=\"").append(esc(atts.getValue(i))).append('"');
         }

         this.w.write(sb.toString());
      }

      private static final String esc(String str) {
         StringBuilder sb = new StringBuilder(str.length());

         for(int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            switch(ch) {
            case '"':
               sb.append("&quot;");
               break;
            case '&':
               sb.append("&amp;");
               break;
            case '<':
               sb.append("&lt;");
               break;
            case '>':
               sb.append("&gt;");
               break;
            default:
               if (ch > 127) {
                  sb.append("&#").append(Integer.toString(ch)).append(';');
               } else {
                  sb.append(ch);
               }
            }
         }

         return sb.toString();
      }

      private final void writeIdent() throws IOException {
         int n = this.ident;

         while(n > 0) {
            if (n > OFF.length) {
               this.w.write(OFF);
               n -= OFF.length;
            } else {
               this.w.write(OFF, 0, n);
               n = 0;
            }
         }

      }

      private final void closeElement() throws IOException {
         if (this.openElement) {
            this.w.write(">\n");
         }

         this.openElement = false;
      }
   }

   private static final class SubdocumentHandlerFactory implements Processor.ContentHandlerFactory {
      private final ContentHandler subdocumentHandler;

      SubdocumentHandlerFactory(ContentHandler subdocumentHandler) {
         this.subdocumentHandler = subdocumentHandler;
      }

      public final ContentHandler createContentHandler() {
         return this.subdocumentHandler;
      }
   }

   private static final class TransformerHandlerFactory implements Processor.ContentHandlerFactory {
      private SAXTransformerFactory saxtf;
      private final Templates templates;
      private ContentHandler outputHandler;

      TransformerHandlerFactory(SAXTransformerFactory saxtf, Templates templates, ContentHandler outputHandler) {
         this.saxtf = saxtf;
         this.templates = templates;
         this.outputHandler = outputHandler;
      }

      public final ContentHandler createContentHandler() {
         try {
            TransformerHandler handler = this.saxtf.newTransformerHandler(this.templates);
            handler.setResult(new SAXResult(this.outputHandler));
            return handler;
         } catch (TransformerConfigurationException var2) {
            throw new RuntimeException(var2.toString());
         }
      }
   }

   private static final class ASMContentHandlerFactory implements Processor.ContentHandlerFactory {
      final OutputStream os;

      ASMContentHandlerFactory(OutputStream os) {
         this.os = os;
      }

      public final ContentHandler createContentHandler() {
         final ClassWriter cw = new ClassWriter(1);
         return new ASMContentHandler(cw) {
            public void endDocument() throws SAXException {
               try {
                  ASMContentHandlerFactory.this.os.write(cw.toByteArray());
               } catch (IOException var2) {
                  throw new SAXException(var2);
               }
            }
         };
      }
   }

   private static final class SAXWriterFactory implements Processor.ContentHandlerFactory {
      private final Writer w;
      private final boolean optimizeEmptyElements;

      SAXWriterFactory(Writer w, boolean optimizeEmptyElements) {
         this.w = w;
         this.optimizeEmptyElements = optimizeEmptyElements;
      }

      public final ContentHandler createContentHandler() {
         return new Processor.SAXWriter(this.w, this.optimizeEmptyElements);
      }
   }

   private interface ContentHandlerFactory {
      ContentHandler createContentHandler();
   }

   private static final class ProtectedInputStream extends InputStream {
      private final InputStream is;

      ProtectedInputStream(InputStream is) {
         this.is = is;
      }

      public final void close() throws IOException {
      }

      public final int read() throws IOException {
         return this.is.read();
      }

      public final int read(byte[] b, int off, int len) throws IOException {
         return this.is.read(b, off, len);
      }

      public final int available() throws IOException {
         return this.is.available();
      }
   }
}
