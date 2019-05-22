package org.apache.maven.scm.provider.hg.command.inventory;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgOutgoingConsumer extends HgConsumer {
   private List<HgChangeSet> changes = new ArrayList();
   private static final String BRANCH = "branch";

   public HgOutgoingConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      String branch = null;
      if (line.startsWith("branch")) {
         branch = line.substring("branch".length() + 7);
      }

      this.changes.add(new HgChangeSet(branch));
   }

   public List<HgChangeSet> getChanges() {
      return this.changes;
   }
}
