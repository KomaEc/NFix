package groovy.xml;

import groovy.util.BuilderSupport;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMBuilder extends BuilderSupport {
   Document document;
   DocumentBuilder documentBuilder;

   public static DOMBuilder newInstance() throws ParserConfigurationException {
      return newInstance(false, true);
   }

   public static DOMBuilder newInstance(boolean validating, boolean namespaceAware) throws ParserConfigurationException {
      DocumentBuilderFactory factory = FactorySupport.createDocumentBuilderFactory();
      factory.setNamespaceAware(namespaceAware);
      factory.setValidating(validating);
      return new DOMBuilder(factory.newDocumentBuilder());
   }

   public static Document parse(Reader reader) throws SAXException, IOException, ParserConfigurationException {
      return parse(reader, false, true);
   }

   public static Document parse(Reader reader, boolean validating, boolean namespaceAware) throws SAXException, IOException, ParserConfigurationException {
      DocumentBuilderFactory factory = FactorySupport.createDocumentBuilderFactory();
      factory.setNamespaceAware(namespaceAware);
      factory.setValidating(validating);
      DocumentBuilder documentBuilder = factory.newDocumentBuilder();
      return documentBuilder.parse(new InputSource(reader));
   }

   public Document parseText(String text) throws SAXException, IOException, ParserConfigurationException {
      return parse(new StringReader(text));
   }

   public DOMBuilder(Document document) {
      this.document = document;
   }

   public DOMBuilder(DocumentBuilder documentBuilder) {
      this.documentBuilder = documentBuilder;
   }

   protected void setParent(Object parent, Object child) {
      Node current = (Node)parent;
      Node node = (Node)child;
      current.appendChild(node);
   }

   protected Object createNode(Object name) {
      if (this.document == null) {
         this.document = this.createDocument();
      }

      if (name instanceof QName) {
         QName qname = (QName)name;
         return this.document.createElementNS(qname.getNamespaceURI(), qname.getQualifiedName());
      } else {
         return this.document.createElement(name.toString());
      }
   }

   protected Document createDocument() {
      if (this.documentBuilder == null) {
         throw new IllegalArgumentException("No Document or DOMImplementation available so cannot create Document");
      } else {
         return this.documentBuilder.newDocument();
      }
   }

   protected Object createNode(Object name, Object value) {
      Element element = (Element)this.createNode(name);
      element.appendChild(this.document.createTextNode(value.toString()));
      return element;
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      Element element = (Element)this.createNode(name, attributes);
      element.appendChild(this.document.createTextNode(value.toString()));
      return element;
   }

   protected Object createNode(Object name, Map attributes) {
      Element element = (Element)this.createNode(name);
      Iterator iter = attributes.entrySet().iterator();

      while(true) {
         while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            String attrName = entry.getKey().toString();
            Object value = entry.getValue();
            if ("xmlns".equals(attrName)) {
               if (value instanceof Map) {
                  this.appendNamespaceAttributes(element, (Map)value);
               } else {
                  if (!(value instanceof String)) {
                     throw new IllegalArgumentException("The value of the xmlns attribute must be a Map of QNames to String URIs");
                  }

                  this.setStringNS(element, "", value);
               }
            } else if (attrName.startsWith("xmlns:") && value instanceof String) {
               this.setStringNS(element, attrName.substring(6), value);
            } else {
               String valueText = value != null ? value.toString() : "";
               element.setAttribute(attrName, valueText);
            }
         }

         return element;
      }
   }

   protected void appendNamespaceAttributes(Element element, Map<Object, Object> attributes) {
      Iterator i$ = attributes.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();
         Object key = entry.getKey();
         Object value = entry.getValue();
         if (value == null) {
            throw new IllegalArgumentException("The value of key: " + key + " cannot be null");
         }

         if (key instanceof String) {
            this.setStringNS(element, key, value);
         } else {
            if (!(key instanceof QName)) {
               throw new IllegalArgumentException("The key: " + key + " should be an instanceof of " + QName.class);
            }

            QName qname = (QName)key;
            element.setAttributeNS(qname.getNamespaceURI(), qname.getQualifiedName(), value.toString());
         }
      }

   }

   private void setStringNS(Element element, Object key, Object value) {
      String prefix = (String)key;
      element.setAttributeNS("http://www.w3.org/2000/xmlns/", "".equals(prefix) ? "xmlns" : "xmlns:" + prefix, value.toString());
   }
}
