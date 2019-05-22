package org.testng.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.testng.xml.ISuiteParser;
import org.testng.xml.XMLParser;
import org.testng.xml.XmlSuite;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DomXmlParser extends XMLParser<XmlSuite> implements ISuiteParser {
   public XmlSuite parse(String currentFile, InputStream inputStream, boolean loadClasses) {
      XmlSuite result = null;

      try {
         result = this.parse2(currentFile, inputStream, loadClasses);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return result;
   }

   public boolean accept(String fileName) {
      return fileName.endsWith(".xml");
   }

   public XmlSuite parse2(String currentFile, InputStream inputStream, boolean loadClasses) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(inputStream);
      DomUtil xpu = new DomUtil(doc);
      XmlSuite result = new XmlSuite();
      xpu.populate(result);
      System.out.println(result.toXml());
      return result;
   }
}
