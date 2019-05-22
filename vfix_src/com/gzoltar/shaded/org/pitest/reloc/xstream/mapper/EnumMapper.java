package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.enums.EnumSingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class EnumMapper extends MapperWrapper implements Caching {
   private transient AttributeMapper attributeMapper;
   private transient Map<Class, SingleValueConverter> enumConverterMap;

   /** @deprecated */
   @Deprecated
   public EnumMapper(Mapper wrapped, ConverterLookup lookup) {
      super(wrapped);
      this.readResolve();
   }

   public EnumMapper(Mapper wrapped) {
      super(wrapped);
      this.readResolve();
   }

   public String serializedClass(Class type) {
      if (type == null) {
         return super.serializedClass(type);
      } else if (Enum.class.isAssignableFrom(type) && type.getSuperclass() != Enum.class) {
         return super.serializedClass(type.getSuperclass());
      } else {
         return EnumSet.class.isAssignableFrom(type) ? super.serializedClass(EnumSet.class) : super.serializedClass(type);
      }
   }

   public boolean isImmutableValueType(Class type) {
      return Enum.class.isAssignableFrom(type) || super.isImmutableValueType(type);
   }

   public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
      SingleValueConverter converter = this.getLocalConverter(fieldName, type, definedIn);
      return converter == null ? super.getConverterFromItemType(fieldName, type, definedIn) : converter;
   }

   public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
      SingleValueConverter converter = this.getLocalConverter(attribute, type, definedIn);
      return converter == null ? super.getConverterFromAttribute(definedIn, attribute, type) : converter;
   }

   private SingleValueConverter getLocalConverter(String fieldName, Class type, Class definedIn) {
      if (this.attributeMapper != null && Enum.class.isAssignableFrom(type) && this.attributeMapper.shouldLookForSingleValueConverter(fieldName, type, definedIn)) {
         synchronized(this.enumConverterMap) {
            SingleValueConverter singleValueConverter = (SingleValueConverter)this.enumConverterMap.get(type);
            if (singleValueConverter == null) {
               singleValueConverter = super.getConverterFromItemType(fieldName, type, definedIn);
               if (singleValueConverter == null) {
                  singleValueConverter = new EnumSingleValueConverter(type);
               }

               this.enumConverterMap.put(type, singleValueConverter);
            }

            return (SingleValueConverter)singleValueConverter;
         }
      } else {
         return null;
      }
   }

   public void flushCache() {
      if (this.enumConverterMap.size() > 0) {
         synchronized(this.enumConverterMap) {
            this.enumConverterMap.clear();
         }
      }

   }

   private Object readResolve() {
      this.enumConverterMap = new HashMap();
      this.attributeMapper = (AttributeMapper)this.lookupMapperOfType(AttributeMapper.class);
      return this;
   }
}
