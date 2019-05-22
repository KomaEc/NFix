package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.util.Iterator;

class WFTrackDeletedFile extends WorkingFileCommandBase {
   private String cpid = null;

   WFTrackDeletedFile(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      WorkingFileList toBeDropped = new WorkingFileList();
      WorkingFileList toBeReverted = new WorkingFileList();
      Iterator iWorkingFiles = workingFiles.iterator();

      while(iWorkingFiles.hasNext()) {
         WorkingFile wf = (WorkingFile)iWorkingFiles.next();
         if (wf.isDeferred() || wf.isLockedByMe()) {
            toBeReverted.add(wf);
         }

         if (wf.isMember()) {
            toBeDropped.add(wf);
         }
      }

      Response[] response = null;
      int j;
      if (toBeReverted.size() > 0) {
         SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
         response = this.runApiCommand(siRevert, toBeReverted, this.interactive);

         for(j = 0; j < response.length; ++j) {
            if (response[j] != null && response[j].getAPIException() != null) {
               return response[j];
            }
         }
      }

      if (toBeDropped.size() > 0) {
         SIDropCommand siDrop = new SIDropCommand(this.getCmdRunnerCreator());
         siDrop.setDeferred(true);
         siDrop.setCloseCP(false);
         siDrop.setCpid(this.cpid);
         response = this.runApiCommand(siDrop, toBeDropped, this.interactive);

         for(j = 0; j < response.length; ++j) {
            if (response[j] != null && response[j].getAPIException() != null) {
               return response[j];
            }
         }
      }

      return null;
   }

   public void setCpid(String cpid) {
      this.cpid = cpid;
   }
}
