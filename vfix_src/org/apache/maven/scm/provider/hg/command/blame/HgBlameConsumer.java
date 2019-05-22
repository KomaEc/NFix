package org.apache.maven.scm.provider.hg.command.blame;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgBlameConsumer extends HgConsumer {
   private List<BlameLine> lines = new ArrayList();
   private static final String HG_TIMESTAMP_PATTERN = "EEE MMM dd HH:mm:ss yyyy Z";

   public HgBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
      String annotation;
      if (trimmedLine.indexOf(": ") > -1) {
         annotation = trimmedLine.substring(0, trimmedLine.indexOf(": ")).trim();
      } else {
         annotation = trimmedLine.substring(0, trimmedLine.lastIndexOf(":")).trim();
      }

      String author = annotation.substring(0, annotation.indexOf(32));
      annotation = annotation.substring(annotation.indexOf(32) + 1).trim();
      String revision = annotation.substring(0, annotation.indexOf(32));
      annotation = annotation.substring(annotation.indexOf(32) + 1).trim();
      Date dateTime = this.parseDate(annotation, (String)null, "EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
      this.lines.add(new BlameLine(dateTime, revision, author));
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
