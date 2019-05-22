package org.apache.maven.scm.provider.perforce.command.changelog;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class PerforceChangesConsumer extends AbstractConsumer {
   private List<String> entries = new ArrayList();
   private static final Pattern PATTERN = Pattern.compile("^Change (\\d+) on (.*) by (.*)@");

   public PerforceChangesConsumer(ScmLogger logger) {
      super(logger);
   }

   public List<String> getChanges() throws ScmException {
      return this.entries;
   }

   public void consumeLine(String line) {
      Matcher matcher = PATTERN.matcher(line);
      if (matcher.find()) {
         this.entries.add(matcher.group(1));
      }

   }
}
