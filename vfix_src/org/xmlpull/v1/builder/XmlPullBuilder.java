package org.xmlpull.v1.builder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.builder.impl.XmlPullBuilderImpl;

public abstract class XmlPullBuilder {
   protected XmlPullParserFactory factory;

   public static XmlPullBuilder newInstance() throws XmlBuilderException {
      XmlPullBuilderImpl impl = new XmlPullBuilderImpl();

      try {
         impl.factory = XmlPullParserFactory.newInstance();
         impl.factory.setNamespaceAware(true);
         return impl;
      } catch (XmlPullParserException var2) {
         throw new XmlBuilderException("could not create XmlPull factory:" + var2, var2);
      }
   }

   public XmlPullParserFactory getFactory() throws XmlBuilderException {
      return this.factory;
   }

   public XmlDocument newDocument() throws XmlBuilderException {
      return this.newDocument((String)null, (Boolean)null, (String)null);
   }

   public abstract XmlDocument newDocument(String var1, Boolean var2, String var3) throws XmlBuilderException;

   public abstract XmlElement newFragment(String var1) throws XmlBuilderException;

   public abstract XmlElement newFragment(String var1, String var2) throws XmlBuilderException;

   public abstract XmlElement newFragment(XmlNamespace var1, String var2) throws XmlBuilderException;

   public abstract XmlNamespace newNamespace(String var1) throws XmlBuilderException;

   public abstract XmlNamespace newNamespace(String var1, String var2) throws XmlBuilderException;

   public abstract XmlDocument parse(XmlPullParser var1) throws XmlBuilderException;

   public abstract Object parseItem(XmlPullParser var1) throws XmlBuilderException;

   public abstract XmlElement parseStartTag(XmlPullParser var1) throws XmlBuilderException;

   public XmlDocument parseInputStream(InputStream is) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(is, (String)null);
      } catch (XmlPullParserException var4) {
         throw new XmlBuilderException("could not start parsing input stream", var4);
      }

      return this.parse(pp);
   }

   public XmlDocument parseInputStream(InputStream is, String encoding) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(is, encoding);
      } catch (XmlPullParserException var5) {
         throw new XmlBuilderException("could not start parsing input stream (encoding=" + encoding + ")", var5);
      }

      return this.parse(pp);
   }

   public XmlDocument parseReader(Reader reader) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(reader);
      } catch (XmlPullParserException var4) {
         throw new XmlBuilderException("could not start parsing input from reader", var4);
      }

      return this.parse(pp);
   }

   public abstract XmlDocument parseLocation(String var1) throws XmlBuilderException;

   public abstract XmlElement parseFragment(XmlPullParser var1) throws XmlBuilderException;

   public XmlElement parseFragmentFromInputStream(InputStream is) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(is, (String)null);

         try {
            pp.nextTag();
         } catch (IOException var4) {
            throw new XmlBuilderException("IO error when starting to parse input stream", var4);
         }
      } catch (XmlPullParserException var5) {
         throw new XmlBuilderException("could not start parsing input stream", var5);
      }

      return this.parseFragment(pp);
   }

   public XmlElement parseFragementFromInputStream(InputStream is, String encoding) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(is, encoding);

         try {
            pp.nextTag();
         } catch (IOException var5) {
            throw new XmlBuilderException("IO error when starting to parse input stream (encoding=" + encoding + ")", var5);
         }
      } catch (XmlPullParserException var6) {
         throw new XmlBuilderException("could not start parsing input stream (encoding=" + encoding + ")", var6);
      }

      return this.parseFragment(pp);
   }

   public XmlElement parseFragmentFromReader(Reader reader) throws XmlBuilderException {
      XmlPullParser pp = null;

      try {
         pp = this.factory.newPullParser();
         pp.setInput(reader);

         try {
            pp.nextTag();
         } catch (IOException var4) {
            throw new XmlBuilderException("IO error when starting to parse from reader", var4);
         }
      } catch (XmlPullParserException var5) {
         throw new XmlBuilderException("could not start parsing input from reader", var5);
      }

      return this.parseFragment(pp);
   }

   public void skipSubTree(XmlPullParser pp) throws XmlBuilderException {
      try {
         pp.require(2, (String)null, (String)null);
         int level = 1;

         while(level > 0) {
            int eventType = pp.next();
            if (eventType == 3) {
               --level;
            } else if (eventType == 2) {
               ++level;
            }
         }

      } catch (XmlPullParserException var4) {
         throw new XmlBuilderException("could not skip subtree", var4);
      } catch (IOException var5) {
         throw new XmlBuilderException("IO error when skipping subtree", var5);
      }
   }

   public abstract void serializeStartTag(XmlElement var1, XmlSerializer var2) throws XmlBuilderException;

   public abstract void serializeEndTag(XmlElement var1, XmlSerializer var2) throws XmlBuilderException;

   public abstract void serialize(Object var1, XmlSerializer var2) throws XmlBuilderException;

   public abstract void serializeItem(Object var1, XmlSerializer var2) throws XmlBuilderException;

   public void serializeToOutputStream(Object item, OutputStream os) throws XmlBuilderException {
      this.serializeToOutputStream(item, os, "UTF8");
   }

   public void serializeToOutputStream(Object item, OutputStream os, String encoding) throws XmlBuilderException {
      XmlSerializer ser = null;

      try {
         ser = this.factory.newSerializer();
         ser.setOutput(os, encoding);
      } catch (Exception var6) {
         throw new XmlBuilderException("could not serialize node to output stream (encoding=" + encoding + ")", var6);
      }

      this.serialize(item, ser);
   }

   public void serializeToWriter(Object item, Writer writer) throws XmlBuilderException {
      XmlSerializer ser = null;

      try {
         ser = this.factory.newSerializer();
         ser.setOutput(writer);
      } catch (Exception var5) {
         throw new XmlBuilderException("could not serialize node to writer", var5);
      }

      this.serialize(item, ser);
   }

   public String serializeToString(Object item) throws XmlBuilderException {
      StringWriter sw = new StringWriter();
      this.serializeToWriter(item, sw);
      return sw.toString();
   }
}
