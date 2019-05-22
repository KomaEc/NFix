package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.IOUtil;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.pull.XmlPullParserException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Xpp3DomBuilder {
   private static final boolean DEFAULT_TRIM = true;

   public static Xpp3Dom build(@WillClose @Nonnull Reader reader) throws XmlPullParserException {
      return build(reader, true);
   }

   public static Xpp3Dom build(@WillClose InputStream is, @Nonnull String encoding) throws XmlPullParserException {
      return build(is, encoding, true);
   }

   public static Xpp3Dom build(@WillClose InputStream is, @Nonnull String encoding, boolean trim) throws XmlPullParserException {
      try {
         Reader reader = new InputStreamReader(is, encoding);
         return build(reader, trim);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static Xpp3Dom build(@WillClose Reader reader, boolean trim) throws XmlPullParserException {
      Xpp3Dom var3;
      try {
         Xpp3DomBuilder.DocHandler docHandler = parseSax(new InputSource(reader), trim);
         var3 = docHandler.result;
      } finally {
         IOUtil.close(reader);
      }

      return var3;
   }

   private static Xpp3DomBuilder.DocHandler parseSax(@Nonnull InputSource inputSource, boolean trim) throws XmlPullParserException {
      try {
         Xpp3DomBuilder.DocHandler ch = new Xpp3DomBuilder.DocHandler(trim);
         XMLReader parser = createXmlReader();
         parser.setContentHandler(ch);
         parser.parse(inputSource);
         return ch;
      } catch (IOException var4) {
         throw new XmlPullParserException(var4);
      } catch (SAXException var5) {
         throw new XmlPullParserException(var5);
      }
   }

   private static XMLReader createXmlReader() throws SAXException {
      XMLReader comSunXmlReader = instantiate("com.sun.org.apache.xerces.internal.parsers.SAXParser");
      if (comSunXmlReader != null) {
         return comSunXmlReader;
      } else {
         String key = "org.xml.sax.driver";
         String oldParser = System.getProperty(key);
         System.clearProperty(key);

         XMLReader var3;
         try {
            var3 = XMLReaderFactory.createXMLReader();
         } finally {
            if (oldParser != null) {
               System.setProperty(key, oldParser);
            }

         }

         return var3;
      }
   }

   private static XMLReader instantiate(String s) {
      try {
         Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass(s);
         return (XMLReader)aClass.newInstance();
      } catch (ClassNotFoundException var2) {
         return null;
      } catch (InstantiationException var3) {
         return null;
      } catch (IllegalAccessException var4) {
         return null;
      }
   }

   private static class DocHandler extends DefaultHandler {
      private final List<Xpp3Dom> elemStack = new ArrayList();
      private final List<StringBuilder> values = new ArrayList();
      private final List<SAXParseException> warnings = new ArrayList();
      private final List<SAXParseException> errors = new ArrayList();
      private final List<SAXParseException> fatals = new ArrayList();
      Xpp3Dom result = null;
      private final boolean trim;
      private boolean spacePreserve = false;

      DocHandler(boolean trim) {
         this.trim = trim;
      }

      public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
         this.spacePreserve = false;
         Xpp3Dom child = new Xpp3Dom(localName);
         this.attachToParent(child);
         this.pushOnStack(child);
         this.values.add(new StringBuilder());
         int size = attributes.getLength();

         for(int i = 0; i < size; ++i) {
            String name = attributes.getQName(i);
            String value = attributes.getValue(i);
            child.setAttribute(name, value);
            this.spacePreserve = this.spacePreserve || "xml:space".equals(name) && "preserve".equals(value);
         }

      }

      private boolean pushOnStack(Xpp3Dom child) {
         return this.elemStack.add(child);
      }

      private void attachToParent(Xpp3Dom child) {
         int depth = this.elemStack.size();
         if (depth > 0) {
            ((Xpp3Dom)this.elemStack.get(depth - 1)).addChild(child);
         }

      }

      public void warning(SAXParseException e) throws SAXException {
         this.warnings.add(e);
      }

      public void error(SAXParseException e) throws SAXException {
         this.errors.add(e);
      }

      public void fatalError(SAXParseException e) throws SAXException {
         this.fatals.add(e);
      }

      private Xpp3Dom pop() {
         int depth = this.elemStack.size() - 1;
         return (Xpp3Dom)this.elemStack.remove(depth);
      }

      public void endElement(String uri, String localName, String qName) throws SAXException {
         int depth = this.elemStack.size() - 1;
         Xpp3Dom element = this.pop();
         Object accumulatedValue = this.values.remove(depth);
         if (element.getChildCount() == 0) {
            if (accumulatedValue == null) {
               element.setValue("");
            } else {
               element.setValue(accumulatedValue.toString());
            }
         }

         if (depth == 0) {
            this.result = element;
         }

      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         String text = new String(ch, start, length);
         this.appendToTopValue(this.trim && !this.spacePreserve ? text.trim() : text);
      }

      private void appendToTopValue(String toAppend) {
         StringBuilder stringBuilder = (StringBuilder)this.values.get(this.values.size() - 1);
         stringBuilder.append(toAppend);
      }
   }
}
