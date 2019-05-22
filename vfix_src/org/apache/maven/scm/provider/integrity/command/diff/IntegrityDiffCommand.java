package org.apache.maven.scm.provider.integrity.command.diff;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.APISession;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class IntegrityDiffCommand extends AbstractDiffCommand {
   public DiffScmResult executeDiffCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion startRevision, ScmVersion endRevision) throws ScmException {
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      APISession api = iRepo.getAPISession();
      this.getLogger().info("Showing differences bettween local files in " + fileSet.getBasedir().getAbsolutePath() + " and server project " + iRepo.getConfigruationPath());
      Commandline shell = new Commandline();
      shell.setWorkingDirectory(fileSet.getBasedir());
      shell.setExecutable("si");
      shell.createArg().setValue("diff");
      shell.createArg().setValue("--hostname=" + api.getHostName());
      shell.createArg().setValue("--port=" + api.getPort());
      shell.createArg().setValue("--user=" + api.getUserName());
      shell.createArg().setValue("-R");
      shell.createArg().setValue("--filter=changed:all");
      shell.createArg().setValue("--filter=format:text");
      IntegrityDiffConsumer shellConsumer = new IntegrityDiffConsumer(this.getLogger());

      try {
         this.getLogger().debug("Executing: " + shell.getCommandline());
         int exitCode = CommandLineUtils.executeCommandLine(shell, shellConsumer, new CommandLineUtils.StringStreamConsumer());
         boolean success = exitCode != 128;
         ScmResult scmResult = new ScmResult(shell.getCommandline().toString(), "", "Exit Code: " + exitCode, success);
         return new DiffScmResult(new ArrayList(), new HashMap(), "", scmResult);
      } catch (CommandLineException var13) {
         this.getLogger().error("Command Line Exception: " + var13.getMessage());
         DiffScmResult result = new DiffScmResult(shell.getCommandline().toString(), var13.getMessage(), "", false);
         return result;
      }
   }
}
