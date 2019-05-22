package org.xmlpull.v1.builder;

public interface XmlCharacters extends XmlContainer {
   String getText();

   Boolean isWhitespaceContent();

   XmlElement getParent();
}
