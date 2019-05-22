package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DefaultMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;

public class JavaFieldConverter implements Converter {
   private final SingleValueConverter javaClassConverter;
   private final Mapper mapper;

   public JavaFieldConverter(ClassLoaderReference classLoaderReference) {
      this(new JavaClassConverter(classLoaderReference), new DefaultMapper(classLoaderReference));
   }

   /** @deprecated */
   public JavaFieldConverter(ClassLoader classLoader) {
      this(new ClassLoaderReference(classLoader));
   }

   protected JavaFieldConverter(SingleValueConverter javaClassConverter, Mapper mapper) {
      this.javaClassConverter = javaClassConverter;
      this.mapper = mapper;
   }

   public boolean canConvert(Class type) {
      return type.equals(Field.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      Field field = (Field)source;
      Class type = field.getDeclaringClass();
      writer.startNode("name");
      writer.setValue(this.mapper.serializedMember(type, field.getName()));
      writer.endNode();
      writer.startNode("clazz");
      writer.setValue(this.javaClassConverter.toString(type));
      writer.endNode();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      String methodName = null;

      String declaringClassName;
      for(declaringClassName = null; (methodName == null || declaringClassName == null) && reader.hasMoreChildren(); reader.moveUp()) {
         reader.moveDown();
         if (reader.getNodeName().equals("name")) {
            methodName = reader.getValue();
         } else if (reader.getNodeName().equals("clazz")) {
            declaringClassName = reader.getValue();
         }
      }

      Class declaringClass = (Class)this.javaClassConverter.fromString(declaringClassName);

      try {
         return declaringClass.getDeclaredField(this.mapper.realMember(declaringClass, methodName));
      } catch (NoSuchFieldException var7) {
         throw new ConversionException(var7);
      }
   }
}
