package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import nu.xom.Attribute;
import nu.xom.Element;

public class XomWriter extends AbstractDocumentWriter {
   public XomWriter() {
      this((Element)null);
   }

   public XomWriter(Element parentElement) {
      this(parentElement, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public XomWriter(Element parentElement, NameCoder nameCoder) {
      super(parentElement, (NameCoder)nameCoder);
   }

   /** @deprecated */
   public XomWriter(Element parentElement, XmlFriendlyReplacer replacer) {
      this(parentElement, (NameCoder)replacer);
   }

   protected Object createNode(String name) {
      Element newNode = new Element(this.encodeNode(name));
      Element top = this.top();
      if (top != null) {
         this.top().appendChild(newNode);
      }

      return newNode;
   }

   public void addAttribute(String name, String value) {
      this.top().addAttribute(new Attribute(this.encodeAttribute(name), value));
   }

   public void setValue(String text) {
      this.top().appendChild(text);
   }

   private Element top() {
      return (Element)this.getCurrent();
   }
}
