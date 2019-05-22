package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.XmlHeaderAwareReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.xppdom.XppDom;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public abstract class AbstractXppDomDriver extends AbstractXmlDriver {
   public AbstractXppDomDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   public HierarchicalStreamReader createReader(Reader in) {
      try {
         XmlPullParser parser = this.createParser();
         parser.setInput(in);
         return new XppDomReader(XppDom.build(parser), this.getNameCoder());
      } catch (XmlPullParserException var3) {
         throw new StreamException(var3);
      } catch (IOException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      try {
         return this.createReader((Reader)(new XmlHeaderAwareReader(in)));
      } catch (UnsupportedEncodingException var3) {
         throw new StreamException(var3);
      } catch (IOException var4) {
         throw new StreamException(var4);
      }
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      return new PrettyPrintWriter(out, this.getNameCoder());
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      return this.createWriter((Writer)(new OutputStreamWriter(out)));
   }

   protected abstract XmlPullParser createParser() throws XmlPullParserException;
}
