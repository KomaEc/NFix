package org.apache.maven.scm.provider.tfs.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.command.changelog.AbstractChangeLogCommand;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.apache.maven.scm.provider.tfs.command.consumer.TfsChangeLogConsumer;

public class TfsChangeLogCommand extends AbstractChangeLogCommand {
   protected ChangeLogScmResult executeChangeLogCommand(ScmProviderRepository r, ScmFileSet f, Date startDate, Date endDate, ScmBranch branch, String datePattern) throws ScmException {
      List<ChangeSet> changeLogs = new ArrayList();
      Iterator<File> iter = f.getFileList().iterator();
      if (!iter.hasNext()) {
         List<File> dir = new ArrayList();
         dir.add(f.getBasedir());
         iter = dir.iterator();
      }

      TfsCommand command = null;

      while(iter.hasNext()) {
         TfsChangeLogConsumer out = new TfsChangeLogConsumer(this.getLogger());
         ErrorStreamConsumer err = new ErrorStreamConsumer();
         command = this.createCommand(r, f, (File)iter.next());
         int status = command.execute(out, err);
         if (status != 0 || !out.hasBeenFed() && err.hasBeenFed()) {
            return new ChangeLogScmResult(command.getCommandString(), "Error code for TFS changelog command - " + status, err.getOutput(), false);
         }

         changeLogs.addAll(out.getLogs());
      }

      return new ChangeLogScmResult(command.getCommandString(), new ChangeLogSet(changeLogs, startDate, endDate));
   }

   protected TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, File file) {
      TfsCommand command = new TfsCommand("history", r, f, this.getLogger());
      command.addArgument("-format:detailed");
      command.addArgument(file.getName());
      return command;
   }
}
