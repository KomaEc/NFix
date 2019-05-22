package org.apache.maven.scm.command.add;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class AddScmResult extends ScmResult {
   private static final long serialVersionUID = 1L;
   private List<ScmFile> addedFiles;

   public AddScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.addedFiles = new ArrayList(0);
   }

   public AddScmResult(String commandLine, List<ScmFile> addedFiles) {
      this(commandLine, (String)null, (String)null, true);
      if (addedFiles == null) {
         throw new NullPointerException("addedFiles can't be null");
      } else {
         this.addedFiles = addedFiles;
      }
   }

   public AddScmResult(List<ScmFile> addedFiles, ScmResult result) {
      super(result);
      if (addedFiles == null) {
         throw new NullPointerException("addedFiles can't be null");
      } else {
         this.addedFiles = addedFiles;
      }
   }

   public List<ScmFile> getAddedFiles() {
      return this.addedFiles;
   }
}
