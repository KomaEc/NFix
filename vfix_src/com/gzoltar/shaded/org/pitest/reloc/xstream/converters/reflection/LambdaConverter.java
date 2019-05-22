package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.MarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.ClassLoaderReference;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Types;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.io.Serializable;

public class LambdaConverter extends SerializableConverter {
   public LambdaConverter(Mapper mapper, ReflectionProvider reflectionProvider, ClassLoaderReference classLoaderReference) {
      super(mapper, reflectionProvider, classLoaderReference);
   }

   public boolean canConvert(Class type) {
      return Types.isLambdaType(type) && (JVM.canCreateDerivedObjectOutputStream() || !Serializable.class.isAssignableFrom(type));
   }

   public void marshal(Object original, HierarchicalStreamWriter writer, MarshallingContext context) {
      if (original instanceof Serializable) {
         super.marshal(original, writer, context);
      }

   }
}
