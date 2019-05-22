package org.xmlpull.v1.wrapper;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public interface XmlPullParserWrapper extends XmlPullParser {
   String NO_NAMESPACE = "";
   String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

   String getAttributeValue(String var1);

   String getPITarget() throws IllegalStateException;

   String getPIData() throws IllegalStateException;

   String getRequiredAttributeValue(String var1) throws IOException, XmlPullParserException;

   String getRequiredAttributeValue(String var1, String var2) throws IOException, XmlPullParserException;

   String getRequiredElementText(String var1, String var2) throws IOException, XmlPullParserException;

   boolean isNil() throws IOException, XmlPullParserException;

   boolean matches(int var1, String var2, String var3) throws XmlPullParserException;

   void nextStartTag() throws XmlPullParserException, IOException;

   void nextStartTag(String var1) throws XmlPullParserException, IOException;

   void nextStartTag(String var1, String var2) throws XmlPullParserException, IOException;

   void nextEndTag() throws XmlPullParserException, IOException;

   void nextEndTag(String var1) throws XmlPullParserException, IOException;

   void nextEndTag(String var1, String var2) throws XmlPullParserException, IOException;

   String nextText(String var1, String var2) throws IOException, XmlPullParserException;

   void skipSubTree() throws XmlPullParserException, IOException;
}
