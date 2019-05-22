package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

public class DurationConverter extends AbstractSingleValueConverter {
   private final DatatypeFactory factory;

   public DurationConverter() {
      this(((<undefinedtype>)(new Object() {
         DatatypeFactory getFactory() {
            try {
               return DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException var2) {
               return null;
            }
         }
      })).getFactory());
   }

   public DurationConverter(DatatypeFactory factory) {
      this.factory = factory;
   }

   public boolean canConvert(Class c) {
      return this.factory != null && Duration.class.isAssignableFrom(c);
   }

   public Object fromString(String s) {
      return this.factory.newDuration(s);
   }
}
