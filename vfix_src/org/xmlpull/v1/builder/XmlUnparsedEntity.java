package org.xmlpull.v1.builder;

public interface XmlUnparsedEntity extends XmlContainer {
   String getName();

   String getSystemIdentifier();

   String getPublicIdentifier();

   String getDeclarationBaseUri();

   String getNotationName();

   XmlNotation getNotation();
}
