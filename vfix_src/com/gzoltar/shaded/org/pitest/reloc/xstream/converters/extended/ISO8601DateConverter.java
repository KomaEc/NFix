package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import java.util.Calendar;
import java.util.Date;

public class ISO8601DateConverter extends ISO8601GregorianCalendarConverter {
   public boolean canConvert(Class type) {
      return type.equals(Date.class);
   }

   public Object fromString(String str) {
      return ((Calendar)super.fromString(str)).getTime();
   }

   public String toString(Object obj) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime((Date)obj);
      return super.toString(calendar);
   }
}
