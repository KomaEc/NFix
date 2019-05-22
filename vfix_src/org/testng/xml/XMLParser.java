package org.testng.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.testng.TestNGException;
import org.testng.internal.ClassHelper;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLParser<T> implements IFileParser<T> {
   private static final SAXParser m_saxParser;

   public void parse(InputStream is, DefaultHandler dh) throws SAXException, IOException {
      synchronized(m_saxParser) {
         m_saxParser.parse(is, dh);
      }
   }

   private static SAXParserFactory loadSAXParserFactory() {
      SAXParserFactory spf = null;
      StringBuffer errorLog = new StringBuffer();

      Class factoryClass;
      try {
         factoryClass = ClassHelper.forName("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
         spf = (SAXParserFactory)factoryClass.newInstance();
      } catch (Exception var6) {
         errorLog.append("JDK5 SAXParserFactory cannot be loaded: " + var6.getMessage());
      }

      if (null == spf) {
         try {
            factoryClass = ClassHelper.forName("org.apache.crimson.jaxp.SAXParserFactoryImpl");
            spf = (SAXParserFactory)factoryClass.newInstance();
         } catch (Exception var5) {
            errorLog.append("\n").append("JDK1.4 SAXParserFactory cannot be loaded: " + var5.getMessage());
         }
      }

      Throwable cause = null;
      if (null == spf) {
         try {
            spf = SAXParserFactory.newInstance();
         } catch (FactoryConfigurationError var4) {
            cause = var4;
         }
      }

      if (null == spf) {
         throw new TestNGException("Cannot initialize a SAXParserFactory\n" + errorLog.toString(), cause);
      } else {
         return spf;
      }
   }

   private static boolean supportsValidation(SAXParserFactory spf) {
      try {
         return spf.getFeature("http://xml.org/sax/features/validation");
      } catch (Exception var2) {
         return false;
      }
   }

   static {
      SAXParserFactory spf = loadSAXParserFactory();
      if (supportsValidation(spf)) {
         spf.setValidating(true);
      }

      SAXParser parser = null;

      try {
         parser = spf.newSAXParser();
      } catch (SAXException | ParserConfigurationException var3) {
         var3.printStackTrace();
      }

      m_saxParser = parser;
   }
}
