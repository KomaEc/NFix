package org.apache.maven.scm.command.blame;

import java.util.List;
import org.apache.maven.scm.ScmResult;

public class BlameScmResult extends ScmResult {
   private static final long serialVersionUID = -3877526036464636595L;
   private List<BlameLine> lines;

   public BlameScmResult(String commandLine, List<BlameLine> lines) {
      this(commandLine, (String)null, (String)null, true);
      this.lines = lines;
   }

   public BlameScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public BlameScmResult(List<BlameLine> lines, ScmResult scmResult) {
      super(scmResult);
      this.lines = lines;
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
