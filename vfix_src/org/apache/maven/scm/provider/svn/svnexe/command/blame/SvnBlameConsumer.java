package org.apache.maven.scm.provider.svn.svnexe.command.blame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class SvnBlameConsumer extends AbstractConsumer {
   private static final String SVN_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
   private static final Pattern LINE_PATTERN = Pattern.compile("line-number=\"(.*)\"");
   private static final Pattern REVISION_PATTERN = Pattern.compile("revision=\"(.*)\"");
   private static final Pattern AUTHOR_PATTERN = Pattern.compile("<author>(.*)</author>");
   private static final Pattern DATE_PATTERN = Pattern.compile("<date>(.*)T(.*)\\.(.*)Z</date>");
   private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   private List<BlameLine> lines = new ArrayList();
   private int lineNumber;
   private String revision;
   private String author;

   public SvnBlameConsumer(ScmLogger logger) {
      super(logger);
      this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
   }

   public void consumeLine(String line) {
      Matcher matcher;
      String date;
      if ((matcher = LINE_PATTERN.matcher(line)).find()) {
         date = matcher.group(1);
         this.lineNumber = Integer.parseInt(date);
      } else if ((matcher = REVISION_PATTERN.matcher(line)).find()) {
         this.revision = matcher.group(1);
      } else if ((matcher = AUTHOR_PATTERN.matcher(line)).find()) {
         this.author = matcher.group(1);
      } else if ((matcher = DATE_PATTERN.matcher(line)).find()) {
         date = matcher.group(1);
         String time = matcher.group(2);
         Date dateTime = this.parseDateTime(date + " " + time);
         this.lines.add(new BlameLine(dateTime, this.revision, this.author));
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Author of line " + this.lineNumber + ": " + this.author + " (" + date + ")");
         }
      }

   }

   protected Date parseDateTime(String dateTimeStr) {
      try {
         return this.dateFormat.parse(dateTimeStr);
      } catch (ParseException var3) {
         this.getLogger().error("skip ParseException: " + var3.getMessage() + " during parsing date " + dateTimeStr, var3);
         return null;
      }
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
