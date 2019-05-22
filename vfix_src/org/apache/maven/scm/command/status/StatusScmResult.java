package org.apache.maven.scm.command.status;

import java.util.Collections;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class StatusScmResult extends ScmResult {
   private static final long serialVersionUID = 7152442589455369403L;
   private List<ScmFile> changedFiles;

   public StatusScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.changedFiles = Collections.emptyList();
   }

   public StatusScmResult(String commandLine, List<ScmFile> changedFiles) {
      super(commandLine, (String)null, (String)null, true);
      if (changedFiles == null) {
         throw new NullPointerException("changedFiles can't be null.");
      } else {
         this.changedFiles = changedFiles;
      }
   }

   public StatusScmResult(List<ScmFile> changedFiles, ScmResult result) {
      super(result);
      if (changedFiles == null) {
         throw new NullPointerException("changedFiles can't be null.");
      } else {
         this.changedFiles = changedFiles;
      }
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }
}
