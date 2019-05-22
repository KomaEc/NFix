package groovy.util;

import groovy.xml.FactorySupport;
import groovy.xml.QName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class XmlParser implements ContentHandler {
   private StringBuffer bodyText;
   private List<Node> stack;
   private Locator locator;
   private XMLReader reader;
   private Node parent;
   private boolean trimWhitespace;
   private boolean namespaceAware;

   public XmlParser() throws ParserConfigurationException, SAXException {
      this(false, true);
   }

   public XmlParser(boolean validating, boolean namespaceAware) throws ParserConfigurationException, SAXException {
      this.bodyText = new StringBuffer();
      this.stack = new ArrayList();
      this.trimWhitespace = true;
      SAXParserFactory factory = FactorySupport.createSaxParserFactory();
      factory.setNamespaceAware(namespaceAware);
      this.namespaceAware = namespaceAware;
      factory.setValidating(validating);
      this.reader = factory.newSAXParser().getXMLReader();
   }

   public XmlParser(XMLReader reader) {
      this.bodyText = new StringBuffer();
      this.stack = new ArrayList();
      this.trimWhitespace = true;
      this.reader = reader;
   }

   public XmlParser(SAXParser parser) throws SAXException {
      this.bodyText = new StringBuffer();
      this.stack = new ArrayList();
      this.trimWhitespace = true;
      this.reader = parser.getXMLReader();
   }

   public boolean isTrimWhitespace() {
      return this.trimWhitespace;
   }

   public void setTrimWhitespace(boolean trimWhitespace) {
      this.trimWhitespace = trimWhitespace;
   }

   public Node parse(File file) throws IOException, SAXException {
      InputSource input = new InputSource(new FileInputStream(file));
      input.setSystemId("file://" + file.getAbsolutePath());
      this.getXMLReader().parse(input);
      return this.parent;
   }

   public Node parse(InputSource input) throws IOException, SAXException {
      this.getXMLReader().parse(input);
      return this.parent;
   }

   public Node parse(InputStream input) throws IOException, SAXException {
      InputSource is = new InputSource(input);
      this.getXMLReader().parse(is);
      return this.parent;
   }

   public Node parse(Reader in) throws IOException, SAXException {
      InputSource is = new InputSource(in);
      this.getXMLReader().parse(is);
      return this.parent;
   }

   public Node parse(String uri) throws IOException, SAXException {
      InputSource is = new InputSource(uri);
      this.getXMLReader().parse(is);
      return this.parent;
   }

   public Node parseText(String text) throws IOException, SAXException {
      return this.parse((Reader)(new StringReader(text)));
   }

   public boolean isNamespaceAware() {
      return this.namespaceAware;
   }

   public void setNamespaceAware(boolean namespaceAware) {
      this.namespaceAware = namespaceAware;
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
      this.parent = null;
   }

   public void endDocument() throws SAXException {
      this.stack.clear();
   }

   public void startElement(String namespaceURI, String localName, String qName, Attributes list) throws SAXException {
      this.addTextToNode();
      Object nodeName = this.getElementName(namespaceURI, localName, qName);
      int size = list.getLength();
      Map<Object, String> attributes = new LinkedHashMap(size);

      for(int i = 0; i < size; ++i) {
         Object attributeName = this.getElementName(list.getURI(i), list.getLocalName(i), list.getQName(i));
         String value = list.getValue(i);
         attributes.put(attributeName, value);
      }

      this.parent = this.createNode(this.parent, nodeName, attributes);
      this.stack.add(this.parent);
   }

   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
      this.addTextToNode();
      if (!this.stack.isEmpty()) {
         this.stack.remove(this.stack.size() - 1);
         if (!this.stack.isEmpty()) {
            this.parent = (Node)this.stack.get(this.stack.size() - 1);
         }
      }

   }

   public void characters(char[] buffer, int start, int length) throws SAXException {
      this.bodyText.append(buffer, start, length);
   }

   public void startPrefixMapping(String prefix, String namespaceURI) throws SAXException {
   }

   public void endPrefixMapping(String prefix) throws SAXException {
   }

   public void ignorableWhitespace(char[] buffer, int start, int len) throws SAXException {
   }

   public void processingInstruction(String target, String data) throws SAXException {
   }

   public Locator getDocumentLocator() {
      return this.locator;
   }

   public void setDocumentLocator(Locator locator) {
      this.locator = locator;
   }

   public void skippedEntity(String name) throws SAXException {
   }

   protected XMLReader getXMLReader() {
      this.reader.setContentHandler(this);
      return this.reader;
   }

   protected void addTextToNode() {
      String text = this.bodyText.toString();
      if (this.trimWhitespace) {
         text = text.trim();
      }

      if (text.length() > 0) {
         this.parent.children().add(text);
      }

      this.bodyText = new StringBuffer();
   }

   protected Node createNode(Node parent, Object name, Map attributes) {
      return new Node(parent, name, attributes);
   }

   protected Object getElementName(String namespaceURI, String localName, String qName) {
      String name = localName;
      String prefix = "";
      if (localName == null || localName.length() < 1) {
         name = qName;
      }

      if (namespaceURI != null && namespaceURI.length() > 0) {
         if (qName != null && qName.length() > 0 && this.namespaceAware) {
            int index = qName.lastIndexOf(":");
            if (index > 0) {
               prefix = qName.substring(0, index);
            }
         }

         return new QName(namespaceURI, name, prefix);
      } else {
         return name;
      }
   }
}
