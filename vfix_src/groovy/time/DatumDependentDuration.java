package groovy.time;

import java.util.Calendar;
import java.util.Date;

public class DatumDependentDuration extends BaseDuration {
   public DatumDependentDuration(int years, int months, int days, int hours, int minutes, int seconds, int millis) {
      super(years, months, days, hours, minutes, seconds, millis);
   }

   public int getMonths() {
      return this.months;
   }

   public int getYears() {
      return this.years;
   }

   public DatumDependentDuration plus(DatumDependentDuration rhs) {
      return new DatumDependentDuration(this.getYears() + rhs.getYears(), this.getMonths() + rhs.getMonths(), this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public DatumDependentDuration plus(TimeDatumDependentDuration rhs) {
      return rhs.plus(this);
   }

   public DatumDependentDuration plus(Duration rhs) {
      return new DatumDependentDuration(this.getYears(), this.getMonths(), this.getDays() + rhs.getDays(), this.getHours() + rhs.getHours(), this.getMinutes() + rhs.getMinutes(), this.getSeconds() + rhs.getSeconds(), this.getMillis() + rhs.getMillis());
   }

   public DatumDependentDuration plus(TimeDuration rhs) {
      return rhs.plus(this);
   }

   public DatumDependentDuration minus(DatumDependentDuration rhs) {
      return new DatumDependentDuration(this.getYears() - rhs.getYears(), this.getMonths() - rhs.getMonths(), this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public DatumDependentDuration minus(Duration rhs) {
      return new DatumDependentDuration(this.getYears(), this.getMonths(), this.getDays() - rhs.getDays(), this.getHours() - rhs.getHours(), this.getMinutes() - rhs.getMinutes(), this.getSeconds() - rhs.getSeconds(), this.getMillis() - rhs.getMillis());
   }

   public long toMilliseconds() {
      Date now = new Date();
      return TimeCategory.minus(this.plus((Date)now), now).toMilliseconds();
   }

   public Date getAgo() {
      Calendar cal = Calendar.getInstance();
      cal.add(1, -this.getYears());
      cal.add(2, -this.getMonths());
      cal.add(6, -this.getDays());
      cal.add(11, -this.getHours());
      cal.add(12, -this.getMinutes());
      cal.add(13, -this.getSeconds());
      cal.add(14, -this.getMillis());
      cal.set(11, 0);
      cal.set(12, 0);
      cal.set(13, 0);
      cal.set(14, 0);
      return new java.sql.Date(cal.getTimeInMillis());
   }

   public BaseDuration.From getFrom() {
      return new BaseDuration.From() {
         public Date getNow() {
            Calendar cal = Calendar.getInstance();
            cal.add(1, DatumDependentDuration.this.getYears());
            cal.add(2, DatumDependentDuration.this.getMonths());
            cal.add(6, DatumDependentDuration.this.getDays());
            cal.add(11, DatumDependentDuration.this.getHours());
            cal.add(12, DatumDependentDuration.this.getMinutes());
            cal.add(13, DatumDependentDuration.this.getSeconds());
            cal.add(14, DatumDependentDuration.this.getMillis());
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return new java.sql.Date(cal.getTimeInMillis());
         }
      };
   }
}
