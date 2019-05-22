package groovy.util;

import groovy.util.slurpersupport.GPathResult;
import groovy.util.slurpersupport.NodeChild;
import groovy.xml.FactorySupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSlurper extends DefaultHandler {
   private final XMLReader reader;
   private groovy.util.slurpersupport.Node currentNode;
   private final Stack stack;
   private final StringBuffer charBuffer;
   private final Map<String, String> namespaceTagHints;
   private boolean keepWhitespace;

   public XmlSlurper() throws ParserConfigurationException, SAXException {
      this(false, true);
   }

   public XmlSlurper(boolean validating, boolean namespaceAware) throws ParserConfigurationException, SAXException {
      this.currentNode = null;
      this.stack = new Stack();
      this.charBuffer = new StringBuffer();
      this.namespaceTagHints = new Hashtable();
      this.keepWhitespace = false;
      SAXParserFactory factory = FactorySupport.createSaxParserFactory();
      factory.setNamespaceAware(namespaceAware);
      factory.setValidating(validating);
      this.reader = factory.newSAXParser().getXMLReader();
   }

   public XmlSlurper(XMLReader reader) {
      this.currentNode = null;
      this.stack = new Stack();
      this.charBuffer = new StringBuffer();
      this.namespaceTagHints = new Hashtable();
      this.keepWhitespace = false;
      this.reader = reader;
   }

   public XmlSlurper(SAXParser parser) throws SAXException {
      this(parser.getXMLReader());
   }

   public void setKeepWhitespace(boolean keepWhitespace) {
      this.keepWhitespace = keepWhitespace;
   }

   public GPathResult getDocument() {
      NodeChild var1;
      try {
         var1 = new NodeChild(this.currentNode, (GPathResult)null, this.namespaceTagHints);
      } finally {
         this.currentNode = null;
      }

      return var1;
   }

   public GPathResult parse(InputSource input) throws IOException, SAXException {
      this.reader.setContentHandler(this);
      this.reader.parse(input);
      return this.getDocument();
   }

   public GPathResult parse(File file) throws IOException, SAXException {
      InputSource input = new InputSource(new FileInputStream(file));
      input.setSystemId("file://" + file.getAbsolutePath());
      return this.parse(input);
   }

   public GPathResult parse(InputStream input) throws IOException, SAXException {
      return this.parse(new InputSource(input));
   }

   public GPathResult parse(Reader in) throws IOException, SAXException {
      return this.parse(new InputSource(in));
   }

   public GPathResult parse(String uri) throws IOException, SAXException {
      return this.parse(new InputSource(uri));
   }

   public GPathResult parseText(String text) throws IOException, SAXException {
      return this.parse((Reader)(new StringReader(text)));
   }

   public DTDHandler getDTDHandler() {
      return this.reader.getDTDHandler();
   }

   public EntityResolver getEntityResolver() {
      return this.reader.getEntityResolver();
   }

   public ErrorHandler getErrorHandler() {
      return this.reader.getErrorHandler();
   }

   public boolean getFeature(String uri) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.reader.getFeature(uri);
   }

   public Object getProperty(String uri) throws SAXNotRecognizedException, SAXNotSupportedException {
      return this.reader.getProperty(uri);
   }

   public void setDTDHandler(DTDHandler dtdHandler) {
      this.reader.setDTDHandler(dtdHandler);
   }

   public void setEntityResolver(EntityResolver entityResolver) {
      this.reader.setEntityResolver(entityResolver);
   }

   public void setEntityBaseUrl(final URL base) {
      this.reader.setEntityResolver(new EntityResolver() {
         public InputSource resolveEntity(String publicId, String systemId) throws IOException {
            return new InputSource((new URL(base, systemId)).openStream());
         }
      });
   }

   public void setErrorHandler(ErrorHandler errorHandler) {
      this.reader.setErrorHandler(errorHandler);
   }

   public void setFeature(String uri, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.reader.setFeature(uri, value);
   }

   public void setProperty(String uri, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
      this.reader.setProperty(uri, value);
   }

   public void startDocument() throws SAXException {
      this.currentNode = null;
      this.charBuffer.setLength(0);
   }

   public void startPrefixMapping(String tag, String uri) throws SAXException {
      this.namespaceTagHints.put(tag, uri);
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
      this.addCdata();
      Map attributes = new HashMap();
      Map attributeNamespaces = new HashMap();

      for(int i = atts.getLength() - 1; i != -1; --i) {
         if (atts.getURI(i).length() == 0) {
            attributes.put(atts.getQName(i), atts.getValue(i));
         } else {
            attributes.put(atts.getLocalName(i), atts.getValue(i));
            attributeNamespaces.put(atts.getLocalName(i), atts.getURI(i));
         }
      }

      groovy.util.slurpersupport.Node newElement;
      if (namespaceURI.length() == 0) {
         newElement = new groovy.util.slurpersupport.Node(this.currentNode, qName, attributes, attributeNamespaces, namespaceURI);
      } else {
         newElement = new groovy.util.slurpersupport.Node(this.currentNode, localName, attributes, attributeNamespaces, namespaceURI);
      }

      if (this.currentNode != null) {
         this.currentNode.addChild(newElement);
      }

      this.stack.push(this.currentNode);
      this.currentNode = newElement;
   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.charBuffer.append(ch, start, length);
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      this.addCdata();
      Object oldCurrentNode = this.stack.pop();
      if (oldCurrentNode != null) {
         this.currentNode = (groovy.util.slurpersupport.Node)oldCurrentNode;
      }

   }

   public void endDocument() throws SAXException {
   }

   private void addCdata() {
      if (this.charBuffer.length() != 0) {
         String cdata = this.charBuffer.toString();
         this.charBuffer.setLength(0);
         if (this.keepWhitespace || cdata.trim().length() != 0) {
            this.currentNode.addChild(cdata);
         }
      }

   }
}
