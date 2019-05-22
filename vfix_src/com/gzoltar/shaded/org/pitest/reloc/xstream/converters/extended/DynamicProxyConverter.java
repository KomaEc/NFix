package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DynamicProxyMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DynamicProxyConverter implements Converter {
   private ClassLoaderReference classLoaderReference;
   private Mapper mapper;
   private static final Field HANDLER = Fields.locate(Proxy.class, InvocationHandler.class, false);
   private static final InvocationHandler DUMMY = new InvocationHandler() {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         return null;
      }
   };

   /** @deprecated */
   public DynamicProxyConverter(Mapper mapper) {
      this(mapper, DynamicProxyConverter.class.getClassLoader());
   }

   public DynamicProxyConverter(Mapper mapper, ClassLoaderReference classLoaderReference) {
      this.classLoaderReference = classLoaderReference;
      this.mapper = mapper;
   }

   /** @deprecated */
   public DynamicProxyConverter(Mapper mapper, ClassLoader classLoader) {
      this(mapper, new ClassLoaderReference(classLoader));
   }

   public boolean canConvert(Class type) {
      return type.equals(DynamicProxyMapper.DynamicProxy.class) || Proxy.isProxyClass(type);
   }

   public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
      InvocationHandler invocationHandler = Proxy.getInvocationHandler(source);
      this.addInterfacesToXml(source, writer);
      writer.startNode("handler");
      String attributeName = this.mapper.aliasForSystemAttribute("class");
      if (attributeName != null) {
         writer.addAttribute(attributeName, this.mapper.serializedClass(invocationHandler.getClass()));
      }

      context.convertAnother(invocationHandler);
      writer.endNode();
   }

   private void addInterfacesToXml(Object source, HierarchicalStreamWriter writer) {
      Class[] interfaces = source.getClass().getInterfaces();

      for(int i = 0; i < interfaces.length; ++i) {
         Class currentInterface = interfaces[i];
         writer.startNode("interface");
         writer.setValue(this.mapper.serializedClass(currentInterface));
         writer.endNode();
      }

   }

   public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
      List interfaces = new ArrayList();
      InvocationHandler handler = null;

      Class handlerType;
      for(handlerType = null; reader.hasMoreChildren(); reader.moveUp()) {
         reader.moveDown();
         String elementName = reader.getNodeName();
         if (elementName.equals("interface")) {
            interfaces.add(this.mapper.realClass(reader.getValue()));
         } else if (elementName.equals("handler")) {
            String attributeName = this.mapper.aliasForSystemAttribute("class");
            if (attributeName != null) {
               handlerType = this.mapper.realClass(reader.getAttribute(attributeName));
               break;
            }
         }
      }

      if (handlerType == null) {
         throw new ConversionException("No InvocationHandler specified for dynamic proxy");
      } else {
         Class[] interfacesAsArray = new Class[interfaces.size()];
         interfaces.toArray(interfacesAsArray);
         Object proxy = null;
         if (HANDLER != null) {
            proxy = Proxy.newProxyInstance(this.classLoaderReference.getReference(), interfacesAsArray, DUMMY);
         }

         handler = (InvocationHandler)context.convertAnother(proxy, handlerType);
         reader.moveUp();
         if (HANDLER != null) {
            Fields.write(HANDLER, proxy, handler);
         } else {
            proxy = Proxy.newProxyInstance(this.classLoaderReference.getReference(), interfacesAsArray, handler);
         }

         return proxy;
      }
   }
}
