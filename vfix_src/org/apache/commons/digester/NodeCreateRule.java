package org.apache.commons.digester;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class NodeCreateRule extends Rule {
   private DocumentBuilder documentBuilder;
   private int nodeType;

   public NodeCreateRule() throws ParserConfigurationException {
      this(1);
   }

   public NodeCreateRule(DocumentBuilder documentBuilder) {
      this(1, documentBuilder);
   }

   public NodeCreateRule(int nodeType) throws ParserConfigurationException {
      this(nodeType, DocumentBuilderFactory.newInstance().newDocumentBuilder());
   }

   public NodeCreateRule(int nodeType, DocumentBuilder documentBuilder) {
      this.documentBuilder = null;
      this.nodeType = 1;
      if (nodeType != 11 && nodeType != 1) {
         throw new IllegalArgumentException("Can only create nodes of type DocumentFragment and Element");
      } else {
         this.nodeType = nodeType;
         this.documentBuilder = documentBuilder;
      }
   }

   public void begin(String namespaceURI, String name, Attributes attributes) throws Exception {
      XMLReader xmlReader = this.getDigester().getXMLReader();
      Document doc = this.documentBuilder.newDocument();
      NodeCreateRule.NodeBuilder builder = null;
      if (this.nodeType == 1) {
         Element element = null;
         int i;
         if (this.getDigester().getNamespaceAware()) {
            element = doc.createElementNS(namespaceURI, name);

            for(i = 0; i < attributes.getLength(); ++i) {
               element.setAttributeNS(attributes.getURI(i), attributes.getLocalName(i), attributes.getValue(i));
            }
         } else {
            element = doc.createElement(name);

            for(i = 0; i < attributes.getLength(); ++i) {
               element.setAttribute(attributes.getQName(i), attributes.getValue(i));
            }
         }

         builder = new NodeCreateRule.NodeBuilder(doc, element);
      } else {
         builder = new NodeCreateRule.NodeBuilder(doc, doc.createDocumentFragment());
      }

      xmlReader.setContentHandler(builder);
   }

   public void end() throws Exception {
      Object var1 = this.digester.pop();
   }

   private class NodeBuilder extends DefaultHandler {
      protected ContentHandler oldContentHandler = null;
      protected int depth = 0;
      protected Document doc = null;
      protected Node root = null;
      protected Node top = null;

      public NodeBuilder(Document doc, Node root) throws ParserConfigurationException, SAXException {
         this.doc = doc;
         this.root = root;
         this.top = root;
         this.oldContentHandler = NodeCreateRule.this.digester.getXMLReader().getContentHandler();
      }

      public void characters(char[] ch, int start, int length) throws SAXException {
         try {
            String str = new String(ch, start, length);
            if (str.trim().length() > 0) {
               this.top.appendChild(this.doc.createTextNode(str));
            }

         } catch (DOMException var5) {
            throw new SAXException(var5.getMessage());
         }
      }

      public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
         try {
            if (this.depth == 0) {
               NodeCreateRule.this.getDigester().getXMLReader().setContentHandler(this.oldContentHandler);
               NodeCreateRule.this.getDigester().push(this.root);
               NodeCreateRule.this.getDigester().endElement(namespaceURI, localName, qName);
            }

            this.top = this.top.getParentNode();
            --this.depth;
         } catch (DOMException var5) {
            throw new SAXException(var5.getMessage());
         }
      }

      public void processingInstruction(String target, String data) throws SAXException {
         try {
            this.top.appendChild(this.doc.createProcessingInstruction(target, data));
         } catch (DOMException var4) {
            throw new SAXException(var4.getMessage());
         }
      }

      public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
         try {
            Node previousTop = this.top;
            if (localName != null && localName.length() != 0) {
               this.top = this.doc.createElementNS(namespaceURI, localName);
            } else {
               this.top = this.doc.createElement(qName);
            }

            for(int i = 0; i < atts.getLength(); ++i) {
               Attr attr = null;
               if (atts.getLocalName(i) != null && atts.getLocalName(i).length() != 0) {
                  attr = this.doc.createAttributeNS(atts.getURI(i), atts.getLocalName(i));
                  attr.setNodeValue(atts.getValue(i));
                  ((Element)this.top).setAttributeNodeNS(attr);
               } else {
                  attr = this.doc.createAttribute(atts.getQName(i));
                  attr.setNodeValue(atts.getValue(i));
                  ((Element)this.top).setAttributeNode(attr);
               }
            }

            previousTop.appendChild(this.top);
            ++this.depth;
         } catch (DOMException var8) {
            throw new SAXException(var8.getMessage());
         }
      }
   }
}
