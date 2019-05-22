package org.xmlpull.v1.builder;

public interface XmlUnexpandedEntityReference extends XmlContainer {
   String getName();

   String getSystemIdentifier();

   String getPublicIdentifier();

   String getDeclarationBaseUri();

   XmlElement getParent();
}
