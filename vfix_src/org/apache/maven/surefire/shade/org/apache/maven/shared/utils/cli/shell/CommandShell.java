package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell;

public class CommandShell extends Shell {
   public CommandShell() {
      this.setShellCommand("command.com");
      this.setShellArgs(new String[]{"/C"});
   }
}
