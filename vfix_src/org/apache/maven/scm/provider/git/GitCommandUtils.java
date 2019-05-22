package org.apache.maven.scm.provider.git;

import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.provider.git.repository.GitScmProviderRepository;
import org.apache.maven.scm.provider.git.util.GitUtil;
import org.apache.maven.scm.providers.gitlib.settings.Settings;
import org.codehaus.plexus.util.cli.Commandline;

public class GitCommandUtils {
   private GitCommandUtils() {
   }

   public static Commandline getBaseCommand(String commandName, GitScmProviderRepository repo, ScmFileSet fileSet) {
      return getBaseCommand(commandName, repo, fileSet, (String)null);
   }

   public static Commandline getBaseCommand(String commandName, GitScmProviderRepository repo, ScmFileSet fileSet, String options) {
      Settings settings = GitUtil.getSettings();
      Commandline cl = new Commandline();
      cl.setExecutable("git");
      cl.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());
      if (settings.getTraceGitCommand() != null) {
         cl.addEnvironment("GIT_TRACE", settings.getTraceGitCommand());
      }

      cl.createArg().setLine(options);
      cl.createArg().setValue(commandName);
      return cl;
   }

   public static String getRevParseDateFormat() {
      return GitUtil.getSettings().getRevParseDateFormat();
   }
}
