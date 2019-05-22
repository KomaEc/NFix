package org.xmlpull.v1.builder;

import java.util.Iterator;

public interface XmlDoctype extends XmlContainer {
   String getSystemIdentifier();

   String getPublicIdentifier();

   Iterator children();

   XmlDocument getParent();

   XmlProcessingInstruction addProcessingInstruction(String var1, String var2);

   void removeAllProcessingInstructions();
}
