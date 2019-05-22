package org.apache.maven.scm.provider.perforce.command.blame;

import java.io.File;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.blame.AbstractBlameCommand;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceBlameCommand extends AbstractBlameCommand implements PerforceCommand {
   public BlameScmResult executeBlameCommand(ScmProviderRepository repo, ScmFileSet workingDirectory, String filename) throws ScmException {
      PerforceScmProviderRepository p4repo = (PerforceScmProviderRepository)repo;
      String clientspec = PerforceScmProvider.getClientspecName(this.getLogger(), p4repo, workingDirectory.getBasedir());
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, workingDirectory.getBasedir(), filename, clientspec);
      PerforceBlameConsumer blameConsumer = new PerforceBlameConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         exitCode = CommandLineUtils.executeCommandLine(cl, blameConsumer, stderr);
      } catch (CommandLineException var16) {
         throw new ScmException("Error while executing command.", var16);
      }

      if (exitCode != 0) {
         return new BlameScmResult(cl.toString(), "The perforce command failed.", stderr.getOutput(), false);
      } else {
         cl = createFilelogCommandLine((PerforceScmProviderRepository)repo, workingDirectory.getBasedir(), filename, clientspec);
         PerforceFilelogConsumer filelogConsumer = new PerforceFilelogConsumer(this.getLogger());

         try {
            exitCode = CommandLineUtils.executeCommandLine(cl, filelogConsumer, stderr);
         } catch (CommandLineException var15) {
            throw new ScmException("Error while executing command.", var15);
         }

         if (exitCode != 0) {
            return new BlameScmResult(cl.toString(), "The perforce command failed.", stderr.getOutput(), false);
         } else {
            List<BlameLine> lines = blameConsumer.getLines();

            for(int i = 0; i < lines.size(); ++i) {
               BlameLine line = (BlameLine)lines.get(i);
               String revision = line.getRevision();
               line.setAuthor(filelogConsumer.getAuthor(revision));
               line.setDate(filelogConsumer.getDate(revision));
            }

            return new BlameScmResult(cl.toString(), lines);
         }
      }
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, String filename, String clientspec) {
      Commandline cl = PerforceScmProvider.createP4Command(repo, workingDirectory);
      if (clientspec != null) {
         cl.createArg().setValue("-c");
         cl.createArg().setValue(clientspec);
      }

      cl.createArg().setValue("annotate");
      cl.createArg().setValue("-q");
      cl.createArg().setValue(filename);
      return cl;
   }

   public static Commandline createFilelogCommandLine(PerforceScmProviderRepository repo, File workingDirectory, String filename, String clientspec) {
      Commandline cl = PerforceScmProvider.createP4Command(repo, workingDirectory);
      if (clientspec != null) {
         cl.createArg().setValue("-c");
         cl.createArg().setValue(clientspec);
      }

      cl.createArg().setValue("filelog");
      cl.createArg().setValue(filename);
      return cl;
   }
}
