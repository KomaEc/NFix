package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import org.jdom.DefaultJDOMFactory;
import org.jdom.Element;
import org.jdom.JDOMFactory;

public class JDomWriter extends AbstractDocumentWriter {
   private final JDOMFactory documentFactory;

   public JDomWriter(Element container, JDOMFactory factory, NameCoder nameCoder) {
      super(container, (NameCoder)nameCoder);
      this.documentFactory = factory;
   }

   /** @deprecated */
   public JDomWriter(Element container, JDOMFactory factory, XmlFriendlyReplacer replacer) {
      this(container, factory, (NameCoder)replacer);
   }

   public JDomWriter(Element container, JDOMFactory factory) {
      this(container, factory, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public JDomWriter(JDOMFactory factory, NameCoder nameCoder) {
      this((Element)null, factory, (NameCoder)nameCoder);
   }

   /** @deprecated */
   public JDomWriter(JDOMFactory factory, XmlFriendlyReplacer replacer) {
      this((Element)null, factory, (NameCoder)replacer);
   }

   public JDomWriter(JDOMFactory factory) {
      this((Element)null, (JDOMFactory)factory);
   }

   public JDomWriter(Element container, NameCoder nameCoder) {
      this(container, new DefaultJDOMFactory(), (NameCoder)nameCoder);
   }

   /** @deprecated */
   public JDomWriter(Element container, XmlFriendlyReplacer replacer) {
      this(container, new DefaultJDOMFactory(), (NameCoder)replacer);
   }

   public JDomWriter(Element container) {
      this((Element)container, (JDOMFactory)(new DefaultJDOMFactory()));
   }

   public JDomWriter() {
      this((JDOMFactory)(new DefaultJDOMFactory()));
   }

   protected Object createNode(String name) {
      Element element = this.documentFactory.element(this.encodeNode(name));
      Element parent = this.top();
      if (parent != null) {
         parent.addContent(element);
      }

      return element;
   }

   public void setValue(String text) {
      this.top().addContent(this.documentFactory.text(text));
   }

   public void addAttribute(String key, String value) {
      this.top().setAttribute(this.documentFactory.attribute(this.encodeAttribute(key), value));
   }

   private Element top() {
      return (Element)this.getCurrent();
   }
}
