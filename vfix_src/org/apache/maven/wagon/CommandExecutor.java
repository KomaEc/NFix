package org.apache.maven.wagon;

public interface CommandExecutor extends Wagon {
   String ROLE = CommandExecutor.class.getName();

   void executeCommand(String var1) throws CommandExecutionException;

   Streams executeCommand(String var1, boolean var2) throws CommandExecutionException;
}
