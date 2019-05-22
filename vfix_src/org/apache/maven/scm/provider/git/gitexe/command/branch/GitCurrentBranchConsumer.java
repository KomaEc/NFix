package org.apache.maven.scm.provider.git.gitexe.command.branch;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class GitCurrentBranchConsumer extends AbstractConsumer {
   private static final String BRANCH_INDICATOR = "refs/heads/";
   private String branch;

   public GitCurrentBranchConsumer(ScmLogger logger) {
      super(logger);
   }

   public String getBranchName() {
      return this.branch;
   }

   public void consumeLine(String line) {
      line = line.trim();
      if (line.startsWith("refs/heads/")) {
         this.branch = line.substring("refs/heads/".length());
      }

   }
}
