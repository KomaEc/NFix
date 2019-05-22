package org.apache.maven.scm.provider.git.gitexe.command.update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;
import org.codehaus.plexus.util.StringUtils;

public class GitLatestRevisionCommandConsumer extends AbstractConsumer {
   private static final Pattern LATESTREV_PATTERN = Pattern.compile("^commit \\s*(.*)");
   private String latestRevision;

   public GitLatestRevisionCommandConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("GitLatestRevisionCommandConsumer consumeLine : " + line);
      }

      if (line != null && !StringUtils.isEmpty(line)) {
         this.processGetLatestRevision(line);
      }
   }

   public String getLatestRevision() {
      return this.latestRevision;
   }

   private void processGetLatestRevision(String line) {
      Matcher matcher = LATESTREV_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.latestRevision = matcher.group(1);
      }

   }
}
