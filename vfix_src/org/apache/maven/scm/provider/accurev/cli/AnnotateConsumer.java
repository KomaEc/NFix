package org.apache.maven.scm.provider.accurev.cli;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class AnnotateConsumer extends AbstractConsumer {
   private static final Pattern LINE_PATTERN = Pattern.compile("^\\s+(\\d+)\\s+(\\w+)\\s+([0-9/]+ [0-9:]+).*");
   private List<BlameLine> lines;

   public AnnotateConsumer(List<BlameLine> lines, ScmLogger scmLogger) {
      super(scmLogger);
      this.lines = lines;
   }

   public void consumeLine(String line) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.matches()) {
         String revision = matcher.group(1).trim();
         String author = matcher.group(2).trim();
         String dateStr = matcher.group(3).trim();
         Date date = this.parseDate(dateStr, (String)null, "yyyy/MM/dd HH:mm:ss");
         this.lines.add(new BlameLine(date, revision, author));
      } else {
         throw new RuntimeException("Unable to parse annotation from line: " + line);
      }
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
