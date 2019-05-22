package org.codehaus.groovy.runtime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DateGroovyMethods extends DefaultGroovyMethodsSupport {
   private static final Map<String, Integer> CAL_MAP = new HashMap();

   public static int getAt(Date self, int field) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(self);
      return cal.get(field);
   }

   public static Calendar toCalendar(Date self) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(self);
      return cal;
   }

   public static int getAt(Calendar self, int field) {
      return self.get(field);
   }

   public static void putAt(Calendar self, int field, int value) {
      self.set(field, value);
   }

   public static void putAt(Date self, int field, int value) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(self);
      putAt(cal, field, value);
      self.setTime(cal.getTimeInMillis());
   }

   public static void set(Calendar self, Map<Object, Integer> updates) {
      Iterator i$ = updates.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Object, Integer> entry = (Entry)i$.next();
         Object key = entry.getKey();
         if (key instanceof String) {
            key = CAL_MAP.get(key);
         }

         if (key instanceof Integer) {
            self.set((Integer)key, (Integer)entry.getValue());
         }
      }

   }

   public static Calendar updated(Calendar self, Map<Object, Integer> updates) {
      Calendar result = (Calendar)self.clone();
      Iterator i$ = updates.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Object, Integer> entry = (Entry)i$.next();
         Object key = entry.getKey();
         if (key instanceof String) {
            key = CAL_MAP.get(key);
         }

         if (key instanceof Integer) {
            result.set((Integer)key, (Integer)entry.getValue());
         }
      }

      return result;
   }

   public static void set(Date self, Map<Object, Integer> updates) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(self);
      set(cal, updates);
      self.setTime(cal.getTimeInMillis());
   }

   public static Date updated(Date self, Map<Object, Integer> updates) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(self);
      set(cal, updates);
      return cal.getTime();
   }

   public static Date next(Date self) {
      return plus((Date)self, 1);
   }

   public static java.sql.Date next(java.sql.Date self) {
      return new java.sql.Date(next((Date)self).getTime());
   }

   public static Date previous(Date self) {
      return minus((Date)self, 1);
   }

   public static java.sql.Date previous(java.sql.Date self) {
      return new java.sql.Date(previous((Date)self).getTime());
   }

   public static Date plus(Date self, int days) {
      Calendar calendar = (Calendar)Calendar.getInstance().clone();
      calendar.setTime(self);
      calendar.add(6, days);
      return calendar.getTime();
   }

   public static java.sql.Date plus(java.sql.Date self, int days) {
      return new java.sql.Date(plus((Date)self, days).getTime());
   }

   public static Date minus(Date self, int days) {
      return plus(self, -days);
   }

   public static java.sql.Date minus(java.sql.Date self, int days) {
      return new java.sql.Date(minus((Date)self, days).getTime());
   }

   public static int minus(Calendar self, Calendar then) {
      Calendar a = self;
      Calendar b = then;
      boolean swap = self.before(then);
      if (swap) {
         a = then;
         b = self;
      }

      int days = 0;
      b = (Calendar)b.clone();

      while(a.get(1) > b.get(1)) {
         days += 1 + (b.getActualMaximum(6) - b.get(6));
         b.set(6, 1);
         b.add(1, 1);
      }

      days += a.get(6) - b.get(6);
      if (swap) {
         days = -days;
      }

      return days;
   }

   public static int minus(Date self, Date then) {
      Calendar a = (Calendar)Calendar.getInstance().clone();
      a.setTime(self);
      Calendar b = (Calendar)Calendar.getInstance().clone();
      b.setTime(then);
      return minus(a, b);
   }

   public static String format(Date self, String format) {
      return (new SimpleDateFormat(format)).format(self);
   }

   public static String getDateString(Date self) {
      return DateFormat.getDateInstance(3).format(self);
   }

   public static String getTimeString(Date self) {
      return DateFormat.getTimeInstance(2).format(self);
   }

   public static String getDateTimeString(Date self) {
      return DateFormat.getDateTimeInstance(3, 2).format(self);
   }

   private static void clearTimeCommon(Calendar self) {
      self.set(11, 0);
      self.clear(12);
      self.clear(13);
      self.clear(14);
   }

   public static void clearTime(Date self) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(self);
      clearTimeCommon(calendar);
      self.setTime(calendar.getTime().getTime());
   }

   public static void clearTime(java.sql.Date self) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(self);
      clearTimeCommon(calendar);
      self.setTime(calendar.getTime().getTime());
   }

   public static void clearTime(Calendar self) {
      clearTimeCommon(self);
   }

   public static String format(Calendar self, String pattern) {
      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      sdf.setTimeZone(self.getTimeZone());
      return sdf.format(self.getTime());
   }

   static {
      CAL_MAP.put("year", 1);
      CAL_MAP.put("month", 2);
      CAL_MAP.put("date", 5);
      CAL_MAP.put("hourOfDay", 11);
      CAL_MAP.put("minute", 12);
      CAL_MAP.put("second", 13);
   }
}
