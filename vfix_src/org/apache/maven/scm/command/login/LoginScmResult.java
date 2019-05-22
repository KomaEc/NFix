package org.apache.maven.scm.command.login;

import org.apache.maven.scm.ScmResult;

public class LoginScmResult extends ScmResult {
   private static final long serialVersionUID = -179242524702253809L;

   public LoginScmResult(String commandLine, String providerMessage, String commandOutput, boolean success) {
      super(commandLine, providerMessage, commandOutput, success);
   }
}
