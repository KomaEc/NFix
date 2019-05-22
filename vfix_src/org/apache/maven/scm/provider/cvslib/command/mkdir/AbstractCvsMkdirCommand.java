package org.apache.maven.scm.provider.cvslib.command.mkdir;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.mkdir.AbstractMkdirCommand;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;

public abstract class AbstractCvsMkdirCommand extends AbstractMkdirCommand {
   protected MkdirScmResult executeMkdirCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean createInLocal) throws ScmException {
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.MESSAGE, message == null ? "" : message);
      parameters.setString(CommandParameter.BINARY, "false");
      Command cmd = this.getAddCommand();
      cmd.setLogger(this.getLogger());
      ScmResult addResult = cmd.execute(repository, fileSet, parameters);
      if (!addResult.isSuccess()) {
         return new MkdirScmResult(addResult.getCommandLine().toString(), "The cvs command failed.", addResult.getCommandOutput(), false);
      } else {
         List<ScmFile> addedFiles = new ArrayList();
         Iterator i$ = fileSet.getFileList().iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            ScmFile scmFile = new ScmFile(file.getPath(), ScmFileStatus.ADDED);
            addedFiles.add(scmFile);
         }

         return new MkdirScmResult(addResult.getCommandLine().toString(), addedFiles);
      }
   }

   protected abstract Command getAddCommand();
}
