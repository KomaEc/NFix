package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class ISO8601GregorianCalendarConverter extends AbstractSingleValueConverter {
   private static final DateTimeFormatter[] formattersUTC = new DateTimeFormatter[]{ISODateTimeFormat.dateTime(), ISODateTimeFormat.dateTimeNoMillis(), ISODateTimeFormat.basicDateTime(), ISODateTimeFormat.basicOrdinalDateTime(), ISODateTimeFormat.basicOrdinalDateTimeNoMillis(), ISODateTimeFormat.basicTime(), ISODateTimeFormat.basicTimeNoMillis(), ISODateTimeFormat.basicTTime(), ISODateTimeFormat.basicTTimeNoMillis(), ISODateTimeFormat.basicWeekDateTime(), ISODateTimeFormat.basicWeekDateTimeNoMillis(), ISODateTimeFormat.ordinalDateTime(), ISODateTimeFormat.ordinalDateTimeNoMillis(), ISODateTimeFormat.time(), ISODateTimeFormat.timeNoMillis(), ISODateTimeFormat.tTime(), ISODateTimeFormat.tTimeNoMillis(), ISODateTimeFormat.weekDateTime(), ISODateTimeFormat.weekDateTimeNoMillis()};
   private static final DateTimeFormatter[] formattersNoUTC = new DateTimeFormatter[]{ISODateTimeFormat.basicDate(), ISODateTimeFormat.basicOrdinalDate(), ISODateTimeFormat.basicWeekDate(), ISODateTimeFormat.date(), ISODateTimeFormat.dateHour(), ISODateTimeFormat.dateHourMinute(), ISODateTimeFormat.dateHourMinuteSecond(), ISODateTimeFormat.dateHourMinuteSecondFraction(), ISODateTimeFormat.dateHourMinuteSecondMillis(), ISODateTimeFormat.hour(), ISODateTimeFormat.hourMinute(), ISODateTimeFormat.hourMinuteSecond(), ISODateTimeFormat.hourMinuteSecondFraction(), ISODateTimeFormat.hourMinuteSecondMillis(), ISODateTimeFormat.ordinalDate(), ISODateTimeFormat.weekDate(), ISODateTimeFormat.year(), ISODateTimeFormat.yearMonth(), ISODateTimeFormat.yearMonthDay(), ISODateTimeFormat.weekyear(), ISODateTimeFormat.weekyearWeek(), ISODateTimeFormat.weekyearWeekDay()};

   public boolean canConvert(Class type) {
      return type.equals(GregorianCalendar.class);
   }

   public Object fromString(String str) {
      int i = 0;

      while(i < formattersUTC.length) {
         DateTimeFormatter formatter = formattersUTC[i];

         try {
            DateTime dt = formatter.parseDateTime(str);
            Calendar calendar = dt.toGregorianCalendar();
            calendar.setTimeZone(TimeZone.getDefault());
            return calendar;
         } catch (IllegalArgumentException var8) {
            ++i;
         }
      }

      DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(TimeZone.getDefault());
      int i = 0;

      while(i < formattersNoUTC.length) {
         try {
            DateTimeFormatter formatter = formattersNoUTC[i].withZone(dateTimeZone);
            DateTime dt = formatter.parseDateTime(str);
            Calendar calendar = dt.toGregorianCalendar();
            calendar.setTimeZone(TimeZone.getDefault());
            return calendar;
         } catch (IllegalArgumentException var7) {
            ++i;
         }
      }

      throw new ConversionException("Cannot parse date " + str);
   }

   public String toString(Object obj) {
      DateTime dt = new DateTime(obj);
      return dt.toString(formattersUTC[0]);
   }
}
