package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import org.jdom2.DefaultJDOMFactory;
import org.jdom2.Element;
import org.jdom2.JDOMFactory;

public class JDom2Writer extends AbstractDocumentWriter {
   private final JDOMFactory documentFactory;

   public JDom2Writer(Element container, JDOMFactory factory, NameCoder nameCoder) {
      super(container, (NameCoder)nameCoder);
      this.documentFactory = factory;
   }

   public JDom2Writer(Element container, JDOMFactory factory) {
      this(container, factory, new XmlFriendlyNameCoder());
   }

   public JDom2Writer(JDOMFactory factory, NameCoder nameCoder) {
      this((Element)null, factory, nameCoder);
   }

   public JDom2Writer(JDOMFactory factory) {
      this((Element)null, (JDOMFactory)factory);
   }

   public JDom2Writer(Element container, NameCoder nameCoder) {
      this(container, new DefaultJDOMFactory(), nameCoder);
   }

   public JDom2Writer(Element container) {
      this((Element)container, (JDOMFactory)(new DefaultJDOMFactory()));
   }

   public JDom2Writer() {
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
