package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.WorkingFileFactory;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.File;
import java.util.List;

public class WFCommands extends IDECommands {
   public WFCommands(CmdRunnerCreator session, boolean isInteractive) throws APIException {
      super(session, isInteractive);
   }

   public List getStatus(List fileNames) {
      WorkingFileList workingFiles = WorkingFileFactory.getWorkingFiles((SICommands)this, fileNames);
      return workingFiles.getList();
   }

   public Response resync(String[] fileNames) throws APIException {
      SIResyncCommand siResync = new SIResyncCommand(this.getCmdRunnerCreator());
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siResync);
      return runner.execute(fileNames, this.isInteractive);
   }

   public Response resync(List fileNames, boolean overwriteChanged, boolean overwriteDeferred, boolean overwritePending, boolean merge, String mergeType, String mergeConflict, boolean downgradeOnConflict) throws APIException {
      SIResyncCommand siResync = new SIResyncCommand(this.getCmdRunnerCreator());
      siResync.setOverwrite(overwriteChanged);
      siResync.setOverwriteDeferred(overwriteDeferred);
      siResync.setOverwritePending(overwritePending);
      siResync.setMerge(merge);
      siResync.setMergeConflict("launchtool");
      siResync.setMergeType("automatic");
      siResync.setDowngradeOnLockConflict(downgradeOnConflict);
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siResync);
      return runner.execute(WorkingFileFactory.getWorkingFiles(this.getCmdRunnerCreator(), fileNames), this.isInteractive);
   }

   public Response checkOut(List fileNames, boolean overwriteChanged, boolean overwriteDeferred, boolean lock, boolean merge, String mergeType, String mergeConflict) throws APIException {
      SICheckOutCommand siCO = new SICheckOutCommand(this.getCmdRunnerCreator());
      siCO.setOverwrite(overwriteChanged);
      siCO.setOverwriteDeferred(overwriteDeferred);
      siCO.setMerge(merge);
      siCO.setMergeConflict("launchtool");
      siCO.setMergeType("automatic");
      siCO.setLock(lock);
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siCO);
      return runner.execute(WorkingFileFactory.getWorkingFiles(this.getCmdRunnerCreator(), fileNames), this.isInteractive);
   }

   public Response revert(String[] fileNames) throws APIException {
      SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siRevert);
      return runner.execute(fileNames, this.isInteractive);
   }

   public Response revert(List fileNames, boolean overwriteDeferred, boolean overwriteChanged) throws APIException {
      SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
      siRevert.setOverwriteIfChanged(overwriteChanged);
      siRevert.setOverwriteIfDeferred(overwriteDeferred);
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siRevert);
      return runner.execute(WorkingFileFactory.getWorkingFiles(this.getCmdRunnerCreator(), fileNames), this.isInteractive);
   }

   public Response lockFile(String workingFilePath, String cpid, boolean isUpgrade) throws APIException {
      SILockCommand siLock = new SILockCommand(this.getCmdRunnerCreator());
      siLock.setCpid(cpid);
      if (isUpgrade) {
         siLock.setAction("upgrade");
      }

      siLock.setAllowPrompting(false);
      GenericWFCommandRunner runner = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siLock);
      return runner.execute(new String[]{workingFilePath}, this.isInteractive);
   }

   public Response newFile(String workingFilePath, String cpid, boolean binary, boolean allowCreateSubs) throws APIException {
      WFTrackCommandBase trackNewFile = new WFTrackNewFile(this.getCmdRunnerCreator(), binary);
      trackNewFile.setCpid(cpid);
      trackNewFile.setAllowCreateSubs(allowCreateSubs);
      return trackNewFile.execute(new String[]{workingFilePath}, this.isInteractive);
   }

   public Response newFiles(String[] workingFiles, String cpid, File sandboxRoot) throws APIException {
      WFTrackCommandBase trackNewFile = new WFTrackNewFile(this.getCmdRunnerCreator());
      trackNewFile.setCpid(cpid);
      trackNewFile.setPreferredRoot(sandboxRoot);
      return trackNewFile.execute(workingFiles, this.isInteractive);
   }

   public Response deletedFile(String workingFilePath, String cpid) throws APIException {
      return this.deletedFiles(new String[]{workingFilePath}, cpid);
   }

   public Response deletedFiles(String[] workingFiles, String cpid) throws APIException {
      WFTrackDeletedFile trackDeletedFile = new WFTrackDeletedFile(this.getCmdRunnerCreator());
      trackDeletedFile.setCpid(cpid);
      return trackDeletedFile.execute(workingFiles, this.isInteractive);
   }

   public Response movedFile(String from, String to, String cpid, boolean allowCreateSubs) throws APIException {
      String[] from1 = new String[]{from};
      WFTrackMovedFile trackMovedFile = new WFTrackMovedFile(this.getCmdRunnerCreator());
      trackMovedFile.setCpid(cpid);
      trackMovedFile.setMapping(from1, new String[]{to});
      trackMovedFile.setAllowCreateSubs(allowCreateSubs);
      return trackMovedFile.execute(from1, this.isInteractive);
   }

   public Response movedFile(String from, String to, String cpid, File sandboxRoot) throws APIException {
      String[] from1 = new String[]{from};
      WFTrackMovedFile trackMovedFile = new WFTrackMovedFile(this.getCmdRunnerCreator());
      trackMovedFile.setCpid(cpid);
      trackMovedFile.setPreferredRoot(sandboxRoot);
      trackMovedFile.setMapping(from1, new String[]{to});
      trackMovedFile.setAllowCreateSubs(true);
      return trackMovedFile.execute(from1, this.isInteractive);
   }

   public Response addToCP(String workingFilePath, String destinationCpid) throws APIException {
      return this.addToCP(new String[]{workingFilePath}, destinationCpid, (File)null);
   }

   public Response addToCP(String[] workingFiles, String destinationCpid, File sandboxRoot) throws APIException {
      WFTrackAddToCP trackAddToCP = new WFTrackAddToCP(this.getCmdRunnerCreator());
      trackAddToCP.setCpid(destinationCpid);
      trackAddToCP.setPreferredRoot(sandboxRoot);
      return trackAddToCP.execute(workingFiles, this.isInteractive);
   }

   public Response submitChanges(String[] workingFile, String cpid) throws APIException {
      WFSubmitTrackedChanges wfSubmit = new WFSubmitTrackedChanges(this.getCmdRunnerCreator());
      wfSubmit.setCpid(cpid);
      return wfSubmit.execute(workingFile, this.isInteractive);
   }
}
