package org.apache.maven.scm.provider.jazz.command.changelog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzHistoryConsumer extends AbstractRepositoryConsumer {
   private static final Pattern CHANGESET_PATTERN = Pattern.compile("\\((\\d+)\\) (.*)");
   private List<ChangeSet> entries;

   public JazzHistoryConsumer(ScmProviderRepository repo, ScmLogger logger, List<ChangeSet> entries) {
      super(repo, logger);
      this.entries = entries;
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      Matcher matcher = CHANGESET_PATTERN.matcher(line);
      if (matcher.find()) {
         String changesetAlias = matcher.group(1);
         ChangeSet changeSet = new ChangeSet();
         changeSet.setRevision(changesetAlias);
         this.entries.add(changeSet);
      }

   }
}
