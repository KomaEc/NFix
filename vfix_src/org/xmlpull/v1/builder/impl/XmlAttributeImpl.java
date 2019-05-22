package org.xmlpull.v1.builder.impl;

import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;

public class XmlAttributeImpl implements XmlAttribute {
   private XmlElement owner_;
   private String prefix_;
   private XmlNamespace namespace_;
   private String name_;
   private String value_;
   private String type_;
   private boolean default_;

   XmlAttributeImpl(XmlElement owner, String name, String value) {
      this.type_ = "CDATA";
      this.owner_ = owner;
      this.name_ = name;
      if (value == null) {
         throw new IllegalArgumentException("attribute value can not be null");
      } else {
         this.value_ = value;
      }
   }

   XmlAttributeImpl(XmlElement owner, XmlNamespace namespace, String name, String value) {
      this(owner, name, value);
      this.namespace_ = namespace;
   }

   XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value) {
      this(owner, namespace, name, value);
      this.type_ = type;
   }

   XmlAttributeImpl(XmlElement owner, String type, XmlNamespace namespace, String name, String value, boolean specified) {
      this(owner, namespace, name, value);
      if (type == null) {
         throw new IllegalArgumentException("attribute type can not be null");
      } else {
         this.type_ = type;
         this.default_ = !specified;
      }
   }

   public XmlElement getOwner() {
      return this.owner_;
   }

   public XmlNamespace getNamespace() {
      return this.namespace_;
   }

   public String getNamespaceName() {
      return this.namespace_ != null ? this.namespace_.getNamespaceName() : null;
   }

   public String getName() {
      return this.name_;
   }

   public String getValue() {
      return this.value_;
   }

   public String getType() {
      return this.type_;
   }

   public boolean isSpecified() {
      return !this.default_;
   }

   public String toString() {
      return "name=" + this.name_ + " value=" + this.value_;
   }
}
