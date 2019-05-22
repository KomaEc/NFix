package com.mks.api.commands.ide;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.response.APIError;
import com.mks.api.response.APIException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class WorkingFileList {
   private List list;

   public WorkingFileList() {
   }

   public WorkingFileList(WorkingFile[] wfArray) {
      this();
      this.add(wfArray);
   }

   public synchronized void add(WorkingFile wf) {
      if (this.list == null) {
         this.list = new ArrayList(1);
      }

      this.list.add(wf);
   }

   public void add(WorkingFile[] array) {
      for(int i = 0; i < array.length; ++i) {
         this.add(array[i]);
      }

   }

   public Iterator iterator() {
      return this.getList().iterator();
   }

   public int size() {
      return this.getList().size();
   }

   public boolean isEmpty() {
      return this.getList().isEmpty();
   }

   public synchronized List getList() {
      return this.list == null ? Collections.EMPTY_LIST : Collections.unmodifiableList(this.list);
   }

   public Map getWorkingFileBuckets() {
      try {
         return this.getWorkingFileBuckets((CmdRunnerCreator)null, (File)null);
      } catch (APIException var2) {
         throw new APIError(var2);
      }
   }

   public Map getWorkingFileBuckets(CmdRunnerCreator session, File preferredRootSandbox) throws APIException {
      Map wdMap = new HashMap();
      Map workingFileBuckets = new HashMap();
      Iterator i = this.iterator();

      while(true) {
         WorkingFile wf;
         do {
            if (!i.hasNext()) {
               return workingFileBuckets;
            }

            wf = (WorkingFile)i.next();
         } while(!wf.isInSandboxDir());

         File root = wf.getWorkingRoot();
         if (root == null && preferredRootSandbox != null) {
            File wfDir = wf.getFile().getParentFile();
            WorkingDirectory wd = (WorkingDirectory)wdMap.get(wfDir);
            if (wd == null) {
               wd = WorkingDirectoryFactory.getWorkingDirectory(session, wfDir);
               wdMap.put(wfDir, wd);
            }

            if (wd.isAmbiguous()) {
               Iterator sandboxes = wd.getSandboxes().iterator();

               while(sandboxes.hasNext()) {
                  SandboxInfo sandbox = (SandboxInfo)sandboxes.next();
                  if (sandbox.isRelatedTo(session, preferredRootSandbox)) {
                     root = sandbox.getSandboxFile();
                     break;
                  }
               }
            } else {
               root = wd.getSandboxLocation();
            }
         }

         if (root != null) {
            WorkingFileList bucket = (WorkingFileList)workingFileBuckets.get(root);
            if (bucket == null) {
               bucket = new WorkingFileList();
               workingFileBuckets.put(root, bucket);
            }

            bucket.add(wf);
         }
      }
   }

   List getFileList() {
      List fileList = new ArrayList();
      Iterator i = this.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         fileList.add(wf.getFile());
      }

      return fileList;
   }

   public List getMemberList() {
      List fileList = new ArrayList();
      Iterator i = this.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         fileList.add(wf.getMemberName());
      }

      return fileList;
   }

   public void invalidate() {
      WorkingFileCache wfCache = WorkingFileCache.getInstance();
      wfCache.invalidate(this, WorkingFileFactory.getNewOrdinal());
   }
}
