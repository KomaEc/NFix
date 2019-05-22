package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ReaderWrapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class StaxDriver extends AbstractXmlDriver {
   private QNameMap qnameMap;
   private XMLInputFactory inputFactory;
   private XMLOutputFactory outputFactory;

   public StaxDriver() {
      this(new QNameMap());
   }

   public StaxDriver(QNameMap qnameMap) {
      this(qnameMap, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public StaxDriver(QNameMap qnameMap, NameCoder nameCoder) {
      super(nameCoder);
      this.qnameMap = qnameMap;
   }

   public StaxDriver(NameCoder nameCoder) {
      this(new QNameMap(), nameCoder);
   }

   /** @deprecated */
   public StaxDriver(QNameMap qnameMap, XmlFriendlyReplacer replacer) {
      this(qnameMap, (NameCoder)replacer);
   }

   /** @deprecated */
   public StaxDriver(XmlFriendlyReplacer replacer) {
      this(new QNameMap(), (NameCoder)replacer);
   }

   public HierarchicalStreamReader createReader(Reader xml) {
      try {
         return this.createStaxReader(this.createParser(xml));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      try {
         return this.createStaxReader(this.createParser(in));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamReader createReader(URL in) {
      try {
         final InputStream stream = in.openStream();
         HierarchicalStreamReader reader = this.createStaxReader(this.createParser((Source)(new StreamSource(stream, in.toExternalForm()))));
         return new ReaderWrapper(reader) {
            public void close() {
               super.close();

               try {
                  stream.close();
               } catch (IOException var2) {
               }

            }
         };
      } catch (XMLStreamException var4) {
         throw new StreamException(var4);
      } catch (IOException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(File in) {
      try {
         final InputStream stream = new FileInputStream(in);
         HierarchicalStreamReader reader = this.createStaxReader(this.createParser((Source)(new StreamSource(stream, in.toURI().toASCIIString()))));
         return new ReaderWrapper(reader) {
            public void close() {
               super.close();

               try {
                  stream.close();
               } catch (IOException var2) {
               }

            }
         };
      } catch (XMLStreamException var4) {
         throw new StreamException(var4);
      } catch (FileNotFoundException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      try {
         return this.createStaxWriter(this.getOutputFactory().createXMLStreamWriter(out));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      try {
         return this.createStaxWriter(this.getOutputFactory().createXMLStreamWriter(out));
      } catch (XMLStreamException var3) {
         throw new StreamException(var3);
      }
   }

   public AbstractPullReader createStaxReader(XMLStreamReader in) {
      return new StaxReader(this.qnameMap, in, this.getNameCoder());
   }

   public StaxWriter createStaxWriter(XMLStreamWriter out, boolean writeStartEndDocument) throws XMLStreamException {
      return new StaxWriter(this.qnameMap, out, writeStartEndDocument, this.isRepairingNamespace(), this.getNameCoder());
   }

   public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException {
      return this.createStaxWriter(out, true);
   }

   public QNameMap getQnameMap() {
      return this.qnameMap;
   }

   public void setQnameMap(QNameMap qnameMap) {
      this.qnameMap = qnameMap;
   }

   public XMLInputFactory getInputFactory() {
      if (this.inputFactory == null) {
         this.inputFactory = this.createInputFactory();
      }

      return this.inputFactory;
   }

   public XMLOutputFactory getOutputFactory() {
      if (this.outputFactory == null) {
         this.outputFactory = this.createOutputFactory();
      }

      return this.outputFactory;
   }

   public boolean isRepairingNamespace() {
      return Boolean.TRUE.equals(this.getOutputFactory().getProperty("javax.xml.stream.isRepairingNamespaces"));
   }

   public void setRepairingNamespace(boolean repairing) {
      this.getOutputFactory().setProperty("javax.xml.stream.isRepairingNamespaces", repairing ? Boolean.TRUE : Boolean.FALSE);
   }

   protected XMLStreamReader createParser(Reader xml) throws XMLStreamException {
      return this.getInputFactory().createXMLStreamReader(xml);
   }

   protected XMLStreamReader createParser(InputStream xml) throws XMLStreamException {
      return this.getInputFactory().createXMLStreamReader(xml);
   }

   protected XMLStreamReader createParser(Source source) throws XMLStreamException {
      return this.getInputFactory().createXMLStreamReader(source);
   }

   protected XMLInputFactory createInputFactory() {
      return XMLInputFactory.newInstance();
   }

   protected XMLOutputFactory createOutputFactory() {
      return XMLOutputFactory.newInstance();
   }
}
