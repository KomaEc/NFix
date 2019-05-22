package com.mks.api.commands.ide;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.SICommands;
import com.mks.api.response.APIException;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class WorkingDirectoryFactory {
   private WorkingDirectoryFactory() {
   }

   public static WorkingDirectory getWorkingDirectory(CmdRunnerCreator session, File directory) throws APIException {
      SICommands si = new SICommands(session, false);
      String directoryPath = directory.getAbsolutePath();
      WorkingDirectory workingDirectory = new WorkingDirectory(directory);

      try {
         ISandboxInfo info = SandboxInfo.getSandboxInfo(si, directory);
         if (info != null) {
            workingDirectory.addSandbox(info);
            WorkingFileCache.getInstance().monitorSandbox(si.getCmdRunnerCreator(), info.getSandboxName());
         }

         return workingDirectory;
      } catch (APIException var14) {
         if (!"common.AmbiguousParentSandbox".equals(var14.getExceptionId())) {
            throw var14;
         } else {
            Set closestSandboxes = null;
            File closestSandboxDir = null;
            String directoryTestPath = directoryPath;
            if (isWin32()) {
               directoryTestPath = directoryPath.toLowerCase();
            }

            WorkItemIterator wii = si.getSandboxes(true).getWorkItems();

            String sandboxName;
            while(wii.hasNext()) {
               WorkItem wi = wii.next();
               sandboxName = wi.getField("sandboxName").getValueAsString();
               File sandboxLocation = (new File(sandboxName)).getParentFile();
               String testPath = sandboxLocation.getAbsolutePath();
               if (isWin32()) {
                  testPath = testPath.toLowerCase();
               }

               int closestSandboxPathLength = closestSandboxDir != null ? closestSandboxDir.getPath().length() : 0;
               if (testPath.length() > closestSandboxPathLength && directoryTestPath.startsWith(testPath)) {
                  closestSandboxDir = sandboxLocation;
                  closestSandboxes = new HashSet();
               }

               if (sandboxLocation.equals(closestSandboxDir)) {
                  closestSandboxes.add(sandboxName);
               }
            }

            if (closestSandboxes != null) {
               Iterator it = closestSandboxes.iterator();

               while(it.hasNext()) {
                  sandboxName = (String)it.next();
                  ISandboxInfo info = SandboxInfo.getSandboxInfo(si, new File(sandboxName));
                  if (info != null) {
                     workingDirectory.addSandbox(info);
                  }
               }
            }

            WorkingFileCache.getInstance().monitorDirectory(si.getCmdRunnerCreator(), workingDirectory);
            return workingDirectory;
         }
      }
   }

   static boolean isWin32() {
      return WorkingFileFactory.isWin32();
   }

   public static void refresh(File directory) {
      refresh(directory, WorkingFileFactory.getNewOrdinal());
   }

   public static void refresh(File directory, long ordinal) {
      WorkingFileCache.getInstance().invalidate(directory, ordinal);
   }
}
