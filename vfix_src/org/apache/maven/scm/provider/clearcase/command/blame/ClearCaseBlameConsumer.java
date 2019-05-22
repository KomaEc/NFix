package org.apache.maven.scm.provider.clearcase.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class ClearCaseBlameConsumer extends AbstractConsumer {
   private static final String CLEARCASE_TIMESTAMP_PATTERN = "yyyyMMdd.HHmmss";
   private static final Pattern LINE_PATTERN = Pattern.compile("VERSION:(.*)@@@USER:(.*)@@@DATE:(.*)@@@(.*)");
   private List<BlameLine> lines = new ArrayList();

   public ClearCaseBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.matches()) {
         String revision = matcher.group(1);
         String author = matcher.group(2).toLowerCase();
         String dateTimeStr = matcher.group(3);
         Date dateTime = this.parseDate(dateTimeStr, (String)null, "yyyyMMdd.HHmmss");
         this.lines.add(new BlameLine(dateTime, revision, author));
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(author + " " + dateTimeStr);
         }
      }

   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
