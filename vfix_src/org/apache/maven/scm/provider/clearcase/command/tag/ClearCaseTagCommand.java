package org.apache.maven.scm.provider.clearcase.command.tag;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.clearcase.command.ClearCaseCommand;
import org.apache.maven.scm.provider.clearcase.command.checkin.ClearCaseCheckInConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class ClearCaseTagCommand extends AbstractTagCommand implements ClearCaseCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(scmProviderRepository, fileSet, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("executing tag command...");
      }

      Commandline cl = createCommandLine(fileSet, tag);
      ClearCaseCheckInConsumer consumer = new ClearCaseCheckInConsumer(this.getLogger());
      CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

      int exitCode;
      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Creating label: " + tag);
         }

         Commandline newLabelCommandLine = createNewLabelCommandLine(fileSet, tag);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Executing: " + newLabelCommandLine.getWorkingDirectory().getAbsolutePath() + ">>" + newLabelCommandLine.toString());
         }

         exitCode = CommandLineUtils.executeCommandLine(newLabelCommandLine, new CommandLineUtils.StringStreamConsumer(), stderr);
         if (exitCode == 0) {
            this.getLogger().debug("Executing: " + cl.getWorkingDirectory().getAbsolutePath() + ">>" + cl.toString());
            exitCode = CommandLineUtils.executeCommandLine(cl, consumer, stderr);
         }
      } catch (CommandLineException var10) {
         throw new ScmException("Error while executing clearcase command.", var10);
      }

      return exitCode != 0 ? new TagScmResult(cl.toString(), "The cleartool command failed.", stderr.getOutput(), false) : new TagScmResult(cl.toString(), consumer.getCheckedInFiles());
   }

   public static Commandline createCommandLine(ScmFileSet scmFileSet, String tag) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("mklabel");
      List<File> files = scmFileSet.getFileList();
      if (files.isEmpty()) {
         command.createArg().setValue("-recurse");
      }

      command.createArg().setValue(tag);
      if (files.size() > 0) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            command.createArg().setValue(file.getName());
         }
      } else {
         command.createArg().setValue(".");
      }

      return command;
   }

   private static Commandline createNewLabelCommandLine(ScmFileSet scmFileSet, String tag) {
      Commandline command = new Commandline();
      File workingDirectory = scmFileSet.getBasedir();
      command.setWorkingDirectory(workingDirectory.getAbsolutePath());
      command.setExecutable("cleartool");
      command.createArg().setValue("mklbtype");
      command.createArg().setValue("-nc");
      command.createArg().setValue(tag);
      return command;
   }
}
