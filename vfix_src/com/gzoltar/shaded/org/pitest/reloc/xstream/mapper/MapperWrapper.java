package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;

public abstract class MapperWrapper implements Mapper {
   private final Mapper wrapped;

   public MapperWrapper(Mapper wrapped) {
      this.wrapped = wrapped;
   }

   public String serializedClass(Class type) {
      return this.wrapped.serializedClass(type);
   }

   public Class realClass(String elementName) {
      return this.wrapped.realClass(elementName);
   }

   public String serializedMember(Class type, String memberName) {
      return this.wrapped.serializedMember(type, memberName);
   }

   public String realMember(Class type, String serialized) {
      return this.wrapped.realMember(type, serialized);
   }

   public boolean isImmutableValueType(Class type) {
      return this.wrapped.isImmutableValueType(type);
   }

   public Class defaultImplementationOf(Class type) {
      return this.wrapped.defaultImplementationOf(type);
   }

   public String aliasForAttribute(String attribute) {
      return this.wrapped.aliasForAttribute(attribute);
   }

   public String attributeForAlias(String alias) {
      return this.wrapped.attributeForAlias(alias);
   }

   public String aliasForSystemAttribute(String attribute) {
      return this.wrapped.aliasForSystemAttribute(attribute);
   }

   public String getFieldNameForItemTypeAndName(Class definedIn, Class itemType, String itemFieldName) {
      return this.wrapped.getFieldNameForItemTypeAndName(definedIn, itemType, itemFieldName);
   }

   public Class getItemTypeForItemFieldName(Class definedIn, String itemFieldName) {
      return this.wrapped.getItemTypeForItemFieldName(definedIn, itemFieldName);
   }

   public Mapper.ImplicitCollectionMapping getImplicitCollectionDefForFieldName(Class itemType, String fieldName) {
      return this.wrapped.getImplicitCollectionDefForFieldName(itemType, fieldName);
   }

   public boolean shouldSerializeMember(Class definedIn, String fieldName) {
      return this.wrapped.shouldSerializeMember(definedIn, fieldName);
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromItemType(String fieldName, Class type) {
      return this.wrapped.getConverterFromItemType(fieldName, type);
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromItemType(Class type) {
      return this.wrapped.getConverterFromItemType(type);
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromAttribute(String name) {
      return this.wrapped.getConverterFromAttribute(name);
   }

   public Converter getLocalConverter(Class definedIn, String fieldName) {
      return this.wrapped.getLocalConverter(definedIn, fieldName);
   }

   public Mapper lookupMapperOfType(Class type) {
      return (Mapper)(type.isAssignableFrom(this.getClass()) ? this : this.wrapped.lookupMapperOfType(type));
   }

   public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
      return this.wrapped.getConverterFromItemType(fieldName, type, definedIn);
   }

   /** @deprecated */
   public String aliasForAttribute(Class definedIn, String fieldName) {
      return this.wrapped.aliasForAttribute(definedIn, fieldName);
   }

   /** @deprecated */
   public String attributeForAlias(Class definedIn, String alias) {
      return this.wrapped.attributeForAlias(definedIn, alias);
   }

   /** @deprecated */
   public SingleValueConverter getConverterFromAttribute(Class type, String attribute) {
      return this.wrapped.getConverterFromAttribute(type, attribute);
   }

   public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
      return this.wrapped.getConverterFromAttribute(definedIn, attribute, type);
   }
}
