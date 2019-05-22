package org.codehaus.groovy.runtime;

import groovy.time.BaseDuration;
import groovy.time.DatumDependentDuration;
import groovy.time.Duration;
import groovy.time.TimeDuration;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/** @deprecated */
public class TimeCategory {
   public static Date plus(Date date, BaseDuration duration) {
      return duration.plus(date);
   }

   public static Date minus(Date date, BaseDuration duration) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(1, -duration.getYears());
      cal.add(2, -duration.getMonths());
      cal.add(6, -duration.getDays());
      cal.add(11, -duration.getHours());
      cal.add(12, -duration.getMinutes());
      cal.add(13, -duration.getSeconds());
      cal.add(14, -duration.getMillis());
      return cal.getTime();
   }

   public static TimeZone getTimeZone(Date self) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(self);
      return calendar.getTimeZone();
   }

   public static Duration getDaylightSavingsOffset(Date self) {
      TimeZone timeZone = getTimeZone(self);
      int millis = timeZone.useDaylightTime() && timeZone.inDaylightTime(self) ? timeZone.getDSTSavings() : 0;
      return new TimeDuration(0, 0, 0, millis);
   }

   public static Duration getDaylightSavingsOffset(BaseDuration self) {
      return getDaylightSavingsOffset(new Date(self.toMilliseconds() + 1L));
   }

   public static Duration getRelativeDaylightSavingsOffset(Date self, Date other) {
      Duration d1 = getDaylightSavingsOffset(self);
      Duration d2 = getDaylightSavingsOffset(other);
      return new TimeDuration(0, 0, 0, (int)(d2.toMilliseconds() - d1.toMilliseconds()));
   }

   public static TimeDuration minus(Date lhs, Date rhs) {
      long milliseconds = lhs.getTime() - rhs.getTime();
      long days = milliseconds / 86400000L;
      milliseconds -= days * 24L * 60L * 60L * 1000L;
      int hours = (int)(milliseconds / 3600000L);
      milliseconds -= (long)(hours * 60 * 60 * 1000);
      int minutes = (int)(milliseconds / 60000L);
      milliseconds -= (long)(minutes * 60 * 1000);
      int seconds = (int)(milliseconds / 1000L);
      milliseconds -= (long)(seconds * 1000);
      return new TimeDuration((int)days, hours, minutes, seconds, (int)milliseconds);
   }

   public static DatumDependentDuration getMonths(Integer self) {
      return new DatumDependentDuration(0, self, 0, 0, 0, 0, 0);
   }

   public static DatumDependentDuration getMonth(Integer self) {
      return getMonths(self);
   }

   public static DatumDependentDuration getYears(Integer self) {
      return new DatumDependentDuration(self, 0, 0, 0, 0, 0, 0);
   }

   public static DatumDependentDuration getYear(Integer self) {
      return getYears(self);
   }

   public static Duration getWeeks(Integer self) {
      return new Duration(self * 7, 0, 0, 0, 0);
   }

   public static Duration getWeek(Integer self) {
      return getWeeks(self);
   }

   public static Duration getDays(Integer self) {
      return new Duration(self, 0, 0, 0, 0);
   }

   public static Duration getDay(Integer self) {
      return getDays(self);
   }

   public static TimeDuration getHours(Integer self) {
      return new TimeDuration(0, self, 0, 0, 0);
   }

   public static TimeDuration getHour(Integer self) {
      return getHours(self);
   }

   public static TimeDuration getMinutes(Integer self) {
      return new TimeDuration(0, 0, self, 0, 0);
   }

   public static TimeDuration getMinute(Integer self) {
      return getMinutes(self);
   }

   public static TimeDuration getSeconds(Integer self) {
      return new TimeDuration(0, 0, 0, self, 0);
   }

   public static TimeDuration getSecond(Integer self) {
      return getSeconds(self);
   }

   public static TimeDuration getMilliseconds(Integer self) {
      return new TimeDuration(0, 0, 0, 0, self);
   }

   public static TimeDuration getMillisecond(Integer self) {
      return getMilliseconds(self);
   }
}
