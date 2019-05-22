package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractDriver;
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
import java.io.Writer;
import java.net.URL;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class JDom2Driver extends AbstractDriver {
   public JDom2Driver() {
      super(new XmlFriendlyNameCoder());
   }

   public JDom2Driver(NameCoder nameCoder) {
      super(nameCoder);
   }

   public HierarchicalStreamReader createReader(Reader reader) {
      try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(reader);
         return new JDom2Reader(document, this.getNameCoder());
      } catch (IOException var4) {
         throw new StreamException(var4);
      } catch (JDOMException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(in);
         return new JDom2Reader(document, this.getNameCoder());
      } catch (IOException var4) {
         throw new StreamException(var4);
      } catch (JDOMException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(URL in) {
      try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(in);
         return new JDom2Reader(document, this.getNameCoder());
      } catch (IOException var4) {
         throw new StreamException(var4);
      } catch (JDOMException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(File in) {
      try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(in);
         return new JDom2Reader(document, this.getNameCoder());
      } catch (IOException var4) {
         throw new StreamException(var4);
      } catch (JDOMException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out, this.getNameCoder());
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      return new PrettyPrintWriter(new OutputStreamWriter(out));
   }
}
