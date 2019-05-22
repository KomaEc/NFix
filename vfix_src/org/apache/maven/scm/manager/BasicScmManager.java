package org.apache.maven.scm.manager;

import org.apache.maven.scm.log.DefaultLog;
import org.apache.maven.scm.log.ScmLogger;

public class BasicScmManager extends AbstractScmManager {
   protected ScmLogger getScmLogger() {
      return new DefaultLog();
   }
}
