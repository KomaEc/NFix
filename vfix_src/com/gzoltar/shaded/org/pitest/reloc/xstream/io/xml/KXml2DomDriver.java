package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import org.kxml2.io.KXmlParser;

public class KXml2DomDriver extends AbstractXppDomDriver {
   public KXml2DomDriver() {
      super(new XmlFriendlyNameCoder());
   }

   public KXml2DomDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   protected XmlPullParser createParser() {
      return new KXmlParser();
   }
}
