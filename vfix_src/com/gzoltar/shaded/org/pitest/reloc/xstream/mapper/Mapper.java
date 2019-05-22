package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;

public interface Mapper {
   String serializedClass(Class var1);

   Class realClass(String var1);

   String serializedMember(Class var1, String var2);

   String realMember(Class var1, String var2);

   boolean isImmutableValueType(Class var1);

   Class defaultImplementationOf(Class var1);

   String aliasForAttribute(String var1);

   String attributeForAlias(String var1);

   String aliasForSystemAttribute(String var1);

   String getFieldNameForItemTypeAndName(Class var1, Class var2, String var3);

   Class getItemTypeForItemFieldName(Class var1, String var2);

   Mapper.ImplicitCollectionMapping getImplicitCollectionDefForFieldName(Class var1, String var2);

   boolean shouldSerializeMember(Class var1, String var2);

   /** @deprecated */
   SingleValueConverter getConverterFromItemType(String var1, Class var2);

   /** @deprecated */
   SingleValueConverter getConverterFromItemType(Class var1);

   /** @deprecated */
   SingleValueConverter getConverterFromAttribute(String var1);

   Converter getLocalConverter(Class var1, String var2);

   Mapper lookupMapperOfType(Class var1);

   SingleValueConverter getConverterFromItemType(String var1, Class var2, Class var3);

   /** @deprecated */
   String aliasForAttribute(Class var1, String var2);

   /** @deprecated */
   String attributeForAlias(Class var1, String var2);

   /** @deprecated */
   SingleValueConverter getConverterFromAttribute(Class var1, String var2);

   SingleValueConverter getConverterFromAttribute(Class var1, String var2, Class var3);

   public interface ImplicitCollectionMapping {
      String getFieldName();

      String getItemFieldName();

      Class getItemType();

      String getKeyFieldName();
   }

   public static class Null {
   }
}
