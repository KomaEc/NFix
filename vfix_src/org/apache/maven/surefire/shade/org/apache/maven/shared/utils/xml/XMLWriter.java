package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml;

public interface XMLWriter {
   void setEncoding(String var1);

   void setDocType(String var1);

   void startElement(String var1);

   void addAttribute(String var1, String var2);

   void writeText(String var1);

   void writeMarkup(String var1);

   void endElement();
}
