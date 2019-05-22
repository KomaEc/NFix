package org.apache.maven.scm.provider.cvslib.cvsjava.command.mkdir;

import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.provider.cvslib.command.mkdir.AbstractCvsMkdirCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.add.CvsJavaAddCommand;

public class CvsJavaMkdirCommand extends AbstractCvsMkdirCommand {
   protected Command getAddCommand() {
      return new CvsJavaAddCommand();
   }
}
