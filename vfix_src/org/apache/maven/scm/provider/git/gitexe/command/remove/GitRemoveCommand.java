package org.apache.maven.scm.provider.git.gitexe.command.remove;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.remove.AbstractRemoveCommand;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.git.command.GitCommand;
import org.apache.maven.scm.provider.git.gitexe.command.GitCommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitRemoveCommand extends AbstractRemoveCommand implements GitCommand {
   protected ScmResult executeRemoveCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message) throws ScmException {
      if (fileSet.getFileList().isEmpty()) {
         throw new ScmException("You must provide at least one file/directory to remove");
      } else {
         Commandline cl = createCommandLine(fileSet.getBasedir(), fileSet.getFileList());
         GitRemoveConsumer consumer = new GitRemoveConsumer(this.getLogger());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         int exitCode = GitCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
         return exitCode != 0 ? new RemoveScmResult(cl.toString(), "The git command failed.", stderr.getOutput(), false) : new RemoveScmResult(cl.toString(), consumer.getRemovedFiles());
      }
   }

   public static Commandline createCommandLine(File workingDirectory, List<File> files) throws ScmException {
      Commandline cl = GitCommandLineUtils.getBaseGitCommandLine(workingDirectory, "rm");
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         if (file.isAbsolute()) {
            if (file.isDirectory()) {
               cl.createArg().setValue("-r");
               break;
            }
         } else {
            File absFile = new File(workingDirectory, file.getPath());
            if (absFile.isDirectory()) {
               cl.createArg().setValue("-r");
               break;
            }
         }
      }

      GitCommandLineUtils.addTarget(cl, files);
      return cl;
   }
}
