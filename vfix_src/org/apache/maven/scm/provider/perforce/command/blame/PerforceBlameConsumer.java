package org.apache.maven.scm.provider.perforce.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class PerforceBlameConsumer extends AbstractConsumer {
   private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+):");
   private List<BlameLine> lines = new ArrayList();

   public PerforceBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.find()) {
         String revision = matcher.group(1).trim();
         this.lines.add(new BlameLine((Date)null, revision, (String)null));
      }

   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
