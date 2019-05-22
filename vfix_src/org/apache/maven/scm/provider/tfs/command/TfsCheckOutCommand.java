package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkout.AbstractCheckOutCommand;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;

public class TfsCheckOutCommand extends AbstractCheckOutCommand {
   protected CheckOutScmResult executeCheckOutCommand(ScmProviderRepository r, ScmFileSet f, ScmVersion v, boolean recursive) throws ScmException {
      TfsScmProviderRepository tfsRepo = (TfsScmProviderRepository)r;
      String url = tfsRepo.getServerPath();
      String tfsUrl = tfsRepo.getTfsUrl();
      String workspace = tfsRepo.getWorkspace();
      boolean workspaceProvided = workspace != null && !workspace.trim().equals("");
      if (workspaceProvided) {
         this.createWorkspace(r, f, workspace, tfsUrl);
      }

      if (workspaceProvided) {
         this.executeUnmapCommand(r, f);
      }

      ErrorStreamConsumer out = new ErrorStreamConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status;
      TfsCommand command;
      if (workspaceProvided) {
         command = new TfsCommand("workfold", r, (ScmFileSet)null, this.getLogger());
         command.addArgument("-workspace:" + workspace);
         command.addArgument("-map");
         command.addArgument(url);
         command.addArgument(f.getBasedir().getAbsolutePath());
         status = command.execute(out, err);
         if (status != 0 || err.hasBeenFed()) {
            return new CheckOutScmResult(command.getCommandString(), "Error code for TFS checkout (workfold map) command - " + status, err.getOutput(), false);
         }
      }

      FileListConsumer fileConsumer = new FileListConsumer();
      err = new ErrorStreamConsumer();
      command = this.createGetCommand(r, f, v, recursive);
      status = command.execute(fileConsumer, err);
      return status == 0 && !err.hasBeenFed() ? new CheckOutScmResult(command.getCommandString(), fileConsumer.getFiles()) : new CheckOutScmResult(command.getCommandString(), "Error code for TFS checkout (get) command - " + status, err.getOutput(), false);
   }

   public TfsCommand createGetCommand(ScmProviderRepository r, ScmFileSet f, ScmVersion v, boolean recursive) {
      TfsCommand command = new TfsCommand("get", r, f, this.getLogger());
      if (recursive) {
         command.addArgument("-recursive");
      }

      command.addArgument("-force");
      if (v != null && !v.equals("")) {
         String vType = "";
         if (v.getType().equals("Tag")) {
            vType = "L";
         }

         if (v.getType().equals("Revision")) {
            vType = "C";
         }

         command.addArgument("-version:" + vType + v.getName());
      }

      command.addArgument(f.getBasedir().getAbsolutePath());
      return command;
   }

   public int executeUnmapCommand(ScmProviderRepository r, ScmFileSet f) throws ScmException {
      TfsScmProviderRepository tfsRepo = (TfsScmProviderRepository)r;
      String url = tfsRepo.getServerPath();
      String workspace = tfsRepo.getWorkspace();
      ErrorStreamConsumer out = new ErrorStreamConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = new TfsCommand("workfold", r, (ScmFileSet)null, this.getLogger());
      command.addArgument("-workspace:" + workspace);
      command.addArgument("-unmap");
      command.addArgument(url);
      return command.execute(out, err);
   }

   private void createWorkspace(ScmProviderRepository r, ScmFileSet f, String workspace, String url) throws ScmException {
      ErrorStreamConsumer out = new ErrorStreamConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      TfsCommand command = new TfsCommand("workspace", r, (ScmFileSet)null, this.getLogger());
      command.addArgument("-new");
      command.addArgument("-comment:Creating workspace for maven command");
      command.addArgument("-server:" + url);
      command.addArgument(workspace);
      command.execute(out, err);
   }
}
