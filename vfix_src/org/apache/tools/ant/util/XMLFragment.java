package org.apache.tools.ant.util;

import org.apache.tools.ant.DynamicConfiguratorNS;
import org.apache.tools.ant.DynamicElementNS;
import org.apache.tools.ant.ProjectComponent;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class XMLFragment extends ProjectComponent implements DynamicElementNS {
   private Document doc = JAXPUtils.getDocumentBuilder().newDocument();
   private DocumentFragment fragment;

   public XMLFragment() {
      this.fragment = this.doc.createDocumentFragment();
   }

   public DocumentFragment getFragment() {
      return this.fragment;
   }

   public void addText(String s) {
      this.addText(this.fragment, s);
   }

   public Object createDynamicElement(String uri, String name, String qName) {
      Element e = null;
      if (uri.equals("")) {
         e = this.doc.createElement(name);
      } else {
         e = this.doc.createElementNS(uri, qName);
      }

      this.fragment.appendChild(e);
      return new XMLFragment.Child(e);
   }

   private void addText(Node n, String s) {
      s = this.getProject().replaceProperties(s);
      if (s != null && !s.trim().equals("")) {
         Text t = this.doc.createTextNode(s.trim());
         n.appendChild(t);
      }

   }

   public class Child implements DynamicConfiguratorNS {
      private Element e;

      Child(Element e) {
         this.e = e;
      }

      public void addText(String s) {
         XMLFragment.this.addText(this.e, s);
      }

      public void setDynamicAttribute(String uri, String name, String qName, String value) {
         if (uri.equals("")) {
            this.e.setAttribute(name, value);
         } else {
            this.e.setAttributeNS(uri, qName, value);
         }

      }

      public Object createDynamicElement(String uri, String name, String qName) {
         Element e2 = null;
         if (uri.equals("")) {
            e2 = XMLFragment.this.doc.createElement(name);
         } else {
            e2 = XMLFragment.this.doc.createElementNS(uri, qName);
         }

         this.e.appendChild(e2);
         return XMLFragment.this.new Child(e2);
      }
   }
}
