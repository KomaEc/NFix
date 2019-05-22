package org.apache.maven.scm.provider.git.gitexe.command.remoteinfo;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitRemoteInfoConsumer implements StreamConsumer {
   private static final Pattern BRANCH_PATTERN = Pattern.compile("^(.*)\\s+refs/heads/(.*)");
   private static final Pattern TAGS_PATTERN = Pattern.compile("^(.*)\\s+refs/tags/(.*)");
   private ScmLogger logger;
   private RemoteInfoScmResult remoteInfoScmResult;

   public GitRemoteInfoConsumer(ScmLogger logger, String commandLine) {
      this.logger = logger;
      this.remoteInfoScmResult = new RemoteInfoScmResult(commandLine, new HashMap(), new HashMap());
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      Matcher matcher = BRANCH_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.remoteInfoScmResult.getBranches().put(matcher.group(2), matcher.group(1));
      }

      matcher = TAGS_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.remoteInfoScmResult.getTags().put(matcher.group(2), matcher.group(1));
      }

   }

   public RemoteInfoScmResult getRemoteInfoScmResult() {
      return this.remoteInfoScmResult;
   }
}
