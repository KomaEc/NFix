package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

public final class SaxWriter extends AbstractXmlWriter implements XMLReader {
   public static final String CONFIGURED_XSTREAM_PROPERTY = "http://com.thoughtworks.xstream/sax/property/configured-xstream";
   public static final String SOURCE_OBJECT_LIST_PROPERTY = "http://com.thoughtworks.xstream/sax/property/source-object-list";
   private EntityResolver entityResolver;
   private DTDHandler dtdHandler;
   private ContentHandler contentHandler;
   private ErrorHandler errorHandler;
   private Map features;
   private final Map properties;
   private final boolean includeEnclosingDocument;
   private int depth;
   private List elementStack;
   private char[] buffer;
   private boolean startTagInProgress;
   private final AttributesImpl attributeList;

   public SaxWriter(NameCoder nameCoder) {
      this(true, nameCoder);
   }

   public SaxWriter(boolean includeEnclosingDocument, NameCoder nameCoder) {
      super(nameCoder);
      this.entityResolver = null;
      this.dtdHandler = null;
      this.contentHandler = null;
      this.errorHandler = null;
      this.features = new HashMap();
      this.properties = new HashMap();
      this.depth = 0;
      this.elementStack = new LinkedList();
      this.buffer = new char[128];
      this.startTagInProgress = false;
      this.attributeList = new AttributesImpl();
      this.includeEnclosingDocument = includeEnclosingDocument;
   }

   /** @deprecated */
   public SaxWriter(XmlFriendlyReplacer replacer) {
      this(true, replacer);
   }

   /** @deprecated */
   public SaxWriter(boolean includeEnclosingDocument, XmlFriendlyReplacer replacer) {
      this(includeEnclosingDocument, (NameCoder)replacer);
   }

   public SaxWriter(boolean includeEnclosingDocument) {
      this(includeEnclosingDocument, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public SaxWriter() {
      this(true);
   }

   public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
      if (!name.equals("http://xml.org/sax/features/namespaces") && !name.equals("http://xml.org/sax/features/namespace-prefixes")) {
         throw new SAXNotRecognizedException(name);
      } else {
         this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
      }
   }

   public boolean getFeature(String name) throws SAXNotRecognizedException {
      if (!name.equals("http://xml.org/sax/features/namespaces") && !name.equals("http://xml.org/sax/features/namespace-prefixes")) {
         throw new SAXNotRecognizedException(name);
      } else {
         Boolean value = (Boolean)((Boolean)this.features.get(name));
         if (value == null) {
            value = Boolean.FALSE;
         }

         return value;
      }
   }

   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
      if (name.equals("http://com.thoughtworks.xstream/sax/property/configured-xstream")) {
         if (!(value instanceof XStream)) {
            throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/configured-xstream\" must be a non-null XStream object");
         }
      } else {
         if (!name.equals("http://com.thoughtworks.xstream/sax/property/source-object-list")) {
            throw new SAXNotRecognizedException(name);
         }

         if (!(value instanceof List)) {
            throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" must be a non-null List object");
         }

         List list = (List)value;
         if (list.isEmpty()) {
            throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" shall not be an empty list");
         }

         value = Collections.unmodifiableList(new ArrayList(list));
      }

      this.properties.put(name, value);
   }

   public Object getProperty(String name) throws SAXNotRecognizedException {
      if (!name.equals("http://com.thoughtworks.xstream/sax/property/configured-xstream") && !name.equals("http://com.thoughtworks.xstream/sax/property/source-object-list")) {
         throw new SAXNotRecognizedException(name);
      } else {
         return this.properties.get(name);
      }
   }

   public void setEntityResolver(EntityResolver resolver) {
      if (resolver == null) {
         throw new NullPointerException("resolver");
      } else {
         this.entityResolver = resolver;
      }
   }

   public EntityResolver getEntityResolver() {
      return this.entityResolver;
   }

   public void setDTDHandler(DTDHandler handler) {
      if (handler == null) {
         throw new NullPointerException("handler");
      } else {
         this.dtdHandler = handler;
      }
   }

   public DTDHandler getDTDHandler() {
      return this.dtdHandler;
   }

   public void setContentHandler(ContentHandler handler) {
      if (handler == null) {
         throw new NullPointerException("handler");
      } else {
         this.contentHandler = handler;
      }
   }

   public ContentHandler getContentHandler() {
      return this.contentHandler;
   }

   public void setErrorHandler(ErrorHandler handler) {
      if (handler == null) {
         throw new NullPointerException("handler");
      } else {
         this.errorHandler = handler;
      }
   }

   public ErrorHandler getErrorHandler() {
      return this.errorHandler;
   }

   public void parse(String systemId) throws SAXException {
      this.parse();
   }

   public void parse(InputSource input) throws SAXException {
      this.parse();
   }

   private void parse() throws SAXException {
      XStream xstream = (XStream)((XStream)this.properties.get("http://com.thoughtworks.xstream/sax/property/configured-xstream"));
      if (xstream == null) {
         xstream = new XStream();
      }

      List source = (List)((List)this.properties.get("http://com.thoughtworks.xstream/sax/property/source-object-list"));
      if (source != null && !source.isEmpty()) {
         try {
            this.startDocument(true);
            Iterator i = source.iterator();

            while(i.hasNext()) {
               xstream.marshal(i.next(), this);
            }

            this.endDocument(true);
         } catch (StreamException var4) {
            if (var4.getCause() instanceof SAXException) {
               throw (SAXException)((SAXException)var4.getCause());
            } else {
               throw new SAXException(var4);
            }
         }
      } else {
         throw new SAXException("Missing or empty source object list. Setting property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" is mandatory");
      }
   }

   public void startNode(String name) {
      try {
         if (this.depth != 0) {
            this.flushStartTag();
         } else if (this.includeEnclosingDocument) {
            this.startDocument(false);
         }

         this.elementStack.add(0, this.escapeXmlName(name));
         this.startTagInProgress = true;
         ++this.depth;
      } catch (SAXException var3) {
         throw new StreamException(var3);
      }
   }

   public void addAttribute(String name, String value) {
      if (this.startTagInProgress) {
         String escapedName = this.escapeXmlName(name);
         this.attributeList.addAttribute("", escapedName, escapedName, "CDATA", value);
      } else {
         throw new StreamException(new IllegalStateException("No startElement being processed"));
      }
   }

   public void setValue(String text) {
      try {
         this.flushStartTag();
         int lg = text.length();
         if (lg > this.buffer.length) {
            this.buffer = new char[lg];
         }

         text.getChars(0, lg, this.buffer, 0);
         this.contentHandler.characters(this.buffer, 0, lg);
      } catch (SAXException var3) {
         throw new StreamException(var3);
      }
   }

   public void endNode() {
      try {
         this.flushStartTag();
         String tagName = (String)((String)this.elementStack.remove(0));
         this.contentHandler.endElement("", tagName, tagName);
         --this.depth;
         if (this.depth == 0 && this.includeEnclosingDocument) {
            this.endDocument(false);
         }

      } catch (SAXException var2) {
         throw new StreamException(var2);
      }
   }

   private void startDocument(boolean multiObjectMode) throws SAXException {
      if (this.depth == 0) {
         this.contentHandler.startDocument();
         if (multiObjectMode) {
            ++this.depth;
         }
      }

   }

   private void endDocument(boolean multiObjectMode) throws SAXException {
      if (this.depth == 0 || this.depth == 1 && multiObjectMode) {
         this.contentHandler.endDocument();
         this.depth = 0;
      }

   }

   private void flushStartTag() throws SAXException {
      if (this.startTagInProgress) {
         String tagName = (String)((String)this.elementStack.get(0));
         this.contentHandler.startElement("", tagName, tagName, this.attributeList);
         this.attributeList.clear();
         this.startTagInProgress = false;
      }

   }

   public void flush() {
   }

   public void close() {
   }
}
