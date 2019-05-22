package org.testng.xml;

import java.io.IOException;
import java.io.InputStream;
import org.testng.TestNGException;
import org.xml.sax.SAXException;

public class SuiteXmlParser extends XMLParser<XmlSuite> implements ISuiteParser {
   public XmlSuite parse(String currentFile, InputStream inputStream, boolean loadClasses) {
      TestNGContentHandler contentHandler = new TestNGContentHandler(currentFile, loadClasses);

      try {
         this.parse(inputStream, contentHandler);
         return contentHandler.getSuite();
      } catch (IOException | SAXException var6) {
         throw new TestNGException(var6);
      }
   }

   public boolean accept(String fileName) {
      return fileName.endsWith(".xml");
   }
}
