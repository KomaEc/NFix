package org.apache.maven.scm.command.checkin;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class CheckInScmResult extends ScmResult {
   private static final long serialVersionUID = 954225589449445354L;
   private List<ScmFile> checkedInFiles;
   private String scmRevision;

   public CheckInScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public CheckInScmResult(String commandLine, List<ScmFile> checkedInFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.checkedInFiles = checkedInFiles;
   }

   public CheckInScmResult(String commandLine, List<ScmFile> checkedInFiles, String scmRevision) {
      this(commandLine, checkedInFiles);
      this.scmRevision = scmRevision;
   }

   public CheckInScmResult(List<ScmFile> checkedInFiles, ScmResult result) {
      super(result);
      this.checkedInFiles = checkedInFiles;
   }

   public List<ScmFile> getCheckedInFiles() {
      if (this.checkedInFiles == null) {
         this.checkedInFiles = new ArrayList();
      }

      return this.checkedInFiles;
   }

   public String getScmRevision() {
      return this.scmRevision;
   }
}
