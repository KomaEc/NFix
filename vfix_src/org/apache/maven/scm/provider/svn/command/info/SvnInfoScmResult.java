package org.apache.maven.scm.provider.svn.command.info;

import java.util.List;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.info.InfoScmResult;

/** @deprecated */
public class SvnInfoScmResult extends InfoScmResult {
   private static final long serialVersionUID = 955993340040530451L;

   public SvnInfoScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public SvnInfoScmResult(String commandLine, List<SvnInfoItem> files) {
      super(commandLine, (String)null, (String)null, true);
      if (files != null) {
         this.getInfoItems().addAll(files);
      }

   }

   public SvnInfoScmResult(List<SvnInfoItem> files, ScmResult result) {
      super(result);
      if (files != null) {
         this.getInfoItems().addAll(files);
      }

   }
}
