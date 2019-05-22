package org.apache.maven.scm.command.remoteinfo;

import java.util.HashMap;
import java.util.Map;
import org.apache.maven.scm.ScmResult;

public class RemoteInfoScmResult extends ScmResult {
   private Map<String, String> branches = new HashMap();
   private Map<String, String> tags = new HashMap();

   public RemoteInfoScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }

   public RemoteInfoScmResult(String commandLine, Map<String, String> branches, Map<String, String> tags) {
      super(commandLine, (String)null, (String)null, true);
      this.branches = branches;
      this.tags = tags;
   }

   public Map<String, String> getBranches() {
      return this.branches;
   }

   public void setBranches(Map<String, String> branches) {
      this.branches = branches;
   }

   public Map<String, String> getTags() {
      return this.tags;
   }

   public void setTags(Map<String, String> tags) {
      this.tags = tags;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("RemoteInfoScmResult");
      sb.append("{branches=").append(this.branches);
      sb.append(", tags=").append(this.tags);
      sb.append('}');
      return sb.toString();
   }
}
