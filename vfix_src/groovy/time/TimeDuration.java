package groovy.time;

import java.util.Calendar;
import java.util.Date;

public class TimeDuration extends Duration {
   public TimeDuration(int hours, int minutes, int seconds, int millis) {
      super(0, hours, minutes, seconds, millis);
   }

   public TimeDuration(int days, int hours, int minutes, int seconds, int millis) {
      super(days, hours, minutes, seconds, millis);
   }

   public Duration plus(Duration rhs) {
      return new TimeDuration(this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public DatumDependentDuration plus(DatumDependentDuration rhs) {
      return new TimeDatumDependentDuration(rhs.getYears(), rhs.getMonths(), this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public Duration minus(Duration rhs) {
      return new TimeDuration(this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public DatumDependentDuration minus(DatumDependentDuration rhs) {
      return new TimeDatumDependentDuration(-rhs.getYears(), -rhs.getMonths(), this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public Date getAgo() {
      Calendar cal = Calendar.getInstance();
      cal.add(6, -this.getDays());
      cal.add(11, -this.getHours());
      cal.add(12, -this.getMinutes());
      cal.add(13, -this.getSeconds());
      cal.add(14, -this.getMillis());
      return cal.getTime();
   }

   public BaseDuration.From getFrom() {
      return new BaseDuration.From() {
         public Date getNow() {
            Calendar cal = Calendar.getInstance();
            cal.add(6, TimeDuration.this.getDays());
            cal.add(11, TimeDuration.this.getHours());
            cal.add(12, TimeDuration.this.getMinutes());
            cal.add(13, TimeDuration.this.getSeconds());
            cal.add(14, TimeDuration.this.getMillis());
            return cal.getTime();
         }
      };
   }
}
