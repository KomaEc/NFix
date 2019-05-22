package org.xmlpull.v1.builder.impl;

import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlNamespace;

public class XmlNamespaceImpl implements XmlNamespace {
   private String namespaceName;
   private String prefix;

   XmlNamespaceImpl(String prefix, String namespaceName) {
      this.prefix = prefix;
      if (namespaceName == null) {
         throw new XmlBuilderException("namespace name can not be null");
      } else {
         this.namespaceName = namespaceName;
      }
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getNamespaceName() {
      return this.namespaceName;
   }
}
