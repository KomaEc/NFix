package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.response.APIException;

abstract class TrackingCommandBase extends CommandBase implements IHasChangePackage, IWorkingFileCompatibleCommand {
   private String sandbox = null;
   private String cwd = null;
   private String cpid = null;
   private Boolean closeCP = null;
   private Boolean deferred = null;
   private boolean allowCreateSubs = true;

   protected TrackingCommandBase(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   public void setCloseCP(boolean closeCP) {
      this.closeCP = new Boolean(closeCP);
   }

   public boolean isCloseCPOverridden() {
      return this.closeCP != null;
   }

   public void resetCloseCP() {
      this.closeCP = null;
   }

   public void setCpid(String cpid) {
      this.cpid = cpid;
   }

   public void setSandbox(String sandbox) {
      this.sandbox = sandbox;
   }

   public void setCwd(String cwd) {
      this.cwd = cwd;
   }

   public void setAllowCreateSubs(boolean allowCreateSubs) {
      this.allowCreateSubs = allowCreateSubs;
   }

   public void setDeferred(boolean deferred) {
      this.deferred = new Boolean(deferred);
   }

   protected OptionList getTrackableCommandOptions() {
      OptionList options = new OptionList();
      if (this.sandbox != null) {
         options.add("sandbox", this.sandbox);
      }

      if (this.cwd != null) {
         options.add("cwd", this.cwd);
      }

      if (this.cpid != null) {
         options.add("cpid", this.cpid);
      }

      if (this.deferred != null && this.deferred) {
         options.add(new Option("defer"));
      }

      if (this.deferred != null && !this.deferred) {
         options.add(new Option("nodefer"));
      }

      if (!this.allowCreateSubs) {
         options.add(new Option("nocreateSubprojects"));
      }

      if (this.closeCP != null) {
         options.add(this.createBinaryOption("closecp", this.closeCP));
      }

      return options;
   }
}
