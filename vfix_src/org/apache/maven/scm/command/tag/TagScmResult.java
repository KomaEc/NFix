package org.apache.maven.scm.command.tag;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class TagScmResult extends ScmResult {
   private static final long serialVersionUID = -5068975000282095635L;
   private List<ScmFile> taggedFiles;

   public TagScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public TagScmResult(String commandLine, List<ScmFile> taggedFiles) {
      super(commandLine, (String)null, (String)null, true);
      this.taggedFiles = taggedFiles;
   }

   public TagScmResult(List<ScmFile> taggedFiles, ScmResult result) {
      super(result);
      this.taggedFiles = taggedFiles;
   }

   public List<ScmFile> getTaggedFiles() {
      return this.taggedFiles;
   }
}
