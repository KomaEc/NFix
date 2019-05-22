package org.apache.commons.digester;

import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import org.apache.commons.digester.parser.GenericParser;
import org.apache.commons.digester.parser.XercesParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class ParserFeatureSetterFactory {
   private static boolean isXercesUsed;

   public static SAXParser newSAXParser(Properties properties) throws ParserConfigurationException, SAXException, SAXNotRecognizedException, SAXNotSupportedException {
      return isXercesUsed ? XercesParser.newSAXParser(properties) : GenericParser.newSAXParser(properties);
   }

   static {
      try {
         Class versionClass = Class.forName("org.apache.xerces.impl.Version");
         isXercesUsed = true;
      } catch (Exception var1) {
         isXercesUsed = false;
      }

   }
}
