package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomDriver extends AbstractXmlDriver {
   private final String encoding;
   private final DocumentBuilderFactory documentBuilderFactory;

   public DomDriver() {
      this((String)null);
   }

   public DomDriver(String encoding) {
      this(encoding, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public DomDriver(String encoding, NameCoder nameCoder) {
      super(nameCoder);
      this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
      this.encoding = encoding;
   }

   /** @deprecated */
   public DomDriver(String encoding, XmlFriendlyReplacer replacer) {
      this(encoding, (NameCoder)replacer);
   }

   public HierarchicalStreamReader createReader(Reader in) {
      return this.createReader(new InputSource(in));
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      return this.createReader(new InputSource(in));
   }

   public HierarchicalStreamReader createReader(URL in) {
      return this.createReader(new InputSource(in.toExternalForm()));
   }

   public HierarchicalStreamReader createReader(File in) {
      return this.createReader(new InputSource(in.toURI().toASCIIString()));
   }

   private HierarchicalStreamReader createReader(InputSource source) {
      try {
         DocumentBuilder documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
         if (this.encoding != null) {
            source.setEncoding(this.encoding);
         }

         Document document = documentBuilder.parse(source);
         return new DomReader(document, this.getNameCoder());
      } catch (FactoryConfigurationError var4) {
         throw new StreamException(var4);
      } catch (ParserConfigurationException var5) {
         throw new StreamException(var5);
      } catch (SAXException var6) {
         throw new StreamException(var6);
      } catch (IOException var7) {
         throw new StreamException(var7);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out, this.getNameCoder());
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      try {
         return this.createWriter((Writer)(this.encoding != null ? new OutputStreamWriter(out, this.encoding) : new OutputStreamWriter(out)));
      } catch (UnsupportedEncodingException var3) {
         throw new StreamException(var3);
      }
   }
}
