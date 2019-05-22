package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.mxp1.MXParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.AbstractXppDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.XmlFriendlyNameCoder;

public class PitXmlDriver extends AbstractXppDriver {
   public PitXmlDriver() {
      super(new XmlFriendlyNameCoder());
   }

   protected synchronized XmlPullParser createParser() throws XmlPullParserException {
      return new MXParser();
   }
}
