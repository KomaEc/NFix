package org.xmlpull.v1.builder.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
import org.xmlpull.v1.builder.XmlPullBuilder;
import org.xmlpull.v1.builder.XmlSerializable;

public class XmlPullBuilderImpl extends XmlPullBuilder {
   private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
   private static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";

   public XmlDocument newDocument(String version, Boolean standalone, String characterEncoding) {
      return new XmlDocumentImpl(version, standalone, characterEncoding);
   }

   public XmlElement newFragment(String elementName) {
      return new XmlElementImpl((XmlNamespace)null, elementName);
   }

   public XmlElement newFragment(String elementNamespaceName, String elementName) {
      return new XmlElementImpl(elementNamespaceName, elementName);
   }

   public XmlElement newFragment(XmlNamespace elementNamespace, String elementName) {
      return new XmlElementImpl(elementNamespace, elementName);
   }

   public XmlNamespace newNamespace(String namespaceName) {
      return new XmlNamespaceImpl((String)null, namespaceName);
   }

   public XmlNamespace newNamespace(String prefix, String namespaceName) {
      return new XmlNamespaceImpl(prefix, namespaceName);
   }

   public XmlDocument parse(XmlPullParser pp) {
      XmlDocument doc = this.parseDocumentStart(pp);
      XmlElement root = this.parseFragment(pp);
      doc.setDocumentElement(root);
      return doc;
   }

   public Object parseItem(XmlPullParser pp) {
      try {
         int eventType = pp.getEventType();
         if (eventType == 2) {
            return this.parseStartTag(pp);
         } else if (eventType == 4) {
            return pp.getText();
         } else if (eventType == 0) {
            return this.parseDocumentStart(pp);
         } else {
            throw new XmlBuilderException("currently unsupported event type " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
         }
      } catch (XmlPullParserException var3) {
         throw new XmlBuilderException("could not parse XML item", var3);
      }
   }

   private XmlDocument parseDocumentStart(XmlPullParser pp) {
      XmlDocumentImpl doc = null;

      try {
         if (pp.getEventType() != 0) {
            throw new XmlBuilderException("parser must be positioned on beginning of document and not " + pp.getPositionDescription());
         } else {
            pp.next();
            String xmlDeclVersion = (String)pp.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-version");
            Boolean xmlDeclStandalone = (Boolean)pp.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone");
            String characterEncoding = pp.getInputEncoding();
            doc = new XmlDocumentImpl(xmlDeclVersion, xmlDeclStandalone, characterEncoding);
            return doc;
         }
      } catch (XmlPullParserException var6) {
         throw new XmlBuilderException("could not parse XML document prolog", var6);
      } catch (IOException var7) {
         throw new XmlBuilderException("could not read XML document prolog", var7);
      }
   }

   public XmlElement parseFragment(XmlPullParser pp) {
      try {
         int eventType = pp.getEventType();
         if (eventType != 2) {
            throw new XmlBuilderException("expected parser to be on start tag and not " + XmlPullParser.TYPES[eventType] + pp.getPositionDescription());
         } else {
            XmlElement curElem = this.parseStartTag(pp);

            while(true) {
               while(true) {
                  eventType = pp.next();
                  if (eventType == 2) {
                     XmlElement child = this.parseStartTag(pp);
                     curElem.addChild(child);
                     curElem = child;
                  } else if (eventType == 3) {
                     XmlContainer parent = curElem.getParent();
                     if (parent == null) {
                        return curElem;
                     }

                     curElem = (XmlElement)parent;
                  } else if (eventType == 4) {
                     curElem.addChild(pp.getText());
                  }
               }
            }
         }
      } catch (XmlPullParserException var5) {
         throw new XmlBuilderException("could not build tree from XML", var5);
      } catch (IOException var6) {
         throw new XmlBuilderException("could not read XML tree content", var6);
      }
   }

   public XmlElement parseStartTag(XmlPullParser pp) {
      try {
         if (pp.getEventType() != 2) {
            throw new XmlBuilderException("parser must be on START_TAG and not " + pp.getPositionDescription());
         } else {
            XmlElement el = new XmlElementImpl(pp.getNamespace(), pp.getName());

            for(int i = pp.getNamespaceCount(pp.getDepth() - 1); i < pp.getNamespaceCount(pp.getDepth()); ++i) {
               String prefix = pp.getNamespacePrefix(i);
               el.declareNamespace(prefix == null ? "" : prefix, pp.getNamespaceUri(i));
            }

            for(int i = 0; i < pp.getAttributeCount(); ++i) {
               el.addAttribute(pp.getAttributeType(i), pp.getAttributePrefix(i), pp.getAttributeNamespace(i), pp.getAttributeName(i), pp.getAttributeValue(i), !pp.isAttributeDefault(i));
            }

            return el;
         }
      } catch (XmlPullParserException var5) {
         throw new XmlBuilderException("could not parse XML start tag", var5);
      }
   }

   public XmlDocument parseLocation(String locationUrl) {
      URL url = null;

      try {
         url = new URL(locationUrl);
      } catch (MalformedURLException var5) {
         throw new XmlBuilderException("could not parse URL " + locationUrl, var5);
      }

      try {
         return this.parseInputStream(url.openStream());
      } catch (IOException var4) {
         throw new XmlBuilderException("could not open connection to URL " + locationUrl, var4);
      }
   }

   public void serialize(Object item, XmlSerializer serializer) {
      if (item instanceof XmlContainer) {
         this.serializeContainer((XmlContainer)item, serializer);
      } else {
         this.serializeItem(item, serializer);
      }

   }

   private void serializeContainer(XmlContainer node, XmlSerializer serializer) {
      if (node instanceof XmlSerializable) {
         ((XmlSerializable)node).serialize(serializer);
      } else if (node instanceof XmlDocument) {
         this.serializeDocument((XmlDocument)node, serializer);
      } else {
         if (!(node instanceof XmlElement)) {
            throw new IllegalArgumentException("could not serialzie unknown XML container " + node.getClass());
         }

         this.serializeFragment((XmlElement)node, serializer);
      }

   }

   public void serializeItem(Object item, XmlSerializer ser) {
      try {
         if (item instanceof XmlSerializable) {
            ((XmlSerializable)item).serialize(ser);
         } else {
            if (!(item instanceof String)) {
               throw new IllegalArgumentException("could not serialize " + item.getClass());
            }

            ser.text(item.toString());
         }

      } catch (IOException var4) {
         throw new XmlBuilderException("serializing XML start tag failed", var4);
      }
   }

   public void serializeStartTag(XmlElement el, XmlSerializer ser) {
      try {
         Iterator iter;
         if (el.hasNamespaceDeclarations()) {
            iter = el.namespaces();

            while(iter.hasNext()) {
               XmlNamespace n = (XmlNamespace)iter.next();
               ser.setPrefix(n.getPrefix(), n.getNamespaceName());
            }
         }

         ser.startTag(el.getNamespaceName(), el.getName());
         if (el.hasAttributes()) {
            iter = el.attributes();

            while(iter.hasNext()) {
               XmlAttribute a = (XmlAttribute)iter.next();
               ser.attribute(a.getNamespaceName(), a.getName(), a.getValue());
            }
         }

      } catch (IOException var5) {
         throw new XmlBuilderException("serializing XML start tag failed", var5);
      }
   }

   public void serializeEndTag(XmlElement el, XmlSerializer ser) {
      try {
         ser.endTag(el.getNamespaceName(), el.getName());
      } catch (IOException var4) {
         throw new XmlBuilderException("serializing XML end tag failed", var4);
      }
   }

   private void serializeDocument(XmlDocument doc, XmlSerializer ser) {
      try {
         ser.startDocument(doc.getCharacterEncodingScheme(), doc.isStandalone());
      } catch (IOException var5) {
         throw new XmlBuilderException("serialziing XML document start failed", var5);
      }

      this.serializeFragment(doc.getDocumentElement(), ser);

      try {
         ser.endDocument();
      } catch (IOException var4) {
         throw new XmlBuilderException("serializing XML document end failed", var4);
      }
   }

   private void serializeFragment(XmlElement el, XmlSerializer ser) {
      this.serializeStartTag(el, ser);
      if (el.hasChildren()) {
         Iterator iter = el.children();

         while(iter.hasNext()) {
            Object child = iter.next();
            if (child instanceof XmlSerializable) {
               ((XmlSerializable)child).serialize(ser);
            } else if (child instanceof XmlElement) {
               this.serializeFragment((XmlElement)child, ser);
            } else {
               this.serializeItem(child, ser);
            }
         }
      }

      this.serializeEndTag(el, ser);
   }
}
