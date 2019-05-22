package org.apache.maven.scm.provider.jazz.command.diff;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.diff.AbstractDiffCommand;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.JazzScmCommand;
import org.apache.maven.scm.provider.jazz.command.consumer.DebugLoggerConsumer;
import org.apache.maven.scm.provider.jazz.command.consumer.ErrorConsumer;
import org.apache.maven.scm.provider.jazz.command.status.JazzStatusCommand;

public class JazzDiffCommand extends AbstractDiffCommand {
   protected DiffScmResult executeDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, ScmVersion startRevision, ScmVersion endRevision) throws ScmException {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Executing diff command...");
      }

      File baseDir = fileSet.getBasedir();
      File parentFolder = baseDir.getParentFile() != null ? baseDir.getParentFile() : baseDir;
      JazzStatusCommand statusCmd = new JazzStatusCommand();
      statusCmd.setLogger(this.getLogger());
      StatusScmResult statusCmdResult = statusCmd.executeStatusCommand(repo, fileSet);
      List<ScmFile> statusScmFiles = statusCmdResult.getChangedFiles();
      JazzScmCommand diffCmd = null;
      StringBuilder patch = new StringBuilder();
      Map<String, CharSequence> differences = new HashMap();
      Iterator i$ = statusScmFiles.iterator();

      while(true) {
         ScmFile file;
         do {
            if (!i$.hasNext()) {
               return new DiffScmResult(diffCmd.toString(), statusCmdResult.getChangedFiles(), differences, patch.toString());
            }

            file = (ScmFile)i$.next();
         } while(file.getStatus() != ScmFileStatus.MODIFIED);

         File fullPath = new File(parentFolder, file.getPath());
         String relativePath = fullPath.toString().substring(baseDir.toString().length());
         this.getLogger().debug("Full Path     : '" + fullPath + "'");
         this.getLogger().debug("Relative Path : '" + relativePath + "'");
         DebugLoggerConsumer diffConsumer = new DebugLoggerConsumer(this.getLogger());
         ErrorConsumer errConsumer = new ErrorConsumer(this.getLogger());
         diffCmd = this.createDiffCommand(repo, fileSet, relativePath);
         int status = diffCmd.execute(diffConsumer, errConsumer);
         if (status != 0 || errConsumer.hasBeenFed()) {
            return new DiffScmResult(diffCmd.toString(), "The scm diff command failed.", errConsumer.getOutput(), false);
         }

         patch.append(diffConsumer.getOutput());
         differences.put(relativePath, diffConsumer.getOutput());
      }
   }

   public JazzScmCommand createDiffCommand(ScmProviderRepository repo, ScmFileSet fileSet, String relativePath) {
      JazzScmCommand command = new JazzScmCommand("diff", repo, fileSet, this.getLogger());
      command.addArgument("file");
      command.addArgument(relativePath);
      return command;
   }
}
