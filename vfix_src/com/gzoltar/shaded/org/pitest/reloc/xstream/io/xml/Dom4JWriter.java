package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import org.dom4j.Branch;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

public class Dom4JWriter extends AbstractDocumentWriter {
   private final DocumentFactory documentFactory;

   public Dom4JWriter(Branch root, DocumentFactory factory, NameCoder nameCoder) {
      super(root, (NameCoder)nameCoder);
      this.documentFactory = factory;
   }

   public Dom4JWriter(DocumentFactory factory, NameCoder nameCoder) {
      this((Branch)null, factory, (NameCoder)nameCoder);
   }

   public Dom4JWriter(Branch root, NameCoder nameCoder) {
      this(root, new DocumentFactory(), nameCoder);
   }

   /** @deprecated */
   public Dom4JWriter(Branch root, DocumentFactory factory, XmlFriendlyReplacer replacer) {
      this(root, factory, (NameCoder)replacer);
   }

   /** @deprecated */
   public Dom4JWriter(DocumentFactory factory, XmlFriendlyReplacer replacer) {
      this((Branch)null, factory, (NameCoder)replacer);
   }

   public Dom4JWriter(DocumentFactory documentFactory) {
      this((DocumentFactory)documentFactory, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   /** @deprecated */
   public Dom4JWriter(Branch root, XmlFriendlyReplacer replacer) {
      this(root, new DocumentFactory(), (NameCoder)replacer);
   }

   public Dom4JWriter(Branch root) {
      this(root, new DocumentFactory(), (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public Dom4JWriter() {
      this((DocumentFactory)(new DocumentFactory()), (NameCoder)(new XmlFriendlyNameCoder()));
   }

   protected Object createNode(String name) {
      Element element = this.documentFactory.createElement(this.encodeNode(name));
      Branch top = this.top();
      if (top != null) {
         this.top().add(element);
      }

      return element;
   }

   public void setValue(String text) {
      this.top().setText(text);
   }

   public void addAttribute(String key, String value) {
      ((Element)this.top()).addAttribute(this.encodeAttribute(key), value);
   }

   private Branch top() {
      return (Branch)this.getCurrent();
   }
}
