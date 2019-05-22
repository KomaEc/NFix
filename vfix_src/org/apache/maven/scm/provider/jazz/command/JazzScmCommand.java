package org.apache.maven.scm.provider.jazz.command;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.repository.JazzScmProviderRepository;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class JazzScmCommand {
   private ScmLogger fLogger;
   private Commandline fCommand;

   public JazzScmCommand(String cmd, ScmProviderRepository repo, ScmFileSet fileSet, ScmLogger logger) {
      this(cmd, (String)null, repo, true, fileSet, logger);
   }

   public JazzScmCommand(String cmd, String subCmd, ScmProviderRepository repo, ScmFileSet fileSet, ScmLogger logger) {
      this(cmd, subCmd, repo, true, fileSet, logger);
   }

   public JazzScmCommand(String cmd, String subCmd, ScmProviderRepository repo, boolean addRepositoryWorkspaceArg, ScmFileSet fileSet, ScmLogger logger) {
      this.fLogger = logger;
      this.fCommand = new Commandline();
      this.fCommand.setExecutable("scm");
      if (fileSet != null) {
         this.fCommand.setWorkingDirectory(fileSet.getBasedir().getAbsolutePath());
         if (!this.fCommand.getWorkingDirectory().exists()) {
            boolean success = this.fCommand.getWorkingDirectory().mkdirs();
            if (!success) {
               this.logErrorMessage("Working directory did not exist and it couldn't be created: " + this.fCommand.getWorkingDirectory());
            }
         }
      }

      if (!StringUtils.isEmpty(cmd)) {
         this.addArgument(cmd);
      }

      if (!StringUtils.isEmpty(subCmd)) {
         this.addArgument(subCmd);
      }

      JazzScmProviderRepository jazzRepo = (JazzScmProviderRepository)repo;
      String user;
      if (addRepositoryWorkspaceArg) {
         user = jazzRepo.getRepositoryURI();
         if (!StringUtils.isEmpty(user)) {
            this.addArgument("--repository-uri");
            this.addArgument(jazzRepo.getRepositoryURI());
         }
      }

      user = jazzRepo.getUser();
      if (!StringUtils.isEmpty(user)) {
         this.addArgument("--username");
         this.addArgument(jazzRepo.getUser());
      }

      String password = jazzRepo.getPassword();
      if (!StringUtils.isEmpty(password)) {
         this.addArgument("--password");
         this.addArgument(jazzRepo.getPassword());
      }

   }

   public void addArgument(ScmFileSet fileSet) {
      this.logInfoMessage("files: " + fileSet.getBasedir().getAbsolutePath());
      Iterator iter = fileSet.getFileList().iterator();

      while(iter.hasNext()) {
         this.fCommand.createArg().setValue(((File)iter.next()).getPath());
      }

   }

   public void addArgument(String arg) {
      this.fCommand.createArg().setValue(arg);
   }

   public int execute(StreamConsumer out, ErrorConsumer err) throws ScmException {
      this.logInfoMessage("Executing: " + cryptPassword(this.fCommand));
      if (this.fCommand.getWorkingDirectory() != null) {
         this.logInfoMessage("Working directory: " + this.fCommand.getWorkingDirectory().getAbsolutePath());
      }

      boolean var3 = false;

      int status;
      try {
         status = CommandLineUtils.executeCommandLine(this.fCommand, out, err);
      } catch (CommandLineException var6) {
         String errorOutput = err.getOutput();
         if (errorOutput.length() > 0) {
            this.logErrorMessage("Error: " + err.getOutput());
         }

         throw new ScmException("Error while executing Jazz SCM command line - " + this.getCommandString(), var6);
      }

      String errorOutput = err.getOutput();
      if (errorOutput.length() > 0) {
         this.logErrorMessage("Error: " + err.getOutput());
      }

      return status;
   }

   public String getCommandString() {
      return this.fCommand.toString();
   }

   public Commandline getCommandline() {
      return this.fCommand;
   }

   private void logErrorMessage(String message) {
      if (this.fLogger != null) {
         this.fLogger.error(message);
      }

   }

   private void logInfoMessage(String message) {
      if (this.fLogger != null) {
         this.fLogger.info(message);
      }

   }

   private void logDebugMessage(String message) {
      if (this.fLogger != null) {
         this.fLogger.debug(message);
      }

   }

   public static String cryptPassword(Commandline cl) {
      String clString = cl.toString();
      int pos = clString.indexOf("--password");
      if (pos > 0) {
         String beforePassword = clString.substring(0, pos + "--password ".length());
         String afterPassword = clString.substring(pos + "--password ".length());
         pos = afterPassword.indexOf(32);
         if (pos > 0) {
            afterPassword = afterPassword.substring(pos);
         } else {
            afterPassword = "\"";
         }

         if (Os.isFamily("windows")) {
            clString = beforePassword + "*****" + afterPassword;
         } else {
            clString = beforePassword + "'*****'";
         }
      }

      return clString;
   }
}
