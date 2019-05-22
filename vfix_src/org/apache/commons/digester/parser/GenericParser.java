package org.apache.commons.digester.parser;

import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

public class GenericParser {
   protected static Log log = LogFactory.getLog("org.apache.commons.digester.Digester.sax");
   private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
   protected static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

   public static SAXParser newSAXParser(Properties properties) throws ParserConfigurationException, SAXException, SAXNotRecognizedException {
      SAXParserFactory factory = (SAXParserFactory)properties.get("SAXParserFactory");
      SAXParser parser = factory.newSAXParser();
      String schemaLocation = (String)properties.get("schemaLocation");
      String schemaLanguage = (String)properties.get("schemaLanguage");

      try {
         if (schemaLocation != null) {
            parser.setProperty(JAXP_SCHEMA_LANGUAGE, schemaLanguage);
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", schemaLocation);
         }
      } catch (SAXNotRecognizedException var6) {
         log.info(parser.getClass().getName() + ": " + var6.getMessage() + " not supported.");
      }

      return parser;
   }
}
