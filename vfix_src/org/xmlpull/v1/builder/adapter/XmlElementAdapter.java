package org.xmlpull.v1.builder.adapter;

import java.util.Iterator;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;

public class XmlElementAdapter implements XmlElement {
   private XmlElement target;
   private XmlContainer parent;

   public XmlElementAdapter(XmlElement target) {
      this.target = target;
      if (target.getParent() != null) {
         XmlContainer parent = target.getParent();
         if (parent instanceof XmlDocument) {
            XmlDocument doc = (XmlDocument)parent;
            doc.setDocumentElement(this);
         }

         if (parent instanceof XmlElement) {
            XmlElement parentEl = (XmlElement)parent;
            parentEl.replaceChild(this, target);
         }
      }

      Iterator iter = target.children();

      while(iter.hasNext()) {
         Object child = iter.next();
         this.fixParent(child);
      }

      target.setParent((XmlContainer)null);
   }

   private void fixParent(Object child) {
      if (child instanceof XmlElement) {
         XmlElement childEl = (XmlElement)child;
         this.fixElementParent(childEl);
      }

   }

   private XmlElement fixElementParent(XmlElement el) {
      el.setParent(this);
      return el;
   }

   public XmlContainer getParent() {
      return this.parent;
   }

   public void setParent(XmlContainer parent) {
      this.parent = parent;
   }

   public XmlNamespace newNamespace(String prefix, String namespaceName) {
      return this.target.newNamespace(prefix, namespaceName);
   }

   public XmlAttribute findAttribute(String attributeNamespaceName, String attributeName) {
      return this.target.findAttribute(attributeNamespaceName, attributeName);
   }

   public Iterator attributes() {
      return this.target.attributes();
   }

   public void removeAllChildren() {
      this.target.removeAllChildren();
   }

   public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
      return this.target.addAttribute(attributeType, attributePrefix, attributeNamespace, attributeName, attributeValue, specified);
   }

   public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
      return this.target.lookupNamespaceByPrefix(namespacePrefix);
   }

   public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
      return this.target.addAttribute(namespace, name, value);
   }

   public String getNamespaceName() {
      return this.target.getNamespaceName();
   }

   public void ensureChildrenCapacity(int minCapacity) {
      this.target.ensureChildrenCapacity(minCapacity);
   }

   public Iterator namespaces() {
      return this.target.namespaces();
   }

   public void removeAllAttributes() {
      this.target.removeAllAttributes();
   }

   public XmlNamespace getNamespace() {
      return this.target.getNamespace();
   }

   public String getBaseUri() {
      return this.target.getBaseUri();
   }

   public void removeAttribute(XmlAttribute attr) {
      this.target.removeAttribute(attr);
   }

   public XmlNamespace declareNamespace(String prefix, String namespaceName) {
      return this.target.declareNamespace(prefix, namespaceName);
   }

   public void removeAllNamespaceDeclarations() {
      this.target.removeAllNamespaceDeclarations();
   }

   public boolean hasAttributes() {
      return this.target.hasAttributes();
   }

   public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value, boolean specified) {
      return this.target.addAttribute(type, namespace, name, value, specified);
   }

   public XmlNamespace declareNamespace(XmlNamespace namespace) {
      return this.target.declareNamespace(namespace);
   }

   public XmlAttribute addAttribute(String name, String value) {
      return this.target.addAttribute(name, value);
   }

   public boolean hasNamespaceDeclarations() {
      return this.target.hasNamespaceDeclarations();
   }

   public XmlNamespace lookupNamespaceByName(String namespaceName) {
      return this.target.lookupNamespaceByName(namespaceName);
   }

   public XmlNamespace newNamespace(String namespaceName) {
      return this.target.newNamespace(namespaceName);
   }

   public void setBaseUri(String baseUri) {
      this.target.setBaseUri(baseUri);
   }

   public void setNamespace(XmlNamespace namespace) {
      this.target.setNamespace(namespace);
   }

   public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
      this.target.ensureNamespaceDeclarationsCapacity(minCapacity);
   }

   public String getName() {
      return this.target.getName();
   }

   public void setName(String name) {
      this.target.setName(name);
   }

   public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value) {
      return this.target.addAttribute(type, namespace, name, value);
   }

   public void ensureAttributeCapacity(int minCapacity) {
      this.target.ensureAttributeCapacity(minCapacity);
   }

   public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
      return this.target.addAttribute(attributeValueToAdd);
   }

   public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
      return this.target.findElementByName(name, elementToStartLooking);
   }

   public XmlElement newElement(XmlNamespace namespace, String name) {
      return this.target.newElement(namespace, name);
   }

   public XmlElement addElement(XmlElement element) {
      return this.fixElementParent(this.target.addElement(element));
   }

   public XmlElement addElement(String name) {
      return this.fixElementParent(this.target.addElement(name));
   }

   public XmlElement findElementByName(String namespaceName, String name) {
      return this.target.findElementByName(namespaceName, name);
   }

   public void addChild(Object child) {
      this.target.addChild(child);
      this.fixParent(child);
   }

   public void insertChild(int pos, Object childToInsert) {
      this.target.insertChild(pos, childToInsert);
      this.fixParent(childToInsert);
   }

   public XmlElement findElementByName(String name) {
      return this.target.findElementByName(name);
   }

   public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
      return this.target.findElementByName(namespaceName, name, elementToStartLooking);
   }

   public void removeChild(Object child) {
      this.target.removeChild(child);
   }

   public Iterator children() {
      return this.target.children();
   }

   public boolean hasChild(Object child) {
      return this.target.hasChild(child);
   }

   public XmlElement newElement(String namespaceName, String name) {
      return this.target.newElement(namespaceName, name);
   }

   public XmlElement addElement(XmlNamespace namespace, String name) {
      return this.fixElementParent(this.target.addElement(namespace, name));
   }

   public boolean hasChildren() {
      return this.target.hasChildren();
   }

   public void addChild(int pos, Object child) {
      this.target.addChild(pos, child);
      this.fixParent(child);
   }

   public void replaceChild(Object newChild, Object oldChild) {
      this.target.replaceChild(newChild, oldChild);
      this.fixParent(newChild);
   }

   public XmlElement newElement(String name) {
      return this.target.newElement(name);
   }
}
