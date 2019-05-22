package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.Os;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;

public class BourneShell extends Shell {
   private static final char[] BASH_QUOTING_TRIGGER_CHARS = new char[]{' ', '$', ';', '&', '|', '<', '>', '*', '?', '(', ')', '[', ']', '{', '}', '`'};

   public BourneShell() {
      this.setShellCommand("/bin/sh");
      this.setArgumentQuoteDelimiter('\'');
      this.setExecutableQuoteDelimiter('"');
      this.setSingleQuotedArgumentEscaped(true);
      this.setSingleQuotedExecutableEscaped(false);
      this.setQuotedExecutableEnabled(true);
   }

   public String getExecutable() {
      return Os.isFamily("windows") ? super.getExecutable() : unifyQuotes(super.getExecutable());
   }

   public List<String> getShellArgsList() {
      List<String> shellArgs = new ArrayList();
      List<String> existingShellArgs = super.getShellArgsList();
      if (existingShellArgs != null && !existingShellArgs.isEmpty()) {
         shellArgs.addAll(existingShellArgs);
      }

      shellArgs.add("-c");
      return shellArgs;
   }

   public String[] getShellArgs() {
      String[] shellArgs = super.getShellArgs();
      if (shellArgs == null) {
         shellArgs = new String[0];
      }

      if (shellArgs.length > 0 && !shellArgs[shellArgs.length - 1].equals("-c")) {
         String[] newArgs = new String[shellArgs.length + 1];
         System.arraycopy(shellArgs, 0, newArgs, 0, shellArgs.length);
         newArgs[shellArgs.length] = "-c";
         shellArgs = newArgs;
      }

      return shellArgs;
   }

   protected String getExecutionPreamble() {
      if (this.getWorkingDirectoryAsString() == null) {
         return null;
      } else {
         String dir = this.getWorkingDirectoryAsString();
         StringBuilder sb = new StringBuilder();
         sb.append("cd ");
         sb.append(unifyQuotes(dir));
         sb.append(" && ");
         return sb.toString();
      }
   }

   protected char[] getQuotingTriggerChars() {
      return BASH_QUOTING_TRIGGER_CHARS;
   }

   private static String unifyQuotes(String path) {
      if (path == null) {
         return null;
      } else {
         return path.indexOf(32) == -1 && path.indexOf(39) != -1 && path.indexOf(34) == -1 ? StringUtils.escape(path) : StringUtils.quoteAndEscape(path, '"', BASH_QUOTING_TRIGGER_CHARS);
      }
   }
}
