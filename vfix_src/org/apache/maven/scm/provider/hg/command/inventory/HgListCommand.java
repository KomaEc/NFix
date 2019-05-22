package org.apache.maven.scm.provider.hg.command.inventory;

import java.io.File;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.list.AbstractListCommand;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgListCommand extends AbstractListCommand implements Command {
   protected ListScmResult executeListCommand(ScmProviderRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion scmVersion) throws ScmException {
      File workingDir = fileSet.getBasedir();
      String[] listCmd = new String[]{"locate"};
      StringBuilder cmd = new StringBuilder();

      for(int i = 0; i < listCmd.length; ++i) {
         String s = listCmd[i];
         cmd.append(s);
         if (i < listCmd.length - 1) {
            cmd.append(" ");
         }
      }

      HgListConsumer consumer = new HgListConsumer(this.getLogger());
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDir, listCmd);
      if (result.isSuccess()) {
         return new ListScmResult(consumer.getFiles(), result);
      } else {
         throw new ScmException("Error while executing command " + cmd.toString());
      }
   }
}
