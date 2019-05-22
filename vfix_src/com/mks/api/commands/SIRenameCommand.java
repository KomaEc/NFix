package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Response;

class SIRenameCommand extends TrackingCommandBase {
   private String newName;
   private Boolean renameFile;

   SIRenameCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      if (selection != null && selection.size() == 1) {
         OptionList options = this.getBaseOptions();
         options.add(this.getTrackableCommandOptions());
         if (this.interactive) {
            options.add(new Option("g"));
         }

         options.add("newName", this.newName);
         if (this.renameFile != null) {
            options.add(this.createBinaryOption("renameWorkingFile", this.renameFile));
         }

         Command cmd = new Command("si", "rename");
         cmd.setOptionList(options);
         cmd.setSelectionList(selection);
         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("The SI Rename command requires a selection of exactly one member.");
      }
   }

   public void setNewName(String newName) {
      this.newName = newName;
   }

   public void setRenameFile(boolean rename) {
      this.renameFile = new Boolean(rename);
   }
}
