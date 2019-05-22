package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Response;

class SIDropCommand extends TrackingCommandBase {
   SIDropCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      if (selection != null && selection.size() != 0) {
         Command myCommand = new Command("si", "drop");
         OptionList options = this.getBaseOptions();
         options.add(this.getTrackableCommandOptions());
         options.add(this.createBinaryOption("delete", true));
         if (this.interactive) {
            options.add(new Option("gui"));
         }

         myCommand.setOptionList(options);
         myCommand.setSelectionList(selection);
         return this.runAPICommand(myCommand);
      } else {
         throw new InvalidCommandSelectionException("The SI Drop command requires a selection.");
      }
   }
}
