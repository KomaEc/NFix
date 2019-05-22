package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Response;

class SISetMemberWritableCommand extends TrackingCommandBase {
   SISetMemberWritableCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      if (selection != null && selection.size() != 0) {
         Command myCommand = new Command("si", "si.HooksSetAlwaysWritable");
         myCommand.setSelectionList(selection);
         return this.runAPICommand(myCommand);
      } else {
         throw new InvalidCommandSelectionException("The SISetMemberWritable command requires a selection.");
      }
   }
}
