package org.apache.maven.scm;

public class NoSuchCommandScmException extends ScmException {
   static final long serialVersionUID = 5789657554664703221L;
   private String commandName;

   public NoSuchCommandScmException(String commandName) {
      super("No such command '" + commandName + "'.");
   }

   public String getCommandName() {
      return this.commandName;
   }
}
