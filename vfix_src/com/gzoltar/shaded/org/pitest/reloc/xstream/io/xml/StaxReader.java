package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxReader extends AbstractPullReader {
   private final QNameMap qnameMap;
   private final XMLStreamReader in;

   public StaxReader(QNameMap qnameMap, XMLStreamReader in) {
      this(qnameMap, in, (NameCoder)(new XmlFriendlyNameCoder()));
   }

   public StaxReader(QNameMap qnameMap, XMLStreamReader in, NameCoder replacer) {
      super(replacer);
      this.qnameMap = qnameMap;
      this.in = in;
      this.moveDown();
   }

   /** @deprecated */
   public StaxReader(QNameMap qnameMap, XMLStreamReader in, XmlFriendlyReplacer replacer) {
      this(qnameMap, in, (NameCoder)replacer);
   }

   protected int pullNextEvent() {
      try {
         switch(this.in.next()) {
         case 1:
         case 7:
            return 1;
         case 2:
         case 8:
            return 2;
         case 3:
         case 6:
         default:
            return 0;
         case 4:
            return 3;
         case 5:
            return 4;
         }
      } catch (XMLStreamException var2) {
         throw new StreamException(var2);
      }
   }

   protected String pullElementName() {
      QName qname = this.in.getName();
      return this.qnameMap.getJavaClassName(qname);
   }

   protected String pullText() {
      return this.in.getText();
   }

   public String getAttribute(String name) {
      return this.in.getAttributeValue((String)null, this.encodeAttribute(name));
   }

   public String getAttribute(int index) {
      return this.in.getAttributeValue(index);
   }

   public int getAttributeCount() {
      return this.in.getAttributeCount();
   }

   public String getAttributeName(int index) {
      return this.decodeAttribute(this.in.getAttributeLocalName(index));
   }

   public void appendErrors(ErrorWriter errorWriter) {
      errorWriter.add("line number", String.valueOf(this.in.getLocation().getLineNumber()));
   }

   public void close() {
      try {
         this.in.close();
      } catch (XMLStreamException var2) {
         throw new StreamException(var2);
      }
   }
}
