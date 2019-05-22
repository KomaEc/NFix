package com.gzoltar.shaded.org.pitest.reloc.xstream.io.json;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.AbstractDriver;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;

public class JsonHierarchicalStreamDriver extends AbstractDriver {
   public JsonHierarchicalStreamDriver() {
   }

   public JsonHierarchicalStreamDriver(NameCoder nameCoder) {
      super(nameCoder);
   }

   public HierarchicalStreamReader createReader(Reader in) {
      throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
   }

   public HierarchicalStreamReader createReader(InputStream in) {
      throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
   }

   public HierarchicalStreamReader createReader(URL in) {
      throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
   }

   public HierarchicalStreamReader createReader(File in) {
      throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
   }

   public HierarchicalStreamWriter createWriter(Writer out) {
      return new JsonWriter(out);
   }

   public HierarchicalStreamWriter createWriter(OutputStream out) {
      try {
         return this.createWriter((Writer)(new OutputStreamWriter(out, "UTF-8")));
      } catch (UnsupportedEncodingException var3) {
         throw new StreamException(var3);
      }
   }
}
