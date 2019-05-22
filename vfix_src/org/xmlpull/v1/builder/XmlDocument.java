package org.xmlpull.v1.builder;

import java.util.Iterator;

public interface XmlDocument extends XmlContainer {
   Iterator children();

   XmlElement getDocumentElement();

   Iterator notations();

   Iterator unparsedEntities();

   String getBaseUri();

   String getCharacterEncodingScheme();

   void setCharacterEncodingScheme(String var1);

   Boolean isStandalone();

   String getVersion();

   boolean isAllDeclarationsProcessed();

   void setDocumentElement(XmlElement var1);

   void addChild(Object var1);

   void insertChild(int var1, Object var2);

   void removeAllChildren();

   XmlComment newComment(String var1);

   XmlComment addComment(String var1);

   XmlDoctype newDoctype(String var1, String var2);

   XmlDoctype addDoctype(String var1, String var2);

   XmlElement addDocumentElement(String var1);

   XmlElement addDocumentElement(XmlNamespace var1, String var2);

   XmlProcessingInstruction newProcessingInstruction(String var1, String var2);

   XmlProcessingInstruction addProcessingInstruction(String var1, String var2);

   void remocveAllUnparsedEntities();

   XmlNotation addNotation(String var1, String var2, String var3, String var4);

   void removeAllNotations();
}
