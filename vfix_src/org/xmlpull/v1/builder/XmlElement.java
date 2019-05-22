package org.xmlpull.v1.builder;

import java.util.Iterator;

public interface XmlElement extends XmlContainer {
   String NO_NAMESPACE = "";

   Iterator children();

   Iterator attributes();

   Iterator namespaces();

   String getBaseUri();

   void setBaseUri(String var1);

   XmlContainer getParent();

   void setParent(XmlContainer var1);

   XmlNamespace getNamespace();

   String getNamespaceName();

   void setNamespace(XmlNamespace var1);

   String getName();

   void setName(String var1);

   XmlAttribute addAttribute(XmlAttribute var1);

   XmlAttribute addAttribute(String var1, String var2);

   XmlAttribute addAttribute(XmlNamespace var1, String var2, String var3);

   XmlAttribute addAttribute(String var1, XmlNamespace var2, String var3, String var4);

   XmlAttribute addAttribute(String var1, XmlNamespace var2, String var3, String var4, boolean var5);

   XmlAttribute addAttribute(String var1, String var2, String var3, String var4, String var5, boolean var6);

   void ensureAttributeCapacity(int var1);

   XmlAttribute findAttribute(String var1, String var2);

   boolean hasAttributes();

   void removeAttribute(XmlAttribute var1);

   void removeAllAttributes();

   XmlNamespace declareNamespace(String var1, String var2);

   XmlNamespace declareNamespace(XmlNamespace var1);

   void ensureNamespaceDeclarationsCapacity(int var1);

   boolean hasNamespaceDeclarations();

   XmlNamespace lookupNamespaceByPrefix(String var1);

   XmlNamespace lookupNamespaceByName(String var1);

   XmlNamespace newNamespace(String var1);

   XmlNamespace newNamespace(String var1, String var2);

   void removeAllNamespaceDeclarations();

   void addChild(Object var1);

   void addChild(int var1, Object var2);

   XmlElement addElement(String var1);

   XmlElement addElement(XmlElement var1);

   XmlElement addElement(XmlNamespace var1, String var2);

   boolean hasChildren();

   boolean hasChild(Object var1);

   void ensureChildrenCapacity(int var1);

   XmlElement findElementByName(String var1);

   XmlElement findElementByName(String var1, String var2);

   XmlElement findElementByName(String var1, XmlElement var2);

   XmlElement findElementByName(String var1, String var2, XmlElement var3);

   void insertChild(int var1, Object var2);

   XmlElement newElement(String var1);

   XmlElement newElement(XmlNamespace var1, String var2);

   XmlElement newElement(String var1, String var2);

   void removeAllChildren();

   void removeChild(Object var1);

   void replaceChild(Object var1, Object var2);
}
