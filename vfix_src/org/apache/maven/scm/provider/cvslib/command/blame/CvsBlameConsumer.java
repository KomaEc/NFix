package org.apache.maven.scm.provider.cvslib.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class CvsBlameConsumer extends AbstractConsumer {
   private static final String CVS_TIMESTAMP_PATTERN = "dd-MMM-yy";
   private static final Pattern LINE_PATTERN = Pattern.compile("(.*)\\((.*)\\s+(.*)\\)");
   private List<BlameLine> lines = new ArrayList();

   public CvsBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      if (line != null && line.indexOf(58) > 0) {
         String annotation = line.substring(0, line.indexOf(58));
         Matcher matcher = LINE_PATTERN.matcher(annotation);
         if (matcher.matches()) {
            String revision = matcher.group(1).trim();
            String author = matcher.group(2).trim();
            String dateTimeStr = matcher.group(3).trim();
            Date dateTime = this.parseDate(dateTimeStr, (String)null, "dd-MMM-yy", Locale.US);
            this.lines.add(new BlameLine(dateTime, revision, author));
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug(author + " " + dateTimeStr);
            }
         }
      }

   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
