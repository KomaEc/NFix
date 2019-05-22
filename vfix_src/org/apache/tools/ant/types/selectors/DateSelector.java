package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.util.FileUtils;

public class DateSelector extends BaseExtendSelector {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private long millis = -1L;
   private String dateTime = null;
   private boolean includeDirs = false;
   private long granularity = 0L;
   private String pattern;
   private TimeComparison when;
   public static final String MILLIS_KEY = "millis";
   public static final String DATETIME_KEY = "datetime";
   public static final String CHECKDIRS_KEY = "checkdirs";
   public static final String GRANULARITY_KEY = "granularity";
   public static final String WHEN_KEY = "when";
   public static final String PATTERN_KEY = "pattern";

   public DateSelector() {
      this.when = TimeComparison.EQUAL;
      this.granularity = FILE_UTILS.getFileTimestampGranularity();
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("{dateselector date: ");
      buf.append(this.dateTime);
      buf.append(" compare: ").append(this.when.getValue());
      buf.append(" granularity: ");
      buf.append(this.granularity);
      if (this.pattern != null) {
         buf.append(" pattern: ").append(this.pattern);
      }

      buf.append("}");
      return buf.toString();
   }

   public void setMillis(long millis) {
      this.millis = millis;
   }

   public long getMillis() {
      if (this.dateTime != null) {
         this.validate();
      }

      return this.millis;
   }

   public void setDatetime(String dateTime) {
      this.dateTime = dateTime;
      this.millis = -1L;
   }

   public void setCheckdirs(boolean includeDirs) {
      this.includeDirs = includeDirs;
   }

   public void setGranularity(int granularity) {
      this.granularity = (long)granularity;
   }

   public void setWhen(DateSelector.TimeComparisons tcmp) {
      this.setWhen((TimeComparison)tcmp);
   }

   public void setWhen(TimeComparison t) {
      this.when = t;
   }

   public void setPattern(String pattern) {
      this.pattern = pattern;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("millis".equalsIgnoreCase(paramname)) {
               try {
                  this.setMillis(new Long(parameters[i].getValue()));
               } catch (NumberFormatException var6) {
                  this.setError("Invalid millisecond setting " + parameters[i].getValue());
               }
            } else if ("datetime".equalsIgnoreCase(paramname)) {
               this.setDatetime(parameters[i].getValue());
            } else if ("checkdirs".equalsIgnoreCase(paramname)) {
               this.setCheckdirs(Project.toBoolean(parameters[i].getValue()));
            } else if ("granularity".equalsIgnoreCase(paramname)) {
               try {
                  this.setGranularity(new Integer(parameters[i].getValue()));
               } catch (NumberFormatException var5) {
                  this.setError("Invalid granularity setting " + parameters[i].getValue());
               }
            } else if ("when".equalsIgnoreCase(paramname)) {
               this.setWhen(new TimeComparison(parameters[i].getValue()));
            } else if ("pattern".equalsIgnoreCase(paramname)) {
               this.setPattern(parameters[i].getValue());
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.dateTime == null && this.millis < 0L) {
         this.setError("You must provide a datetime or the number of milliseconds.");
      } else if (this.millis < 0L && this.dateTime != null) {
         Object df = this.pattern == null ? DateFormat.getDateTimeInstance(3, 3, Locale.US) : new SimpleDateFormat(this.pattern);

         try {
            this.setMillis(((DateFormat)df).parse(this.dateTime).getTime());
            if (this.millis < 0L) {
               this.setError("Date of " + this.dateTime + " results in negative milliseconds value" + " relative to epoch (January 1, 1970, 00:00:00 GMT).");
            }
         } catch (ParseException var3) {
            this.setError("Date of " + this.dateTime + " Cannot be parsed correctly. It should be in" + (this.pattern == null ? " MM/DD/YYYY HH:MM AM_PM" : this.pattern) + " format.");
         }
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      return file.isDirectory() && !this.includeDirs || this.when.evaluate(file.lastModified(), this.millis, this.granularity);
   }

   public static class TimeComparisons extends TimeComparison {
   }
}
