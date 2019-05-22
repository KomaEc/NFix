package org.apache.maven.scm.provider.tfs.command;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class TfsCommand {
   private ScmLogger logger;
   private Commandline command = new Commandline();

   public TfsCommand(String cmd, ScmProviderRepository r, ScmFileSet f, ScmLogger logger) {
      this.command.setExecutable("tf");
      if (f != null) {
         this.command.setWorkingDirectory(f.getBasedir().getAbsolutePath());
      }

      this.command.createArg().setValue(cmd);
      if (r.getUser() != null) {
         this.command.createArg().setValue("-login:" + r.getUser() + "," + r.getPassword());
      }

      this.logger = logger;
   }

   public void addArgument(ScmFileSet f) {
      this.info("files: " + f.getBasedir().getAbsolutePath());
      Iterator iter = f.getFileList().iterator();

      while(iter.hasNext()) {
         this.command.createArg().setValue(((File)iter.next()).getPath());
      }

   }

   public void addArgument(String s) {
      this.command.createArg().setValue(s);
   }

   public int execute(StreamConsumer out, ErrorStreamConsumer err) throws ScmException {
      this.info("Command line - " + this.getCommandString());

      int status;
      try {
         status = CommandLineUtils.executeCommandLine(this.command, out, err);
      } catch (CommandLineException var7) {
         throw new ScmException("Error while executing TFS command line - " + this.getCommandString(), var7);
      }

      this.info("err - " + err.getOutput());
      if (out instanceof CommandLineUtils.StringStreamConsumer) {
         CommandLineUtils.StringStreamConsumer sc = (CommandLineUtils.StringStreamConsumer)out;
         this.debug(sc.getOutput());
      }

      if (out instanceof FileListConsumer) {
         FileListConsumer f = (FileListConsumer)out;
         Iterator i = f.getFiles().iterator();

         while(i.hasNext()) {
            ScmFile file = (ScmFile)i.next();
            this.debug(file.getPath());
         }
      }

      return status;
   }

   public String getCommandString() {
      return this.command.toString();
   }

   public Commandline getCommandline() {
      return this.command;
   }

   private void info(String message) {
      if (this.logger != null) {
         this.logger.info(message);
      }

   }

   private void debug(String message) {
      if (this.logger != null) {
         this.logger.debug(message);
      }

   }
}
