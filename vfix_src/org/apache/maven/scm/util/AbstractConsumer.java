package org.apache.maven.scm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public abstract class AbstractConsumer implements StreamConsumer {
   private ScmLogger logger;

   public AbstractConsumer(ScmLogger logger) {
      this.setLogger(logger);
   }

   public ScmLogger getLogger() {
      return this.logger;
   }

   public void setLogger(ScmLogger logger) {
      this.logger = logger;
   }

   protected Date parseDate(String date, String userPattern, String defaultPattern) {
      return this.parseDate(date, userPattern, defaultPattern, (Locale)null);
   }

   protected Date parseDate(String date, String userPattern, String defaultPattern, Locale locale) {
      String patternUsed = null;
      Object format;
      if (StringUtils.isNotEmpty(userPattern)) {
         format = new SimpleDateFormat(userPattern);
         patternUsed = userPattern;
      } else if (StringUtils.isNotEmpty(defaultPattern)) {
         if (locale != null) {
            format = new SimpleDateFormat(defaultPattern, locale);
         } else {
            format = new SimpleDateFormat(defaultPattern);
         }

         patternUsed = defaultPattern;
      } else {
         format = DateFormat.getDateInstance(3, Locale.ENGLISH);
         patternUsed = " DateFormat.SHORT ";
      }

      try {
         return ((DateFormat)format).parse(date);
      } catch (ParseException var8) {
         if (this.getLogger() != null && this.getLogger().isWarnEnabled()) {
            this.getLogger().warn("skip ParseException: " + var8.getMessage() + " during parsing date " + date + " with pattern " + patternUsed + " with Locale " + (locale == null ? Locale.ENGLISH : locale), var8);
         }

         return null;
      }
   }
}
