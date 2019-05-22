package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell;

import java.util.Arrays;
import java.util.List;

public class CmdShell extends Shell {
   public CmdShell() {
      this.setShellCommand("cmd.exe");
      this.setQuotedExecutableEnabled(true);
      this.setShellArgs(new String[]{"/X", "/C"});
   }

   public List<String> getCommandLine(String executable, String... arguments) {
      StringBuilder sb = new StringBuilder();
      sb.append('"');
      sb.append((String)super.getCommandLine(executable, arguments).get(0));
      sb.append('"');
      return Arrays.asList(sb.toString());
   }
}
