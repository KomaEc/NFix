package com.mks.api.commands.ide;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.response.APIException;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WorkingFileCache {
   private static final long serialVersionUID = 1L;
   private static WorkingFileCache singleton = null;
   private static InheritableThreadLocal tempInstance = new InheritableThreadLocal();
   private Map cache = new HashMap();

   protected WorkingFileCache() {
   }

   static WorkingFileCache getInstance() {
      return getInstance(false);
   }

   static synchronized WorkingFileCache getInstance(boolean retain) {
      WorkingFileCache cache = singleton;
      if (cache == null) {
         cache = (WorkingFileCache)tempInstance.get();
         if (cache == null) {
            cache = new WorkingFileCache();
         }
      }

      if (retain) {
         tempInstance.set(cache);
      }

      return cache;
   }

   protected static synchronized void setInstance(WorkingFileCache instance) {
      singleton = instance;
   }

   final synchronized void addWorkingFile(CmdRunnerCreator session, WorkingFile wf) {
      WorkingFile cachedWF = (WorkingFile)this.cache.get(wf.getFile());
      if (cachedWF == null || cachedWF.getOrdinal() <= wf.getOrdinal()) {
         this.cache.put(wf.getFile(), wf);

         try {
            this.monitorSandbox(session, wf.getSandbox());
         } catch (APIException var8) {
            IntegrationPointFactory.getLogger().exception("WARNING", 0, var8);
            wf.invalidate();
         } finally {
            this.notifyAdded(wf);
         }
      }

   }

   protected void notifyAdded(WorkingFile addedWF) {
   }

   final synchronized WorkingFile getWorkingFile(File file) {
      return (WorkingFile)this.cache.get(file);
   }

   final synchronized void removeWorkingFile(File file) {
      WorkingFile wf = (WorkingFile)this.cache.remove(file);
      if (wf != null) {
         this.notifyRemoved(wf);
      }

   }

   protected void notifyRemoved(WorkingFile removedWF) {
   }

   private synchronized WorkingFileList getWorkingFiles(File directory) {
      WorkingFileList wfList = new WorkingFileList();
      String dirPath = directory.getAbsolutePath();
      if (WorkingFileFactory.isWin32()) {
         dirPath = dirPath.toLowerCase();
      }

      Iterator it = this.cache.values().iterator();

      while(it.hasNext()) {
         WorkingFile wf = (WorkingFile)it.next();
         String name = wf.getName();
         if (WorkingFileFactory.isWin32()) {
            name = name.toLowerCase();
         }

         if (name.startsWith(dirPath)) {
            wfList.add(wf);
         }
      }

      return wfList;
   }

   protected void notifyInvalidated(WorkingFileList invalidatedWFs) {
   }

   void invalidate(WorkingFileList workingFiles, long ordinal) {
      WorkingFileList invalidatedList = new WorkingFileList();
      Iterator it = workingFiles.iterator();

      while(it.hasNext()) {
         WorkingFile wf = (WorkingFile)it.next();
         WorkingFile cachedWF = this.getWorkingFile(wf.getFile());
         if (cachedWF == null) {
            cachedWF = wf;
         }

         if (wf != cachedWF) {
            wf.invalidate();
         }

         if (cachedWF.getOrdinal() <= ordinal && !cachedWF.isInvalid()) {
            cachedWF.invalidate();
            invalidatedList.add(cachedWF);
         }
      }

      this.notifyInvalidated(invalidatedList);
   }

   void invalidate(File directory, long ordinal) {
      this.invalidate(this.getWorkingFiles(directory), ordinal);
   }

   final void monitorDirectory(CmdRunnerCreator session, WorkingDirectory wd) throws APIException {
      Iterator it = wd.getSandboxes().iterator();

      while(it.hasNext()) {
         ISandboxInfo si = (ISandboxInfo)it.next();
         this.monitorSandbox(session, si.getSandboxName());
      }

   }

   final void unmonitorDirectory(CmdRunnerCreator session, WorkingDirectory wd) throws APIException {
      Iterator it = wd.getSandboxes().iterator();

      while(it.hasNext()) {
         ISandboxInfo si = (ISandboxInfo)it.next();
         this.unmonitorSandbox(session, si.getSandboxName());
      }

   }

   protected void monitorSandbox(CmdRunnerCreator session, String sandbox) throws APIException {
   }

   protected void unmonitorSandbox(CmdRunnerCreator session, String sandbox) throws APIException {
   }
}
