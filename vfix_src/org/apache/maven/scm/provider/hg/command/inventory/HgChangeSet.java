package org.apache.maven.scm.provider.hg.command.inventory;

import org.apache.maven.scm.ChangeSet;

public class HgChangeSet extends ChangeSet {
   private static final long serialVersionUID = -4556377494055110302L;
   private String branch;

   public HgChangeSet() {
   }

   public HgChangeSet(String branch) {
      this.branch = branch;
   }

   public String getBranch() {
      return this.branch;
   }
}
