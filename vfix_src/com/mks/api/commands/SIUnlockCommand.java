package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

class SIUnlockCommand extends CommandBase implements IWorkingFileCompatibleCommand {
   private String sandbox;
   private String cwd;
   private String action;

   SIUnlockCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      Command cmd = new Command("si", "unlock");
      OptionList options = this.getBaseOptions();
      if (this.interactive) {
         options.add(new Option("g"));
      }

      if (this.sandbox != null) {
         options.add("sandbox", this.sandbox);
      }

      if (this.cwd != null) {
         options.add("cwd", this.cwd);
      }

      if (this.action != null) {
         options.add("action", this.action);
      }

      cmd.setOptionList(options);
      cmd.setSelectionList(selection);
      return this.runAPICommand(cmd);
   }

   public void setSandbox(String sandbox) {
      this.sandbox = sandbox;
   }

   public void setCwd(String cwd) {
      this.cwd = cwd;
   }

   public void setAction(String action) {
      this.action = action;
   }
}
