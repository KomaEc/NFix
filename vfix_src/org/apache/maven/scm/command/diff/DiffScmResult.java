package org.apache.maven.scm.command.diff;

import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class DiffScmResult extends ScmResult {
   private static final long serialVersionUID = 4036970486972633082L;
   private List<ScmFile> changedFiles;
   private Map<String, CharSequence> differences;
   private String patch;

   public DiffScmResult(String commandLine, List<ScmFile> changedFiles, Map<String, CharSequence> differences, String patch) {
      this(commandLine, (String)null, (String)null, true);
      this.changedFiles = changedFiles;
      this.differences = differences;
      this.patch = patch;
   }

   public DiffScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public DiffScmResult(List<ScmFile> changedFiles, Map<String, CharSequence> differences, String patch, ScmResult result) {
      super(result);
      this.changedFiles = changedFiles;
      this.differences = differences;
      this.patch = patch;
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }

   public Map<String, CharSequence> getDifferences() {
      return this.differences;
   }

   public String getPatch() {
      return this.patch;
   }
}
