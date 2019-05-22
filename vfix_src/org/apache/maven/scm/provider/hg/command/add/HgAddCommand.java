package org.apache.maven.scm.provider.hg.command.add;

import java.io.File;
import java.util.Iterator;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;

public class HgAddCommand extends AbstractAddCommand implements Command {
   protected ScmResult executeAddCommand(ScmProviderRepository repo, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      String[] addCmd = new String[]{"add", "--verbose"};
      addCmd = HgUtils.expandCommandLine(addCmd, fileSet);
      File workingDir = fileSet.getBasedir();
      HgAddConsumer consumer = new HgAddConsumer(this.getLogger(), workingDir);
      ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDir, addCmd);
      AddScmResult addScmResult = new AddScmResult(consumer.getAddedFiles(), result);
      Iterator i$ = fileSet.getFileList().iterator();

      while(i$.hasNext()) {
         File workingFile = (File)i$.next();
         File file = new File(workingDir + "/" + workingFile.getPath());
         if (file.isDirectory() && file.listFiles().length == 0) {
            addScmResult.getAddedFiles().add(new ScmFile(workingFile.getPath(), ScmFileStatus.ADDED));
         }
      }

      return addScmResult;
   }
}
