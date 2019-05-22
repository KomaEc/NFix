package org.apache.maven.scm.provider.vss.commands.tag;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.vss.commands.VssCommandLineUtils;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class VssTagCommand extends AbstractTagCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tagName, ScmTagParameters scmTagParameters) throws ScmException {
      return this.executeTagCommand(repository, fileSet, tagName, scmTagParameters == null ? "" : scmTagParameters.getMessage());
   }

   protected ScmResult executeTagCommand(ScmProviderRepository repository, ScmFileSet fileSet, String tagName, String message) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing tag command...");
      }

      VssScmProviderRepository repo = (VssScmProviderRepository)repository;
      Commandline cl = this.buildCmdLine(repo, fileSet, tagName, message);
      VssTagConsumer consumer = new VssTagConsumer(repo, this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
      }

      int exitCode = VssCommandLineUtils.executeCommandline(cl, consumer, stderr, this.getLogger());
      if (exitCode != 0) {
         String error = stderr.getOutput();
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("VSS returns error: [" + error + "] return code: [" + exitCode + "]");
         }

         if (error.indexOf("A writable copy of") < 0) {
            return new TagScmResult(cl.toString(), "The vss command failed.", error, false);
         }

         if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn(error);
         }
      }

      return new TagScmResult(cl.toString(), consumer.getUpdatedFiles());
   }

   public Commandline buildCmdLine(VssScmProviderRepository repo, ScmFileSet fileSet, String tagName, String message) throws ScmException {
      Commandline command = new Commandline();
      command.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());

      try {
         command.addSystemEnvironment();
      } catch (Exception var7) {
         throw new ScmException("Can't add system environment.", var7);
      }

      command.addEnvironment("SSDIR", repo.getVssdir());
      String ssDir = VssCommandLineUtils.getSsDir();
      command.setExecutable(ssDir + "ss");
      command.createArg().setValue("Label");
      command.createArg().setValue("$" + repo.getProject());
      if (repo.getUserPassword() != null) {
         command.createArg().setValue("-Y" + repo.getUserPassword());
      }

      command.createArg().setValue("-I-");
      command.createArg().setValue("-L" + tagName);
      return command;
   }
}
