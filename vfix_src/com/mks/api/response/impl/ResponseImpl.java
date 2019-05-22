package com.mks.api.response.impl;

import com.mks.api.CmdRunner;
import com.mks.api.response.APIException;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Result;
import com.mks.api.response.modifiable.ModifiableResponse;

public class ResponseImpl extends DelegatedResponseContainer implements ModifiableResponse {
   private String app;
   private String commandName;
   private String commandString;
   private CmdRunner cmdRunner;

   protected ResponseImpl(ResponseContainer rc, String app, String cmdName) {
      this((CmdRunner)null, rc, app, cmdName);
   }

   protected ResponseImpl(CmdRunner cmdRunner, ResponseContainer rc, String app, String cmdName) {
      super(rc);
      this.cmdRunner = cmdRunner;
      this.app = app;
      this.commandName = cmdName;
   }

   public synchronized void release() throws APIException {
      if (this.cmdRunner != null) {
         try {
            this.cmdRunner.interrupt();
            this.cmdRunner.release();
         } finally {
            this.cmdRunner = null;
         }
      }

   }

   public void setApplicationName(String appName) {
      this.app = appName;
   }

   public String getCommandString() {
      return this.commandString;
   }

   public void setCommandString(String commandString) {
      this.commandString = commandString;
   }

   public String getCommandName() {
      return this.commandName;
   }

   public String getApplicationName() {
      return this.app;
   }

   public Result getResult() throws InterruptedException {
      return this.rc.getResult();
   }

   public APIException getAPIException() throws InterruptedException {
      return this.rc.getAPIException();
   }

   public void interrupt() {
   }
}
