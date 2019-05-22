package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.awt.Color;
import java.util.HashMap;

public class ColorConverter implements Converter {
   public boolean canConvert(Class type) {
      return type.getName().equals("java.awt.Color");
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Color color = (Color)source;
      this.write("red", color.getRed(), writer);
      this.write("green", color.getGreen(), writer);
      this.write("blue", color.getBlue(), writer);
      this.write("alpha", color.getAlpha(), writer);
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      HashMap elements = new HashMap();

      while(reader.hasMoreChildren()) {
         reader.moveDown();
         elements.put(reader.getNodeName(), Integer.valueOf(reader.getValue()));
         reader.moveUp();
      }

      return new Color((Integer)elements.get("red"), (Integer)elements.get("green"), (Integer)elements.get("blue"), (Integer)elements.get("alpha"));
   }

   private void write(String fieldName, int value, HierarchicalStreamWriter writer) {
      ExtendedHierarchicalStreamWriterHelper.startNode(writer, fieldName, Integer.TYPE);
      writer.setValue(String.valueOf(value));
      writer.endNode();
   }
}
