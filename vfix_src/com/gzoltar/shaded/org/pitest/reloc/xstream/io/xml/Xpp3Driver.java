package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.mxp1.MXParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

public class Xpp3Driver extends AbstractXppDriver {
   public Xpp3Driver() {
      super(new XmlFriendlyNameCoder());
   }

   public Xpp3Driver(NameCoder nameCoder) {
      super(nameCoder);
   }

   protected XmlPullParser createParser() {
      return new MXParser();
   }
}
