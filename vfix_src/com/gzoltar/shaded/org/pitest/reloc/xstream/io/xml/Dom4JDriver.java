package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.FilterWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4JDriver extends AbstractXmlDriver {
   private DocumentFactory documentFactory;
   private OutputFormat outputFormat;

   public Dom4JDriver() {
      this(new XmlFriendlyNameCoder());
   }

   public Dom4JDriver(NameCoder nameCoder) {
      this(new DocumentFactory(), OutputFormat.createPrettyPrint(), nameCoder);
      this.outputFormat.setTrimText(false);
   }

   public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat) {
      this(documentFactory, outputFormat, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat, NameCoder nameCoder) {
      super(nameCoder);
      this.documentFactory = documentFactory;
      this.outputFormat = outputFormat;
   }

   /** @deprecated */
   public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat, XmlFriendlyReplacer replacer) {
      this(documentFactory, outputFormat, (NameCoder)replacer);
   }

   public DocumentFactory getDocumentFactory() {
      return this.documentFactory;
   }

   public void setDocumentFactory(DocumentFactory documentFactory) {
      this.documentFactory = documentFactory;
   }

   public OutputFormat getOutputFormat() {
      return this.outputFormat;
   }

   public void setOutputFormat(OutputFormat outputFormat) {
      this.outputFormat = outputFormat;
   }

   public HierarchicalStreamReader createReader(Reader text) {
      try {
         SAXReader reader = new SAXReader();
         Document document = reader.read(text);
         return new Dom4JReader(document, this.getNameCoder());
      } catch (DocumentException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      try {
         SAXReader reader = new SAXReader();
         Document document = reader.read(in);
         return new Dom4JReader(document, this.getNameCoder());
      } catch (DocumentException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamReader createReader(URL in) {
      try {
         SAXReader reader = new SAXReader();
         Document document = reader.read(in);
         return new Dom4JReader(document, this.getNameCoder());
      } catch (DocumentException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamReader createReader(File in) {
      try {
         SAXReader reader = new SAXReader();
         Document document = reader.read(in);
         return new Dom4JReader(document, this.getNameCoder());
      } catch (DocumentException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      final HierarchicalStreamWriter[] writer = new HierarchicalStreamWriter[1];
      FilterWriter filter = new FilterWriter(out) {
         public void close() {
            writer[0].close();
         }
      };
      writer[0] = new Dom4JXmlWriter(new XMLWriter(filter, this.outputFormat), this.getNameCoder());
      return writer[0];
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      Writer writer = new OutputStreamWriter(out);
      return this.createWriter((Writer)writer);
   }
}
