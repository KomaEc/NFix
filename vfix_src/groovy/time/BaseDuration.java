package groovy.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public abstract class BaseDuration {
   protected final int years;
   protected final int months;
   protected final int days;
   protected final int hours;
   protected final int minutes;
   protected final int seconds;
   protected final int millis;

   protected BaseDuration(int years, int months, int days, int hours, int minutes, int seconds, int millis) {
      this.years = years;
      this.months = months;
      this.days = days;
      this.hours = hours;
      this.minutes = minutes;
      this.seconds = seconds;
      this.millis = millis;
   }

   protected BaseDuration(int days, int hours, int minutes, int seconds, int millis) {
      this(0, 0, days, hours, minutes, seconds, millis);
   }

   public int getYears() {
      return this.years;
   }

   public int getMonths() {
      return this.months;
   }

   public int getDays() {
      return this.days;
   }

   public int getHours() {
      return this.hours;
   }

   public int getMinutes() {
      return this.minutes;
   }

   public int getSeconds() {
      return this.seconds;
   }

   public int getMillis() {
      return this.millis;
   }

   public Date plus(Date date) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.add(1, this.years);
      cal.add(2, this.months);
      cal.add(6, this.days);
      cal.add(11, this.hours);
      cal.add(12, this.minutes);
      cal.add(13, this.seconds);
      cal.add(14, this.millis);
      return cal.getTime();
   }

   public String toString() {
      List buffer = new ArrayList();
      if (this.years != 0) {
         buffer.add(this.years + " years");
      }

      if (this.months != 0) {
         buffer.add(this.months + " months");
      }

      if (this.days != 0) {
         buffer.add(this.days + " days");
      }

      if (this.hours != 0) {
         buffer.add(this.hours + " hours");
      }

      if (this.minutes != 0) {
         buffer.add(this.minutes + " minutes");
      }

      if (this.seconds != 0 || this.millis != 0) {
         buffer.add((this.seconds == 0 ? (this.millis < 0 ? "-0" : "0") : this.seconds) + "." + DefaultGroovyMethods.padLeft("" + Math.abs(this.millis), 3, "0") + " seconds");
      }

      return buffer.size() != 0 ? DefaultGroovyMethods.join((Collection)buffer, ", ") : "0";
   }

   public abstract long toMilliseconds();

   public abstract Date getAgo();

   public abstract BaseDuration.From getFrom();

   public abstract static class From {
      public abstract Date getNow();

      public Date getToday() {
         return this.getNow();
      }
   }
}
