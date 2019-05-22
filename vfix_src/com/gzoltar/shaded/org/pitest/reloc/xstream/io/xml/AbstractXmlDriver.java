package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;

/** @deprecated */
public abstract class AbstractXmlDriver extends AbstractDriver {
   /** @deprecated */
   public AbstractXmlDriver() {
      this((NameCoder)(new XmlFriendlyNameCoder()));
   }

   public AbstractXmlDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   /** @deprecated */
   public AbstractXmlDriver(XmlFriendlyReplacer replacer) {
      this((NameCoder)replacer);
   }

   /** @deprecated */
   protected XmlFriendlyReplacer xmlFriendlyReplacer() {
      NameCoder nameCoder = this.getNameCoder();
      return nameCoder instanceof XmlFriendlyReplacer ? (XmlFriendlyReplacer)nameCoder : null;
   }
}
