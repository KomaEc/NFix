package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.extended;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.ThreadSafeSimpleDateFormat;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.TimeZone;

public class SqlTimestampConverter extends AbstractSingleValueConverter {
   private final ThreadSafeSimpleDateFormat format = new ThreadSafeSimpleDateFormat("yyyy-MM-dd HH:mm:ss", TimeZone.getTimeZone("UTC"), 0, 5, false);

   public boolean canConvert(Class type) {
      return type.equals(Timestamp.class);
   }

   public String toString(Object obj) {
      Timestamp timestamp = (Timestamp)obj;
      StringBuffer buffer = (new StringBuffer(this.format.format(timestamp))).append('.');
      if (timestamp.getNanos() == 0) {
         buffer.append('0');
      } else {
         String nanos = String.valueOf(timestamp.getNanos() + 1000000000);

         int last;
         for(last = 10; last > 2 && nanos.charAt(last - 1) == '0'; --last) {
         }

         buffer.append(nanos.subSequence(1, last));
      }

      return buffer.toString();
   }

   public Object fromString(String str) {
      int idx = str.lastIndexOf(46);
      if (idx >= 0 && str.length() - idx >= 2 && str.length() - idx <= 10) {
         try {
            Timestamp timestamp = new Timestamp(this.format.parse(str.substring(0, idx)).getTime());
            StringBuffer buffer = new StringBuffer(str.substring(idx + 1));

            while(buffer.length() != 9) {
               buffer.append('0');
            }

            timestamp.setNanos(Integer.parseInt(buffer.toString()));
            return timestamp;
         } catch (NumberFormatException var5) {
            throw new ConversionException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]", var5);
         } catch (ParseException var6) {
            throw new ConversionException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]", var6);
         }
      } else {
         throw new ConversionException("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
      }
   }
}
