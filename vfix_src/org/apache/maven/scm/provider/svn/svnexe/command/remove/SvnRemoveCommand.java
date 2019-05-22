package org.apache.maven.scm.provider.svn.svnexe.command.remove;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnRemoveCommand extends AbstractRemoveCommand implements SvnCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("You must provide at least one file/directory to remove");
      } else {
         Commandline cl = createCommandLine(fileSet.getBasedir(), fileSet.getFileList());
         SvnRemoveConsumer consumer = new SvnRemoveConsumer(this.getLogger());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
            this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
         }

         int exitCode;
         try {
            exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
         } catch (CommandLineException var9) {
            throw new ScmException("Error while executing command.", var9);
         }

         return exitCode != 0 ? new RemoveScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new RemoveScmResult(cl.toString(), consumer.getRemovedFiles());
      }
   }

   private static Commandline createCommandLine(File workingDirectory, List<File> files) throws ScmException {
      Commandline cl = new Commandline();
      cl.setExecutable("svn");
      cl.setWorkingDirectory(workingDirectory.getAbsolutePath());
      cl.createArg().setValue("remove");

      try {
         SvnCommandLineUtils.addTarget(cl, files);
         return cl;
      } catch (IOException var4) {
         throw new ScmException("Can't create the targets file", var4);
      }
   }
}
