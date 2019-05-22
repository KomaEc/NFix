package org.apache.maven.scm.provider.perforce.command.edit;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.edit.AbstractEditCommand;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceEditCommand extends AbstractEditCommand implements PerforceCommand {
   protected ScmResult executeEditCommand(ScmProviderRepository repo, ScmFileSet files) throws ScmException {
      Commandline cl = createCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), files);
      PerforceEditConsumer consumer = new PerforceEditConsumer();

      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing " + cl.toString()));
         }

         CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();
         int exitCode = CommandLineUtils.executeCommandLine(cl, consumer, err);
         if (exitCode != 0) {
            String cmdLine = CommandLineUtils.toString(cl.getCommandline());
            StringBuilder msg = new StringBuilder("Exit code: " + exitCode + " - " + err.getOutput());
            msg.append('\n');
            msg.append("Command line was:" + cmdLine);
            throw new CommandLineException(msg.toString());
         }
      } catch (CommandLineException var9) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var9.getMessage(), var9);
         }
      }

      return consumer.isSuccess() ? new EditScmResult(cl.toString(), consumer.getEdits()) : new EditScmResult(cl.toString(), "Unable to edit file(s)", consumer.getErrorMessage(), false);
   }

   public static Commandline createCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmFileSet files) throws ScmException {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("edit");

      try {
         String candir = workingDirectory.getCanonicalPath();

         String canfile;
         for(Iterator i$ = files.getFileList().iterator(); i$.hasNext(); command.createArg().setValue(canfile)) {
            File f = (File)i$.next();
            File file = null;
            if (f.isAbsolute()) {
               file = new File(f.getPath());
            } else {
               file = new File(workingDirectory, f.getPath());
            }

            canfile = file.getCanonicalPath();
            if (canfile.startsWith(candir)) {
               canfile = canfile.substring(candir.length() + 1);
            }
         }

         return command;
      } catch (IOException var9) {
         throw new ScmException(var9.getMessage(), var9);
      }
   }
}
