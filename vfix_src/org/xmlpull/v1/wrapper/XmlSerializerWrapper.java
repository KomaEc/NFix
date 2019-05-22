package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public interface XmlSerializerWrapper extends XmlSerializer {
   String NO_NAMESPACE = "";
   String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

   String getCurrentNamespaceForElements();

   String setCurrentNamespaceForElements(String var1);

   XmlSerializerWrapper attribute(String var1, String var2) throws IOException, IllegalArgumentException, IllegalStateException;

   XmlSerializerWrapper startTag(String var1) throws IOException, IllegalArgumentException, IllegalStateException;

   XmlSerializerWrapper endTag(String var1) throws IOException, IllegalArgumentException, IllegalStateException;

   XmlSerializerWrapper element(String var1, String var2, String var3) throws IOException, XmlPullParserException;

   XmlSerializerWrapper element(String var1, String var2) throws IOException, XmlPullParserException;

   void fragment(String var1) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;

   void event(XmlPullParser var1) throws IOException, IllegalArgumentException, IllegalStateException, XmlPullParserException;

   String escapeText(String var1);

   String escapeAttributeValue(String var1);
}
