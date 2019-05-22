package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Iterator;

public class EnumSetConverter implements Converter {
   private static final Field typeField = Fields.locate(EnumSet.class, Class.class, false);
   private final Mapper mapper;

   public EnumSetConverter(Mapper mapper) {
      this.mapper = mapper;
   }

   public boolean canConvert(Class type) {
      return typeField != null && EnumSet.class.isAssignableFrom(type);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      EnumSet set = (EnumSet)source;
      Class enumTypeForSet = (Class)Fields.read(typeField, set);
      String attributeName = this.mapper.aliasForSystemAttribute("enum-type");
      if (attributeName != null) {
         writer.addAttribute(attributeName, this.mapper.serializedClass(enumTypeForSet));
      }

      writer.setValue(this.joinEnumValues(set));
   }

   private String joinEnumValues(EnumSet set) {
      boolean seenFirst = false;
      StringBuffer result = new StringBuffer();

      Enum value;
      for(Iterator iterator = set.iterator(); iterator.hasNext(); result.append(value.name())) {
         value = (Enum)iterator.next();
         if (seenFirst) {
            result.append(',');
         } else {
            seenFirst = true;
         }
      }

      return result.toString();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String attributeName = this.mapper.aliasForSystemAttribute("enum-type");
      if (attributeName == null) {
         throw new ConversionException("No EnumType specified for EnumSet");
      } else {
         Class enumTypeForSet = this.mapper.realClass(reader.getAttribute(attributeName));
         EnumSet set = EnumSet.noneOf(enumTypeForSet);
         String[] enumValues = reader.getValue().split(",");

         for(int i = 0; i < enumValues.length; ++i) {
            String enumValue = enumValues[i];
            if (enumValue.length() > 0) {
               set.add(Enum.valueOf(enumTypeForSet, enumValue));
            }
         }

         return set;
      }
   }
}
