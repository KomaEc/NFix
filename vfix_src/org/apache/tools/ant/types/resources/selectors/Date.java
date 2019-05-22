package org.apache.tools.ant.types.resources.selectors;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.util.FileUtils;

public class Date implements ResourceSelector {
   private static final String MILLIS_OR_DATETIME = "Either the millis or the datetime attribute must be set.";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private Long millis = null;
   private String dateTime = null;
   private String pattern = null;
   private TimeComparison when;
   private long granularity;

   public Date() {
      this.when = TimeComparison.EQUAL;
      this.granularity = FILE_UTILS.getFileTimestampGranularity();
   }

   public synchronized void setMillis(long m) {
      this.millis = new Long(m);
   }

   public synchronized long getMillis() {
      return this.millis == null ? -1L : this.millis;
   }

   public synchronized void setDateTime(String s) {
      this.dateTime = s;
      this.millis = null;
   }

   public synchronized String getDatetime() {
      return this.dateTime;
   }

   public synchronized void setGranularity(long g) {
      this.granularity = g;
   }

   public synchronized long getGranularity() {
      return this.granularity;
   }

   public synchronized void setPattern(String p) {
      this.pattern = p;
   }

   public synchronized String getPattern() {
      return this.pattern;
   }

   public synchronized void setWhen(TimeComparison c) {
      this.when = c;
   }

   public synchronized TimeComparison getWhen() {
      return this.when;
   }

   public synchronized boolean isSelected(Resource r) {
      if (this.dateTime == null && this.millis == null) {
         throw new BuildException("Either the millis or the datetime attribute must be set.");
      } else {
         if (this.millis == null) {
            Object df = this.pattern == null ? DateFormat.getDateTimeInstance(3, 3, Locale.US) : new SimpleDateFormat(this.pattern);

            try {
               long m = ((DateFormat)df).parse(this.dateTime).getTime();
               if (m < 0L) {
                  throw new BuildException("Date of " + this.dateTime + " results in negative milliseconds value" + " relative to epoch (January 1, 1970, 00:00:00 GMT).");
               }

               this.setMillis(m);
            } catch (ParseException var6) {
               throw new BuildException("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in" + (this.pattern == null ? " MM/DD/YYYY HH:MM AM_PM" : this.pattern) + " format.");
            }
         }

         return this.when.evaluate(r.getLastModified(), this.millis, this.granularity);
      }
   }
}
