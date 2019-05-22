package org.apache.maven.scm.provider.jazz.command.blame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzBlameConsumer extends AbstractRepositoryConsumer {
   private static final String JAZZ_TIMESTAMP_PATTERN = "yyyy-MM-dd";
   private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+) (.*) \\((\\d+)\\) (\\d+-\\d+-\\d+) (.*)");
   private List<BlameLine> fLines = new ArrayList();
   private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

   public JazzBlameConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(repository, logger);
      this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      Matcher matcher = LINE_PATTERN.matcher(line);
      if (matcher.matches()) {
         String lineNumberStr = matcher.group(1);
         String owner = matcher.group(2);
         String changeSetNumberStr = matcher.group(3);
         String dateStr = matcher.group(4);
         Date date = this.parseDate(dateStr, "yyyy-MM-dd", (String)null);
         this.fLines.add(new BlameLine(date, changeSetNumberStr, owner));
      }

   }

   public List<BlameLine> getLines() {
      return this.fLines;
   }
}
