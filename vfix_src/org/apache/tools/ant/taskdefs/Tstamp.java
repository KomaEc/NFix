package org.apache.tools.ant.taskdefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;

public class Tstamp extends Task {
   private Vector customFormats = new Vector();
   private String prefix = "";

   public void setPrefix(String prefix) {
      this.prefix = prefix;
      if (!this.prefix.endsWith(".")) {
         this.prefix = this.prefix + ".";
      }

   }

   public void execute() throws BuildException {
      try {
         Date d = new Date();
         Enumeration i = this.customFormats.elements();

         while(i.hasMoreElements()) {
            Tstamp.CustomFormat cts = (Tstamp.CustomFormat)i.nextElement();
            cts.execute(this.getProject(), d, this.getLocation());
         }

         SimpleDateFormat dstamp = new SimpleDateFormat("yyyyMMdd");
         this.setProperty("DSTAMP", dstamp.format(d));
         SimpleDateFormat tstamp = new SimpleDateFormat("HHmm");
         this.setProperty("TSTAMP", tstamp.format(d));
         SimpleDateFormat today = new SimpleDateFormat("MMMM d yyyy", Locale.US);
         this.setProperty("TODAY", today.format(d));
      } catch (Exception var6) {
         throw new BuildException(var6);
      }
   }

   public Tstamp.CustomFormat createFormat() {
      Tstamp.CustomFormat cts = new Tstamp.CustomFormat();
      this.customFormats.addElement(cts);
      return cts;
   }

   private void setProperty(String name, String value) {
      this.getProject().setNewProperty(this.prefix + name, value);
   }

   public static class Unit extends EnumeratedAttribute {
      private static final String MILLISECOND = "millisecond";
      private static final String SECOND = "second";
      private static final String MINUTE = "minute";
      private static final String HOUR = "hour";
      private static final String DAY = "day";
      private static final String WEEK = "week";
      private static final String MONTH = "month";
      private static final String YEAR = "year";
      private static final String[] UNITS = new String[]{"millisecond", "second", "minute", "hour", "day", "week", "month", "year"};
      private Map calendarFields = new HashMap();

      public Unit() {
         this.calendarFields.put("millisecond", new Integer(14));
         this.calendarFields.put("second", new Integer(13));
         this.calendarFields.put("minute", new Integer(12));
         this.calendarFields.put("hour", new Integer(11));
         this.calendarFields.put("day", new Integer(5));
         this.calendarFields.put("week", new Integer(3));
         this.calendarFields.put("month", new Integer(2));
         this.calendarFields.put("year", new Integer(1));
      }

      public int getCalendarField() {
         String key = this.getValue().toLowerCase();
         Integer i = (Integer)this.calendarFields.get(key);
         return i;
      }

      public String[] getValues() {
         return UNITS;
      }
   }

   public class CustomFormat {
      private TimeZone timeZone;
      private String propertyName;
      private String pattern;
      private String language;
      private String country;
      private String variant;
      private int offset = 0;
      private int field = 5;

      public void setProperty(String propertyName) {
         this.propertyName = propertyName;
      }

      public void setPattern(String pattern) {
         this.pattern = pattern;
      }

      public void setLocale(String locale) {
         StringTokenizer st = new StringTokenizer(locale, " \t\n\r\f,");

         try {
            this.language = st.nextToken();
            if (st.hasMoreElements()) {
               this.country = st.nextToken();
               if (st.hasMoreElements()) {
                  this.variant = st.nextToken();
                  if (st.hasMoreElements()) {
                     throw new BuildException("bad locale format", Tstamp.this.getLocation());
                  }
               }
            } else {
               this.country = "";
            }

         } catch (NoSuchElementException var4) {
            throw new BuildException("bad locale format", var4, Tstamp.this.getLocation());
         }
      }

      public void setTimezone(String id) {
         this.timeZone = TimeZone.getTimeZone(id);
      }

      public void setOffset(int offset) {
         this.offset = offset;
      }

      /** @deprecated */
      public void setUnit(String unit) {
         Tstamp.this.log("DEPRECATED - The setUnit(String) method has been deprecated. Use setUnit(Tstamp.Unit) instead.");
         Tstamp.Unit u = new Tstamp.Unit();
         u.setValue(unit);
         this.field = u.getCalendarField();
      }

      public void setUnit(Tstamp.Unit unit) {
         this.field = unit.getCalendarField();
      }

      public void execute(Project project, Date date, Location location) {
         if (this.propertyName == null) {
            throw new BuildException("property attribute must be provided", location);
         } else if (this.pattern == null) {
            throw new BuildException("pattern attribute must be provided", location);
         } else {
            SimpleDateFormat sdf;
            if (this.language == null) {
               sdf = new SimpleDateFormat(this.pattern);
            } else if (this.variant == null) {
               sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country));
            } else {
               sdf = new SimpleDateFormat(this.pattern, new Locale(this.language, this.country, this.variant));
            }

            if (this.offset != 0) {
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(date);
               calendar.add(this.field, this.offset);
               date = calendar.getTime();
            }

            if (this.timeZone != null) {
               sdf.setTimeZone(this.timeZone);
            }

            Tstamp.this.setProperty(this.propertyName, sdf.format(date));
         }
      }
   }
}
