package org.apache.maven.scm.provider.starteam.command.tag;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.starteam.command.StarteamCommand;
import org.apache.maven.scm.provider.starteam.command.StarteamCommandLineUtils;
import org.apache.maven.scm.provider.starteam.repository.StarteamScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class StarteamTagCommand extends AbstractTagCommand implements StarteamCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(repo, fileSet, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("This provider doesn't support tagging subsets of a directory");
      } else if (tag != null && tag.trim().length() != 0) {
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Working directory: " + fileSet.getBasedir().getAbsolutePath());
         }

         StarteamScmProviderRepository repository = (StarteamScmProviderRepository)repo;
         StarteamTagConsumer consumer = new StarteamTagConsumer(this.getLogger());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         Commandline cl = createCommandLine(repository, fileSet.getBasedir(), tag);
         int exitCode = StarteamCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
         return exitCode != 0 ? new TagScmResult(cl.toString(), "The starteam command failed.", stderr.getOutput(), false) : new TagScmResult(cl.toString(), consumer.getTaggedFiles());
      } else {
         throw new ScmException("tag must be specified");
      }
   }

   public static Commandline createCommandLine(StarteamScmProviderRepository repo, File workingDirectory, String tag) throws ScmException {
      Commandline cl = StarteamCommandLineUtils.createStarteamBaseCommandLine("label", repo);
      cl.createArg().setValue("-p");
      cl.createArg().setValue(repo.getFullUrl());
      cl.createArg().setValue("-nl");
      cl.createArg().setValue(tag);
      cl.createArg().setValue("-b");
      return cl;
   }
}
