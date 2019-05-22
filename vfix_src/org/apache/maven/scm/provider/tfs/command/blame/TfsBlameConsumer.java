package org.apache.maven.scm.provider.tfs.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class TfsBlameConsumer extends AbstractConsumer {
   private static final String TFS_TIMESTAMP_PATTERN = "MM/dd/yyyy";
   private static final Pattern LINE_PATTERN = Pattern.compile("([^ ]+)[ ]+([^ ]+)[ ]+([^ ]+)");
   private List<BlameLine> lines = new ArrayList();

   public TfsBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.find()) {
         String revision = matcher.group(1).trim();
         String author = matcher.group(2).trim();
         String dateStr = matcher.group(3).trim();
         Date date = this.parseDate(dateStr, (String)null, "MM/dd/yyyy");
         this.lines.add(new BlameLine(date, revision, author));
      }

   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
