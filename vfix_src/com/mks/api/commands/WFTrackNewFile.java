package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.util.Iterator;

class WFTrackNewFile extends WFTrackCommandBase {
   private boolean binary;

   WFTrackNewFile(CmdRunnerCreator session, boolean binary) throws APIException {
      super(session);
      this.binary = binary;
   }

   WFTrackNewFile(CmdRunnerCreator session) throws APIException {
      this(session, false);
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      WorkingFileList toBeAdded = new WorkingFileList();
      WorkingFileList toBeReverted = new WorkingFileList();
      Iterator iWorkingFiles = workingFiles.iterator();

      while(true) {
         while(true) {
            WorkingFile wf;
            do {
               if (!iWorkingFiles.hasNext()) {
                  if (!toBeReverted.isEmpty()) {
                     Response r = this.revertDeferred(toBeReverted, true);
                     if (r != null && r.getAPIException() != null) {
                        return r;
                     }
                  }

                  if (!toBeAdded.isEmpty()) {
                     SIAddCommand siAdd = new SIAddCommand(this.getCmdRunnerCreator());
                     siAdd.setDeferred(true);
                     siAdd.setCpid(this.cpid);
                     siAdd.setBinary(this.binary);
                     siAdd.setCloseCP(false);
                     siAdd.setAllowCreateSubs(this.allowCreateSubs);
                     Response[] response = null;
                     response = this.runApiCommand(siAdd, toBeAdded, this.interactive);

                     for(int j = 0; j < response.length; ++j) {
                        if (response[j] != null && response[j].getAPIException() != null) {
                           return response[j];
                        }
                     }
                  }

                  return null;
               }

               wf = (WorkingFile)iWorkingFiles.next();
            } while(!wf.isInSandboxDir());

            if (wf.isControlled() && !wf.isFormerMember() && wf.isDropped()) {
               toBeReverted.add(wf);
            } else if (!wf.isMember()) {
               toBeAdded.add(wf);
            }
         }
      }
   }
}
