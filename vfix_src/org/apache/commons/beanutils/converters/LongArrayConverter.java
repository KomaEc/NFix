package org.apache.commons.beanutils.converters;

import java.util.List;
import org.apache.commons.beanutils.ConversionException;

public final class LongArrayConverter extends AbstractArrayConverter {
   private static long[] model = new long[0];

   public LongArrayConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public LongArrayConverter(Object defaultValue) {
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
         long[] results;
         int i;
         if (AbstractArrayConverter.strings.getClass() == value.getClass()) {
            try {
               String[] values = (String[])value;
               results = new long[values.length];

               for(i = 0; i < values.length; ++i) {
                  results[i] = Long.parseLong(values[i]);
               }

               return results;
            } catch (Exception var6) {
               if (this.useDefault) {
                  return this.defaultValue;
               } else {
                  throw new ConversionException(value.toString(), var6);
               }
            }
         } else {
            try {
               List list = this.parseElements(value.toString());
               results = new long[list.size()];

               for(i = 0; i < results.length; ++i) {
                  results[i] = Long.parseLong((String)list.get(i));
               }

               return results;
            } catch (Exception var7) {
               if (this.useDefault) {
                  return this.defaultValue;
               } else {
                  throw new ConversionException(value.toString(), var7);
               }
            }
         }
      }
   }
}
