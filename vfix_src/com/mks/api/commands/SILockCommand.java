package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.Option;
import com.mks.api.SelectionList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;

class SILockCommand extends CommandBase implements IWorkingFileCompatibleCommand, IHasChangePackage {
   private String sandbox = null;
   private Boolean closeCP = null;
   private String cpid = null;
   private String cwd = null;
   private Boolean allowPrompting = null;
   private String action = null;
   private String lockType = null;

   SILockCommand(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(SelectionList selection) throws APIException {
      Command cmd = new Command("si", "lock");
      if (this.interactive) {
         if (this.allowPrompting != null && this.allowPrompting) {
            cmd.addOption(new Option("settingsUI", "gui"));
         } else {
            cmd.addOption(new Option("status", "gui"));
         }
      }

      if (this.sandbox != null) {
         cmd.addOption(new Option("sandbox", this.sandbox));
      }

      if (this.cwd != null) {
         cmd.addOption(new Option("cwd", this.cwd));
      }

      if (this.cpid != null) {
         cmd.addOption(new Option("cpid", this.cpid));
      }

      cmd.addOption(new Option("recurse"));
      if (this.action != null) {
         cmd.addOption(new Option("action", this.action));
      }

      if (this.lockType != null) {
         cmd.addOption(new Option("lockType", this.lockType));
      }

      cmd.setSelectionList(selection);
      return this.runAPICommand(cmd);
   }

   public void setAllowPrompting(boolean allowPrompting) {
      this.allowPrompting = new Boolean(allowPrompting);
   }

   public void setAction(String action) {
      this.action = action;
   }

   public void setLockType(String lockType) {
      this.lockType = lockType;
   }

   public void setSandbox(String sandbox) {
      this.sandbox = sandbox;
   }

   public void setCwd(String cwd) {
      this.cwd = cwd;
   }

   public boolean isCloseCPOverridden() {
      return this.closeCP != null;
   }

   public void resetCloseCP() {
      this.closeCP = null;
   }

   public void setCloseCP(boolean closeCP) {
      this.closeCP = new Boolean(closeCP);
   }

   public void setCpid(String cpid) {
      this.cpid = cpid;
   }
}
