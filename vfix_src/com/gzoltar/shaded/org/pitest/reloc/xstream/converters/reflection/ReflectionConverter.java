package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class ReflectionConverter extends AbstractReflectionConverter {
   private static final Class eventHandlerType = JVM.loadClassForName("java.beans.EventHandler");
   private Class type;

   public ReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
      super(mapper, reflectionProvider);
   }

   public ReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider, Class type) {
      this(mapper, reflectionProvider);
      this.type = type;
   }

   public boolean canConvert(Class type) {
      return (this.type != null && this.type == type || this.type == null && type != null && type != eventHandlerType) && this.canAccess(type);
   }
}
