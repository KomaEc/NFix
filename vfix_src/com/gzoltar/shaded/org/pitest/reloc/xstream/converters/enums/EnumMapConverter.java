package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.collections.MapConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.util.EnumMap;

public class EnumMapConverter extends MapConverter {
   private static final Field typeField = Fields.locate(EnumMap.class, Class.class, false);

   public EnumMapConverter(Mapper mapper) {
      super(mapper);
   }

   public boolean canConvert(Class type) {
      return typeField != null && type == EnumMap.class;
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Class type = (Class)Fields.read(typeField, source);
      String attributeName = this.mapper().aliasForSystemAttribute("enum-type");
      if (attributeName != null) {
         writer.addAttribute(attributeName, this.mapper().serializedClass(type));
      }

      super.marshal(source, writer, context);
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String attributeName = this.mapper().aliasForSystemAttribute("enum-type");
      if (attributeName == null) {
         throw new ConversionException("No EnumType specified for EnumMap");
      } else {
         Class type = this.mapper().realClass(reader.getAttribute(attributeName));
         EnumMap map = new EnumMap(type);
         this.populateMap(reader, context, map);
         return map;
      }
   }
}
