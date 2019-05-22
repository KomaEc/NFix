package org.xmlpull.v1.builder;

public interface XmlProcessingInstruction extends XmlContainer {
   String getTarget();

   String getContent();

   String getBaseUri();

   XmlNotation getNotation();

   XmlContainer getParent();
}
