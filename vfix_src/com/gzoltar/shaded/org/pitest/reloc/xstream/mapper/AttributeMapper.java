package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttributeMapper extends MapperWrapper {
   private final Map fieldNameToTypeMap;
   private final Set typeSet;
   private ConverterLookup converterLookup;
   private ReflectionProvider reflectionProvider;
   private final Set fieldToUseAsAttribute;

   /** @deprecated */
   public AttributeMapper(Mapper wrapped) {
      this(wrapped, (ConverterLookup)null, (ReflectionProvider)null);
   }

   public AttributeMapper(Mapper wrapped, ConverterLookup converterLookup, ReflectionProvider refProvider) {
      super(wrapped);
      this.fieldNameToTypeMap = new HashMap();
      this.typeSet = new HashSet();
      this.fieldToUseAsAttribute = new HashSet();
      this.converterLookup = converterLookup;
      this.reflectionProvider = refProvider;
   }

   /** @deprecated */
   public void setConverterLookup(ConverterLookup converterLookup) {
      this.converterLookup = converterLookup;
   }

   public void addAttributeFor(String fieldName, Class type) {
      this.fieldNameToTypeMap.put(fieldName, type);
   }

   public void addAttributeFor(Class type) {
      this.typeSet.add(type);
   }

   private SingleValueConverter getLocalConverterFromItemType(Class type) {
      Converter converter = this.converterLookup.lookupConverterForType(type);
      return converter != null && converter instanceof SingleValueConverter ? (SingleValueConverter)converter : null;
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromItemType(String fieldName, Class type) {
      return this.fieldNameToTypeMap.get(fieldName) == type ? this.getLocalConverterFromItemType(type) : null;
   }

   public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
      if (this.shouldLookForSingleValueConverter(fieldName, type, definedIn)) {
         SingleValueConverter converter = this.getLocalConverterFromItemType(type);
         if (converter != null) {
            return converter;
         }
      }

      return super.getConverterFromItemType(fieldName, type, definedIn);
   }

   public boolean shouldLookForSingleValueConverter(String fieldName, Class type, Class definedIn) {
      if (this.typeSet.contains(type)) {
         return true;
      } else if (this.fieldNameToTypeMap.get(fieldName) == type) {
         return true;
      } else if (fieldName != null && definedIn != null) {
         Field field = this.reflectionProvider.getField(definedIn, fieldName);
         return this.fieldToUseAsAttribute.contains(field);
      } else {
         return false;
      }
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromItemType(Class type) {
      return this.typeSet.contains(type) ? this.getLocalConverterFromItemType(type) : null;
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromAttribute(String attributeName) {
      SingleValueConverter converter = null;
      Class type = (Class)this.fieldNameToTypeMap.get(attributeName);
      if (type != null) {
         converter = this.getLocalConverterFromItemType(type);
      }

      return converter;
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute) {
      Field field = this.reflectionProvider.getField(definedIn, attribute);
      return this.getConverterFromAttribute(definedIn, attribute, field.getType());
   }

   public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
      if (this.shouldLookForSingleValueConverter(attribute, type, definedIn)) {
         SingleValueConverter converter = this.getLocalConverterFromItemType(type);
         if (converter != null) {
            return converter;
         }
      }

      return super.getConverterFromAttribute(definedIn, attribute, type);
   }

   public void addAttributeFor(Field field) {
      this.fieldToUseAsAttribute.add(field);
   }

   public void addAttributeFor(Class definedIn, String fieldName) {
      this.fieldToUseAsAttribute.add(this.reflectionProvider.getField(definedIn, fieldName));
   }
}
