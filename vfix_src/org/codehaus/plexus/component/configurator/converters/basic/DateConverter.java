package org.codehaus.plexus.component.configurator.converters.basic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends AbstractBasicConverter {
   private static final DateFormat[] formats = new DateFormat[]{new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S a"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ssa")};
   // $FF: synthetic field
   static Class class$java$util$Date;

   public boolean canConvert(Class type) {
      return type.equals(class$java$util$Date == null ? (class$java$util$Date = class$("java.util.Date")) : class$java$util$Date);
   }

   public Object fromString(String str) {
      int i = 0;

      while(i < formats.length) {
         try {
            return formats[i].parse(str);
         } catch (ParseException var4) {
            ++i;
         }
      }

      return null;
   }

   public String toString(Object obj) {
      Date date = (Date)obj;
      return formats[0].format(date);
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
