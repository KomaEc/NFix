package org.jboss.util.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jboss.util.NestedRuntimeException;
import org.jboss.util.Strings;

public class DateEditor extends PropertyEditorSupport {
   private static DateFormat[] formats;
   private String text;

   public static void initialize() {
      PrivilegedAction action = new PrivilegedAction() {
         public Object run() {
            String defaultFormat = System.getProperty("org.jboss.util.propertyeditor.DateEditor.format", "MMM d, yyyy");
            String defaultLocale = System.getProperty("org.jboss.util.propertyeditor.DateEditor.locale");
            SimpleDateFormat defaultDateFormat;
            if (defaultLocale != null && defaultLocale.length() != 0) {
               defaultDateFormat = new SimpleDateFormat(defaultFormat, Strings.parseLocaleString(defaultLocale));
            } else {
               defaultDateFormat = new SimpleDateFormat(defaultFormat);
            }

            DateEditor.formats = new DateFormat[]{defaultDateFormat, new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"), new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")};
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }

   public void setValue(Object value) {
      if (!(value instanceof Date) && value != null) {
         throw new IllegalArgumentException("setValue() expected java.util.Date value, got " + value.getClass().getName());
      } else {
         this.text = null;
         super.setValue(value);
      }
   }

   public void setAsText(String text) {
      ParseException pe = null;
      int i = 0;

      while(i < formats.length) {
         try {
            DateFormat df = formats[i];
            Date date = df.parse(text);
            this.text = text;
            super.setValue(date);
            return;
         } catch (ParseException var6) {
            pe = var6;
            ++i;
         }
      }

      throw new NestedRuntimeException(pe);
   }

   public String getAsText() {
      if (this.text == null) {
         DateFormat df = formats[formats.length - 1];
         Date date = (Date)this.getValue();
         this.text = df.format(date);
      }

      return this.text;
   }

   static {
      initialize();
   }
}
