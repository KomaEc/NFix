package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Response;

class SIAddCommand extends TrackingCommandBase {
   private boolean binary = false;

   SIAddCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      OptionList options = this.getBaseOptions();
      if (selection != null && selection.size() != 0) {
         Command myCommand = new Command("si", "add");
         if (this.interactive) {
            options.add(new Option("g"));
         }

         if (this.binary) {
            options.add(new Option("--binaryFormat"));
         }

         options.add(this.getTrackableCommandOptions());
         myCommand.setOptionList(options);
         myCommand.setSelectionList(selection);
         return this.runAPICommand(myCommand);
      } else {
         throw new InvalidCommandSelectionException("The SI Add command requires a selection.");
      }
   }

   public void setBinary(boolean binary) {
      this.binary = binary;
   }
}
