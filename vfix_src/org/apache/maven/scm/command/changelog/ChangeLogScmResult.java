package org.apache.maven.scm.command.changelog;

import org.apache.maven.scm.ScmResult;

public class ChangeLogScmResult extends ScmResult {
   private static final long serialVersionUID = 559431861541372265L;
   private ChangeLogSet changeLog;

   public ChangeLogScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public ChangeLogScmResult(String commandLine, ChangeLogSet changeLog) {
      super(commandLine, (String)null, (String)null, true);
      this.changeLog = changeLog;
   }

   public ChangeLogScmResult(ChangeLogSet changeLog, ScmResult result) {
      super(result);
      this.changeLog = changeLog;
   }

   public ChangeLogSet getChangeLog() {
      return this.changeLog;
   }
}
