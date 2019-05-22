package org.apache.maven.scm.provider.accurev.cli;

import java.util.regex.Pattern;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

final class ErrorConsumer implements StreamConsumer {
   private static final Pattern[] SKIPPED_WARNINGS = new Pattern[]{Pattern.compile(".*replica sync on the master server.*"), Pattern.compile("No elements selected.*"), Pattern.compile("You are not in a directory.*"), Pattern.compile("Note.*"), Pattern.compile("\\s+(members,|conjunction).*")};
   private final ScmLogger logger;
   private final StringBuilder errors;

   public ErrorConsumer(ScmLogger logger, StringBuilder errors) {
      this.logger = logger;
      this.errors = errors;
   }

   public void consumeLine(String line) {
      this.errors.append(line);
      this.errors.append('\n');
      boolean matched = false;

      for(int i = this.logger.isDebugEnabled() ? SKIPPED_WARNINGS.length : 0; !matched && i < SKIPPED_WARNINGS.length; matched = SKIPPED_WARNINGS[i++].matcher(line).matches()) {
      }

      if (!matched) {
         this.logger.warn(line);
      }

   }
}
