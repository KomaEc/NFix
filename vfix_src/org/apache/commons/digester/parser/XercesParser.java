package org.apache.commons.digester.parser;

import java.lang.reflect.Method;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class XercesParser {
   protected static Log log = LogFactory.getLog("org.apache.commons.digester.Digester.sax");
   private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
   protected static String XERCES_DYNAMIC = "http://apache.org/xml/features/validation/dynamic";
   protected static String XERCES_SCHEMA = "http://apache.org/xml/features/validation/schema";
   protected static float version;
   protected static String versionNumber = null;

   private static String getXercesVersion() {
      String versionNumber = "1.0";

      try {
         Class versionClass = Class.forName("org.apache.xerces.impl.Version");
         Method method = versionClass.getMethod("getVersion", (Class[])null);
         String version = (String)method.invoke((Object)null, (Object[])null);
         versionNumber = version.substring("Xerces-J".length(), version.lastIndexOf("."));
      } catch (Exception var4) {
      }

      return versionNumber;
   }

   public static SAXParser newSAXParser(Properties properties) throws ParserConfigurationException, SAXException, SAXNotSupportedException {
      SAXParserFactory factory = (SAXParserFactory)properties.get("SAXParserFactory");
      if (versionNumber == null) {
         versionNumber = getXercesVersion();
         version = new Float(versionNumber);
      }

      if ((double)version > 2.1D) {
         configureXerces(factory);
         return factory.newSAXParser();
      } else {
         SAXParser parser = factory.newSAXParser();
         configureOldXerces(parser, properties);
         return parser;
      }
   }

   private static void configureOldXerces(SAXParser parser, Properties properties) throws ParserConfigurationException, SAXNotSupportedException {
      String schemaLocation = (String)properties.get("schemaLocation");
      String schemaLanguage = (String)properties.get("schemaLanguage");

      try {
         if (schemaLocation != null) {
            parser.setProperty(JAXP_SCHEMA_LANGUAGE, schemaLanguage);
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schemaLocation);
         }
      } catch (SAXNotRecognizedException var5) {
         log.info(parser.getClass().getName() + ": " + var5.getMessage() + " not supported.");
      }

   }

   private static void configureXerces(SAXParserFactory factory) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
      factory.setFeature(XERCES_DYNAMIC, true);
      factory.setFeature(XERCES_SCHEMA, true);
   }
}
