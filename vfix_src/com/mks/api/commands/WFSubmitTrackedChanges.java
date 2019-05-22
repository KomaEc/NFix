package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileFactory;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.util.HashSet;
import java.util.Iterator;

class WFSubmitTrackedChanges extends WFTrackCommandBase {
   WFSubmitTrackedChanges(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      WorkingFileList toBeReverted = new WorkingFileList();
      WorkingFileList toBeAdded = new WorkingFileList();
      WorkingFileList toBeDropped = new WorkingFileList();
      WorkingFileList toBeLocked = new WorkingFileList();
      WorkingFileList toBeCheckedIn = new WorkingFileList();
      WorkingFileList toBeSubmitted = new WorkingFileList();
      WorkingFileList toBeCheckedInDeferred = new WorkingFileList();
      HashSet emptySandboxCandidates = new HashSet();
      HashSet knownNonemptySandboxes = new HashSet();
      Iterator i = workingFiles.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         if (wf.isAdded() || !wf.isControlled() && wf.getFile().exists()) {
            toBeAdded.add(wf);
         }

         if (!wf.getFile().exists() && wf.isMember()) {
            toBeDropped.add(wf);
         }

         if (wf.isDeferred() && (!wf.isLockedByMe() || wf.isMoved()) && !toBeAdded.getList().contains(wf) && !toBeDropped.getList().contains(wf)) {
            toBeSubmitted.add(wf);
         }

         if (!wf.isMoved() && !wf.isDropped()) {
            knownNonemptySandboxes.add(wf.getSandbox());
         } else {
            emptySandboxCandidates.add(wf.getSandbox());
         }

         if (wf.isModified() && !wf.isLockedByMe() && !wf.isDropped() && wf.getFile().exists()) {
            toBeLocked.add(wf);
            toBeCheckedIn.add(wf);
         }

         if (wf.isLockedByMe() && !this.isOutOfDateMove(wf)) {
            if (wf.hasWorkingDelta() && !wf.isModified()) {
               wf = WorkingFileFactory.refreshWorkingFile(this.getCmdRunnerCreator(), wf.getName());
            }

            if (!wf.isModified() && !wf.isMoved()) {
               toBeReverted.add(wf);
            } else {
               toBeCheckedIn.add(wf);
            }
         }

         if (this.isOutOfDateMove(wf) && wf.isLockedByMe()) {
            toBeCheckedInDeferred.add(wf);
         }
      }

      Response r = this.revertFiles(toBeReverted);
      if (r != null && r.getAPIException() != null) {
         return r;
      } else {
         r = this.addFiles(toBeAdded);
         if (r != null && r.getAPIException() != null) {
            return r;
         } else {
            r = this.setWritable(toBeAdded);
            if (r != null && r.getAPIException() != null) {
               return r;
            } else {
               r = this.dropFiles(toBeDropped);
               if (r != null && r.getAPIException() != null) {
                  return r;
               } else {
                  r = this.lockFiles(toBeLocked);
                  if (r != null && r.getAPIException() != null) {
                     return r;
                  } else {
                     r = this.checkInFiles(toBeCheckedIn);
                     if (r != null && r.getAPIException() != null) {
                        return r;
                     } else {
                        r = this.checkInFilesDeferred(toBeCheckedInDeferred);
                        if (r != null && r.getAPIException() != null) {
                           return r;
                        } else {
                           r = this.submitFiles(toBeSubmitted);
                           if (r != null && r.getAPIException() != null) {
                              return r;
                           } else {
                              emptySandboxCandidates.removeAll(knownNonemptySandboxes);
                              return null;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private Response revertFiles(WorkingFileList toBeReverted) throws APIException {
      SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
      siRevert.setOverwriteIfChanged(false);
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siRevert);
      return runner.execute(toBeReverted, this.interactive);
   }

   private Response addFiles(WorkingFileList toBeAdded) throws APIException {
      if (toBeAdded.isEmpty()) {
         return null;
      } else {
         SIAddCommand siAdd = new SIAddCommand(this.getCmdRunnerCreator());
         siAdd.setCpid(this.cpid);
         siAdd.setDeferred(false);
         siAdd.setCloseCP(false);
         GenericWFCommandRunner wfAdd = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siAdd);
         return wfAdd.execute(toBeAdded, this.interactive);
      }
   }

   private Response setWritable(WorkingFileList added) throws APIException {
      if (added.isEmpty()) {
         return null;
      } else {
         WorkingFileList toSetWritable = this.getWFListToSetWritable(added);
         if (toSetWritable.isEmpty()) {
            return null;
         } else {
            SISetMemberWritableCommand siSetMemberWritable = new SISetMemberWritableCommand(this.getCmdRunnerCreator());
            GenericWFCommandRunner wfSetMemberWritable = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siSetMemberWritable);
            return wfSetMemberWritable.execute(toSetWritable, this.interactive);
         }
      }
   }

   private WorkingFileList getWFListToSetWritable(WorkingFileList unfiltered) {
      WorkingFileList filtered = new WorkingFileList();
      String suffixProperties = System.getProperties().getProperty("mks.api.setwritableonsubmit", "exe,bat,dll");
      suffixProperties = suffixProperties.toLowerCase();
      String[] suffixArray = suffixProperties.split(",");
      Iterator it = unfiltered.iterator();

      while(it.hasNext()) {
         WorkingFile wf = (WorkingFile)it.next();

         for(int i = 0; i < suffixArray.length; ++i) {
            String suffix = suffixArray[i].trim();
            if (wf.getName().toLowerCase().endsWith("." + suffix)) {
               filtered.add(wf);
            }
         }
      }

      return filtered;
   }

   private Response dropFiles(WorkingFileList toBeDropped) throws APIException {
      if (toBeDropped.isEmpty()) {
         return null;
      } else {
         SIDropCommand siDrop = new SIDropCommand(this.getCmdRunnerCreator());
         siDrop.setCpid(this.cpid);
         siDrop.setDeferred(false);
         siDrop.setCloseCP(false);
         GenericWFCommandRunner wfDrop = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siDrop);
         return wfDrop.execute(toBeDropped, this.interactive);
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

   private Response checkInFiles(WorkingFileList toBeCheckedIn) throws APIException {
      if (toBeCheckedIn.isEmpty()) {
         return null;
      } else {
         SICheckinCommand siCheckin = new SICheckinCommand(this.getCmdRunnerCreator());
         siCheckin.setCpid(this.cpid);
         siCheckin.setCloseCP(false);
         siCheckin.setDeferred(false);
         GenericWFCommandRunner wfCheckin = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siCheckin);
         return wfCheckin.execute(toBeCheckedIn, this.interactive);
      }
   }

   private Response checkInFilesDeferred(WorkingFileList toBeCheckedIn) throws APIException {
      if (toBeCheckedIn.isEmpty()) {
         return null;
      } else {
         SICheckinCommand siCheckin = new SICheckinCommand(this.getCmdRunnerCreator());
         siCheckin.setCpid(this.cpid);
         siCheckin.setCloseCP(false);
         siCheckin.setDeferred(true);
         GenericWFCommandRunner wfCheckin = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siCheckin);
         return wfCheckin.execute(toBeCheckedIn, this.interactive);
      }
   }

   private Response submitFiles(WorkingFileList toBeSubmitted) throws APIException {
      if (toBeSubmitted.isEmpty()) {
         return null;
      } else {
         SISubmitCommand siSubmit = new SISubmitCommand(this.getCmdRunnerCreator());
         siSubmit.setCpid(this.cpid);
         siSubmit.setGuiStatus(true);
         siSubmit.setCloseCP(false);
         GenericWFCommandRunner wfSubmit = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siSubmit);
         return wfSubmit.execute(toBeSubmitted, false);
      }
   }

   private boolean isOutOfDateMove(WorkingFile wf) {
      return wf.isMoved() && wf.getWorkingRev() != null && !wf.getWorkingRev().equals(wf.getMemberRev());
   }
}
