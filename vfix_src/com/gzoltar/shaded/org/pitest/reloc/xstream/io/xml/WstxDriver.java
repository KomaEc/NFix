package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class WstxDriver extends StaxDriver {
   public WstxDriver() {
   }

   /** @deprecated */
   public WstxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
      super(qnameMap, (NameCoder)nameCoder);
   }

   public WstxDriver(QNameMap qnameMap, NameCoder nameCoder) {
      super(qnameMap, nameCoder);
   }

   public WstxDriver(QNameMap qnameMap) {
      super(qnameMap);
   }

   /** @deprecated */
   public WstxDriver(XmlFriendlyNameCoder nameCoder) {
      super((NameCoder)nameCoder);
   }

   public WstxDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   protected XMLInputFactory createInputFactory() {
      return new WstxInputFactory();
   }

   protected XMLOutputFactory createOutputFactory() {
      return new WstxOutputFactory();
   }
}
