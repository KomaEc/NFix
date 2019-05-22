package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

abstract class MKSCommands {
   private CmdRunnerCreator cmdRunnerCreator;

   protected MKSCommands(CmdRunnerCreator session) throws APIException {
      CommandBase.validateConnection(this.cmdRunnerCreator = session);
   }

   public final CmdRunnerCreator getCmdRunnerCreator() {
      return this.cmdRunnerCreator;
   }

   protected final Response runAPICommand(Command command) throws APIException {
      return this.runAPICommand(command, false);
   }

   protected final Response runAPICommand(Command command, boolean generatedStreamedResponse) throws APIException {
      return CommandBase.runAPICommand(this.getCmdRunnerCreator(), command, generatedStreamedResponse);
   }
}
