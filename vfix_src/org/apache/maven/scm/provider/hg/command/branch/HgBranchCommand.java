package org.apache.maven.scm.provider.hg.command.branch;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmBranchParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.branch.AbstractBranchCommand;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.command.inventory.HgListConsumer;
import org.apache.maven.scm.provider.hg.repository.HgScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class HgBranchCommand extends AbstractBranchCommand implements Command {
   protected ScmResult executeBranchCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String branch, String message) throws ScmException {
      return this.executeBranchCommand(scmProviderRepository, fileSet, branch, new ScmBranchParameters(message));
   }

   protected ScmResult executeBranchCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String branch, ScmBranchParameters scmBranchParameters) throws ScmException {
      if (StringUtils.isBlank(branch)) {
         throw new ScmException("branch must be specified");
      } else if (!fileSet.getFileList().isEmpty()) {
         throw new ScmException("This provider doesn't support branchging subsets of a directory");
      } else {
         File workingDir = fileSet.getBasedir();
         String[] branchCmd = new String[]{"branch", branch};
         HgConsumer branchConsumer = new HgConsumer(this.getLogger()) {
            public void doConsume(ScmFileStatus status, String trimmedLine) {
            }
         };
         ScmResult result = HgUtils.execute(branchConsumer, this.getLogger(), workingDir, branchCmd);
         HgScmProviderRepository repository = (HgScmProviderRepository)scmProviderRepository;
         if (!result.isSuccess()) {
            throw new ScmException("Error while executing command " + this.joinCmd(branchCmd));
         } else {
            String[] commitCmd = new String[]{"commit", "--message", scmBranchParameters.getMessage()};
            result = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), workingDir, commitCmd);
            if (!result.isSuccess()) {
               throw new ScmException("Error while executing command " + this.joinCmd(commitCmd));
            } else {
               String[] listCmd;
               if (repository.isPushChanges() && !repository.getURI().equals(fileSet.getBasedir().getAbsolutePath())) {
                  listCmd = new String[]{"push", "--new-branch", repository.getURI()};
                  result = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), listCmd);
                  if (!result.isSuccess()) {
                     throw new ScmException("Error while executing command " + this.joinCmd(listCmd));
                  }
               }

               listCmd = new String[]{"locate"};
               HgListConsumer listconsumer = new HgListConsumer(this.getLogger());
               result = HgUtils.execute(listconsumer, this.getLogger(), fileSet.getBasedir(), listCmd);
               if (!result.isSuccess()) {
                  throw new ScmException("Error while executing command " + this.joinCmd(listCmd));
               } else {
                  List<ScmFile> files = listconsumer.getFiles();
                  List<ScmFile> fileList = new ArrayList();
                  Iterator i$ = files.iterator();

                  while(i$.hasNext()) {
                     ScmFile f = (ScmFile)i$.next();
                     fileList.add(new ScmFile(f.getPath(), ScmFileStatus.TAGGED));
                  }

                  return new BranchScmResult(fileList, result);
               }
            }
         }
      }
   }

   private String joinCmd(String[] cmd) {
      StringBuilder result = new StringBuilder();

      for(int i = 0; i < cmd.length; ++i) {
         String s = cmd[i];
         result.append(s);
         if (i < cmd.length - 1) {
            result.append(" ");
         }
      }

      return result.toString();
   }
}
