package com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.Writer;

public class CompactWriter extends PrettyPrintWriter {
   public CompactWriter(Writer writer) {
      super(writer);
   }

   public CompactWriter(Writer writer, int mode) {
      super(writer, mode);
   }

   public CompactWriter(Writer writer, NameCoder nameCoder) {
      super(writer, nameCoder);
   }

   public CompactWriter(Writer writer, int mode, NameCoder nameCoder) {
      super(writer, mode, nameCoder);
   }

   /** @deprecated */
   public CompactWriter(Writer writer, XmlFriendlyReplacer replacer) {
      super(writer, replacer);
   }

   /** @deprecated */
   public CompactWriter(Writer writer, int mode, XmlFriendlyReplacer replacer) {
      super(writer, mode, replacer);
   }

   protected void endOfLine() {
   }
}
