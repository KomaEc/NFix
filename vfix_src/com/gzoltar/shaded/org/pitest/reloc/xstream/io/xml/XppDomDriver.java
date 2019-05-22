package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

public class XppDomDriver extends AbstractXppDomDriver {
   private static XmlPullParserFactory factory;

   public XppDomDriver() {
      super(new XmlFriendlyNameCoder());
   }

   public XppDomDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   /** @deprecated */
   public XppDomDriver(XmlFriendlyReplacer replacer) {
      super(replacer);
   }

   protected synchronized XmlPullParser createParser() throws XmlPullParserException {
      if (factory == null) {
         factory = XmlPullParserFactory.newInstance((String)null, XppDomDriver.class);
      }

      return factory.newPullParser();
   }
}
