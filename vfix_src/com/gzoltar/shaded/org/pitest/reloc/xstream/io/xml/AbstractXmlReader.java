package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

/** @deprecated */
public abstract class AbstractXmlReader extends AbstractReader {
   protected AbstractXmlReader() {
      this((NameCoder)(new XmlFriendlyNameCoder()));
   }

   /** @deprecated */
   protected AbstractXmlReader(XmlFriendlyReplacer replacer) {
      this((NameCoder)replacer);
   }

   protected AbstractXmlReader(NameCoder nameCoder) {
      super(nameCoder);
   }

   /** @deprecated */
   public String unescapeXmlName(String name) {
      return this.decodeNode(name);
   }

   /** @deprecated */
   protected String escapeXmlName(String name) {
      return this.encodeNode(name);
   }
}
