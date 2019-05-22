package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class HierarchicalStreams {
   public static Class readClassType(HierarchicalStreamReader reader, Mapper mapper) {
      String classAttribute = readClassAttribute(reader, mapper);
      Class type;
      if (classAttribute == null) {
         type = mapper.realClass(reader.getNodeName());
      } else {
         type = mapper.realClass(classAttribute);
      }

      return type;
   }

   public static String readClassAttribute(HierarchicalStreamReader reader, Mapper mapper) {
      String attributeName = mapper.aliasForSystemAttribute("resolves-to");
      String classAttribute = attributeName == null ? null : reader.getAttribute(attributeName);
      if (classAttribute == null) {
         attributeName = mapper.aliasForSystemAttribute("class");
         if (attributeName != null) {
            classAttribute = reader.getAttribute(attributeName);
         }
      }

      return classAttribute;
   }
}
