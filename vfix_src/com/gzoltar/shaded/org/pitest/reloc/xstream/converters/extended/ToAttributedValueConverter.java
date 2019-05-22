package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterMatcher;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.AbstractReflectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastField;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Primitives;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ToAttributedValueConverter implements Converter {
   private static final String STRUCTURE_MARKER = "";
   private final Class type;
   private final Mapper mapper;
   private final Mapper enumMapper;
   private final ReflectionProvider reflectionProvider;
   private final ConverterLookup lookup;
   private final Field valueField;

   public ToAttributedValueConverter(Class type, Mapper mapper, ReflectionProvider reflectionProvider, ConverterLookup lookup, String valueFieldName) {
      this(type, mapper, reflectionProvider, lookup, valueFieldName, (Class)null);
   }

   public ToAttributedValueConverter(Class type, Mapper mapper, ReflectionProvider reflectionProvider, ConverterLookup lookup, String valueFieldName, Class valueDefinedIn) {
      this.type = type;
      this.mapper = mapper;
      this.reflectionProvider = reflectionProvider;
      this.lookup = lookup;
      if (valueFieldName == null) {
         this.valueField = null;
      } else {
         Field field = null;

         try {
            field = (valueDefinedIn != null ? valueDefinedIn : type).getDeclaredField(valueFieldName);
            if (!field.isAccessible()) {
               field.setAccessible(true);
            }
         } catch (NoSuchFieldException var9) {
            throw new IllegalArgumentException(var9.getMessage() + ": " + valueFieldName);
         }

         this.valueField = field;
      }

      this.enumMapper = JVM.is15() ? UseAttributeForEnumMapper.createEnumMapper(mapper) : null;
   }

   public boolean canConvert(Class type) {
      return this.type == type;
   }

   public void marshal(Object source, final HierarchicalStreamWriter writer, MarshallingContext context) {
      final Class sourceType = source.getClass();
      final Map defaultFieldDefinition = new HashMap();
      final String[] tagValue = new String[1];
      final Object[] realValue = new Object[1];
      final Class[] fieldType = new Class[1];
      final Class[] definingType = new Class[1];
      this.reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {
         public void visit(String fieldName, Class type, Class definedIn, Object value) {
            if (ToAttributedValueConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
               FastField field = new FastField(definedIn, fieldName);
               String alias = ToAttributedValueConverter.this.mapper.serializedMember(definedIn, fieldName);
               if (!defaultFieldDefinition.containsKey(alias)) {
                  Class lookupType = sourceType;
                  defaultFieldDefinition.put(alias, ToAttributedValueConverter.this.reflectionProvider.getField(lookupType, fieldName));
               } else if (!ToAttributedValueConverter.this.fieldIsEqual(field)) {
                  ConversionException exception = new ConversionException("Cannot write attribute twice for object");
                  exception.add("alias", alias);
                  exception.add("type", sourceType.getName());
                  throw exception;
               }

               ConverterMatcher converter = UseAttributeForEnumMapper.isEnum(type) ? ToAttributedValueConverter.this.enumMapper.getConverterFromItemType((String)null, type, (Class)null) : ToAttributedValueConverter.this.mapper.getLocalConverter(definedIn, fieldName);
               if (converter == null) {
                  converter = ToAttributedValueConverter.this.lookup.lookupConverterForType(type);
               }

               if (value != null) {
                  boolean isValueField = ToAttributedValueConverter.this.valueField != null && ToAttributedValueConverter.this.fieldIsEqual(field);
                  if (isValueField) {
                     definingType[0] = definedIn;
                     fieldType[0] = type;
                     realValue[0] = value;
                     tagValue[0] = "";
                  }

                  if (converter instanceof SingleValueConverter) {
                     String str = ((SingleValueConverter)converter).toString(value);
                     if (isValueField) {
                        tagValue[0] = str;
                     } else if (str != null) {
                        writer.addAttribute(alias, str);
                     }
                  } else if (!isValueField) {
                     ConversionException exceptionx = new ConversionException("Cannot write element as attribute");
                     exceptionx.add("alias", alias);
                     exceptionx.add("type", sourceType.getName());
                     throw exceptionx;
                  }
               }

            }
         }
      });
      if (tagValue[0] != null) {
         Class actualType = realValue[0].getClass();
         Class defaultType = this.mapper.defaultImplementationOf(fieldType[0]);
         if (!actualType.equals(defaultType)) {
            String serializedClassName = this.mapper.serializedClass(actualType);
            if (!serializedClassName.equals(this.mapper.serializedClass(defaultType))) {
               String attributeName = this.mapper.aliasForSystemAttribute("class");
               if (attributeName != null) {
                  writer.addAttribute(attributeName, serializedClassName);
               }
            }
         }

         if (tagValue[0] == "") {
            context.convertAnother(realValue[0]);
         } else {
            writer.setValue(tagValue[0]);
         }
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Object result = this.reflectionProvider.newInstance(context.getRequiredType());
      Class resultType = result.getClass();
      Set seenFields = new HashSet();
      Iterator it = reader.getAttributeNames();
      Set systemAttributes = new HashSet();
      systemAttributes.add(this.mapper.aliasForSystemAttribute("class"));

      String fieldName;
      Field field;
      Class type;
      Object value;
      ConversionException exception;
      while(it.hasNext()) {
         String attrName = (String)it.next();
         if (!systemAttributes.contains(attrName)) {
            fieldName = this.mapper.realMember(resultType, attrName);
            field = this.reflectionProvider.getFieldOrNull(resultType, fieldName);
            if (field != null && !Modifier.isTransient(field.getModifiers())) {
               type = field.getType();
               Class declaringClass = field.getDeclaringClass();
               value = UseAttributeForEnumMapper.isEnum(type) ? this.enumMapper.getConverterFromItemType((String)null, type, (Class)null) : this.mapper.getLocalConverter(declaringClass, fieldName);
               if (value == null) {
                  value = this.lookup.lookupConverterForType(type);
               }

               if (!(value instanceof SingleValueConverter)) {
                  ConversionException exception = new ConversionException("Cannot read field as a single value for object");
                  exception.add("field", fieldName);
                  exception.add("type", resultType.getName());
                  throw exception;
               }

               if (value != null) {
                  Object value = ((SingleValueConverter)value).fromString(reader.getAttribute(attrName));
                  if (type.isPrimitive()) {
                     type = Primitives.box(type);
                  }

                  if (value != null && !type.isAssignableFrom(value.getClass())) {
                     exception = new ConversionException("Cannot assign object to type");
                     exception.add("object type", value.getClass().getName());
                     exception.add("target type", type.getName());
                     throw exception;
                  }

                  this.reflectionProvider.writeField(result, fieldName, value, declaringClass);
                  if (!seenFields.add(new FastField(declaringClass, fieldName))) {
                     throw new AbstractReflectionConverter.DuplicateFieldException(fieldName + " [" + declaringClass.getName() + "]");
                  }
               }
            }
         }
      }

      if (this.valueField != null) {
         Class classDefiningField = this.valueField.getDeclaringClass();
         fieldName = this.valueField.getName();
         field = fieldName == null ? null : this.reflectionProvider.getField(classDefiningField, fieldName);
         if (fieldName == null || field == null) {
            ConversionException exception = new ConversionException("Cannot assign value to field of type");
            exception.add("element", reader.getNodeName());
            exception.add("field", fieldName);
            exception.add("target type", context.getRequiredType().getName());
            throw exception;
         }

         String classAttribute = HierarchicalStreams.readClassAttribute(reader, this.mapper);
         if (classAttribute != null) {
            type = this.mapper.realClass(classAttribute);
         } else {
            type = this.mapper.defaultImplementationOf(this.reflectionProvider.getFieldType(result, fieldName, classDefiningField));
         }

         value = context.convertAnother(result, type, this.mapper.getLocalConverter(field.getDeclaringClass(), field.getName()));
         Class definedType = this.reflectionProvider.getFieldType(result, fieldName, classDefiningField);
         if (!definedType.isPrimitive()) {
            type = definedType;
         }

         if (value != null && !type.isAssignableFrom(value.getClass())) {
            exception = new ConversionException("Cannot assign object to type");
            exception.add("object type", value.getClass().getName());
            exception.add("target type", type.getName());
            throw exception;
         }

         this.reflectionProvider.writeField(result, fieldName, value, classDefiningField);
         if (!seenFields.add(new FastField(classDefiningField, fieldName))) {
            throw new AbstractReflectionConverter.DuplicateFieldException(fieldName + " [" + classDefiningField.getName() + "]");
         }
      }

      return result;
   }

   private boolean fieldIsEqual(FastField field) {
      return this.valueField.getName().equals(field.getName()) && this.valueField.getDeclaringClass().getName().equals(field.getDeclaringClass());
   }
}
