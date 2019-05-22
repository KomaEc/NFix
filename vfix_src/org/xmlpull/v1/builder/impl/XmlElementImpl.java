package org.xmlpull.v1.builder.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;

public class XmlElementImpl implements XmlElement {
   private XmlContainer parent;
   private XmlNamespace namespace;
   private String name;
   private List attrs;
   private List nsList;
   private List children;
   private static final Iterator EMPTY_ITERATOR = new XmlElementImpl.EmptyIterator();

   XmlElementImpl(XmlNamespace namespace, String name) {
      this.namespace = namespace;
      this.name = name;
   }

   XmlElementImpl(String namespaceName, String name) {
      if (namespaceName != null) {
         this.namespace = new XmlNamespaceImpl((String)null, namespaceName);
      }

      this.name = name;
   }

   public XmlContainer getParent() {
      return this.parent;
   }

   public void setParent(XmlContainer parent) {
      if (parent != null) {
         if (parent instanceof XmlElement) {
            Iterator iter = ((XmlElement)parent).children();
            boolean found = false;

            while(iter.hasNext()) {
               Object element = iter.next();
               if (element == this) {
                  found = true;
                  break;
               }
            }

            if (!found) {
               throw new XmlBuilderException("this element must be child of parent to set its parent");
            }
         } else if (parent instanceof XmlDocument) {
            XmlDocument doc = (XmlDocument)parent;
            if (doc.getDocumentElement() != this) {
               throw new XmlBuilderException("this element must be root docuemnt element to have document set as parent but already different element is set as root document element");
            }
         }
      }

      this.parent = parent;
   }

   public XmlNamespace getNamespace() {
      return this.namespace;
   }

   public String getNamespaceName() {
      return this.namespace != null ? this.namespace.getNamespaceName() : null;
   }

   public void setNamespace(XmlNamespace namespace) {
      this.namespace = namespace;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      return "name[" + this.name + "] namespace[" + this.namespace.getNamespaceName() + "]";
   }

   public String getBaseUri() {
      throw new XmlBuilderException("not implemented");
   }

   public void setBaseUri(String baseUri) {
      throw new XmlBuilderException("not implemented");
   }

   public Iterator attributes() {
      return this.attrs == null ? EMPTY_ITERATOR : this.attrs.iterator();
   }

   public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
      if (this.attrs == null) {
         this.ensureAttributeCapacity(5);
      }

      this.attrs.add(attributeValueToAdd);
      return attributeValueToAdd;
   }

   public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
      return this.addAttribute("CDATA", namespace, name, value, false);
   }

   public XmlAttribute addAttribute(String name, String value) {
      return this.addAttribute("CDATA", (XmlNamespace)null, name, value, false);
   }

   public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value) {
      return this.addAttribute(attributeType, namespace, name, value, false);
   }

   public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value, boolean specified) {
      XmlAttribute a = new XmlAttributeImpl(this, attributeType, namespace, name, value, specified);
      return this.addAttribute(a);
   }

   public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
      XmlNamespace n = this.newNamespace(attributePrefix, attributeNamespace);
      return this.addAttribute(attributeType, n, attributeName, attributeValue, specified);
   }

   public void ensureAttributeCapacity(int minCapacity) {
      if (this.attrs == null) {
         this.attrs = new ArrayList(minCapacity);
      } else {
         ((ArrayList)this.attrs).ensureCapacity(minCapacity);
      }

   }

   public boolean hasAttributes() {
      return this.attrs != null && this.attrs.size() > 0;
   }

   public XmlAttribute findAttribute(String attributeNamespace, String attributeName) {
      if (attributeName == null) {
         throw new IllegalArgumentException("attribute name ca not ber null");
      } else if (this.attrs == null) {
         return null;
      } else {
         int length = this.attrs.size();

         for(int i = 0; i < length; ++i) {
            XmlAttribute a = (XmlAttribute)this.attrs.get(i);
            String aName = a.getName();
            if (aName == attributeName || attributeName.equals(aName)) {
               if (attributeNamespace != null) {
                  String aNamespace = a.getNamespaceName();
                  if (attributeNamespace.equals(aNamespace)) {
                     return a;
                  }

                  if (attributeNamespace == "" && aNamespace == null) {
                     return a;
                  }
               } else {
                  if (a.getNamespace() == null) {
                     return a;
                  }

                  if (a.getNamespace().getNamespaceName() == "") {
                     return a;
                  }
               }
            }
         }

         return null;
      }
   }

   public void removeAllAttributes() {
      this.attrs = null;
   }

   public void removeAttribute(XmlAttribute attr) {
      for(int i = 0; i < this.attrs.size(); ++i) {
         if (this.attrs.get(i).equals(attr)) {
            this.attrs.remove(i);
            break;
         }
      }

   }

   public XmlNamespace declareNamespace(String prefix, String namespaceName) {
      if (prefix == null) {
         throw new XmlBuilderException("namespace added to element must have not null prefix");
      } else {
         XmlNamespace n = this.newNamespace(prefix, namespaceName);
         return this.declareNamespace(n);
      }
   }

   public XmlNamespace declareNamespace(XmlNamespace n) {
      if (n.getPrefix() == null) {
         throw new XmlBuilderException("namespace added to element must have not null prefix");
      } else {
         if (this.nsList == null) {
            this.ensureNamespaceDeclarationsCapacity(5);
         }

         this.nsList.add(n);
         return n;
      }
   }

   public boolean hasNamespaceDeclarations() {
      return this.nsList != null && this.nsList.size() > 0;
   }

   public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
      if (namespacePrefix == null) {
         throw new IllegalArgumentException("namespace prefix can not ber null");
      } else {
         if (this.hasNamespaceDeclarations()) {
            int length = this.nsList.size();

            for(int i = 0; i < length; ++i) {
               XmlNamespace n = (XmlNamespace)this.nsList.get(i);
               if (namespacePrefix.equals(n.getPrefix())) {
                  return n;
               }
            }
         }

         return null;
      }
   }

   public XmlNamespace lookupNamespaceByName(String namespaceName) {
      if (namespaceName == null) {
         throw new IllegalArgumentException("namespace name can not ber null");
      } else {
         if (this.hasNamespaceDeclarations()) {
            int length = this.nsList.size();

            for(int i = 0; i < length; ++i) {
               XmlNamespace n = (XmlNamespace)this.nsList.get(i);
               if (namespaceName.equals(n.getNamespaceName())) {
                  return n;
               }
            }
         }

         return null;
      }
   }

   public Iterator namespaces() {
      return this.nsList == null ? EMPTY_ITERATOR : this.nsList.iterator();
   }

   public XmlNamespace newNamespace(String namespaceName) {
      return this.newNamespace((String)null, namespaceName);
   }

   public XmlNamespace newNamespace(String prefix, String namespaceName) {
      return new XmlNamespaceImpl(prefix, namespaceName);
   }

   public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
      if (this.nsList == null) {
         this.nsList = new ArrayList(minCapacity);
      } else {
         ((ArrayList)this.nsList).ensureCapacity(minCapacity);
      }

   }

   public void removeAllNamespaceDeclarations() {
      this.nsList = null;
   }

   public void addChild(Object child) {
      if (this.children == null) {
         this.ensureChildrenCapacity(1);
      }

      this.checkChildParent(child);
      this.children.add(child);
      this.setChildParent(child);
   }

   public void addChild(int index, Object child) {
      if (this.children == null) {
         this.ensureChildrenCapacity(1);
      }

      this.checkChildParent(child);
      this.children.add(index, child);
      this.setChildParent(child);
   }

   private void checkChildParent(Object child) {
      if (child instanceof XmlContainer) {
         if (child instanceof XmlElement) {
            XmlElement elChild = (XmlElement)child;
            XmlContainer p = elChild.getParent();
            if (p != null && p != this.parent) {
               throw new XmlBuilderException("child must have no parent to be added to this node");
            }
         } else if (child instanceof XmlDocument) {
            throw new XmlBuilderException("docuemet can not be stored as element child");
         }
      }

   }

   private void setChildParent(Object child) {
      if (child instanceof XmlElement) {
         XmlElement elChild = (XmlElement)child;
         elChild.setParent(this);
      }

   }

   public XmlElement addElement(XmlElement element) {
      this.addChild(element);
      return element;
   }

   public XmlElement addElement(XmlNamespace namespace, String name) {
      XmlElement el = this.newElement(namespace, name);
      this.addChild(el);
      return el;
   }

   public XmlElement addElement(String name) {
      return this.addElement((XmlNamespace)null, name);
   }

   public Iterator children() {
      return this.children == null ? EMPTY_ITERATOR : this.children.iterator();
   }

   public void ensureChildrenCapacity(int minCapacity) {
      if (this.children == null) {
         this.children = new ArrayList(minCapacity);
      } else {
         ((ArrayList)this.children).ensureCapacity(minCapacity);
      }

   }

   public XmlElement findElementByName(String name) {
      if (this.children == null) {
         return null;
      } else {
         int length = this.children.size();

         for(int i = 0; i < length; ++i) {
            Object child = this.children.get(i);
            if (child instanceof XmlElement) {
               XmlElement childEl = (XmlElement)child;
               if (name.equals(childEl.getName())) {
                  return childEl;
               }
            }
         }

         return null;
      }
   }

   public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
      throw new UnsupportedOperationException();
   }

   public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
      throw new UnsupportedOperationException();
   }

   public XmlElement findElementByName(String namespaceName, String name) {
      throw new UnsupportedOperationException();
   }

   public boolean hasChild(Object child) {
      if (this.children == null) {
         return false;
      } else {
         for(int i = 0; i < this.children.size(); ++i) {
            if (this.children.get(i) == child) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean hasChildren() {
      return this.children != null && this.children.size() > 0;
   }

   public void insertChild(int pos, Object childToInsert) {
      this.children.set(pos, childToInsert);
   }

   public XmlElement newElement(String name) {
      return this.newElement((XmlNamespace)null, name);
   }

   public XmlElement newElement(String namespace, String name) {
      return new XmlElementImpl(namespace, name);
   }

   public XmlElement newElement(XmlNamespace namespace, String name) {
      return new XmlElementImpl(namespace, name);
   }

   public void replaceChild(Object newChild, Object oldChild) {
      if (newChild == null) {
         throw new IllegalArgumentException("new child to replace can not be null");
      } else if (oldChild == null) {
         throw new IllegalArgumentException("old child to replace can not be null");
      } else if (!this.hasChildren()) {
         throw new XmlBuilderException("no children available for replacement");
      } else {
         int pos = this.children.indexOf(oldChild);
         if (pos == -1) {
            throw new XmlBuilderException("could not find child to replace");
         } else {
            this.children.set(pos, newChild);
         }
      }
   }

   public void removeAllChildren() {
      this.children = null;
   }

   public void removeChild(Object child) {
      if (child == null) {
         throw new IllegalArgumentException("child to remove can not be null");
      } else if (!this.hasChildren()) {
         throw new XmlBuilderException("no children to remove");
      } else {
         int pos = this.children.indexOf(child);
         if (pos != -1) {
            this.children.remove(pos);
         }

      }
   }

   private static class EmptyIterator implements Iterator {
      private EmptyIterator() {
      }

      public boolean hasNext() {
         return false;
      }

      public Object next() {
         throw new RuntimeException("this iterator has no content and next() is not allowed");
      }

      public void remove() {
         throw new RuntimeException("this iterator has no content and remove() is not allowed");
      }

      // $FF: synthetic method
      EmptyIterator(Object x0) {
         this();
      }
   }
}
