package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

class GenericWFCommandRunner extends WorkingFileCommandBase {
   private IWorkingFileCompatibleCommand cmd;

   GenericWFCommandRunner(CmdRunnerCreator session, IWorkingFileCompatibleCommand cmd) throws APIException {
      super(session);
      this.cmd = cmd;
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      Response[] response = this.runApiCommand(this.cmd, workingFiles, this.interactive);

      for(int i = 0; i < response.length; ++i) {
         if (response[i] != null && response[i].getAPIException() != null) {
            return response[i];
         }
      }

      return null;
   }
}
