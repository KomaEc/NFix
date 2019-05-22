package org.apache.maven.scm.command.mkdir;

import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmResult;

public class MkdirScmResult extends ScmResult {
   private static final long serialVersionUID = -8717329738246682608L;
   private String revision;
   private List<ScmFile> createdDirs;

   public MkdirScmResult(ScmResult scmResult) {
      super(scmResult);
   }

   public MkdirScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public MkdirScmResult(String commandLine, String revision) {
      this(commandLine, (String)null, (String)null, true);
      this.revision = revision;
   }

   public MkdirScmResult(String commandLine, List<ScmFile> createdDirs) {
      this(commandLine, (String)null, (String)null, true);
      this.createdDirs = createdDirs;
   }

   public MkdirScmResult(String revision, ScmResult result) {
      super(result);
      this.revision = revision;
   }

   public MkdirScmResult(List<ScmFile> createdDirs, ScmResult result) {
      super(result);
      this.createdDirs = createdDirs;
   }

   public String getRevision() {
      return this.revision;
   }

   public List<ScmFile> getCreatedDirs() {
      return this.createdDirs;
   }
}
