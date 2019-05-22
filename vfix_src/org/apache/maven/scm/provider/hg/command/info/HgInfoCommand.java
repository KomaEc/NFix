package org.apache.maven.scm.provider.hg.command.info;

import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.AbstractCommand;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgInfoCommand extends AbstractCommand implements Command {
   protected ScmResult executeCommand(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      String[] revCmd = new String[]{"id", "-i"};
      HgInfoConsumer consumer = new HgInfoConsumer(this.getLogger());
      ScmResult scmResult = HgUtils.execute(consumer, this.getLogger(), fileSet.getBasedir(), revCmd);
      return new InfoScmResult(consumer.getInfoItems(), scmResult);
   }
}
