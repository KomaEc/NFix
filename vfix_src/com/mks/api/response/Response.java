package com.mks.api.response;

public interface Response extends WorkItemContainer, SubRoutineContainer {
   int getExitCode() throws InterruptedException;

   String getApplicationName();

   String getCommandName();

   String getCommandString();

   String getConnectionHostname();

   int getConnectionPort();

   String getConnectionUsername();

   Result getResult() throws InterruptedException;

   boolean getCacheContents();

   void interrupt();

   APIException getAPIException() throws InterruptedException;

   void release() throws APIException;
}
