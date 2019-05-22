package org.apache.maven.scm.provider.perforce.command.blame;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class PerforceFilelogConsumer extends AbstractConsumer {
   private static final String PERFORCE_TIMESTAMP_PATTERN = "yyyy/MM/dd";
   private static final Pattern LINE_PATTERN = Pattern.compile("#(\\d+).*on (.*) by (.*)@");
   private Map<String, Date> dates = new HashMap();
   private Map<String, String> authors = new HashMap();

   public PerforceFilelogConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.find()) {
         String revision = matcher.group(1);
         String dateTimeStr = matcher.group(2);
         String author = matcher.group(3);
         Date dateTime = this.parseDate(dateTimeStr, (String)null, "yyyy/MM/dd");
         this.dates.put(revision, dateTime);
         this.authors.put(revision, author);
      }

   }

   public String getAuthor(String revision) {
      return (String)this.authors.get(revision);
   }

   public Date getDate(String revision) {
      return (Date)this.dates.get(revision);
   }
}
