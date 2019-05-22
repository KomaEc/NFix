package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

class SICheckinCommand extends TrackingCommandBase {
   SICheckinCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      Command cmd = new Command("ii", "checkin");
      OptionList options = this.getBaseOptions();
      options.add(this.getTrackableCommandOptions());
      if (this.interactive) {
         options.add(new Option("g"));
      }

      if (selection != null && selection.size() > 0) {
         options.add(new Option("recurse"));
      }

      cmd.setOptionList(options);
      cmd.setSelectionList(selection);
      return this.runAPICommand(cmd);
   }
}
