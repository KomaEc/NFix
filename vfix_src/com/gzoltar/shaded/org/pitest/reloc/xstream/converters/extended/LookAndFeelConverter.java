package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import javax.swing.LookAndFeel;

public class LookAndFeelConverter extends ReflectionConverter {
   public LookAndFeelConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
      super(mapper, reflectionProvider);
   }

   public boolean canConvert(Class type) {
      return LookAndFeel.class.isAssignableFrom(type) && this.canAccess(type);
   }
}
