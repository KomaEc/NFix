package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

/** @deprecated */
public abstract class AbstractXmlWriter extends AbstractWriter implements XmlFriendlyWriter {
   protected AbstractXmlWriter() {
      this((NameCoder)(new XmlFriendlyNameCoder()));
   }

   /** @deprecated */
   protected AbstractXmlWriter(XmlFriendlyReplacer replacer) {
      this((NameCoder)replacer);
   }

   protected AbstractXmlWriter(NameCoder nameCoder) {
      super(nameCoder);
   }

   /** @deprecated */
   public String escapeXmlName(String name) {
      return super.encodeNode(name);
   }
}
