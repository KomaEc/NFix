package org.apache.maven.scm.provider.perforce.command.tag;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.perforce.PerforceScmProvider;
import org.apache.maven.scm.provider.perforce.command.PerforceCommand;
import org.apache.maven.scm.provider.perforce.command.PerforceInfoCommand;
import org.apache.maven.scm.provider.perforce.repository.PerforceScmProviderRepository;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class PerforceTagCommand extends AbstractTagCommand implements PerforceCommand {
   private String actualRepoLocation = null;
   private static final String NEWLINE = "\r\n";

   protected ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet files, String tag, String message) throws ScmException {
      return this.executeTagCommand(repo, files, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet files, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      PerforceScmProviderRepository prepo = (PerforceScmProviderRepository)repo;
      this.actualRepoLocation = PerforceScmProvider.getRepoPath(this.getLogger(), prepo, files.getBasedir());
      PerforceTagConsumer consumer = new PerforceTagConsumer();
      this.createLabel(repo, files, tag, consumer, false);
      if (consumer.isSuccess()) {
         this.syncLabel(repo, files, tag, consumer);
      }

      if (consumer.isSuccess() && this.shouldLock()) {
         consumer = new PerforceTagConsumer();
         this.createLabel(repo, files, tag, consumer, true);
      }

      return consumer.isSuccess() ? new TagScmResult("p4 label -i", consumer.getTagged()) : new TagScmResult("p4 label -i", "Tag failed", consumer.getOutput(), false);
   }

   private boolean shouldLock() {
      return Boolean.valueOf(System.getProperty("maven.scm.locktag", "true"));
   }

   private void syncLabel(ScmProviderRepository repo, ScmFileSet files, String tag, PerforceTagConsumer consumer) {
      Commandline cl = createLabelsyncCommandLine((PerforceScmProviderRepository)repo, files.getBasedir(), files, tag);

      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing: " + cl.toString()));
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
      } catch (CommandLineException var10) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var10.getMessage(), var10);
         }
      }

   }

   private void createLabel(ScmProviderRepository repo, ScmFileSet files, String tag, PerforceTagConsumer consumer, boolean lock) {
      Commandline cl = createLabelCommandLine((PerforceScmProviderRepository)repo, files.getBasedir());
      DataOutputStream dos = null;
      InputStreamReader isReader = null;
      InputStreamReader isReaderErr = null;

      try {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug(PerforceScmProvider.clean("Executing: " + cl.toString()));
         }

         Process proc = cl.execute();
         OutputStream out = proc.getOutputStream();
         dos = new DataOutputStream(out);
         String label = this.createLabelSpecification((PerforceScmProviderRepository)repo, tag, lock);
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("LabelSpec: \r\n" + label);
         }

         dos.write(label.getBytes());
         dos.close();
         out.close();
         isReader = new InputStreamReader(proc.getInputStream());
         isReaderErr = new InputStreamReader(proc.getErrorStream());
         BufferedReader stdout = new BufferedReader(isReader);

         BufferedReader stderr;
         String line;
         for(stderr = new BufferedReader(isReaderErr); (line = stdout.readLine()) != null; consumer.consumeLine(line)) {
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Consuming stdout: " + line);
            }
         }

         for(; (line = stderr.readLine()) != null; consumer.consumeLine(line)) {
            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Consuming stderr: " + line);
            }
         }

         stderr.close();
         stdout.close();
      } catch (CommandLineException var20) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("CommandLineException " + var20.getMessage(), var20);
         }
      } catch (IOException var21) {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("IOException " + var21.getMessage(), var21);
         }
      } finally {
         IOUtil.close((OutputStream)dos);
         IOUtil.close((Reader)isReader);
         IOUtil.close((Reader)isReaderErr);
      }

   }

   public static Commandline createLabelCommandLine(PerforceScmProviderRepository repo, File workingDirectory) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("label");
      command.createArg().setValue("-i");
      return command;
   }

   public static Commandline createLabelsyncCommandLine(PerforceScmProviderRepository repo, File workingDirectory, ScmFileSet files, String tag) {
      Commandline command = PerforceScmProvider.createP4Command(repo, workingDirectory);
      command.createArg().setValue("labelsync");
      command.createArg().setValue("-l");
      command.createArg().setValue(tag);
      List<File> fs = files.getFileList();
      Iterator i$ = fs.iterator();

      while(i$.hasNext()) {
         File file = (File)i$.next();
         command.createArg().setValue(file.getPath());
      }

      return command;
   }

   public String createLabelSpecification(PerforceScmProviderRepository repo, String tag, boolean lock) {
      StringBuilder buf = new StringBuilder();
      buf.append("Label: ").append(tag).append("\r\n");
      buf.append("View: ").append(PerforceScmProvider.getCanonicalRepoPath(this.actualRepoLocation)).append("\r\n");
      String username = repo.getUser();
      if (username == null) {
         username = PerforceInfoCommand.getInfo(this.getLogger(), repo).getEntry("User name");
      }

      buf.append("Owner: ").append(username).append("\r\n");
      buf.append("Options: ").append(lock ? "" : "un").append("locked").append("\r\n");
      return buf.toString();
   }
}
