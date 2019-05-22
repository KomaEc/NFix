package groovy.xml;

import groovy.util.BuilderSupport;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class SAXBuilder extends BuilderSupport {
   private ContentHandler handler;
   private Attributes emptyAttributes = new AttributesImpl();

   public SAXBuilder(ContentHandler handler) {
      this.handler = handler;
   }

   protected void setParent(Object parent, Object child) {
   }

   protected Object createNode(Object name) {
      this.doStartElement(name, this.emptyAttributes);
      return name;
   }

   protected Object createNode(Object name, Object value) {
      this.doStartElement(name, this.emptyAttributes);
      this.doText(value);
      return name;
   }

   private void doText(Object value) {
      try {
         char[] text = value.toString().toCharArray();
         this.handler.characters(text, 0, text.length);
      } catch (SAXException var3) {
         this.handleException(var3);
      }

   }

   protected Object createNode(Object name, Map attributeMap, Object text) {
      AttributesImpl attributes = new AttributesImpl();

      String uri;
      String localName;
      String qualifiedName;
      String valueText;
      for(Iterator iter = attributeMap.entrySet().iterator(); iter.hasNext(); attributes.addAttribute(uri, localName, qualifiedName, "CDATA", valueText)) {
         Entry entry = (Entry)iter.next();
         Object key = entry.getKey();
         Object value = entry.getValue();
         uri = "";
         localName = null;
         qualifiedName = "";
         valueText = value != null ? value.toString() : "";
         if (key instanceof QName) {
            QName qname = (QName)key;
            uri = qname.getNamespaceURI();
            localName = qname.getLocalPart();
            qualifiedName = qname.getQualifiedName();
         } else {
            localName = key.toString();
            qualifiedName = localName;
         }
      }

      this.doStartElement(name, attributes);
      if (text != null) {
         this.doText(text);
      }

      return name;
   }

   protected void doStartElement(Object name, Attributes attributes) {
      String uri = "";
      String localName = null;
      String qualifiedName = "";
      if (name instanceof QName) {
         QName qname = (QName)name;
         uri = qname.getNamespaceURI();
         localName = qname.getLocalPart();
         qualifiedName = qname.getQualifiedName();
      } else {
         localName = name.toString();
         qualifiedName = localName;
      }

      try {
         this.handler.startElement(uri, localName, qualifiedName, attributes);
      } catch (SAXException var7) {
         this.handleException(var7);
      }

   }

   protected void nodeCompleted(Object parent, Object name) {
      String uri = "";
      String localName = null;
      String qualifiedName = "";
      if (name instanceof QName) {
         QName qname = (QName)name;
         uri = qname.getNamespaceURI();
         localName = qname.getLocalPart();
         qualifiedName = qname.getQualifiedName();
      } else {
         localName = name.toString();
         qualifiedName = localName;
      }

      try {
         this.handler.endElement(uri, localName, qualifiedName);
      } catch (SAXException var7) {
         this.handleException(var7);
      }

   }

   protected void handleException(SAXException e) {
      throw new RuntimeException(e);
   }

   protected Object createNode(Object name, Map attributes) {
      return this.createNode(name, attributes, (Object)null);
   }
}
