package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.ByteConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Base64Encoder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EncodedByteArrayConverter implements Converter, SingleValueConverter {
   private static final Base64Encoder base64 = new Base64Encoder();
   private static final ByteConverter byteConverter = new ByteConverter();

   public boolean canConvert(Class type) {
      return type.isArray() && type.getComponentType().equals(Byte.TYPE);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      writer.setValue(this.toString(source));
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String data = reader.getValue();
      return !reader.hasMoreChildren() ? this.fromString(data) : this.unmarshalIndividualByteElements(reader, context);
   }

   private Object unmarshalIndividualByteElements(HierarchicalStreamReader reader, UnmarshallingContext context) {
      List bytes = new ArrayList();

      for(boolean firstIteration = true; firstIteration || reader.hasMoreChildren(); firstIteration = false) {
         reader.moveDown();
         bytes.add(byteConverter.fromString(reader.getValue()));
         reader.moveUp();
      }

      byte[] result = new byte[bytes.size()];
      int i = 0;

      for(Iterator iterator = bytes.iterator(); iterator.hasNext(); ++i) {
         Byte b = (Byte)iterator.next();
         result[i] = b;
      }

      return result;
   }

   public String toString(Object obj) {
      return base64.encode((byte[])((byte[])obj));
   }

   public Object fromString(String str) {
      return base64.decode(str);
   }
}
