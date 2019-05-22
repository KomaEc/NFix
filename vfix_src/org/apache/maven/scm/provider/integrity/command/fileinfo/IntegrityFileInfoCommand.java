package org.apache.maven.scm.provider.integrity.command.fileinfo;

import java.io.File;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.fileinfo.AbstractFileInfoCommand;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.APISession;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class IntegrityFileInfoCommand extends AbstractFileInfoCommand {
   public ScmResult executeFileInfoCommand(ScmProviderRepository repository, File workingDirectory, String filename) throws ScmException {
      this.getLogger().info("Attempting to display scm file information for file: " + filename);
      if (null != filename && filename.length() != 0) {
         IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
         APISession api = iRepo.getAPISession();
         Commandline shell = new Commandline();
         shell.setWorkingDirectory(workingDirectory);
         shell.setExecutable("si");
         shell.createArg().setValue("memberinfo");
         shell.createArg().setValue("--hostname=" + api.getHostName());
         shell.createArg().setValue("--port=" + api.getPort());
         shell.createArg().setValue("--user=" + api.getUserName());
         shell.createArg().setValue('"' + filename + '"');
         IntegrityFileInfoConsumer shellConsumer = new IntegrityFileInfoConsumer(this.getLogger());

         ScmResult result;
         try {
            this.getLogger().debug("Executing: " + shell.getCommandline());
            int exitCode = CommandLineUtils.executeCommandLine(shell, shellConsumer, new CommandLineUtils.StringStreamConsumer());
            boolean success = exitCode != 128;
            result = new ScmResult(shell.getCommandline().toString(), "", "Exit Code: " + exitCode, success);
         } catch (CommandLineException var11) {
            this.getLogger().error("Command Line Exception: " + var11.getMessage());
            result = new ScmResult(shell.getCommandline().toString(), var11.getMessage(), "", false);
         }

         return result;
      } else {
         throw new ScmException("A single filename is required to execute the fileinfo command!");
      }
   }

   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return this.executeFileInfoCommand(repository, fileSet.getBasedir(), parameters.getString(CommandParameter.FILE));
   }
}
