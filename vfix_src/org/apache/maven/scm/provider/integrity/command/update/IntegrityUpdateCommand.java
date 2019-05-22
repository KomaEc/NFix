package org.apache.maven.scm.provider.integrity.command.update;

import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogCommand;
import org.apache.maven.scm.command.update.AbstractUpdateCommand;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.ExceptionHandler;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.command.changelog.IntegrityChangeLogCommand;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityUpdateCommand extends AbstractUpdateCommand {
   public UpdateScmResult executeUpdateCommand(ScmProviderRepository repository, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      this.getLogger().info("Attempting to synchronize sandbox in " + fileSet.getBasedir().getAbsolutePath());
      List<ScmFile> updatedFiles = new ArrayList();
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      Sandbox siSandbox = iRepo.getSandbox();

      try {
         if (siSandbox.create()) {
            Response res = siSandbox.resync();
            WorkItemIterator wit = res.getWorkItems();

            while(wit.hasNext()) {
               WorkItem wi = wit.next();
               if (wi.getModelType().equals("si.Member")) {
                  Result message = wi.getResult();
                  this.getLogger().debug(wi.getDisplayId() + " " + (null != message ? message.getMessage() : ""));
                  if (null != message && message.getMessage().length() > 0) {
                     updatedFiles.add(new ScmFile(wi.getDisplayId(), message.getMessage().equalsIgnoreCase("removed") ? ScmFileStatus.DELETED : ScmFileStatus.UPDATED));
                  }
               }
            }

            return new UpdateScmResult(res.getCommandString(), updatedFiles);
         } else {
            return new UpdateScmResult("si resync", "Failed to synchronize workspace", "", false);
         }
      } catch (APIException var11) {
         ExceptionHandler eh = new ExceptionHandler(var11);
         this.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.getLogger().info(eh.getCommand() + " exited with return code " + eh.getExitCode());
         return new UpdateScmResult(eh.getCommand(), eh.getMessage(), "Exit Code: " + eh.getExitCode(), false);
      }
   }

   protected ChangeLogCommand getChangeLogCommand() {
      IntegrityChangeLogCommand command = new IntegrityChangeLogCommand();
      command.setLogger(this.getLogger());
      return command;
   }
}
