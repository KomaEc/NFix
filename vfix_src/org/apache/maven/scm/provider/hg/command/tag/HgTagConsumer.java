package org.apache.maven.scm.provider.hg.command.tag;

import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgTagConsumer extends HgConsumer {
   public HgTagConsumer(ScmLogger logger) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
   }
}
