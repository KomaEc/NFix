package org.apache.maven.scm.provider.bazaar.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

public class BazaarBlameConsumer extends BazaarConsumer {
   private static final String BAZAAR_TIMESTAMP_PATTERN = "yyyyMMdd";
   private List<BlameLine> lines = new ArrayList();

   public BazaarBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      String annotation = trimmedLine.substring(0, trimmedLine.indexOf(124)).trim();
      String dateStr = annotation.substring(annotation.lastIndexOf(32) + 1);
      annotation = annotation.substring(0, annotation.lastIndexOf(32));
      String author = annotation.substring(annotation.lastIndexOf(32) + 1);
      annotation = annotation.substring(0, annotation.lastIndexOf(32));
      String revision = annotation.trim();
      Date date = this.parseDate(dateStr, (String)null, "yyyyMMdd");
      this.lines.add(new BlameLine(date, revision, author));
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
