package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.javabean;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.MissingFieldException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastField;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.HashSet;
import java.util.Set;

public class JavaBeanConverter implements Converter {
   protected final Mapper mapper;
   protected final JavaBeanProvider beanProvider;
   private final Class type;
   /** @deprecated */
   private String classAttributeIdentifier;

   public JavaBeanConverter(Mapper mapper) {
      this(mapper, (Class)null);
   }

   public JavaBeanConverter(Mapper mapper, Class type) {
      this(mapper, new BeanProvider(), type);
   }

   public JavaBeanConverter(Mapper mapper, JavaBeanProvider beanProvider) {
      this(mapper, beanProvider, (Class)null);
   }

   public JavaBeanConverter(Mapper mapper, JavaBeanProvider beanProvider, Class type) {
      this.mapper = mapper;
      this.beanProvider = beanProvider;
      this.type = type;
   }

   /** @deprecated */
   public JavaBeanConverter(Mapper mapper, String classAttributeIdentifier) {
      this(mapper, (JavaBeanProvider)(new BeanProvider()));
      this.classAttributeIdentifier = classAttributeIdentifier;
   }

   public boolean canConvert(Class type) {
      return (this.type == null || this.type == type) && this.beanProvider.canInstantiate(type);
   }

   public void marshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
      final String classAttributeName = this.classAttributeIdentifier != null ? this.classAttributeIdentifier : this.mapper.aliasForSystemAttribute("class");
      this.beanProvider.visitSerializableProperties(source, new JavaBeanProvider.Visitor() {
         public boolean shouldVisit(String name, Class definedIn) {
            return JavaBeanConverter.this.mapper.shouldSerializeMember(definedIn, name);
         }

         public void visit(String propertyName, Class fieldType, Class definedIn, Object newObj) {
            if (newObj != null) {
               this.writeField(propertyName, fieldType, newObj, definedIn);
            }

         }

         private void writeField(String propertyName, Class fieldType, Object newObj, Class definedIn) {
            Class actualType = newObj.getClass();
            Class defaultType = JavaBeanConverter.this.mapper.defaultImplementationOf(fieldType);
            String serializedMember = JavaBeanConverter.this.mapper.serializedMember(source.getClass(), propertyName);
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, serializedMember, actualType);
            if (!actualType.equals(defaultType) && classAttributeName != null) {
               writer.addAttribute(classAttributeName, JavaBeanConverter.this.mapper.serializedClass(actualType));
            }

            context.convertAnother(newObj);
            writer.endNode();
         }
      });
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      Object result = this.instantiateNewInstance(context);
      Set seenProperties = new HashSet() {
         public boolean add(Object e) {
            if (!super.add(e)) {
               throw new JavaBeanConverter.DuplicatePropertyException(((FastField)e).getName());
            } else {
               return true;
            }
         }
      };

      for(Class resultType = result.getClass(); reader.hasMoreChildren(); reader.moveUp()) {
         reader.moveDown();
         String propertyName = this.mapper.realMember(resultType, reader.getNodeName());
         if (this.mapper.shouldSerializeMember(resultType, propertyName)) {
            boolean propertyExistsInClass = this.beanProvider.propertyDefinedInClass(propertyName, resultType);
            if (!propertyExistsInClass) {
               throw new MissingFieldException(resultType.getName(), propertyName);
            }

            Class type = this.determineType(reader, result, propertyName);
            Object value = context.convertAnother(result, type);
            this.beanProvider.writeProperty(result, propertyName, value);
            seenProperties.add(new FastField(resultType, propertyName));
         }
      }

      return result;
   }

   private Object instantiateNewInstance(UnmarshallingContext context) {
      Object result = context.currentObject();
      if (result == null) {
         result = this.beanProvider.newInstance(context.getRequiredType());
      }

      return result;
   }

   private Class determineType(HierarchicalStreamReader reader, Object result, String fieldName) {
      String classAttributeName = this.classAttributeIdentifier != null ? this.classAttributeIdentifier : this.mapper.aliasForSystemAttribute("class");
      String classAttribute = classAttributeName == null ? null : reader.getAttribute(classAttributeName);
      return classAttribute != null ? this.mapper.realClass(classAttribute) : this.mapper.defaultImplementationOf(this.beanProvider.getPropertyType(result, fieldName));
   }

   public static class DuplicatePropertyException extends ConversionException {
      public DuplicatePropertyException(String msg) {
         super("Duplicate property " + msg);
         this.add("property", msg);
      }
   }

   /** @deprecated */
   public static class DuplicateFieldException extends ConversionException {
      public DuplicateFieldException(String msg) {
         super(msg);
      }
   }
}
