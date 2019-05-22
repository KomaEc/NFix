package org.apache.maven.scm.provider.integrity;

import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.codehaus.plexus.util.StringUtils;

public class Sandbox {
   public static final SimpleDateFormat RLOG_DATEFORMAT = new SimpleDateFormat("MMMMM d, yyyy - h:mm:ss a");
   private String fs = System.getProperty("file.separator");
   private APISession api;
   private Project siProject;
   private String sandboxDir;
   private String cpid;
   private boolean addSuccess;
   private boolean ciSuccess;

   public static String formatFilePatterns(String pattern) {
      StringBuilder sb = new StringBuilder();
      if (null != pattern && pattern.length() > 0) {
         String[] tokens = StringUtils.split(pattern, ",");

         for(int i = 0; i < tokens.length; ++i) {
            String tkn = tokens[i].trim();
            if (tkn.indexOf("file:") != 0 && tkn.indexOf("dir:") != 0) {
               sb.append(tkn.indexOf(46) > 0 ? StringUtils.replaceOnce(tkn, "**/", "file:") : StringUtils.replaceOnce(tkn, "**/", "dir:"));
            } else {
               sb.append(tkn);
            }

            sb.append(i < tokens.length ? "," : "");
         }
      }

      return sb.toString();
   }

   public Sandbox(APISession api, Project cmProject, String dir) {
      this.siProject = cmProject;
      this.sandboxDir = dir;
      this.api = api;
      this.cpid = System.getProperty("maven.scm.integrity.cpid");
      this.cpid = null != this.cpid && this.cpid.length() != 0 ? this.cpid : ":none";
      this.addSuccess = true;
      this.ciSuccess = true;
   }

   private boolean isValidSandbox(String sandbox) throws APIException {
      Command cmd = new Command("si", "sandboxinfo");
      cmd.addOption(new Option("sandbox", sandbox));
      this.api.getLogger().debug("Validating existing sandbox: " + sandbox);
      Response res = this.api.runCommand(cmd);
      WorkItemIterator wit = res.getWorkItems();

      try {
         WorkItem wi = wit.next();
         return wi.getField("fullConfigSyntax").getValueAsString().equalsIgnoreCase(this.siProject.getConfigurationPath());
      } catch (APIException var7) {
         ExceptionHandler eh = new ExceptionHandler(var7);
         this.api.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.api.getLogger().debug(eh.getCommand() + " completed with exit code " + eh.getExitCode());
         return false;
      }
   }

   private boolean isDelta(Item wfdelta) {
      return wfdelta.getField("isDelta").getBoolean();
   }

   private Response add(File memberFile, String message) throws APIException {
      this.api.getLogger().info("Adding member: " + memberFile.getAbsolutePath());
      Command siAdd = new Command("si", "add");
      siAdd.addOption(new Option("onExistingArchive", "sharearchive"));
      siAdd.addOption(new Option("cpid", this.cpid));
      if (null != message && message.length() > 0) {
         siAdd.addOption(new Option("description", message));
      }

      siAdd.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      siAdd.addSelection(memberFile.getName());
      return this.api.runCommand(siAdd);
   }

   private Response checkin(File memberFile, String relativeName, String message) throws APIException {
      this.api.getLogger().info("Checking in member:  " + memberFile.getAbsolutePath());
      Command sici = new Command("si", "ci");
      sici.addOption(new Option("cpid", this.cpid));
      if (null != message && message.length() > 0) {
         sici.addOption(new Option("description", message));
      }

      sici.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      sici.addSelection(relativeName);
      return this.api.runCommand(sici);
   }

   private Response dropMember(File memberFile, String relativeName) throws APIException {
      this.api.getLogger().info("Dropping member " + memberFile.getAbsolutePath());
      Command siDrop = new Command("si", "drop");
      siDrop.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      siDrop.addOption(new Option("noconfirm"));
      siDrop.addOption(new Option("cpid", this.cpid));
      siDrop.addOption(new Option("delete"));
      siDrop.addSelection(relativeName);
      return this.api.runCommand(siDrop);
   }

   private boolean hasMemberChanged(File memberFile, String relativeName) {
      Command siDiff = new Command("si", "diff");
      siDiff.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      siDiff.addSelection(relativeName);

      try {
         Response res = this.api.runCommand(siDiff);

         try {
            return res.getWorkItems().next().getResult().getField("resultant").getItem().getField("different").getBoolean();
         } catch (NullPointerException var6) {
            this.api.getLogger().warn("Couldn't figure out differences for file: " + memberFile.getAbsolutePath());
            this.api.getLogger().warn("Null value found along response object for WorkItem/Result/Field/Item/Field.getBoolean()");
            this.api.getLogger().warn("Proceeding with the assumption that the file has changed!");
         }
      } catch (APIException var7) {
         ExceptionHandler eh = new ExceptionHandler(var7);
         this.api.getLogger().warn("Couldn't figure out differences for file: " + memberFile.getAbsolutePath());
         this.api.getLogger().warn(eh.getMessage());
         this.api.getLogger().warn("Proceeding with the assumption that the file has changed!");
         this.api.getLogger().debug(eh.getCommand() + " completed with exit Code " + eh.getExitCode());
      }

      return true;
   }

   public String getSandboxDir() {
      return this.sandboxDir;
   }

   public Response lock(File memberFile, String relativeName) throws APIException {
      this.api.getLogger().debug("Locking member: " + memberFile.getAbsolutePath());
      Command siLock = new Command("si", "lock");
      siLock.addOption(new Option("revision", ":member"));
      siLock.addOption(new Option("cpid", this.cpid));
      siLock.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      siLock.addSelection(relativeName);
      return this.api.runCommand(siLock);
   }

   public Response unlock(File memberFile, String relativeName) throws APIException {
      this.api.getLogger().debug("Unlocking member: " + memberFile.getAbsolutePath());
      Command siUnlock = new Command("si", "unlock");
      siUnlock.addOption(new Option("revision", ":member"));
      siUnlock.addOption(new Option("action", "remove"));
      siUnlock.addOption(new Option("cwd", memberFile.getParentFile().getAbsolutePath()));
      siUnlock.addSelection(relativeName);
      return this.api.runCommand(siUnlock);
   }

   public Response drop() throws APIException {
      File project = new File(this.siProject.getProjectName());
      File sandboxpj = new File(this.sandboxDir + this.fs + project.getName());
      this.api.getLogger().debug("Sandbox Project File: " + sandboxpj.getAbsolutePath());
      Command cmd = new Command("si", "dropsandbox");
      cmd.addOption(new Option("delete", "members"));
      cmd.addOption(new Option("sandbox", sandboxpj.getAbsolutePath()));
      cmd.addOption(new Option("cwd", this.sandboxDir));
      return this.api.runCommand(cmd);
   }

   public boolean create() throws APIException {
      File project = new File(this.siProject.getProjectName());
      File sandboxpj = new File(this.sandboxDir + this.fs + project.getName());
      this.api.getLogger().debug("Sandbox Project File: " + sandboxpj.getAbsolutePath());
      if (sandboxpj.isFile()) {
         if (this.isValidSandbox(sandboxpj.getAbsolutePath())) {
            this.api.getLogger().debug("Reusing existing Sandbox in " + this.sandboxDir + " for project " + this.siProject.getConfigurationPath());
            return true;
         } else {
            this.api.getLogger().error("An invalid Sandbox exists in " + this.sandboxDir + ". Please provide a different location!");
            return false;
         }
      } else {
         this.api.getLogger().debug("Creating Sandbox in " + this.sandboxDir + " for project " + this.siProject.getConfigurationPath());

         try {
            Command cmd = new Command("si", "createsandbox");
            cmd.addOption(new Option("recurse"));
            cmd.addOption(new Option("nopopulate"));
            cmd.addOption(new Option("project", this.siProject.getConfigurationPath()));
            cmd.addOption(new Option("cwd", this.sandboxDir));
            this.api.runCommand(cmd);
            return true;
         } catch (APIException var5) {
            ExceptionHandler eh = new ExceptionHandler(var5);
            if (eh.getMessage().indexOf("There is already a registered entry") > 0) {
               return this.create();
            } else {
               throw var5;
            }
         }
      }
   }

   public Response resync() throws APIException {
      this.api.getLogger().debug("Resynchronizing Sandbox in " + this.sandboxDir + " for project " + this.siProject.getConfigurationPath());
      Command cmd = new Command("si", "resync");
      cmd.addOption(new Option("recurse"));
      cmd.addOption(new Option("populate"));
      cmd.addOption(new Option("cwd", this.sandboxDir));
      return this.api.runCommand(cmd);
   }

   public Response makeWriteable() throws APIException {
      this.api.getLogger().debug("Setting files to writeable in " + this.sandboxDir + " for project " + this.siProject.getConfigurationPath());
      Command cmd = new Command("si", "makewritable");
      cmd.addOption(new Option("recurse"));
      cmd.addOption(new Option("cwd", this.sandboxDir));
      return this.api.runCommand(cmd);
   }

   public Response revertMembers() throws APIException {
      this.api.getLogger().debug("Reverting changes in sandbox " + this.sandboxDir + " for project " + this.siProject.getConfigurationPath());
      Command cmd = new Command("si", "revert");
      cmd.addOption(new Option("recurse"));
      cmd.addOption(new Option("cwd", this.sandboxDir));
      return this.api.runCommand(cmd);
   }

   public List<ScmFile> getNewMembers(String exclude, String include) throws APIException {
      List<ScmFile> filesAdded = new ArrayList();
      Command siViewNonMem = new Command("si", "viewnonmembers");
      siViewNonMem.addOption(new Option("recurse"));
      if (null != exclude && exclude.length() > 0) {
         siViewNonMem.addOption(new Option("exclude", exclude));
      }

      if (null != include && include.length() > 0) {
         siViewNonMem.addOption(new Option("include", include));
      }

      siViewNonMem.addOption(new Option("noincludeFormers"));
      siViewNonMem.addOption(new Option("cwd", this.sandboxDir));
      Response response = this.api.runCommand(siViewNonMem);
      WorkItemIterator wit = response.getWorkItems();

      while(wit.hasNext()) {
         filesAdded.add(new ScmFile(wit.next().getField("absolutepath").getValueAsString(), ScmFileStatus.ADDED));
      }

      return filesAdded;
   }

   public List<ScmFile> addNonMembers(String exclude, String include, String message) {
      this.addSuccess = true;
      List<ScmFile> filesAdded = new ArrayList();
      this.api.getLogger().debug("Looking for new members in sandbox dir: " + this.sandboxDir);

      try {
         List<ScmFile> newFileList = this.getNewMembers(exclude, include);
         Iterator sit = newFileList.iterator();

         while(sit.hasNext()) {
            try {
               ScmFile localFile = (ScmFile)sit.next();
               this.add(new File(localFile.getPath()), message);
               filesAdded.add(localFile);
            } catch (APIException var9) {
               this.addSuccess = false;
               ExceptionHandler eh = new ExceptionHandler(var9);
               this.api.getLogger().error("MKS API Exception: " + eh.getMessage());
               this.api.getLogger().debug(eh.getCommand() + " completed with exit Code " + eh.getExitCode());
            }
         }
      } catch (APIException var10) {
         this.addSuccess = false;
         ExceptionHandler eh = new ExceptionHandler(var10);
         this.api.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.api.getLogger().debug(eh.getCommand() + " completed with exit Code " + eh.getExitCode());
      }

      return filesAdded;
   }

   public boolean getOverallAddSuccess() {
      return this.addSuccess;
   }

   public boolean hasWorkingFile(Item wfdelta) {
      return !wfdelta.getField("noWorkingFile").getBoolean();
   }

   public List<WorkItem> getChangeList() throws APIException {
      List<WorkItem> changedFiles = new ArrayList();
      Command siViewSandbox = new Command("si", "viewsandbox");
      MultiValue mv = new MultiValue(",");
      mv.add("name");
      mv.add("context");
      mv.add("wfdelta");
      mv.add("memberarchive");
      siViewSandbox.addOption(new Option("fields", mv));
      siViewSandbox.addOption(new Option("recurse"));
      siViewSandbox.addOption(new Option("noincludeDropped"));
      siViewSandbox.addOption(new Option("filterSubs"));
      siViewSandbox.addOption(new Option("cwd", this.sandboxDir));
      Response r = this.api.runCommand(siViewSandbox);
      WorkItemIterator wit = r.getWorkItems();

      while(wit.hasNext()) {
         WorkItem wi = wit.next();
         this.api.getLogger().debug("Inspecting file: " + wi.getField("name").getValueAsString());
         if (wi.getModelType().equals("si.Member")) {
            Item wfdeltaItem = (Item)wi.getField("wfdelta").getValue();
            if (this.isDelta(wfdeltaItem)) {
               File memberFile = new File(wi.getField("name").getValueAsString());
               if (this.hasWorkingFile(wfdeltaItem)) {
                  if (this.hasMemberChanged(memberFile, wi.getId())) {
                     changedFiles.add(wi);
                  }
               } else {
                  changedFiles.add(wi);
               }
            }
         }
      }

      return changedFiles;
   }

   public List<ScmFile> checkInUpdates(String message) {
      this.ciSuccess = true;
      List<ScmFile> changedFiles = new ArrayList();
      this.api.getLogger().debug("Looking for changed and dropped members in sandbox dir: " + this.sandboxDir);

      try {
         List<WorkItem> changeList = this.getChangeList();
         Iterator wit = changeList.iterator();

         while(wit.hasNext()) {
            try {
               WorkItem wi = (WorkItem)wit.next();
               File memberFile = new File(wi.getField("name").getValueAsString());
               if (this.hasWorkingFile((Item)wi.getField("wfdelta").getValue())) {
                  this.lock(memberFile, wi.getId());
                  this.checkin(memberFile, wi.getId(), message);
                  changedFiles.add(new ScmFile(memberFile.getAbsolutePath(), ScmFileStatus.CHECKED_IN));
               } else {
                  this.dropMember(memberFile, wi.getId());
                  changedFiles.add(new ScmFile(memberFile.getAbsolutePath(), ScmFileStatus.DELETED));
               }
            } catch (APIException var7) {
               this.ciSuccess = false;
               ExceptionHandler eh = new ExceptionHandler(var7);
               this.api.getLogger().error("MKS API Exception: " + eh.getMessage());
               this.api.getLogger().debug(eh.getCommand() + " completed with exit Code " + eh.getExitCode());
            }
         }
      } catch (APIException var8) {
         this.ciSuccess = false;
         ExceptionHandler eh = new ExceptionHandler(var8);
         this.api.getLogger().error("MKS API Exception: " + eh.getMessage());
         this.api.getLogger().debug(eh.getCommand() + " completed with exit Code " + eh.getExitCode());
      }

      return changedFiles;
   }

   public boolean getOverallCheckInSuccess() {
      return this.ciSuccess;
   }

   public Response createSubproject(String dirPath) throws APIException {
      this.api.getLogger().debug("Creating subprojects for: " + dirPath + "/project.pj");
      Command siCreateSubproject = new Command("si", "createsubproject");
      siCreateSubproject.addOption(new Option("cpid", this.cpid));
      siCreateSubproject.addOption(new Option("createSubprojects"));
      siCreateSubproject.addOption(new Option("cwd", this.sandboxDir));
      siCreateSubproject.addSelection(dirPath + "/project.pj");
      return this.api.runCommand(siCreateSubproject);
   }

   public ChangeLogSet getChangeLog(Date startDate, Date endDate) throws APIException {
      ChangeLogSet changeLog = new ChangeLogSet(startDate, endDate);
      Hashtable<String, ChangeSet> changeSetHash = new Hashtable();
      Command siRlog = new Command("si", "rlog");
      siRlog.addOption(new Option("recurse"));
      MultiValue rFilter = new MultiValue(":");
      rFilter.add("daterange");
      rFilter.add("'" + RLOG_DATEFORMAT.format(startDate) + "'-'" + RLOG_DATEFORMAT.format(endDate) + "'");
      siRlog.addOption(new Option("rfilter", rFilter));
      siRlog.addOption(new Option("cwd", this.sandboxDir));
      Response response = this.api.runCommand(siRlog);
      WorkItemIterator wit = response.getWorkItems();

      while(true) {
         String memberName;
         Field revisionsFld;
         do {
            do {
               do {
                  if (!wit.hasNext()) {
                     List<ChangeSet> changeSetList = new ArrayList();
                     changeSetList.addAll(changeSetHash.values());
                     changeLog.setChangeSets(changeSetList);
                     return changeLog;
                  }

                  WorkItem wi = wit.next();
                  memberName = wi.getContext();
                  memberName = memberName.substring(0, memberName.lastIndexOf(47));
                  memberName = memberName + '/' + wi.getId();
                  memberName = memberName.replace('\\', '/');
                  revisionsFld = wi.getField("revisions");
               } while(null == revisionsFld);
            } while(!revisionsFld.getDataType().equals("com.mks.api.response.ItemList"));
         } while(null == revisionsFld.getList());

         List<Item> revList = revisionsFld.getList();
         Iterator lit = revList.iterator();

         while(lit.hasNext()) {
            Item revisionItem = (Item)lit.next();
            String revision = revisionItem.getId();
            String author = revisionItem.getField("author").getItem().getId();

            try {
               author = revisionItem.getField("author").getItem().getField("fullname").getValueAsString();
            } catch (NullPointerException var24) {
            }

            String cpid = ":none";

            try {
               cpid = revisionItem.getField("cpid").getItem().getId();
            } catch (NullPointerException var23) {
            }

            String comment = cpid + ": " + revisionItem.getField("cpsummary").getValueAsString();
            Date date = revisionItem.getField("date").getDateTime();
            ChangeFile changeFile = new ChangeFile(memberName, revision);
            ChangeSet changeSet = (ChangeSet)changeSetHash.get(cpid);
            if (null != changeSet) {
               if (changeSet.getDate().after(date)) {
                  changeSet.setDate(date);
               }

               changeSet.addFile(changeFile);
               changeSetHash.put(cpid, changeSet);
            } else {
               List<ChangeFile> changeFileList = new ArrayList();
               changeFileList.add(changeFile);
               changeSet = new ChangeSet(date, comment, author, changeFileList);
               changeSetHash.put(cpid, changeSet);
            }
         }
      }
   }
}
