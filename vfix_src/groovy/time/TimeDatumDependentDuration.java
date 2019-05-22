package groovy.time;

import java.util.Calendar;
import java.util.Date;

public class TimeDatumDependentDuration extends DatumDependentDuration {
   public TimeDatumDependentDuration(int years, int months, int days, int hours, int minutes, int seconds, int millis) {
      super(years, months, days, hours, minutes, seconds, millis);
   }

   public DatumDependentDuration plus(Duration rhs) {
      return new TimeDatumDependentDuration(this.getYears(), this.getMonths(), this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public DatumDependentDuration plus(DatumDependentDuration rhs) {
      return new TimeDatumDependentDuration(this.getYears() + rhs.getYears(), this.getMonths() + rhs.getMonths(), this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public DatumDependentDuration minus(Duration rhs) {
      return new TimeDatumDependentDuration(this.getYears(), this.getMonths(), this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public DatumDependentDuration minus(DatumDependentDuration rhs) {
      return new TimeDatumDependentDuration(this.getYears() - rhs.getYears(), this.getMonths() - rhs.getMonths(), this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public BaseDuration.From getFrom() {
      return new BaseDuration.From() {
         public Date getNow() {
            Calendar cal = Calendar.getInstance();
            cal.add(1, TimeDatumDependentDuration.this.getYears());
            cal.add(2, TimeDatumDependentDuration.this.getMonths());
            cal.add(6, TimeDatumDependentDuration.this.getDays());
            cal.add(11, TimeDatumDependentDuration.this.getHours());
            cal.add(12, TimeDatumDependentDuration.this.getMinutes());
            cal.add(13, TimeDatumDependentDuration.this.getSeconds());
            cal.add(14, TimeDatumDependentDuration.this.getMillis());
            return cal.getTime();
         }
      };
   }
}
