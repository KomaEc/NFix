package org.xmlpull.v1.builder;

import java.io.IOException;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public interface XmlPullElement extends XmlElement {
   Iterator children();

   boolean fullyConstructed();

   XmlPullElement readNextChild() throws XmlPullParserException, IOException;

   boolean skipNextChild() throws XmlPullParserException, IOException;

   XmlPullParser nextChildAsPullParser() throws IOException, XmlPullParserException;
}
