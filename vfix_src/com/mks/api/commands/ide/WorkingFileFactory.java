package com.mks.api.commands.ide;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.Session;
import com.mks.api.commands.SICommands;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.InvalidItemException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemNotFoundException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class WorkingFileFactory {
   private static final String VIEWSANDBOX_FIELDS = "context,memberrev,workingrev,lockrecord,newrevdelta,revsyncdelta,wfdelta,workingcpid,locker,deferred";
   private static long ordinal = 0L;

   private WorkingFileFactory() {
   }

   private static WorkingFileCache getWorkingFileCache(SICommands si) {
      boolean retainedCache = !(si.getCmdRunnerCreator() instanceof Session);
      return WorkingFileCache.getInstance(retainedCache);
   }

   public static WorkingFile getWorkingFile(CmdRunnerCreator session, String fileName) {
      List fileNames = new ArrayList(1);
      fileNames.add(fileName);
      return (WorkingFile)getWorkingFiles((CmdRunnerCreator)session, fileNames).getList().get(0);
   }

   public static WorkingFile refreshWorkingFile(CmdRunnerCreator session, String fileName) {
      List fileNames = new ArrayList(1);
      fileNames.add(fileName);
      return (WorkingFile)refreshWorkingFiles(session, fileNames).getList().get(0);
   }

   public static WorkingFileList getWorkingFiles(CmdRunnerCreator session, List fileNames) {
      return getWorkingFiles(session, fileNames, false);
   }

   private static WorkingFileList getWorkingFiles(CmdRunnerCreator session, List fileNames, boolean refresh) {
      WorkingFileList wfs;
      try {
         SICommands si = new SICommands(session, false);
         WorkingFileCache wfCache = getWorkingFileCache(si);
         if (refresh) {
            Iterator it = fileNames.iterator();

            while(it.hasNext()) {
               wfCache.removeWorkingFile(new File((String)it.next()));
            }
         }

         wfs = getWorkingFiles(si, wfCache, fileNames);
      } catch (APIException var7) {
         wfs = invalidate(fileNames, new Date());
      }

      return wfs;
   }

   public static WorkingFileList refreshWorkingFiles(CmdRunnerCreator session, List fileNames) {
      return getWorkingFiles(session, fileNames, true);
   }

   public static WorkingFileList getWorkingFiles(SICommands si, List fileNames) {
      return getWorkingFiles(si, getWorkingFileCache(si), fileNames);
   }

   public static synchronized long getNewOrdinal() {
      return (long)(ordinal++);
   }

   private static WorkingFileList getWorkingFiles(SICommands si, WorkingFileCache wfCache, List fileNames) {
      Date timestamp = new Date();
      long ordinal = getNewOrdinal();
      Map dirMap = new HashMap();
      Map sandboxContentMap = new HashMap();
      Map sandboxBuckets = new HashMap();
      Map indexMap = new HashMap();
      List deferredMoves = new ArrayList();
      Object[] fileNameArray = fileNames.toArray();
      File[] files = new File[fileNameArray.length];
      WorkingFile[] wfArray = new WorkingFile[fileNameArray.length];

      for(int i = 0; i < fileNameArray.length; ++i) {
         String fileName = (String)fileNameArray[i];
         files[i] = new File(fileName);
         indexMap.put(files[i], new Integer(i));
         WorkingFile wf = wfCache.getWorkingFile(files[i]);
         if (wf != null) {
            wfArray[i] = wf;
            if (!wf.isInvalid()) {
               continue;
            }
         } else {
            wf = new WorkingFile(files[i], (String)null, (File)null, false, timestamp, ordinal);
            wf.invalidate();
            wfArray[i] = wf;
            wfCache.addWorkingFile(si.getCmdRunnerCreator(), wf);
         }

         File workingRoot;
         try {
            workingRoot = getWorkingRoot(si, files[i], dirMap, sandboxContentMap);
         } catch (APIException var95) {
            wfArray[i] = new WorkingFile(files[i], var95, timestamp, ordinal);
            continue;
         }

         if (workingRoot == null) {
            wfArray[i] = new WorkingFile(files[i], (String)null, (File)null, false, timestamp, ordinal);
            wfCache.addWorkingFile(si.getCmdRunnerCreator(), wfArray[i]);
         } else {
            addToSbxBucket(workingRoot, files[i], sandboxBuckets);
         }
      }

      Iterator workingRoots = sandboxBuckets.keySet().iterator();

      String userId;
      Iterator fileIterator;
      WorkingFile wf;
      while(workingRoots != null && workingRoots.hasNext()) {
         File workingRoot = (File)workingRoots.next();
         List sbxFiles = (List)sandboxBuckets.get(workingRoot);
         List diffFiles = new ArrayList();
         boolean potentialDeferredRename = false;
         Response sbxView = null;
         sbxView = viewSandbox(si, workingRoot, sbxFiles, (OptionList)null);
         WorkItemIterator wis;
         File file;
         WorkItem wi;
         File myWorkingRoot;
         String fileName;
         if (sbxView != null) {
            try {
               wis = sbxView.getWorkItems();
               userId = sbxView.getConnectionUsername();
               fileIterator = sbxFiles.iterator();

               while(fileIterator.hasNext() && wis.hasNext()) {
                  file = (File)fileIterator.next();
                  File sandbox = null;
                  int index = (Integer)indexMap.get(file);

                  try {
                     sandbox = getWorkingSandbox(si, file, dirMap, sandboxContentMap);
                     wi = wis.next();
                     fileName = null;
                     if (sandbox != null) {
                        fileName = sandbox.getAbsolutePath();
                     }

                     wf = new WorkingFile(file, fileName, workingRoot, wi, timestamp, userId, ordinal);
                     wfArray[index] = wf;
                     if (wf.isMoved()) {
                        potentialDeferredRename = true;
                        if (!indexMap.containsKey(wf.getFile())) {
                           indexMap.put(wf.getFile(), new Integer(index));
                           files[index] = wf.getFile();
                        }
                     } else if (wf.hasWorkingDelta() && !wf.isModified()) {
                        diffFiles.add(file);
                     }
                  } catch (ItemNotFoundException var92) {
                     WorkingFile wf;
                     if (sandbox == null) {
                        wf = new WorkingFile(file, var92, timestamp, ordinal);
                     } else if (file.equals(sandbox)) {
                        wf = new WorkingFile(file, sandbox.getAbsolutePath(), workingRoot, true, timestamp, ordinal);
                     } else {
                        WorkItem wi = wis.getLast();
                        myWorkingRoot = null;

                        try {
                           WorkingDirectory wd = getWorkingDirectory(si, workingRoot, dirMap);
                           if (wd.isSandboxAware() && !wd.isAmbiguous()) {
                              myWorkingRoot = workingRoot;
                           }
                        } catch (APIException var88) {
                        }

                        wf = new WorkingFile(file, sandbox.getAbsolutePath(), myWorkingRoot, wi, timestamp, userId, ordinal);
                        if (!wf.isFormerMember()) {
                           potentialDeferredRename = true;
                        }
                     }

                     wfArray[index] = wf;
                  } catch (APIException var93) {
                     wfArray[index] = new WorkingFile(file, var93, timestamp, ordinal);
                  }
               }
            } finally {
               try {
                  sbxView.release();
               } catch (APIException var84) {
               }

            }
         }

         boolean isDestinedMember;
         if (!diffFiles.isEmpty()) {
            Response diff = diffStatus(si, workingRoot, diffFiles);

            try {
               wis = diff != null ? diff.getWorkItems() : null;
               fileIterator = diffFiles.iterator();

               while(fileIterator.hasNext()) {
                  file = (File)fileIterator.next();
                  int index = (Integer)indexMap.get(file);
                  isDestinedMember = true;
                  if (wis != null) {
                     try {
                        wi = wis.next();
                        Item result = wi.getResult().getPrimaryValue();
                        Field status = result.getField("different");
                        isDestinedMember = status.getBoolean();
                     } catch (Throwable var87) {
                     }
                  }

                  if (isDestinedMember) {
                     wfArray[index].markModified();
                  }
               }
            } finally {
               try {
                  if (diff != null) {
                     diff.release();
                  }
               } catch (APIException var85) {
               }

            }
         }

         if (potentialDeferredRename) {
            OptionList options = new OptionList();
            options.add(new Option("filterSubs"));
            options.add("filter", "deferred:move,deferred:rename");
            options.add(new Option("recurse"));
            sbxView = viewSandbox(si, workingRoot, (List)null, options);
            if (sbxView != null) {
               String userId = sbxView.getConnectionUsername();
               wis = sbxView.getWorkItems();

               while(wis.hasNext()) {
                  try {
                     WorkItem wi = wis.next();
                     if (wi.getModelType() != null) {
                        isDestinedMember = wi.getModelType().equals("si.DestinedMember");
                        boolean isMember = wi.getModelType().equals("si.Member");
                        if (isDestinedMember || isMember) {
                           fileName = wi.getField("name").getString();
                           if (fileName != null) {
                              File file = new File(fileName);
                              myWorkingRoot = workingRoot;
                              File sandbox;
                              if (isDestinedMember) {
                                 sandbox = new File(wi.getField("canonicalSandbox").getString());
                                 myWorkingRoot = getWorkingRoot(si, sandbox, dirMap, sandboxContentMap);
                              }

                              sandbox = getWorkingSandbox(si, file, dirMap, sandboxContentMap);
                              WorkingFile wf = new WorkingFile(file, sandbox.getAbsolutePath(), myWorkingRoot, wi, timestamp, userId, ordinal);
                              if (wf.isMoved()) {
                                 Integer indexMarker = (Integer)indexMap.get(file);
                                 if (indexMarker != null) {
                                    int index = indexMarker;
                                    if (wfArray[index].isModified()) {
                                       wf.markModified();
                                    }

                                    wfArray[index] = wf;
                                    getWorkingRoot(si, wf.getMemberName(), dirMap, sandboxContentMap);
                                    if (isDestinedMember && wf.isMoved()) {
                                       deferredMoves.add(wf);
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } catch (APIException var90) {
                  }
               }
            }
         }
      }

      WorkingFileList deferredMoveWFList = new WorkingFileList();
      Iterator mv = deferredMoves.iterator();

      while(mv.hasNext()) {
         deferredMoveWFList.add((WorkingFile)mv.next());
      }

      Map deferredMoveSandboxBuckets = deferredMoveWFList.getWorkingFileBuckets();
      Iterator deferredMoveSandboxes = deferredMoveSandboxBuckets.keySet().iterator();

      while(true) {
         File workingRoot;
         WorkingFileList sbxWorkingFiles;
         Response sbxView;
         do {
            if (!deferredMoveSandboxes.hasNext()) {
               for(int i = 0; i < wfArray.length; ++i) {
                  if (wfArray[i] == null) {
                     wfArray[i] = new WorkingFile(files[i], new InvalidItemException(), timestamp, ordinal);
                  }

                  wfCache.addWorkingFile(si.getCmdRunnerCreator(), wfArray[i]);
                  WorkingFile cachedWF = wfCache.getWorkingFile(wfArray[i].getFile());
                  if (!cachedWF.isInvalid()) {
                     wfArray[i] = cachedWF;
                  }
               }

               WorkingFileList list = new WorkingFileList(wfArray);
               return list;
            }

            workingRoot = (File)deferredMoveSandboxes.next();
            sbxWorkingFiles = (WorkingFileList)deferredMoveSandboxBuckets.get(workingRoot);
            fileIterator = null;
            sbxView = viewSandbox(si, workingRoot, sbxWorkingFiles.getMemberList(), (OptionList)null);
         } while(sbxView == null);

         try {
            userId = sbxView.getConnectionUsername();
            WorkItemIterator wis = sbxView.getWorkItems();
            Iterator workingFileIterator = sbxWorkingFiles.iterator();

            while(workingFileIterator.hasNext() && wis.hasNext()) {
               WorkingFile movedWorkingFile = (WorkingFile)workingFileIterator.next();
               int index = (Integer)indexMap.get(movedWorkingFile.getFile());

               try {
                  WorkItem wi = wis.next();
                  wf = new WorkingFile(movedWorkingFile.getFile(), movedWorkingFile.getSandbox(), workingRoot, wi, timestamp, userId, ordinal);
                  if (wfArray[index].isModified()) {
                     wf.markModified();
                  }

                  wfArray[index] = wf;
               } catch (APIException var86) {
                  wfArray[index] = new WorkingFile(movedWorkingFile.getFile(), var86, timestamp, ordinal);
               }
            }
         } finally {
            try {
               sbxView.release();
            } catch (APIException var83) {
            }

         }
      }
   }

   private static File getLocation(File filePath) {
      if (!filePath.isAbsolute()) {
         filePath = filePath.getAbsoluteFile();
      }

      if (!filePath.isDirectory()) {
         filePath = filePath.getParentFile();
      }

      return filePath;
   }

   private static File getWorkingRoot(SICommands si, File element, Map dirMap, Map sandboxContentMap) throws APIException {
      File root = null;
      WorkingDirectory wd = getWorkingDirectory(si, element, dirMap);
      if (!wd.isSandboxAware()) {
         root = null;
      } else if (!wd.isAmbiguous()) {
         ISandboxInfo sandbox = (ISandboxInfo)wd.getSandboxes().iterator().next();
         String parent = sandbox.getParentName();
         if (parent != null) {
            root = getWorkingRoot(si, new File(parent), dirMap, sandboxContentMap);
            if (!root.isDirectory()) {
               parent = null;
            }
         }

         if (parent == null) {
            root = sandbox.getSandboxLocation();
         }
      } else {
         root = getWorkingSandbox(si, element, dirMap, sandboxContentMap);
      }

      return root;
   }

   private static File getWorkingSandbox(SICommands si, File element, Map dirMap, Map sandboxContentMap) throws APIException {
      WorkingDirectory wd = getWorkingDirectory(si, element, dirMap);
      if (!wd.isSandboxAware()) {
         return null;
      } else if (!wd.isAmbiguous()) {
         ISandboxInfo sandbox = (ISandboxInfo)wd.getSandboxes().iterator().next();
         return sandbox.getSandboxFile();
      } else {
         File sandboxFile = null;
         Iterator sbxs = wd.getSandboxes().iterator();

         while(sbxs.hasNext()) {
            ISandboxInfo sandbox = (ISandboxInfo)sbxs.next();
            sandboxFile = sandbox.getSandboxFile();
            if (sandboxFile.equals(element)) {
               break;
            }

            Collection content = (Collection)sandboxContentMap.get(sandboxFile);
            if (content == null) {
               Response sbxView = viewSandbox(si, sandboxFile, (List)null, (OptionList)null);
               content = new HashSet();
               if (sbxView != null) {
                  try {
                     File wiFile;
                     for(WorkItemIterator i = sbxView.getWorkItems(); i.hasNext(); ((Collection)content).add(wiFile)) {
                        WorkItem wi = null;

                        try {
                           wi = i.next();
                        } catch (APIException var20) {
                           wi = i.getLast();
                        }

                        wiFile = new File(wi.getId());
                        if (!wiFile.isAbsolute()) {
                           wiFile = new File(sandboxFile.getParentFile(), wi.getId());
                        }
                     }

                     if (sbxView.getAPIException() != null) {
                        throw sbxView.getAPIException();
                     }
                  } finally {
                     try {
                        sbxView.release();
                     } catch (APIException var19) {
                     }

                  }
               }

               sandboxContentMap.put(sandboxFile, content);
            }

            if (((Collection)content).contains(element)) {
               break;
            }
         }

         return sandboxFile;
      }
   }

   private static WorkingDirectory getWorkingDirectory(SICommands si, File element, Map dirMap) throws APIException {
      WorkingDirectory wd = null;
      File path = getLocation(element);
      if (dirMap.containsKey(path)) {
         wd = (WorkingDirectory)dirMap.get(path);
      }

      if (wd == null) {
         wd = WorkingDirectoryFactory.getWorkingDirectory(si.getCmdRunnerCreator(), path);
         dirMap.put(path, wd);
      }

      return wd;
   }

   private static void addToSbxBucket(File root, File file, Map sandboxBuckets) {
      List bucket = null;
      if (sandboxBuckets.containsKey(root)) {
         bucket = (List)sandboxBuckets.get(root);
         bucket.add(file);
      } else {
         List bucket = new ArrayList();
         bucket.add(file);
         sandboxBuckets.put(root, bucket);
      }

   }

   private static String[] memberNames(Collection members) {
      String[] memberNames;
      if (members == null) {
         memberNames = null;
      } else {
         memberNames = new String[members.size()];
         Iterator i = members.iterator();

         for(int j = 0; i.hasNext(); ++j) {
            File member = (File)i.next();
            memberNames[j] = member.getAbsolutePath();
         }
      }

      return memberNames;
   }

   private static Response viewSandbox(SICommands si, File sandboxRoot, List members, OptionList options) {
      File sandboxDir = getLocation(sandboxRoot);
      OptionList viewOptions = new OptionList();
      viewOptions.add(new Option("fields", "context,memberrev,workingrev,lockrecord,newrevdelta,revsyncdelta,wfdelta,workingcpid,locker,deferred"));
      viewOptions.add(new Option("norecurse"));
      if (!sandboxRoot.isDirectory()) {
         viewOptions.add(new Option("sandbox", sandboxRoot.getAbsolutePath()));
      }

      if (options != null) {
         Iterator it = options.getOptions();

         while(it.hasNext()) {
            viewOptions.add((Option)it.next());
         }
      }

      Response r;
      try {
         String cwdPath = sandboxDir.getAbsolutePath();
         r = si.getSandboxMemberStatus(cwdPath, memberNames(members), viewOptions, true);
      } catch (APIException var8) {
         r = var8.getResponse();
      }

      return r;
   }

   private static Response diffStatus(SICommands si, File workingRoot, List members) {
      Response r;
      try {
         OptionList options = null;
         String cwdPath = null;
         if (workingRoot != null) {
            cwdPath = getLocation(workingRoot).getAbsolutePath();
            if (!workingRoot.isDirectory()) {
               options = new OptionList();
               options.add(new Option("sandbox", workingRoot.getAbsolutePath()));
            }
         }

         r = si.getSandboxMemberDifferenceStatus(cwdPath, memberNames(members), options);
      } catch (APIException var6) {
         r = var6.getResponse();
      }

      return r;
   }

   private static WorkingFileList invalidate(List fileNames, Date timestamp) {
      WorkingFile[] wfArray = new WorkingFile[fileNames.size()];

      for(int i = 0; i < fileNames.size(); ++i) {
         wfArray[i] = new WorkingFile((File)fileNames.get(i), new InvalidItemException(), timestamp, getNewOrdinal());
      }

      return new WorkingFileList(wfArray);
   }

   static boolean isWin32() {
      return System.getProperty("os.name").indexOf("Windows") != -1;
   }
}
