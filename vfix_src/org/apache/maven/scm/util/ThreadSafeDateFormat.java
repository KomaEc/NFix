package org.apache.maven.scm.util;

import java.lang.ref.SoftReference;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThreadSafeDateFormat extends DateFormat {
   private static final long serialVersionUID = 3786090697869963812L;
   private final String dateFormat;
   private final ThreadLocal<SoftReference<SimpleDateFormat>> formatCache = new ThreadLocal<SoftReference<SimpleDateFormat>>() {
      public SoftReference<SimpleDateFormat> get() {
         SoftReference<SimpleDateFormat> softRef = (SoftReference)super.get();
         if (softRef == null || softRef.get() == null) {
            softRef = new SoftReference(new SimpleDateFormat(ThreadSafeDateFormat.this.dateFormat));
            super.set(softRef);
         }

         return softRef;
      }
   };

   public ThreadSafeDateFormat(String sDateFormat) {
      this.dateFormat = sDateFormat;
   }

   private DateFormat getDateFormat() {
      return (DateFormat)((SoftReference)this.formatCache.get()).get();
   }

   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
      return this.getDateFormat().format(date, toAppendTo, fieldPosition);
   }

   public Date parse(String source, ParsePosition pos) {
      return this.getDateFormat().parse(source, pos);
   }
}
