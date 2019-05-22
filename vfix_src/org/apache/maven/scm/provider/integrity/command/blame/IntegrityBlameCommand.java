package org.apache.maven.scm.provider.integrity.command.blame;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.APISession;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class IntegrityBlameCommand extends AbstractBlameCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repository, ScmFileSet workingDirectory, String filename) throws ScmException {
      this.getLogger().info("Attempting to display blame results for file: " + filename);
      if (null != filename && filename.length() != 0) {
         IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
         APISession api = iRepo.getAPISession();
         Commandline shell = new Commandline();
         shell.setWorkingDirectory(workingDirectory.getBasedir());
         shell.setExecutable("si");
         shell.createArg().setValue("annotate");
         shell.createArg().setValue("--hostname=" + api.getHostName());
         shell.createArg().setValue("--port=" + api.getPort());
         shell.createArg().setValue("--user=" + api.getUserName());
         shell.createArg().setValue("--fields=date,revision,author");
         shell.createArg().setValue('"' + filename + '"');
         IntegrityBlameConsumer shellConsumer = new IntegrityBlameConsumer(this.getLogger());

         try {
            this.getLogger().debug("Executing: " + shell.getCommandline());
            int exitCode = CommandLineUtils.executeCommandLine(shell, shellConsumer, new CommandLineUtils.StringStreamConsumer());
            boolean success = exitCode != 128;
            ScmResult scmResult = new ScmResult(shell.getCommandline().toString(), "", "Exit Code: " + exitCode, success);
            return new BlameScmResult(shellConsumer.getBlameList(), scmResult);
         } catch (CommandLineException var12) {
            this.getLogger().error("Command Line Exception: " + var12.getMessage());
            BlameScmResult result = new BlameScmResult(shell.getCommandline().toString(), var12.getMessage(), "", false);
            return result;
         }
      } else {
         throw new ScmException("A single filename is required to execute the blame command!");
      }
   }
}
