package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.DependencyInjectionFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.AttributeMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DefaultMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

class UseAttributeForEnumMapper extends AttributeMapper {
   public UseAttributeForEnumMapper(Mapper wrapped) {
      super(wrapped, (ConverterLookup)null, (ReflectionProvider)null);
   }

   /** @deprecated */
   public static boolean isEnum(Class type) {
      while(type != null && type != Object.class) {
         if (type.getName().equals("java.lang.Enum")) {
            return true;
         }

         type = type.getSuperclass();
      }

      return false;
   }

   public boolean shouldLookForSingleValueConverter(String fieldName, Class type, Class definedIn) {
      return isEnum(type);
   }

   public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
      return null;
   }

   public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
      return null;
   }

   static Mapper createEnumMapper(Mapper mapper) {
      try {
         Class enumMapperClass = Class.forName("com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.EnumMapper", true, Mapper.class.getClassLoader());
         return (Mapper)DependencyInjectionFactory.newInstance(enumMapperClass, new Object[]{new UseAttributeForEnumMapper(mapper.lookupMapperOfType(DefaultMapper.class))});
      } catch (Exception var2) {
         return null;
      }
   }
}
