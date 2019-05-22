package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Response;

class SIRevertCommand extends CommandBase implements IWorkingFileCompatibleCommand {
   private String sandbox = null;
   private String cwd = null;
   private Boolean overwriteChanged = null;
   private Boolean overwriteDeferred = null;

   SIRevertCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      if (selection != null && selection.size() != 0) {
         Command myCommand = new Command("si", "scc-revert");
         OptionList options = this.getBaseOptions();
         if (this.overwriteChanged != null) {
            options.add(this.createBinaryOption("overwriteChanged", this.overwriteChanged));
         }

         if (this.overwriteDeferred != null) {
            options.add(this.createBinaryOption("overwriteDeferred", this.overwriteDeferred));
         }

         if (this.interactive) {
            options.add(new Option("g"));
         }

         if (this.sandbox != null) {
            options.add("sandbox", this.sandbox);
         }

         if (this.cwd != null) {
            options.add("cwd", this.cwd);
         }

         options.add(new Option("recurse"));
         myCommand.setOptionList(options);
         myCommand.setSelectionList(selection);
         return this.runAPICommand(myCommand);
      } else {
         throw new InvalidCommandSelectionException("SICommands.siRevertMembers: parameter 'members' cannot be null or empty.");
      }
   }

   public void setSandbox(String sandbox) {
      this.sandbox = sandbox;
   }

   public void setCwd(String cwd) {
      this.cwd = cwd;
   }

   public void setOverwriteIfChanged(boolean overwrite) {
      this.overwriteChanged = new Boolean(overwrite);
   }

   public void setOverwriteIfDeferred(boolean overwrite) {
      this.overwriteDeferred = new Boolean(overwrite);
   }
}
