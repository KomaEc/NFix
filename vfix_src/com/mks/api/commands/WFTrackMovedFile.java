package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.ide.SandboxInfo;
import com.mks.api.commands.ide.WorkingDirectory;
import com.mks.api.commands.ide.WorkingDirectoryFactory;
import com.mks.api.commands.ide.WorkingFile;
import com.mks.api.commands.ide.WorkingFileFactory;
import com.mks.api.commands.ide.WorkingFileList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandOptionException;
import com.mks.api.response.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class WFTrackMovedFile extends WFTrackCommandBase {
   private String[] fromNames;
   private String[] toNames;

   WFTrackMovedFile(CmdRunnerCreator session) throws APIException {
      super(session);
   }

   protected Response execute(WorkingFileList workingFiles) throws APIException {
      Map fileNameMapping = this.convertToWorkingFileMap(this.fromNames, this.toNames);
      HashMap targetMoveCpDirBuckets = new HashMap();
      HashMap targetSandboxMap = new HashMap();
      HashMap dirMap = new HashMap();
      WorkingFileList toBeReverted = new WorkingFileList();
      HashMap toBeAdded = new HashMap();
      HashMap toBeDropped = new HashMap();
      WorkingFileList toBeRenamed = new WorkingFileList();
      HashMap toBeLocked = new HashMap();
      Iterator i = workingFiles.iterator();

      while(true) {
         while(true) {
            WorkingFile wf;
            WorkingFile target;
            do {
               do {
                  do {
                     if (!i.hasNext()) {
                        wf = null;
                        Response response = this.revertDeferred(toBeReverted, true);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        response = this.addFiles(toBeAdded);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        response = this.dropFiles(toBeDropped);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        response = this.lockFiles(toBeLocked);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        response = this.renameFiles(fileNameMapping, toBeRenamed);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        response = this.moveFiles(targetMoveCpDirBuckets, targetSandboxMap);
                        if (response != null && response.getAPIException() != null) {
                           return response;
                        }

                        return null;
                     }

                     wf = (WorkingFile)i.next();
                     target = (WorkingFile)fileNameMapping.get(wf.getFile());
                  } while(wf.getAPIException() != null);
               } while(!wf.isControlled() && wf.isInSandboxDir());
            } while(wf.isDropped());

            if (wf.isAdded() || wf.isMoved()) {
               toBeReverted.add(wf);
               if (wf.isLockedByMe()) {
                  this.addToWorkingFileList(toBeLocked, wf);
               }
            }

            if (wf.isAdded()) {
               if (target.isInSandboxDir()) {
                  this.addToWorkingFileList(toBeAdded, target);
               }
            } else {
               if (!wf.isInSandboxDir() && target.isInSandboxDir()) {
                  this.addToWorkingFileList(toBeAdded, target);
               }

               if (wf.isControlled() && !target.isInSandboxDir()) {
                  if (!wf.isAdded()) {
                     this.addToWorkingFileList(toBeDropped, wf);
                  }
               } else {
                  File targetFile = target.getFile();
                  if (target.isMoved()) {
                     targetFile = target.getMemberName();
                  }

                  if (!wf.getMemberName().getName().equals(targetFile.getName())) {
                     toBeRenamed.add(wf);
                  }

                  File targetDir = targetFile.getParentFile();
                  if (!wf.getMemberName().getParentFile().equals(targetDir)) {
                     this.addToTargetBucket(targetMoveCpDirBuckets, wf, targetDir);
                     if (!targetSandboxMap.containsKey(targetDir)) {
                        String sandboxPath = target.getSandbox();
                        File preferredRoot = this.getPreferredRoot();
                        if (target.getWorkingRoot() == null && preferredRoot != null) {
                           WorkingDirectory wd = (WorkingDirectory)dirMap.get(targetDir);
                           if (wd == null) {
                              wd = WorkingDirectoryFactory.getWorkingDirectory(this.getCmdRunnerCreator(), targetDir);
                              dirMap.put(targetDir, wd);
                           }

                           SandboxInfo sandbox = null;
                           Iterator sandboxes = wd.getSandboxes().iterator();

                           while(sandboxes.hasNext()) {
                              sandbox = (SandboxInfo)sandboxes.next();
                              if (sandbox.isRelatedTo(this.getCmdRunnerCreator(), preferredRoot)) {
                                 break;
                              }
                           }

                           if (sandbox != null) {
                              sandboxPath = sandbox.getSandboxName();
                           }
                        }

                        targetSandboxMap.put(targetDir, sandboxPath);
                     }
                  }
               }
            }
         }
      }
   }

   private Response dropFiles(HashMap toBeDropped) throws APIException {
      if (toBeDropped.size() == 0) {
         return null;
      } else {
         Iterator i = toBeDropped.keySet().iterator();

         Response response;
         do {
            if (!i.hasNext()) {
               return null;
            }

            String cpid = (String)i.next();
            WFTrackDeletedFile wfDeleted = new WFTrackDeletedFile(this.getCmdRunnerCreator());
            wfDeleted.setCpid(cpid);
            response = wfDeleted.execute((WorkingFileList)toBeDropped.get(cpid), this.interactive);
         } while(response == null || response.getAPIException() == null);

         return response;
      }
   }

   private Response addFiles(HashMap toBeAdded) throws APIException {
      if (toBeAdded.size() == 0) {
         return null;
      } else {
         Iterator i = toBeAdded.keySet().iterator();

         Response response;
         do {
            if (!i.hasNext()) {
               return null;
            }

            String cpid = (String)i.next();
            WFTrackNewFile wfNew = new WFTrackNewFile(this.getCmdRunnerCreator());
            wfNew.setCpid(cpid);
            wfNew.setPreferredRoot(this.getPreferredRoot());
            wfNew.setAllowCreateSubs(this.allowCreateSubs);
            response = wfNew.execute((WorkingFileList)toBeAdded.get(cpid), this.interactive);
         } while(response == null || response.getAPIException() == null);

         return response;
      }
   }

   private Response lockFiles(HashMap toBeLocked) throws APIException {
      if (toBeLocked.isEmpty()) {
         return null;
      } else {
         Iterator i = toBeLocked.keySet().iterator();

         Response response;
         do {
            if (!i.hasNext()) {
               return null;
            }

            String cpid = (String)i.next();
            SILockCommand siLock = new SILockCommand(this.getCmdRunnerCreator());
            siLock.setCpid(cpid);
            siLock.setAllowPrompting(false);
            GenericWFCommandRunner wfLock = new GenericWFCommandRunner(this.getCmdRunnerCreator(), siLock);
            response = wfLock.execute((WorkingFileList)toBeLocked.get(cpid), this.interactive);
         } while(response == null || response.getAPIException() == null);

         return response;
      }
   }

   private Response renameFiles(Map fileNameMapping, WorkingFileList toBeRenamed) throws APIException {
      Response[] response = null;
      Iterator i = toBeRenamed.iterator();

      while(i.hasNext()) {
         WorkingFile wf = (WorkingFile)i.next();
         WorkingFileList selection = new WorkingFileList();
         selection.add(wf);
         WorkingFile target = (WorkingFile)fileNameMapping.get(wf.getFile());
         String newName = target.getFile().getName();
         if (target.isMoved()) {
            newName = target.getMemberName().getName();
         }

         String workingCpId;
         if (wf.getWorkingCpid() != null && wf.getWorkingCpid().length() > 0) {
            workingCpId = wf.getWorkingCpid();
         } else {
            workingCpId = this.cpid;
         }

         SIRenameCommand siRename = new SIRenameCommand(this.getCmdRunnerCreator());
         siRename.setDeferred(true);
         siRename.setCpid(workingCpId);
         siRename.setCloseCP(false);
         siRename.setNewName(newName);
         siRename.setRenameFile(true);
         response = this.runApiCommand(siRename, selection, this.interactive);

         for(int j = 0; j < response.length; ++j) {
            if (response[j] != null && response[j].getAPIException() != null) {
               return response[j];
            }
         }

         if (wf != null) {
            File file;
            if (wf.getMemberName() != null) {
               file = wf.getMemberName();
            } else {
               file = wf.getFile();
            }

            if (file != null && file.exists()) {
               try {
                  file.delete();
               } catch (SecurityException var13) {
               }
            }
         }
      }

      return null;
   }

   private Response moveFiles(HashMap targetMoveCpDirBuckets, HashMap targetSandboxMap) throws APIException {
      Response[] response = null;
      Iterator k = targetMoveCpDirBuckets.keySet().iterator();

      while(k.hasNext()) {
         String cpid = (String)k.next();
         Map targetMoveDirBuckets = (Map)targetMoveCpDirBuckets.get(cpid);
         Iterator i = targetMoveDirBuckets.keySet().iterator();

         label81:
         while(i.hasNext()) {
            File targetDir = (File)i.next();
            WorkingFileList filesInDir = (WorkingFileList)targetMoveDirBuckets.get(targetDir);
            SIMoveCommand siMove = new SIMoveCommand(this.getCmdRunnerCreator());
            siMove.setCpid(cpid);
            siMove.setDeferred(true);
            siMove.setCloseCP(false);
            siMove.setAllowCreateSubs(this.allowCreateSubs);
            siMove.setMoveWorking(true);
            siMove.setTargetDir(targetDir.getAbsolutePath());
            String targetSandbox = (String)targetSandboxMap.get(targetDir);
            siMove.setTargetSandbox(targetSandbox);
            response = this.runApiCommand(siMove, filesInDir, this.interactive);

            for(int j = 0; j < response.length; ++j) {
               if (response[j] != null && response[j].getAPIException() != null) {
                  return response[j];
               }
            }

            Iterator it = filesInDir.iterator();

            while(true) {
               WorkingFile wf;
               do {
                  do {
                     if (!it.hasNext()) {
                        continue label81;
                     }

                     wf = (WorkingFile)it.next();
                  } while(wf == null);
               } while(wf.getFile() == null);

               if (wf.isMoved()) {
                  for(int j = 0; j < this.fromNames.length; ++j) {
                     if (this.fromNames[j] != null && this.toNames[j] != null && this.fromNames[j].equalsIgnoreCase(wf.getFile().getAbsolutePath())) {
                        String toName = (new File(this.toNames[j])).getName();
                        File dir = new File(wf.getMemberName().getParent());
                        File toDelete = new File(dir, toName);

                        try {
                           toDelete.delete();
                        } catch (SecurityException var20) {
                        }
                     }
                  }
               }

               try {
                  wf.getFile().delete();
               } catch (SecurityException var19) {
               }
            }
         }
      }

      return null;
   }

   private void addToTargetBucket(Map targetCpDirBuckets, WorkingFile source, File targetRoot) {
      String workingCpid = source.getWorkingCpid();
      if (workingCpid == null || workingCpid.length() == 0) {
         workingCpid = this.cpid;
      }

      Object targetDirBuckets;
      if (targetCpDirBuckets.containsKey(workingCpid)) {
         targetDirBuckets = (Map)targetCpDirBuckets.get(workingCpid);
      } else {
         targetDirBuckets = new HashMap();
         targetCpDirBuckets.put(workingCpid, targetDirBuckets);
      }

      WorkingFileList bucket;
      if (((Map)targetDirBuckets).containsKey(targetRoot)) {
         bucket = (WorkingFileList)((Map)targetDirBuckets).get(targetRoot);
         bucket.add(source);
      } else {
         bucket = new WorkingFileList();
         bucket.add(source);
         ((Map)targetDirBuckets).put(targetRoot, bucket);
      }

   }

   private Map convertToWorkingFileMap(String[] from, String[] to) throws APIException {
      if (from != null && to != null) {
         if (from.length != to.length) {
            throw new InvalidCommandOptionException("Unmatched filename mappings for move members.");
         } else {
            Map fileNameMapping = new HashMap();
            ArrayList toList = new ArrayList();

            for(int i = 0; i < to.length; ++i) {
               toList.add(to[i]);
            }

            WorkingFileList destWorkingFiles = WorkingFileFactory.getWorkingFiles((CmdRunnerCreator)this.getCmdRunnerCreator(), toList);
            Iterator destIterator = destWorkingFiles.iterator();

            for(int i = 0; i < from.length; ++i) {
               WorkingFile dest = (WorkingFile)destIterator.next();
               fileNameMapping.put(new File(from[i]), dest);
            }

            return fileNameMapping;
         }
      } else {
         throw new InvalidCommandOptionException("No mapping specified for move members");
      }
   }

   public void setMapping(String[] from, String[] to) {
      this.fromNames = from;
      this.toNames = to;
   }

   private void addToWorkingFileList(Map list, WorkingFile wf) {
      String cpid = wf.getWorkingCpid();
      if (cpid == null || cpid.length() == 0) {
         cpid = this.cpid;
      }

      if (!list.containsKey(cpid)) {
         WorkingFileList wflist = new WorkingFileList();
         wflist.add(wf);
         list.put(cpid, wflist);
      } else {
         ((WorkingFileList)list.get(cpid)).add(wf);
      }

   }
}
