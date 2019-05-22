package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.xppdom;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.mxp1.MXParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import java.io.Reader;

/** @deprecated */
public class Xpp3DomBuilder {
   /** @deprecated */
   public static Xpp3Dom build(Reader reader) throws Exception {
      XmlPullParser parser = new MXParser();
      parser.setInput(reader);

      Xpp3Dom var2;
      try {
         var2 = (Xpp3Dom)XppDom.build(parser);
      } finally {
         reader.close();
      }

      return var2;
   }
}
