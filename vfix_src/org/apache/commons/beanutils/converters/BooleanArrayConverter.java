package org.apache.commons.beanutils.converters;

import java.util.List;
import org.apache.commons.beanutils.ConversionException;

public final class BooleanArrayConverter extends AbstractArrayConverter {
   private static boolean[] model = new boolean[0];

   public BooleanArrayConverter() {
      this.defaultValue = null;
      this.useDefault = false;
   }

   public BooleanArrayConverter(Object defaultValue) {
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
         boolean[] results;
         int i;
         String stringValue;
         if (AbstractArrayConverter.strings.getClass() == value.getClass()) {
            try {
               String[] values = (String[])value;
               results = new boolean[values.length];

               for(i = 0; i < values.length; ++i) {
                  stringValue = values[i];
                  if (!stringValue.equalsIgnoreCase("yes") && !stringValue.equalsIgnoreCase("y") && !stringValue.equalsIgnoreCase("true") && !stringValue.equalsIgnoreCase("on") && !stringValue.equalsIgnoreCase("1")) {
                     if (!stringValue.equalsIgnoreCase("no") && !stringValue.equalsIgnoreCase("n") && !stringValue.equalsIgnoreCase("false") && !stringValue.equalsIgnoreCase("off") && !stringValue.equalsIgnoreCase("0")) {
                        if (this.useDefault) {
                           return this.defaultValue;
                        }

                        throw new ConversionException(value.toString());
                     }

                     results[i] = false;
                  } else {
                     results[i] = true;
                  }
               }

               return results;
            } catch (Exception var7) {
               if (this.useDefault) {
                  return this.defaultValue;
               } else {
                  throw new ConversionException(value.toString(), var7);
               }
            }
         } else {
            try {
               List list = this.parseElements(value.toString());
               results = new boolean[list.size()];

               for(i = 0; i < results.length; ++i) {
                  stringValue = (String)list.get(i);
                  if (!stringValue.equalsIgnoreCase("yes") && !stringValue.equalsIgnoreCase("y") && !stringValue.equalsIgnoreCase("true") && !stringValue.equalsIgnoreCase("on") && !stringValue.equalsIgnoreCase("1")) {
                     if (!stringValue.equalsIgnoreCase("no") && !stringValue.equalsIgnoreCase("n") && !stringValue.equalsIgnoreCase("false") && !stringValue.equalsIgnoreCase("off") && !stringValue.equalsIgnoreCase("0")) {
                        if (this.useDefault) {
                           return this.defaultValue;
                        }

                        throw new ConversionException(value.toString());
                     }

                     results[i] = false;
                  } else {
                     results[i] = true;
                  }
               }

               return results;
            } catch (Exception var8) {
               if (this.useDefault) {
                  return this.defaultValue;
               } else {
                  throw new ConversionException(value.toString(), var8);
               }
            }
         }
      }
   }
}
