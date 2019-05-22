package org.apache.maven.scm.provider.tfs.command.consumer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class TfsChangeLogConsumer extends AbstractConsumer {
   private static final String PATTERN = "^[^:]*:[ \t]([0-9]*)\n[^:]*:[ \t](.*)\n[^:]*:[ \t](.*)\n[^:]*:((?:\n.*)*)\n\n[^\n :]*:(?=\n  )((?:\n[ \t]+.*)*)";
   private static final String PATTERN_ITEM = "\n  ([^$]+) (\\$/.*)";
   private List<ChangeSet> logs = new ArrayList();
   private String buffer = "";
   boolean fed = false;

   public TfsChangeLogConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      this.fed = true;
      if (line.startsWith("-----")) {
         this.addChangeLog();
      }

      this.buffer = this.buffer + line + "\n";
   }

   public List<ChangeSet> getLogs() {
      this.addChangeLog();
      return this.logs;
   }

   private void addChangeLog() {
      if (!this.buffer.equals("")) {
         Pattern p = Pattern.compile("^[^:]*:[ \t]([0-9]*)\n[^:]*:[ \t](.*)\n[^:]*:[ \t](.*)\n[^:]*:((?:\n.*)*)\n\n[^\n :]*:(?=\n  )((?:\n[ \t]+.*)*)");
         Matcher m = p.matcher(this.buffer);
         if (m.find()) {
            String revision = m.group(1).trim();
            String username = m.group(2).trim();
            String dateString = m.group(3).trim();
            String comment = m.group(4).trim();
            Pattern itemPattern = Pattern.compile("\n  ([^$]+) (\\$/.*)");
            Matcher itemMatcher = itemPattern.matcher(m.group(5));
            ArrayList files = new ArrayList();

            while(itemMatcher.find()) {
               ChangeFile file = new ChangeFile(itemMatcher.group(2).trim(), revision);
               files.add(file);
            }

            Date date;
            try {
               date = parseDate(dateString);
            } catch (ParseException var12) {
               this.getLogger().error("Date parse error", var12);
               throw new RuntimeException(var12);
            }

            ChangeSet change = new ChangeSet(date, comment, username, files);
            this.logs.add(change);
         }

         this.buffer = "";
      }

   }

   public boolean hasBeenFed() {
      return this.fed;
   }

   protected static Date parseDate(String dateString) throws ParseException {
      Date date = null;

      try {
         date = new Date(Date.parse(dateString));
      } catch (IllegalArgumentException var3) {
      }

      if (date == null) {
         DateFormat[] formats = createDateFormatsForLocaleAndTimeZone((Locale)null, (TimeZone)null);
         return parseWithFormats(dateString, formats);
      } else {
         return date;
      }
   }

   private static Date parseWithFormats(String input, DateFormat[] formats) throws ParseException {
      ParseException parseException = null;
      int i = 0;

      while(i < formats.length) {
         try {
            return formats[i].parse(input);
         } catch (ParseException var5) {
            parseException = var5;
            ++i;
         }
      }

      throw parseException;
   }

   private static DateFormat[] createDateFormatsForLocaleAndTimeZone(Locale locale, TimeZone timeZone) {
      if (locale == null) {
         locale = Locale.getDefault();
      }

      if (timeZone == null) {
         timeZone = TimeZone.getDefault();
      }

      List<DateFormat> formats = new ArrayList();

      int dateStyle;
      for(dateStyle = 0; dateStyle <= 3; ++dateStyle) {
         for(int timeStyle = 0; timeStyle <= 3; ++timeStyle) {
            DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
            if (timeZone != null) {
               df.setTimeZone(timeZone);
            }

            formats.add(df);
         }
      }

      for(dateStyle = 0; dateStyle <= 3; ++dateStyle) {
         DateFormat df = DateFormat.getDateInstance(dateStyle, locale);
         df.setTimeZone(timeZone);
         formats.add(df);
      }

      return (DateFormat[])((DateFormat[])formats.toArray(new DateFormat[formats.size()]));
   }
}
