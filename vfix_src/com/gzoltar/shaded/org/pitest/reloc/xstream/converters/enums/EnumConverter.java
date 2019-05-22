package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;

public class EnumConverter implements Converter {
   public boolean canConvert(Class type) {
      return type.isEnum() || Enum.class.isAssignableFrom(type);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      writer.setValue(((Enum)source).name());
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Class type = context.getRequiredType();
      if (type.getSuperclass() != Enum.class) {
         type = type.getSuperclass();
      }

      String name = reader.getValue();

      try {
         return Enum.valueOf(type, name);
      } catch (IllegalArgumentException var10) {
         Enum[] var6 = (Enum[])((Enum[])type.getEnumConstants());
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Enum c = var6[var8];
            if (c.name().equalsIgnoreCase(name)) {
               return c;
            }
         }

         throw var10;
      }
   }
}
