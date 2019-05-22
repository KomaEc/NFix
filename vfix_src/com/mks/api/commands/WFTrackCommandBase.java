package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileFactory;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.ItemNotFoundException;
import com.mks.api.response.Response;
import java.util.ArrayList;
import java.util.Iterator;

abstract class WFTrackCommandBase extends WorkingFileCommandBase {
   protected String cpid = null;
   protected boolean allowCreateSubs = true;

   WFTrackCommandBase(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response revertDeferred(WorkingFileList toBeReverted, boolean overwriteIfDeferred) throws APIException {
      if (toBeReverted.size() == 0) {
         return null;
      } else {
         SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
         siRevert.setOverwriteIfDeferred(overwriteIfDeferred);
         siRevert.setOverwriteIfChanged(false);

         try {
            Response[] response = this.runApiCommand(siRevert, toBeReverted, this.interactive);

            for(int j = 0; j < response.length; ++j) {
               if (response[j] != null && response[j].getAPIException() != null) {
                  return response[j];
               }
            }

            return null;
         } catch (ItemNotFoundException var6) {
            return null;
         }
      }
   }

   public void setCpid(String cpid) {
      this.cpid = cpid;
   }

   public void setAllowCreateSubs(boolean allowCreateSubs) {
      this.allowCreateSubs = allowCreateSubs;
   }

   protected WorkingFileList update(WorkingFileList outdated) throws APIException {
      ArrayList fileNames = new ArrayList();
      Iterator i = outdated.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         fileNames.add(wf.getFile().getAbsolutePath());
      }

      return WorkingFileFactory.getWorkingFiles((CmdRunnerCreator)this.getCmdRunnerCreator(), fileNames);
   }
}
