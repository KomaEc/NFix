package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastStack;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.IOException;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class Dom4JXmlWriter extends AbstractXmlWriter {
   private final XMLWriter writer;
   private final FastStack elementStack;
   private AttributesImpl attributes;
   private boolean started;
   private boolean children;

   public Dom4JXmlWriter(XMLWriter writer) {
      this(writer, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public Dom4JXmlWriter(XMLWriter writer, NameCoder nameCoder) {
      super(nameCoder);
      this.writer = writer;
      this.elementStack = new FastStack(16);
      this.attributes = new AttributesImpl();

      try {
         writer.startDocument();
      } catch (SAXException var4) {
         throw new StreamException(var4);
      }
   }

   /** @deprecated */
   public Dom4JXmlWriter(XMLWriter writer, XmlFriendlyReplacer replacer) {
      this(writer, (NameCoder)replacer);
   }

   public void startNode(String name) {
      if (this.elementStack.size() > 0) {
         try {
            this.startElement();
         } catch (SAXException var3) {
            throw new StreamException(var3);
         }

         this.started = false;
      }

      this.elementStack.push(this.encodeNode(name));
      this.children = false;
   }

   public void setValue(String text) {
      char[] value = text.toCharArray();
      if (value.length > 0) {
         try {
            this.startElement();
            this.writer.characters(value, 0, value.length);
         } catch (SAXException var4) {
            throw new StreamException(var4);
         }

         this.children = true;
      }

   }

   public void addAttribute(String key, String value) {
      this.attributes.addAttribute("", "", this.encodeAttribute(key), "string", value);
   }

   public void endNode() {
      try {
         if (!this.children) {
            Element element = new DefaultElement((String)this.elementStack.pop());

            for(int i = 0; i < this.attributes.getLength(); ++i) {
               element.addAttribute(this.attributes.getQName(i), this.attributes.getValue(i));
            }

            this.writer.write(element);
            this.attributes.clear();
            this.children = true;
            this.started = true;
         } else {
            this.startElement();
            this.writer.endElement("", "", (String)this.elementStack.pop());
         }

      } catch (SAXException var3) {
         throw new StreamException(var3);
      } catch (IOException var4) {
         throw new StreamException(var4);
      }
   }

   public void flush() {
      try {
         this.writer.flush();
      } catch (IOException var2) {
         throw new StreamException(var2);
      }
   }

   public void close() {
      try {
         this.writer.endDocument();
      } catch (SAXException var2) {
         throw new StreamException(var2);
      }
   }

   private void startElement() throws SAXException {
      if (!this.started) {
         this.writer.startElement("", "", (String)this.elementStack.peek(), this.attributes);
         this.attributes.clear();
         this.started = true;
      }

   }
}
