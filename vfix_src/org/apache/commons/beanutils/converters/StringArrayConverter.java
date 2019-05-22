package org.apache.commons.beanutils.converters;

import java.util.List;
import org.apache.commons.beanutils.ConversionException;

public final class StringArrayConverter extends AbstractArrayConverter {
   private static String[] model = new String[0];
   private static int[] ints = new int[0];

   public StringArrayConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public StringArrayConverter(Object defaultValue) {
      this.defaultValue = defaultValue;
      this.useDefault = true;
   }

   public Object convert(Class type, Object value) {
      if (value == null) {
         if (this.useDefault) {
            return this.defaultValue;
         } else {
            throw new ConversionException("No value specified");
         }
      } else if (model.getClass() == value.getClass()) {
         return value;
      } else {
         String[] results;
         int i;
         if (ints.getClass() == value.getClass()) {
            int[] values = (int[])value;
            results = new String[values.length];

            for(i = 0; i < values.length; ++i) {
               results[i] = Integer.toString(values[i]);
            }

            return results;
         } else {
            try {
               List list = this.parseElements(value.toString());
               results = new String[list.size()];

               for(i = 0; i < results.length; ++i) {
                  results[i] = (String)list.get(i);
               }

               return results;
            } catch (Exception var6) {
               if (this.useDefault) {
                  return this.defaultValue;
               } else {
                  throw new ConversionException(value.toString(), var6);
               }
            }
         }
      }
   }
}
