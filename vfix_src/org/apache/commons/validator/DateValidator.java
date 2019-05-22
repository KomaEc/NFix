package org.apache.commons.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateValidator {
   private static final DateValidator instance = new DateValidator();

   public static DateValidator getInstance() {
      return instance;
   }

   protected DateValidator() {
   }

   public boolean isValid(String value, String datePattern, boolean strict) {
      if (value != null && datePattern != null && datePattern.length() > 0) {
         SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
         formatter.setLenient(false);

         try {
            formatter.parse(value);
         } catch (ParseException var6) {
            return false;
         }

         return !strict || datePattern.length() == value.length();
      } else {
         return false;
      }
   }

   public boolean isValid(String value, Locale locale) {
      if (value == null) {
         return false;
      } else {
         DateFormat formatter = null;
         if (locale != null) {
            formatter = DateFormat.getDateInstance(3, locale);
         } else {
            formatter = DateFormat.getDateInstance(3, Locale.getDefault());
         }

         formatter.setLenient(false);

         try {
            formatter.parse(value);
            return true;
         } catch (ParseException var5) {
            return false;
         }
      }
   }
}
