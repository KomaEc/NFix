package org.apache.maven.scm.provider.svn.svnexe.command.checkin;

import java.io.File;
import java.io.IOException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnCheckInCommand extends AbstractCheckInCommand implements SvnCommand {
   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, ScmVersion version) throws ScmException {
      if (version != null && StringUtils.isNotEmpty(version.getName())) {
         throw new ScmException("This provider command can't handle tags.");
      } else {
         File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

         try {
            FileUtils.fileWrite(messageFile.getAbsolutePath(), message);
         } catch (IOException var21) {
            return new CheckInScmResult((String)null, "Error while making a temporary file for the commit message: " + var21.getMessage(), (String)null, false);
         }

         Commandline cl = createCommandLine((SvnScmProviderRepository)repo, fileSet, messageFile);
         SvnCheckInConsumer consumer = new SvnCheckInConsumer(this.getLogger(), fileSet.getBasedir());
         CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
         if (this.getLogger().isInfoEnabled()) {
            this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
            this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
         }

         int exitCode;
         try {
            exitCode = SvnCommandLineUtils.execute(cl, (StreamConsumer)consumer, stderr, this.getLogger());
         } catch (CommandLineException var19) {
            throw new ScmException("Error while executing command.", var19);
         } finally {
            try {
               FileUtils.forceDelete(messageFile);
            } catch (IOException var18) {
            }

         }

         return exitCode != 0 ? new CheckInScmResult(cl.toString(), "The svn command failed.", stderr.getOutput(), false) : new CheckInScmResult(cl.toString(), consumer.getCheckedInFiles(), Integer.toString(consumer.getRevision()));
      }
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, ScmFileSet fileSet, File messageFile) throws ScmException {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(fileSet.getBasedir(), repository);
      cl.createArg().setValue("commit");
      cl.createArg().setValue("--file");
      cl.createArg().setValue(messageFile.getAbsolutePath());

      try {
         SvnCommandLineUtils.addTarget(cl, fileSet.getFileList());
         return cl;
      } catch (IOException var5) {
         throw new ScmException("Can't create the targets file", var5);
      }
   }
}
