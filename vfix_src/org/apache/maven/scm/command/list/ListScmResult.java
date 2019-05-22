package org.apache.maven.scm.command.list;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class ListScmResult extends ScmResult {
   private static final long serialVersionUID = 5402161066844465281L;
   private List<ScmFile> files;

   public ListScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.files = new ArrayList(0);
   }

   public ListScmResult(String commandLine, List<ScmFile> files) {
      super(commandLine, (String)null, (String)null, true);
      this.files = files;
   }

   public ListScmResult(List<ScmFile> files, ScmResult result) {
      super(result);
      this.files = files;
   }

   public List<ScmFile> getFiles() {
      return this.files;
   }
}
