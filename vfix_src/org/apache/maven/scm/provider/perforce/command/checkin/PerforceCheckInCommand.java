package org.apache.maven.scm.provider.perforce.command.checkin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceCheckInCommand extends AbstractCheckInCommand implements PerforceCommand {
   private static final String NEWLINE = "\r\n";

   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository repo, ScmFileSet files, String message, ScmVersion version) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir());
      PerforceCheckInConsumer consumer = new PerforceCheckInConsumer();

      try {
         String jobs = System.getProperty("maven.scm.jobs");
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing " + cl.toString()));
         }

         PerforceScmProviderRepository prepo = (PerforceScmProviderRepository)repo;
         String changes = createChangeListSpecification(prepo, files, message, PerforceScmProvider.getRepoPath(this.getLogger(), prepo, files.getBasedir()), jobs);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Sending changelist:\n" + changes);
         }

         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, new ByteArrayInputStream(changes.getBytes()), consumer, err);
         if (exitCode != 0) {
            String cmdLine = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + cmdLine);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var14) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var14.getMessage(), var14);
         }
      }

      return new CheckInScmResult(cl.toString(), consumer.isSuccess() ? "Checkin successful" : "Unable to submit", consumer.getOutput(), consumer.isSuccess());
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("submit");
      command.createArg().setValue("-i");
      return command;
   }

   public static String createChangeListSpecification(PerforceScmProviderRepository repo, ScmFileSet files, String msg, String canonicalPath, String jobs) {
      StringBuilder buf = new StringBuilder();
      buf.append("Change: new").append("\r\n").append("\r\n");
      buf.append("Description:").append("\r\n").append("\t").append(msg).append("\r\n").append("\r\n");
      if (jobs != null && jobs.length() != 0) {
         buf.append("Jobs:").append("\r\n").append("\t").append(jobs).append("\r\n").append("\r\n");
      }

      buf.append("Files:").append("\r\n");

      try {
         Set<String> dupes = new HashSet();
         File workingDir = files.getBasedir();
         String candir = workingDir.getCanonicalPath();
         List<File> fs = files.getFileList();

         for(int i = 0; i < fs.size(); ++i) {
            File file = null;
            if (((File)fs.get(i)).isAbsolute()) {
               file = new File(((File)fs.get(i)).getPath());
            } else {
               file = new File(workingDir, ((File)fs.get(i)).getPath());
            }

            String canfile = file.getCanonicalPath();
            if (dupes.contains(canfile)) {
               System.err.println("Skipping duplicate file: " + file);
            } else {
               dupes.add(canfile);
               if (canfile.startsWith(candir)) {
                  canfile = canfile.substring(candir.length() + 1);
               }

               buf.append("\t").append(canonicalPath).append("/").append(canfile.replace('\\', '/')).append("\r\n");
            }
         }
      } catch (IOException var13) {
         var13.printStackTrace();
      }

      return buf.toString();
   }
}
