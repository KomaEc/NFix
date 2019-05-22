package org.apache.maven.scm.provider.tfs.command;

import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.FileListConsumer;
import org.codehaus.plexus.util.StringUtils;

public class TfsCheckInCommand extends AbstractCheckInCommand {
   private static final String TFS_CHECKIN_POLICIES_ERROR = "TF10139";

   protected CheckInScmResult executeCheckInCommand(ScmProviderRepository r, ScmFileSet f, String m, ScmVersion v) throws ScmException {
      TfsCommand command = this.createCommand(r, f, m);
      FileListConsumer fileConsumer = new FileListConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status = command.execute(fileConsumer, err);
      this.getLogger().debug("status of checkin command is= " + status + "; err= " + err.getOutput());
      if (err.hasBeenFed() && err.getOutput().startsWith("TF10139")) {
         this.getLogger().debug("exclusion: got error TF10139 due to checkin policies. Ignoring it...");
      }

      if (status == 0 && (!err.hasBeenFed() || err.getOutput().startsWith("TF10139"))) {
         return new CheckInScmResult(command.getCommandString(), fileConsumer.getFiles());
      } else {
         this.getLogger().error("ERROR in command: " + command.getCommandString() + "; Error code for TFS checkin command - " + status);
         return new CheckInScmResult(command.getCommandString(), "Error code for TFS checkin command - " + status, err.getOutput(), false);
      }
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, String m) {
      TfsCommand command = new TfsCommand("checkin", r, f, this.getLogger());
      command.addArgument("-noprompt");
      if (StringUtils.isNotBlank(m)) {
         command.addArgument("-comment:" + m);
      }

      command.addArgument(f);
      TfsScmProviderRepository tfsScmProviderRepo = (TfsScmProviderRepository)r;
      if (tfsScmProviderRepo.isUseCheckinPolicies()) {
         command.addArgument("/override:checkin_policy");
      }

      return command;
   }
}
