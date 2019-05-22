package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;

public class Shell implements Cloneable {
   private static final char[] DEFAULT_QUOTING_TRIGGER_CHARS = new char[]{' '};
   private String shellCommand;
   private final List<String> shellArgs = new ArrayList();
   private boolean quotedArgumentsEnabled = true;
   private String executable;
   private String workingDir;
   private boolean quotedExecutableEnabled = true;
   private boolean singleQuotedArgumentEscaped = false;
   private boolean singleQuotedExecutableEscaped = false;
   private char argQuoteDelimiter = '"';
   private char exeQuoteDelimiter = '"';

   void setShellCommand(String shellCommand) {
      this.shellCommand = shellCommand;
   }

   String getShellCommand() {
      return this.shellCommand;
   }

   void setShellArgs(String[] shellArgs) {
      this.shellArgs.clear();
      this.shellArgs.addAll(Arrays.asList(shellArgs));
   }

   String[] getShellArgs() {
      return this.shellArgs != null && !this.shellArgs.isEmpty() ? (String[])this.shellArgs.toArray(new String[this.shellArgs.size()]) : null;
   }

   List<String> getCommandLine(String executable, String... arguments) {
      return this.getRawCommandLine(executable, arguments);
   }

   List<String> getRawCommandLine(String executable, String... arguments) {
      List<String> commandLine = new ArrayList();
      StringBuilder sb = new StringBuilder();
      if (executable != null) {
         String preamble = this.getExecutionPreamble();
         if (preamble != null) {
            sb.append(preamble);
         }

         if (this.isQuotedExecutableEnabled()) {
            char[] escapeChars = this.getEscapeChars(this.isSingleQuotedExecutableEscaped(), this.isDoubleQuotedExecutableEscaped());
            sb.append(StringUtils.quoteAndEscape(this.getExecutable(), this.getExecutableQuoteDelimiter(), escapeChars, this.getQuotingTriggerChars(), '\\', false));
         } else {
            sb.append(this.getExecutable());
         }
      }

      String[] arr$ = arguments;
      int len$ = arguments.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String argument = arr$[i$];
         if (sb.length() > 0) {
            sb.append(' ');
         }

         if (this.isQuotedArgumentsEnabled()) {
            char[] escapeChars = this.getEscapeChars(this.isSingleQuotedArgumentEscaped(), this.isDoubleQuotedArgumentEscaped());
            sb.append(StringUtils.quoteAndEscape(argument, this.getArgumentQuoteDelimiter(), escapeChars, this.getQuotingTriggerChars(), '\\', false));
         } else {
            sb.append(argument);
         }
      }

      commandLine.add(sb.toString());
      return commandLine;
   }

   char[] getQuotingTriggerChars() {
      return DEFAULT_QUOTING_TRIGGER_CHARS;
   }

   String getExecutionPreamble() {
      return null;
   }

   char[] getEscapeChars(boolean includeSingleQuote, boolean includeDoubleQuote) {
      StringBuilder buf = new StringBuilder(2);
      if (includeSingleQuote) {
         buf.append('\'');
      }

      if (includeDoubleQuote) {
         buf.append('"');
      }

      char[] result = new char[buf.length()];
      buf.getChars(0, buf.length(), result, 0);
      return result;
   }

   protected boolean isDoubleQuotedArgumentEscaped() {
      return false;
   }

   protected boolean isSingleQuotedArgumentEscaped() {
      return this.singleQuotedArgumentEscaped;
   }

   boolean isDoubleQuotedExecutableEscaped() {
      return false;
   }

   boolean isSingleQuotedExecutableEscaped() {
      return this.singleQuotedExecutableEscaped;
   }

   void setArgumentQuoteDelimiter(char argQuoteDelimiter) {
      this.argQuoteDelimiter = argQuoteDelimiter;
   }

   char getArgumentQuoteDelimiter() {
      return this.argQuoteDelimiter;
   }

   void setExecutableQuoteDelimiter(char exeQuoteDelimiter) {
      this.exeQuoteDelimiter = exeQuoteDelimiter;
   }

   char getExecutableQuoteDelimiter() {
      return this.exeQuoteDelimiter;
   }

   public List<String> getShellCommandLine(String... arguments) {
      List<String> commandLine = new ArrayList();
      if (this.getShellCommand() != null) {
         commandLine.add(this.getShellCommand());
      }

      if (this.getShellArgs() != null) {
         commandLine.addAll(this.getShellArgsList());
      }

      commandLine.addAll(this.getCommandLine(this.getExecutable(), arguments));
      return commandLine;
   }

   List<String> getShellArgsList() {
      return this.shellArgs;
   }

   public void setQuotedArgumentsEnabled(boolean quotedArgumentsEnabled) {
      this.quotedArgumentsEnabled = quotedArgumentsEnabled;
   }

   boolean isQuotedArgumentsEnabled() {
      return this.quotedArgumentsEnabled;
   }

   void setQuotedExecutableEnabled(boolean quotedExecutableEnabled) {
      this.quotedExecutableEnabled = quotedExecutableEnabled;
   }

   boolean isQuotedExecutableEnabled() {
      return this.quotedExecutableEnabled;
   }

   public void setExecutable(String executable) {
      if (executable != null && executable.length() != 0) {
         this.executable = executable.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      }
   }

   public String getExecutable() {
      return this.executable;
   }

   public void setWorkingDirectory(String path) {
      if (path != null) {
         this.workingDir = path;
      }

   }

   public void setWorkingDirectory(File workingDir) {
      if (workingDir != null) {
         this.workingDir = workingDir.getAbsolutePath();
      }

   }

   public File getWorkingDirectory() {
      return this.workingDir == null ? null : new File(this.workingDir);
   }

   String getWorkingDirectoryAsString() {
      return this.workingDir;
   }

   public Object clone() {
      throw new RuntimeException("Do we ever clone this?");
   }

   void setSingleQuotedArgumentEscaped(boolean singleQuotedArgumentEscaped) {
      this.singleQuotedArgumentEscaped = singleQuotedArgumentEscaped;
   }

   void setSingleQuotedExecutableEscaped(boolean singleQuotedExecutableEscaped) {
      this.singleQuotedExecutableEscaped = singleQuotedExecutableEscaped;
   }
}
