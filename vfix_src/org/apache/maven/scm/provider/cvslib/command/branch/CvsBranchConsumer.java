package org.apache.maven.scm.provider.cvslib.command.branch;

import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.cvslib.command.tag.CvsTagConsumer;

public class CvsBranchConsumer extends CvsTagConsumer {
   public CvsBranchConsumer(ScmLogger logger) {
      super(logger);
   }
}
