package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.SingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class JavaMethodConverter implements Converter {
   private final SingleValueConverter javaClassConverter;

   public JavaMethodConverter(ClassLoaderReference classLoaderReference) {
      this((SingleValueConverter)(new JavaClassConverter(classLoaderReference)));
   }

   /** @deprecated */
   public JavaMethodConverter(ClassLoader classLoader) {
      this(new ClassLoaderReference(classLoader));
   }

   protected JavaMethodConverter(SingleValueConverter javaClassConverter) {
      this.javaClassConverter = javaClassConverter;
   }

   public boolean canConvert(Class type) {
      return type.equals(Method.class) || type.equals(Constructor.class);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      String declaringClassName;
      if (source instanceof Method) {
         Method method = (Method)source;
         declaringClassName = this.javaClassConverter.toString(method.getDeclaringClass());
         this.marshalMethod(writer, declaringClassName, method.getName(), method.getParameterTypes());
      } else {
         Constructor method = (Constructor)source;
         declaringClassName = this.javaClassConverter.toString(method.getDeclaringClass());
         this.marshalMethod(writer, declaringClassName, (String)null, method.getParameterTypes());
      }

   }

   private void marshalMethod(HierarchicalStreamWriter writer, String declaringClassName, String methodName, Class[] parameterTypes) {
      writer.startNode("class");
      writer.setValue(declaringClassName);
      writer.endNode();
      if (methodName != null) {
         writer.startNode("name");
         writer.setValue(methodName);
         writer.endNode();
      }

      writer.startNode("parameter-types");

      for(int i = 0; i < parameterTypes.length; ++i) {
         writer.startNode("class");
         writer.setValue(this.javaClassConverter.toString(parameterTypes[i]));
         writer.endNode();
      }

      writer.endNode();
   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      try {
         boolean isMethodNotConstructor = context.getRequiredType().equals(Method.class);
         reader.moveDown();
         String declaringClassName = reader.getValue();
         Class declaringClass = (Class)this.javaClassConverter.fromString(declaringClassName);
         reader.moveUp();
         String methodName = null;
         if (isMethodNotConstructor) {
            reader.moveDown();
            methodName = reader.getValue();
            reader.moveUp();
         }

         reader.moveDown();
         ArrayList parameterTypeList = new ArrayList();

         while(reader.hasMoreChildren()) {
            reader.moveDown();
            String parameterTypeName = reader.getValue();
            parameterTypeList.add(this.javaClassConverter.fromString(parameterTypeName));
            reader.moveUp();
         }

         Class[] parameterTypes = (Class[])((Class[])parameterTypeList.toArray(new Class[parameterTypeList.size()]));
         reader.moveUp();
         return isMethodNotConstructor ? declaringClass.getDeclaredMethod(methodName, parameterTypes) : declaringClass.getDeclaredConstructor(parameterTypes);
      } catch (NoSuchMethodException var9) {
         throw new ConversionException(var9);
      }
   }
}
