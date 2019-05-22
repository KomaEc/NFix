package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.CannotResolveClassException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.DefaultMapper;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;

public class JavaClassConverter extends AbstractSingleValueConverter {
   private Mapper mapper;

   public JavaClassConverter(ClassLoaderReference classLoaderReference) {
      this((Mapper)(new DefaultMapper(classLoaderReference)));
   }

   /** @deprecated */
   public JavaClassConverter(ClassLoader classLoader) {
      this(new ClassLoaderReference(classLoader));
   }

   protected JavaClassConverter(Mapper mapper) {
      this.mapper = mapper;
   }

   public boolean canConvert(Class clazz) {
      return Class.class.equals(clazz);
   }

   public String toString(Object obj) {
      return this.mapper.serializedClass((Class)obj);
   }

   public Object fromString(String str) {
      try {
         return this.mapper.realClass(str);
      } catch (CannotResolveClassException var3) {
         throw new ConversionException("Cannot load java class " + str, var3.getCause());
      }
   }
}
