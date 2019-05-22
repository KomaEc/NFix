package org.xmlpull.v1.wrapper.classic;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.util.XmlPullUtil;
import org.xmlpull.v1.wrapper.XmlPullParserWrapper;

public class StaticXmlPullParserWrapper extends XmlPullParserDelegate implements XmlPullParserWrapper {
   public StaticXmlPullParserWrapper(XmlPullParser pp) {
      super(pp);
   }

   public String getAttributeValue(String name) {
      return XmlPullUtil.getAttributeValue(this.pp, name);
   }

   public String getRequiredAttributeValue(String name) throws IOException, XmlPullParserException {
      return XmlPullUtil.getRequiredAttributeValue(this.pp, (String)null, name);
   }

   public String getRequiredAttributeValue(String namespace, String name) throws IOException, XmlPullParserException {
      return XmlPullUtil.getRequiredAttributeValue(this.pp, namespace, name);
   }

   public String getRequiredElementText(String namespace, String name) throws IOException, XmlPullParserException {
      if (name == null) {
         throw new XmlPullParserException("name for element can not be null");
      } else {
         String text = null;
         this.nextStartTag(namespace, name);
         if (this.isNil()) {
            this.nextEndTag(namespace, name);
         } else {
            text = this.pp.nextText();
         }

         this.pp.require(3, namespace, name);
         return text;
      }
   }

   public boolean isNil() throws IOException, XmlPullParserException {
      boolean result = false;
      String value = this.pp.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "nil");
      if ("true".equals(value)) {
         result = true;
      }

      return result;
   }

   public String getPITarget() throws IllegalStateException {
      return XmlPullUtil.getPITarget(this.pp);
   }

   public String getPIData() throws IllegalStateException {
      return XmlPullUtil.getPIData(this.pp);
   }

   public boolean matches(int type, String namespace, String name) throws XmlPullParserException {
      return XmlPullUtil.matches(this.pp, type, namespace, name);
   }

   public void nextStartTag() throws XmlPullParserException, IOException {
      if (this.pp.nextTag() != 2) {
         throw new XmlPullParserException("expected START_TAG and not " + this.pp.getPositionDescription());
      }
   }

   public void nextStartTag(String name) throws XmlPullParserException, IOException {
      this.pp.nextTag();
      this.pp.require(2, (String)null, name);
   }

   public void nextStartTag(String namespace, String name) throws XmlPullParserException, IOException {
      this.pp.nextTag();
      this.pp.require(2, namespace, name);
   }

   public void nextEndTag() throws XmlPullParserException, IOException {
      XmlPullUtil.nextEndTag(this.pp);
   }

   public void nextEndTag(String name) throws XmlPullParserException, IOException {
      XmlPullUtil.nextEndTag(this.pp, (String)null, name);
   }

   public void nextEndTag(String namespace, String name) throws XmlPullParserException, IOException {
      XmlPullUtil.nextEndTag(this.pp, namespace, name);
   }

   public String nextText(String namespace, String name) throws IOException, XmlPullParserException {
      return XmlPullUtil.nextText(this.pp, namespace, name);
   }

   public void skipSubTree() throws XmlPullParserException, IOException {
      XmlPullUtil.skipSubTree(this.pp);
   }
}
