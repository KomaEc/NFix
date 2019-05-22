package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.SelectionList;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileFactory;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.CommandCancelledException;
import com.mks.api.response.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

abstract class WorkingFileCommandBase extends CommandBase {
   private File preferredSandboxRoot;

   protected WorkingFileCommandBase(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   public final void setPreferredRoot(File rootSandbox) {
      this.preferredSandboxRoot = rootSandbox;
   }

   protected final File getPreferredRoot() {
      return this.preferredSandboxRoot;
   }

   protected abstract Response execute(WorkingFileList var1) throws APIException;

   final Response execute(WorkingFileList workingFiles, boolean isInteractive) throws APIException {
      if (workingFiles == null) {
         throw new APIException(new IllegalArgumentException("Working files cannot be null"));
      } else {
         Response var3;
         try {
            this.interactive = isInteractive;
            var3 = this.execute(this.cleanInvalids(workingFiles));
         } finally {
            workingFiles.invalidate();
         }

         return var3;
      }
   }

   protected WorkingFileList cleanInvalids(WorkingFileList listToClean) {
      WorkingFileList cleanList = new WorkingFileList();
      Iterator i = listToClean.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         if (wf.isInvalid()) {
            cleanList.add(WorkingFileFactory.refreshWorkingFile(this.getCmdRunnerCreator(), wf.getMemberName().getAbsolutePath()));
         } else {
            cleanList.add(wf);
         }
      }

      return cleanList;
   }

   protected final Response execute(SelectionList selection) throws APIException {
      List fileNames = this.extractSelectionList(selection);
      return this.execute(WorkingFileFactory.getWorkingFiles(this.getCmdRunnerCreator(), fileNames), this.interactive);
   }

   protected final Response[] runApiCommand(IWorkingFileCompatibleCommand cmd, WorkingFileList workingFiles, boolean isInteractive, boolean throwExceptions) throws APIException {
      if (workingFiles.isEmpty()) {
         return new Response[0];
      } else {
         Response[] var5;
         try {
            var5 = this._runApiCommand(cmd, workingFiles, isInteractive, throwExceptions);
         } finally {
            workingFiles.invalidate();
         }

         return var5;
      }
   }

   private Response[] _runApiCommand(IWorkingFileCompatibleCommand cmd, WorkingFileList workingFiles, boolean isInteractive, boolean throwExceptions) throws APIException {
      int buckets = 1;
      Iterator workingRoots = null;
      Map workingFileBuckets = workingFiles.getWorkingFileBuckets(this.getCmdRunnerCreator(), this.preferredSandboxRoot);
      if (workingFileBuckets != null && !workingFileBuckets.isEmpty()) {
         Set keys = workingFileBuckets.keySet();
         buckets = keys.size();
         workingRoots = keys.iterator();
      }

      Response[] responses = new Response[buckets];
      int var9 = 0;

      do {
         Response response = null;
         String[] memberNames = null;
         WorkingFileList sbxWorkingFiles = null;
         File workingRoot = workingRoots == null ? null : (File)workingRoots.next();
         File sandboxRootDir = null;
         if (workingRoot == null) {
            memberNames = this.extractMemberNameArray(workingFiles);
            sandboxRootDir = new File(SICommands.getMemberListCWD(memberNames));
         } else {
            sbxWorkingFiles = (WorkingFileList)workingFileBuckets.get(workingRoot);
            memberNames = this.extractMemberNameArray(sbxWorkingFiles);
            if (workingRoot.isDirectory()) {
               sandboxRootDir = workingRoot;
               cmd.setSandbox((String)null);
            } else {
               sandboxRootDir = workingRoot.getParentFile();
               cmd.setSandbox(workingRoot.getAbsolutePath());
            }
         }

         cmd.setCwd(sandboxRootDir.getAbsolutePath());
         if (cmd instanceof IHasChangePackage) {
            IHasChangePackage cpCmd = (IHasChangePackage)cmd;
            if (!cpCmd.isCloseCPOverridden()) {
               if (workingRoots != null && workingRoots.hasNext()) {
                  cpCmd.setCloseCP(false);
               } else {
                  cpCmd.resetCloseCP();
               }
            }
         }

         try {
            response = cmd.execute(memberNames, isInteractive);
            if (response != null && response.getAPIException() != null && response.getAPIException() instanceof CommandCancelledException) {
               throw response.getAPIException();
            }
         } catch (APIException var16) {
            response = var16.getResponse();
            if (throwExceptions) {
               throw var16;
            }
         }

         responses[var9++] = response;
      } while(workingRoots != null && workingRoots.hasNext());

      return responses;
   }

   protected final Response[] runApiCommand(IWorkingFileCompatibleCommand cmd, WorkingFileList workingFiles, boolean isInteractive) throws APIException {
      return this.runApiCommand(cmd, workingFiles, isInteractive, true);
   }

   private List extractSelectionList(SelectionList selection) {
      List list = new ArrayList(selection.size());
      Iterator i = selection.getSelections();

      while(i.hasNext()) {
         list.add(i.next());
      }

      return list;
   }

   private String[] extractMemberNameArray(WorkingFileList workingFiles) {
      String[] memberNames = new String[workingFiles.size()];
      Iterator i = workingFiles.iterator();
      int var4 = 0;

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         if (wf.getMemberName() != null) {
            memberNames[var4++] = wf.getMemberName().getAbsolutePath();
         } else {
            memberNames[var4++] = wf.getFile().getAbsolutePath();
         }
      }

      return memberNames;
   }
}
