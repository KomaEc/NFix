package org.apache.maven.scm.provider.jazz.command.add;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.command.status.JazzStatusCommand;

public class JazzAddCommand extends AbstractAddCommand {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing add command...");
      }

      return this.executeAddCommand(repo, fileSet);
   }

   public AddScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet) throws ScmException {
      File baseDir = fileSet.getBasedir();
      File parentFolder = baseDir.getParentFile() != null ? baseDir.getParentFile() : baseDir;
      List<ScmFile> changedScmFiles = new ArrayList();
      List<File> changedFiles = new ArrayList();
      List<ScmFile> commitedFiles = new ArrayList();
      JazzStatusCommand statusCmd = new JazzStatusCommand();
      statusCmd.setLogger(this.getLogger());
      StatusScmResult statusCmdResult = statusCmd.executeStatusCommand(repo, fileSet);
      List<ScmFile> statusScmFiles = statusCmdResult.getChangedFiles();
      Iterator i$ = statusScmFiles.iterator();

      while(true) {
         ScmFile file;
         do {
            if (!i$.hasNext()) {
               List<File> files = fileSet.getFileList();
               if (files.size() != 0) {
                  Iterator i$ = files.iterator();

                  while(i$.hasNext()) {
                     File file = (File)i$.next();
                     if (this.fileExistsInFileList(file, changedFiles)) {
                        commitedFiles.add(new ScmFile(file.getPath(), ScmFileStatus.CHECKED_IN));
                     }
                  }
               }

               JazzAddConsumer addConsumer = new JazzAddConsumer(repo, this.getLogger());
               ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
               JazzScmCommand command = this.createAddCommand(repo, fileSet);
               int status = command.execute(addConsumer, errConsumer);
               if (status == 0 && !errConsumer.hasBeenFed()) {
                  return new AddScmResult(command.getCommandString(), addConsumer.getFiles());
               }

               return new AddScmResult(command.getCommandString(), "Error code for Jazz SCM add (checkin) command - " + status, errConsumer.getOutput(), false);
            }

            file = (ScmFile)i$.next();
            this.getLogger().debug("Iterating over statusScmFiles: " + file);
         } while(file.getStatus() != ScmFileStatus.ADDED && file.getStatus() != ScmFileStatus.DELETED && file.getStatus() != ScmFileStatus.MODIFIED);

         changedScmFiles.add(new ScmFile(file.getPath(), ScmFileStatus.CHECKED_IN));
         changedFiles.add(new File(parentFolder, file.getPath()));
      }
   }

   public JazzScmCommand createAddCommand(ScmProviderRepository repo, ScmFileSet fileSet) {
      JazzScmCommand command = new JazzScmCommand("checkin", (String)null, repo, false, fileSet, this.getLogger());
      List<File> files = fileSet.getFileList();
      if (files != null && !files.isEmpty()) {
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            command.addArgument(file.getPath());
         }
      } else {
         command.addArgument(".");
      }

      return command;
   }

   private boolean fileExistsInFileList(File file, List<File> fileList) {
      boolean exists = false;
      Iterator i$ = fileList.iterator();

      while(i$.hasNext()) {
         File changedFile = (File)i$.next();
         if (changedFile.compareTo(file) == 0) {
            exists = true;
            break;
         }
      }

      return exists;
   }
}
