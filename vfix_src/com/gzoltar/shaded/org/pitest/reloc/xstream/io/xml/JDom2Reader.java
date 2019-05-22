package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class JDom2Reader extends AbstractDocumentReader {
   private Element currentElement;

   public JDom2Reader(Element root) {
      super(root);
   }

   public JDom2Reader(Document document) {
      super(document.getRootElement());
   }

   public JDom2Reader(Element root, NameCoder nameCoder) {
      super(root, (NameCoder)nameCoder);
   }

   public JDom2Reader(Document document, NameCoder nameCoder) {
      super(document.getRootElement(), (NameCoder)nameCoder);
   }

   protected void reassignCurrentElement(Object current) {
      this.currentElement = (Element)current;
   }

   protected Object getParent() {
      return this.currentElement.getParentElement();
   }

   protected Object getChild(int index) {
      return this.currentElement.getChildren().get(index);
   }

   protected int getChildCount() {
      return this.currentElement.getChildren().size();
   }

   public String getNodeName() {
      return this.decodeNode(this.currentElement.getName());
   }

   public String getValue() {
      return this.currentElement.getText();
   }

   public String getAttribute(String name) {
      return this.currentElement.getAttributeValue(this.encodeAttribute(name));
   }

   public String getAttribute(int index) {
      return ((Attribute)this.currentElement.getAttributes().get(index)).getValue();
   }

   public int getAttributeCount() {
      return this.currentElement.getAttributes().size();
   }

   public String getAttributeName(int index) {
      return this.decodeAttribute(((Attribute)this.currentElement.getAttributes().get(index)).getQualifiedName());
   }

   public String peekNextChild() {
      List list = this.currentElement.getChildren();
      return null != list && !list.isEmpty() ? this.decodeNode(((Element)list.get(0)).getName()) : null;
   }
}
