package com.gzoltar.shaded.org.jacoco.report.internal.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class XMLDocument extends XMLElement {
   private static final String HEADER = "<?xml version=\"1.0\" encoding=\"%s\"?>";
   private static final String HEADER_STANDALONE = "<?xml version=\"1.0\" encoding=\"%s\" standalone=\"yes\"?>";
   private static final String DOCTYPE = "<!DOCTYPE %s PUBLIC \"%s\" \"%s\">";

   public XMLDocument(String rootnode, String pubId, String system, String encoding, boolean standalone, Writer writer) throws IOException {
      super(writer, rootnode);
      writeHeader(rootnode, pubId, system, encoding, standalone, writer);
      this.beginOpenTag();
   }

   public XMLDocument(String rootnode, String pubId, String system, String encoding, boolean standalone, OutputStream output) throws IOException {
      this(rootnode, pubId, system, encoding, standalone, (Writer)(new OutputStreamWriter(output, encoding)));
   }

   public void close() throws IOException {
      super.close();
      this.writer.close();
   }

   private static void writeHeader(String rootnode, String pubId, String system, String encoding, boolean standalone, Writer writer) throws IOException {
      if (standalone) {
         writer.write(String.format("<?xml version=\"1.0\" encoding=\"%s\" standalone=\"yes\"?>", encoding));
      } else {
         writer.write(String.format("<?xml version=\"1.0\" encoding=\"%s\"?>", encoding));
      }

      if (pubId != null) {
         writer.write(String.format("<!DOCTYPE %s PUBLIC \"%s\" \"%s\">", rootnode, pubId, system));
      }

   }
}
