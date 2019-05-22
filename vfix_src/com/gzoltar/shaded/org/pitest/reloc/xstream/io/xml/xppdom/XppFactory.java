package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.xppdom;

import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParser;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserException;
import com.gzoltar.shaded.org.pitest.reloc.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class XppFactory {
   public static XmlPullParser createDefaultParser() throws XmlPullParserException {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
      return factory.newPullParser();
   }

   public static XppDom buildDom(String xml) throws XmlPullParserException, IOException {
      return buildDom((Reader)(new StringReader(xml)));
   }

   public static XppDom buildDom(Reader r) throws XmlPullParserException, IOException {
      XmlPullParser parser = createDefaultParser();
      parser.setInput(r);
      return XppDom.build(parser);
   }

   public static XppDom buildDom(InputStream in, String encoding) throws XmlPullParserException, IOException {
      XmlPullParser parser = createDefaultParser();
      parser.setInput(in, encoding);
      return XppDom.build(parser);
   }
}
