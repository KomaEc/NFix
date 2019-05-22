package org.apache.maven.scm.command.checkout;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class CheckOutScmResult extends ScmResult {
   private static final long serialVersionUID = 3345964619749320210L;
   private List<ScmFile> checkedOutFiles;
   private String revision;
   protected String relativePathProjectDirectory;

   public CheckOutScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.relativePathProjectDirectory = "";
   }

   public CheckOutScmResult(String commandLine, List<ScmFile> checkedOutFiles) {
      this(commandLine, (String)null, (List)checkedOutFiles);
   }

   public CheckOutScmResult(String commandLine, String revision, List<ScmFile> checkedOutFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.relativePathProjectDirectory = "";
      this.revision = revision;
      this.checkedOutFiles = checkedOutFiles;
   }

   public CheckOutScmResult(String commandLine, List<ScmFile> checkedOutFiles, String relativePathProjectDirectory) {
      this(commandLine, (String)null, (List)checkedOutFiles);
      if (relativePathProjectDirectory != null) {
         this.relativePathProjectDirectory = relativePathProjectDirectory;
      }

   }

   public CheckOutScmResult(String commandLine, String revision, List<ScmFile> checkedOutFiles, String relativePathProjectDirectory) {
      this(commandLine, revision, checkedOutFiles);
      if (relativePathProjectDirectory != null) {
         this.relativePathProjectDirectory = relativePathProjectDirectory;
      }

   }

   public CheckOutScmResult(List<ScmFile> checkedOutFiles, ScmResult result) {
      super(result);
      this.relativePathProjectDirectory = "";
      this.checkedOutFiles = checkedOutFiles;
   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.checkedOutFiles;
   }

   public String getRelativePathProjectDirectory() {
      return this.relativePathProjectDirectory;
   }

   public String getRevision() {
      return this.revision;
   }
}
