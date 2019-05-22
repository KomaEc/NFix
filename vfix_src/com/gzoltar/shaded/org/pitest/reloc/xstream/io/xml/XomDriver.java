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
import java.io.Writer;
import java.net.URL;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class XomDriver extends AbstractXmlDriver {
   private final Builder builder;

   public XomDriver() {
      this(new Builder());
   }

   public XomDriver(Builder builder) {
      this(builder, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public XomDriver(NameCoder nameCoder) {
      this(new Builder(), nameCoder);
   }

   public XomDriver(Builder builder, NameCoder nameCoder) {
      super(nameCoder);
      this.builder = builder;
   }

   /** @deprecated */
   public XomDriver(XmlFriendlyReplacer replacer) {
      this(new Builder(), replacer);
   }

   /** @deprecated */
   public XomDriver(Builder builder, XmlFriendlyReplacer replacer) {
      this((NameCoder)replacer);
   }

   protected Builder getBuilder() {
      return this.builder;
   }

   public HierarchicalStreamReader createReader(Reader text) {
      try {
         Document document = this.builder.build(text);
         return new XomReader(document, this.getNameCoder());
      } catch (ValidityException var3) {
         throw new StreamException(var3);
      } catch (ParsingException var4) {
         throw new StreamException(var4);
      } catch (IOException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      try {
         Document document = this.builder.build(in);
         return new XomReader(document, this.getNameCoder());
      } catch (ValidityException var3) {
         throw new StreamException(var3);
      } catch (ParsingException var4) {
         throw new StreamException(var4);
      } catch (IOException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(URL in) {
      try {
         Document document = this.builder.build(in.toExternalForm());
         return new XomReader(document, this.getNameCoder());
      } catch (ValidityException var3) {
         throw new StreamException(var3);
      } catch (ParsingException var4) {
         throw new StreamException(var4);
      } catch (IOException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamReader createReader(File in) {
      try {
         Document document = this.builder.build(in);
         return new XomReader(document, this.getNameCoder());
      } catch (ValidityException var3) {
         throw new StreamException(var3);
      } catch (ParsingException var4) {
         throw new StreamException(var4);
      } catch (IOException var5) {
         throw new StreamException(var5);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out, this.getNameCoder());
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      return new PrettyPrintWriter(new OutputStreamWriter(out), this.getNameCoder());
   }
}
