package org.testng;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeBombSkipException extends SkipException {
   private static final long serialVersionUID = -8599821478834048537L;
   private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
   private Calendar m_expireDate;
   private DateFormat m_inFormat;
   private DateFormat m_outFormat;

   public TimeBombSkipException(String msg, Date expirationDate) {
      super(msg);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.initExpireDate(expirationDate);
   }

   public TimeBombSkipException(String msg, Date expirationDate, String format) {
      super(msg);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.m_inFormat = new SimpleDateFormat(format);
      this.m_outFormat = new SimpleDateFormat(format);
      this.initExpireDate(expirationDate);
   }

   public TimeBombSkipException(String msg, String date) {
      super(msg);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.initExpireDate(date);
   }

   public TimeBombSkipException(String msg, String date, String format) {
      this(msg, date, format, format);
   }

   public TimeBombSkipException(String msg, String date, String inFormat, String outFormat) {
      super(msg);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.m_inFormat = new SimpleDateFormat(inFormat);
      this.m_outFormat = new SimpleDateFormat(outFormat);
      this.initExpireDate(date);
   }

   public TimeBombSkipException(String msg, Date expirationDate, Throwable cause) {
      super(msg, cause);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.initExpireDate(expirationDate);
   }

   public TimeBombSkipException(String msg, Date expirationDate, String format, Throwable cause) {
      super(msg, cause);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.m_inFormat = new SimpleDateFormat(format);
      this.m_outFormat = new SimpleDateFormat(format);
      this.initExpireDate(expirationDate);
   }

   public TimeBombSkipException(String msg, String date, Throwable cause) {
      super(msg, cause);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.initExpireDate(date);
   }

   public TimeBombSkipException(String msg, String date, String format, Throwable cause) {
      this(msg, date, format, format, cause);
   }

   public TimeBombSkipException(String msg, String date, String inFormat, String outFormat, Throwable cause) {
      super(msg, cause);
      this.m_inFormat = SDF;
      this.m_outFormat = SDF;
      this.m_inFormat = new SimpleDateFormat(inFormat);
      this.m_outFormat = new SimpleDateFormat(outFormat);
      this.initExpireDate(date);
   }

   private void initExpireDate(Date expireDate) {
      this.m_expireDate = Calendar.getInstance();
      this.m_expireDate.setTime(expireDate);
   }

   private void initExpireDate(String date) {
      try {
         synchronized(this.m_inFormat) {
            Date d = this.m_inFormat.parse(date);
            this.initExpireDate(d);
         }
      } catch (ParseException var6) {
         throw new TestNGException("Cannot parse date:" + date + " using pattern: " + this.m_inFormat, var6);
      }
   }

   public boolean isSkip() {
      if (null == this.m_expireDate) {
         return false;
      } else {
         try {
            Calendar now = Calendar.getInstance();
            Date nowDate = this.m_inFormat.parse(this.m_inFormat.format(now.getTime()));
            now.setTime(nowDate);
            return !now.after(this.m_expireDate);
         } catch (ParseException var3) {
            throw new TestNGException("Cannot compare dates.");
         }
      }
   }

   public String getMessage() {
      return this.isSkip() ? super.getMessage() : super.getMessage() + "; Test must have been enabled by: " + this.m_outFormat.format(this.m_expireDate.getTime());
   }

   public void printStackTrace(PrintStream s) {
      this.reduceStackTrace();
      super.printStackTrace(s);
   }

   public void printStackTrace(PrintWriter s) {
      this.reduceStackTrace();
      super.printStackTrace(s);
   }
}
