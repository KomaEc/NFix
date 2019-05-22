package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.util.Iterator;

class WFTrackAddToCP extends WFTrackCommandBase {
   WFTrackAddToCP(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      WorkingFileList toBeReverted = new WorkingFileList();
      WorkingFileList toBeAdded = new WorkingFileList();
      WorkingFileList toBeDropped = new WorkingFileList();
      WorkingFileList toBeMoved = new WorkingFileList();
      WorkingFileList toBeUnlocked = new WorkingFileList();
      WorkingFileList toBeLocked = new WorkingFileList();
      Iterator i = workingFiles.iterator();

      while(true) {
         while(i.hasNext()) {
            WorkingFile wf = (WorkingFile)i.next();
            if (this.cpid == null) {
               if (wf.getWorkingCpid() != null && wf.getWorkingCpid().length() != 0) {
                  toBeReverted.add(wf);
               }
            } else if (!this.cpid.equals(wf.getWorkingCpid())) {
               boolean revert = false;
               if (!wf.isControlled() && wf.getFile().exists()) {
                  toBeAdded.add(wf);
               }

               if (!wf.getFile().exists()) {
                  toBeDropped.add(wf);
               }

               if (wf.isAdded()) {
                  toBeAdded.add(wf);
                  revert = true;
               }

               if (wf.isDropped()) {
                  toBeDropped.add(wf);
                  revert = true;
               }

               if (wf.isMoved()) {
                  throw new APIException("INVALID_MOVE_TO_CP_OPERATION");
               }

               if (revert) {
                  toBeReverted.add(wf);
               }

               if (wf.isLockedByMe()) {
                  toBeUnlocked.add(wf);
               }

               if (!wf.isDropped() && (wf.isModified() || wf.isLockedByMe())) {
                  toBeLocked.add(wf);
               }
            }
         }

         Response r = this.revertDeferred(toBeReverted, false);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         r = this.unlockFiles(toBeUnlocked);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         r = this.dropFiles(toBeDropped);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         r = this.addFiles(toBeAdded);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         r = this.moveFiles(toBeMoved);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         r = this.lockFiles(toBeLocked);
         if (r != null && r.getAPIException() != null) {
            return r;
         }

         return null;
      }
   }

   private Response dropFiles(WorkingFileList toBeDropped) throws APIException {
      if (toBeDropped.isEmpty()) {
         return null;
      } else {
         WFTrackDeletedFile wfDeleted = new WFTrackDeletedFile(this.getCmdRunnerCreator());
         wfDeleted.setCpid(this.cpid);
         return wfDeleted.execute(this.update(toBeDropped), this.interactive);
      }
   }

   private Response addFiles(WorkingFileList toBeAdded) throws APIException {
      if (toBeAdded.isEmpty()) {
         return null;
      } else {
         WFTrackNewFile wfNew = new WFTrackNewFile(this.getCmdRunnerCreator());
         wfNew.setCpid(this.cpid);
         wfNew.setPreferredRoot(this.getPreferredRoot());
         return wfNew.execute(this.update(toBeAdded), this.interactive);
      }
   }

   private Response moveFiles(WorkingFileList toBeMoved) throws APIException {
      if (toBeMoved.isEmpty()) {
         return null;
      } else {
         WFTrackMovedFile wfMoved = new WFTrackMovedFile(this.getCmdRunnerCreator());
         wfMoved.setCpid(this.cpid);
         wfMoved.setPreferredRoot(this.getPreferredRoot());
         String[] fromNames = new String[toBeMoved.size()];
         String[] toNames = new String[toBeMoved.size()];
         int index = 0;

         for(Iterator i = toBeMoved.iterator(); i.hasNext(); ++index) {
            WorkingFile wf = (WorkingFile)i.next();
            fromNames[index] = wf.getMemberName().getAbsolutePath();
            toNames[index] = wf.getFile().getAbsolutePath();
         }

         wfMoved.setMapping(fromNames, toNames);
         return wfMoved.execute(fromNames, this.interactive);
      }
   }

   private Response unlockFiles(WorkingFileList toBeUnlocked) throws APIException {
      if (toBeUnlocked.isEmpty()) {
         return null;
      } else {
         SIUnlockCommand siUnlock = new SIUnlockCommand(this.getCmdRunnerCreator());
         siUnlock.setAction("remove");
         GenericWFCommandRunner wfUnlock = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siUnlock);
         return wfUnlock.execute(toBeUnlocked, this.interactive);
      }
   }

   private Response lockFiles(WorkingFileList toBeLocked) throws APIException {
      if (toBeLocked.isEmpty()) {
         return null;
      } else {
         SILockCommand siLock = new SILockCommand(this.getCmdRunnerCreator());
         siLock.setCpid(this.cpid);
         siLock.setAllowPrompting(false);
         GenericWFCommandRunner wfLock = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siLock);
         return wfLock.execute(toBeLocked, this.interactive);
      }
   }
}
