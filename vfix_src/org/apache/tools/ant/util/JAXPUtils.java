package org.apache.tools.ant.util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.tools.ant.BuildException;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class JAXPUtils {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static SAXParserFactory parserFactory = null;
   private static SAXParserFactory nsParserFactory = null;
   private static DocumentBuilderFactory builderFactory = null;

   public static synchronized SAXParserFactory getParserFactory() throws BuildException {
      if (parserFactory == null) {
         parserFactory = newParserFactory();
      }

      return parserFactory;
   }

   public static synchronized SAXParserFactory getNSParserFactory() throws BuildException {
      if (nsParserFactory == null) {
         nsParserFactory = newParserFactory();
         nsParserFactory.setNamespaceAware(true);
      }

      return nsParserFactory;
   }

   public static SAXParserFactory newParserFactory() throws BuildException {
      try {
         return SAXParserFactory.newInstance();
      } catch (FactoryConfigurationError var1) {
         throw new BuildException("XML parser factory has not been configured correctly: " + var1.getMessage(), var1);
      }
   }

   public static Parser getParser() throws BuildException {
      try {
         return newSAXParser(getParserFactory()).getParser();
      } catch (SAXException var1) {
         throw convertToBuildException(var1);
      }
   }

   public static XMLReader getXMLReader() throws BuildException {
      try {
         return newSAXParser(getParserFactory()).getXMLReader();
      } catch (SAXException var1) {
         throw convertToBuildException(var1);
      }
   }

   public static XMLReader getNamespaceXMLReader() throws BuildException {
      try {
         return newSAXParser(getNSParserFactory()).getXMLReader();
      } catch (SAXException var1) {
         throw convertToBuildException(var1);
      }
   }

   public static String getSystemId(File file) {
      return FILE_UTILS.toURI(file.getAbsolutePath());
   }

   public static DocumentBuilder getDocumentBuilder() throws BuildException {
      try {
         return getDocumentBuilderFactory().newDocumentBuilder();
      } catch (ParserConfigurationException var1) {
         throw new BuildException(var1);
      }
   }

   private static SAXParser newSAXParser(SAXParserFactory factory) throws BuildException {
      try {
         return factory.newSAXParser();
      } catch (ParserConfigurationException var2) {
         throw new BuildException("Cannot create parser for the given configuration: " + var2.getMessage(), var2);
      } catch (SAXException var3) {
         throw convertToBuildException(var3);
      }
   }

   private static BuildException convertToBuildException(SAXException e) {
      Exception nested = e.getException();
      return nested != null ? new BuildException(nested) : new BuildException(e);
   }

   private static synchronized DocumentBuilderFactory getDocumentBuilderFactory() throws BuildException {
      if (builderFactory == null) {
         try {
            builderFactory = DocumentBuilderFactory.newInstance();
         } catch (FactoryConfigurationError var1) {
            throw new BuildException("Document builder factory has not been configured correctly: " + var1.getMessage(), var1);
         }
      }

      return builderFactory;
   }
}
