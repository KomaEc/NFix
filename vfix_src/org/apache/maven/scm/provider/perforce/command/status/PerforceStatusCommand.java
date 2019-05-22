package org.apache.maven.scm.provider.perforce.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.status.AbstractStatusCommand;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.command.PerforceVerbMapper;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceStatusCommand extends AbstractStatusCommand implements PerforceCommand {
   private String actualLocation;

   protected StatusScmResult executeStatusCommand(ScmProviderRepository repo, ScmFileSet files) throws ScmException {
      PerforceScmProviderRepository prepo = (PerforceScmProviderRepository)repo;
      this.actualLocation = PerforceScmProvider.getRepoPath(this.getLogger(), prepo, files.getBasedir());
      PerforceStatusConsumer consumer = new PerforceStatusConsumer();
      Commandline command = this.readOpened(prepo, files, consumer);
      if (consumer.isSuccess()) {
         List<ScmFile> scmfiles = createResults(this.actualLocation, consumer);
         return new StatusScmResult(command.toString(), scmfiles);
      } else {
         return new StatusScmResult(command.toString(), "Unable to get status", consumer.getOutput(), consumer.isSuccess());
      }
   }

   public static List<ScmFile> createResults(String repoPath, PerforceStatusConsumer consumer) {
      List<ScmFile> results = new ArrayList();
      List<String> files = consumer.getDepotfiles();
      Pattern re = Pattern.compile("([^#]+)#\\d+ - ([^ ]+) .*");
      Iterator it = files.iterator();

      while(it.hasNext()) {
         String filepath = (String)it.next();
         Matcher matcher = re.matcher(filepath);
         if (!matcher.matches()) {
            System.err.println("Skipping " + filepath);
         } else {
            String path = matcher.group(1);
            String verb = matcher.group(2);
            ScmFile scmfile = new ScmFile(path.substring(repoPath.length() + 1).trim(), PerforceVerbMapper.toStatus(verb));
            results.add(scmfile);
         }
      }

      return results;
   }

   private Commandline readOpened(PerforceScmProviderRepository prepo, ScmFileSet files, PerforceStatusConsumer consumer) {
      Commandline cl = createOpenedCommandLine(prepo, files.getBasedir(), this.actualLocation);

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

      return cl;
   }

   public static Commandline createOpenedCommandLine(PerforceScmProviderRepository repo, File workingDirectory, String location) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("opened");
      command.createArg().setValue(PerforceScmProvider.getCanonicalRepoPath(location));
      return command;
   }
}
