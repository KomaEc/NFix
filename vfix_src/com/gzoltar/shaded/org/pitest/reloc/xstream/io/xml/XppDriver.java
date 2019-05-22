package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

public class XppDriver extends AbstractXppDriver {
   private static XmlPullParserFactory factory;

   public XppDriver() {
      super(new XmlFriendlyNameCoder());
   }

   public XppDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   /** @deprecated */
   public XppDriver(XmlFriendlyReplacer replacer) {
      this((NameCoder)replacer);
   }

   protected synchronized XmlPullParser createParser() throws XmlPullParserException {
      if (factory == null) {
         factory = XmlPullParserFactory.newInstance();
      }

      return factory.newPullParser();
   }
}
