package org.apache.maven.scm.provider.git.command.info;

import java.util.List;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.info.InfoScmResult;

public class GitInfoScmResult extends InfoScmResult {
   private static final long serialVersionUID = -1314905338508176675L;

   public GitInfoScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public GitInfoScmResult(String commandLine, List<GitInfoItem> files) {
      super(commandLine, (String)null, (String)null, true);
      if (files != null) {
         this.getInfoItems().addAll(files);
      }

   }

   public GitInfoScmResult(List<GitInfoItem> files, ScmResult result) {
      super(result);
      if (files != null) {
         this.getInfoItems().addAll(files);
      }

   }
}
