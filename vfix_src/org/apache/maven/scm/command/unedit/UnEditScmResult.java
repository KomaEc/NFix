package org.apache.maven.scm.command.unedit;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class UnEditScmResult extends ScmResult {
   private static final long serialVersionUID = 257465331122587798L;
   private List<ScmFile> unEditFiles;

   public UnEditScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public UnEditScmResult(String commandLine, List<ScmFile> unEditFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.unEditFiles = unEditFiles;
   }

   public UnEditScmResult(List<ScmFile> unEditFiles, ScmResult result) {
      super(result);
      this.unEditFiles = unEditFiles;
   }

   public List<ScmFile> getUnEditFiles() {
      return this.unEditFiles;
   }
}
