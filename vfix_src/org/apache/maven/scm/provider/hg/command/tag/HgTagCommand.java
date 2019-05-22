package org.apache.maven.scm.provider.hg.command.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.hg.HgUtils;
import org.apache.maven.scm.provider.hg.command.HgConsumer;
import org.apache.maven.scm.provider.hg.command.inventory.HgListConsumer;
import org.apache.maven.scm.provider.hg.repository.HgScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class HgTagCommand extends AbstractTagCommand implements Command {
   protected ScmResult executeTagCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String tag, String message) throws ScmException {
      return this.executeTagCommand(scmProviderRepository, fileSet, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository scmProviderRepository, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (tag != null && !StringUtils.isEmpty(tag.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("This provider doesn't support tagging subsets of a directory : " + fileSet.getFileList());
         } else {
            File workingDir = fileSet.getBasedir();
            String[] tagCmd = new String[]{"tag", "--message", scmTagParameters.getMessage(), tag};
            StringBuilder cmd = this.joinCmd(tagCmd);
            HgTagConsumer consumer = new HgTagConsumer(this.getLogger());
            ScmResult result = HgUtils.execute(consumer, this.getLogger(), workingDir, tagCmd);
            HgScmProviderRepository repository = (HgScmProviderRepository)scmProviderRepository;
            if (result.isSuccess()) {
               if (repository.isPushChanges() && !repository.getURI().equals(fileSet.getBasedir().getAbsolutePath())) {
                  String branchName = HgUtils.getCurrentBranchName(this.getLogger(), workingDir);
                  boolean differentOutgoingBranch = HgUtils.differentOutgoingBranchFound(this.getLogger(), workingDir, branchName);
                  String[] pushCmd = new String[]{"push", differentOutgoingBranch ? "-r" + branchName : null, repository.getURI()};
                  result = HgUtils.execute(new HgConsumer(this.getLogger()), this.getLogger(), fileSet.getBasedir(), pushCmd);
               }

               String[] listCmd = new String[]{"locate"};
               HgListConsumer listconsumer = new HgListConsumer(this.getLogger());
               result = HgUtils.execute(listconsumer, this.getLogger(), fileSet.getBasedir(), listCmd);
               if (result.isSuccess()) {
                  List<ScmFile> files = listconsumer.getFiles();
                  List<ScmFile> fileList = new ArrayList();
                  Iterator i$ = files.iterator();

                  while(i$.hasNext()) {
                     ScmFile f = (ScmFile)i$.next();
                     if (!f.getPath().endsWith(".hgtags")) {
                        fileList.add(new ScmFile(f.getPath(), ScmFileStatus.TAGGED));
                     }
                  }

                  return new TagScmResult(fileList, result);
               } else {
                  throw new ScmException("Error while executing command " + cmd.toString());
               }
            } else {
               throw new ScmException("Error while executing command " + cmd.toString());
            }
         }
      } else {
         throw new ScmException("tag must be specified");
      }
   }

   private StringBuilder joinCmd(String[] cmd) {
      StringBuilder result = new StringBuilder();

      for(int i = 0; i < cmd.length; ++i) {
         String s = cmd[i];
         result.append(s);
         if (i < cmd.length - 1) {
            result.append(" ");
         }
      }

      return result;
   }
}
