package org.apache.maven.scm.command.info;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmResult;

public class InfoScmResult extends ScmResult {
   private static final long serialVersionUID = 955993340040530451L;
   private List<InfoItem> infoItems;

   public InfoScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
      this.infoItems = new ArrayList(0);
   }

   public InfoScmResult(String commandLine, List<InfoItem> files) {
      super(commandLine, (String)null, (String)null, true);
      this.infoItems = files;
   }

   public InfoScmResult(List<InfoItem> infoItems, ScmResult result) {
      super(result);
      this.infoItems = infoItems;
   }

   public InfoScmResult(ScmResult result) {
      super(result);
   }

   public List<InfoItem> getInfoItems() {
      return this.infoItems;
   }
}
