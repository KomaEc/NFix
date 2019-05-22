package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.util.BitSet;
import java.util.StringTokenizer;

public class BitSetConverter implements Converter {
   public boolean canConvert(Class type) {
      return type.equals(BitSet.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      BitSet bitSet = (BitSet)source;
      StringBuffer buffer = new StringBuffer();
      boolean seenFirst = false;

      for(int i = 0; i < bitSet.length(); ++i) {
         if (bitSet.get(i)) {
            if (seenFirst) {
               buffer.append(',');
            } else {
               seenFirst = true;
            }

            buffer.append(i);
         }
      }

      writer.setValue(buffer.toString());
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      BitSet result = new BitSet();
      StringTokenizer tokenizer = new StringTokenizer(reader.getValue(), ",", false);

      while(tokenizer.hasMoreTokens()) {
         int index = Integer.parseInt(tokenizer.nextToken());
         result.set(index);
      }

      return result;
   }
}
