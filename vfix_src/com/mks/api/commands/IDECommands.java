package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.response.APIException;

class IDECommands extends SICommands {
   IDECommands(CmdRunnerCreator session, boolean isInteractive) throws APIException {
      super(session, isInteractive);
   }
}
