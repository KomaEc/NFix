package org.apache.maven.scm.provider.cvslib.cvsexe.command.mkdir;

import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.provider.cvslib.command.mkdir.AbstractCvsMkdirCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.add.CvsExeAddCommand;

public class CvsExeMkdirCommand extends AbstractCvsMkdirCommand {
   protected Command getAddCommand() {
      return new CvsExeAddCommand();
   }
}
