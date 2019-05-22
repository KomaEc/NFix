package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.bea.xml.stream.MXParserFactory;
import com.bea.xml.stream.XMLOutputFactoryBase;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class BEAStaxDriver extends StaxDriver {
   public BEAStaxDriver() {
   }

   /** @deprecated */
   public BEAStaxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
      super(qnameMap, (NameCoder)nameCoder);
   }

   public BEAStaxDriver(QNameMap qnameMap, NameCoder nameCoder) {
      super(qnameMap, nameCoder);
   }

   public BEAStaxDriver(QNameMap qnameMap) {
      super(qnameMap);
   }

   /** @deprecated */
   public BEAStaxDriver(XmlFriendlyNameCoder nameCoder) {
      super((NameCoder)nameCoder);
   }

   public BEAStaxDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   protected XMLInputFactory createInputFactory() {
      return new MXParserFactory();
   }

   protected XMLOutputFactory createOutputFactory() {
      return new XMLOutputFactoryBase();
   }
}
