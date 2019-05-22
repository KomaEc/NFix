package com.mks.api.commands;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.FileOption;
import com.mks.api.Option;
import com.mks.api.OptionList;
import com.mks.api.response.APIException;
import com.mks.api.response.InvalidCommandOptionException;
import com.mks.api.response.InvalidCommandSelectionException;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SICommands extends MKSCommands {
   protected boolean isInteractive;

   public SICommands(CmdRunnerCreator session, boolean isInteractive) throws APIException {
      super(session);
      this.isInteractive = isInteractive;
   }

   public void siClientAboutView() throws APIException {
      Command cmd = new Command("si", "about");
      cmd.addOption(new Option("g"));
      this.runAPICommand(cmd);
   }

   public void launchMKSGUI() throws APIException {
      Command cmd = new Command("si", "gui");
      this.runAPICommand(cmd);
   }

   public void siPreferencesView() throws APIException {
      Command cmd = new Command("si", "viewprefs");
      cmd.addOption(new Option("g"));
      this.runAPICommand(cmd);
   }

   private boolean resolveLocalMemberAccess(Command cmd, String cwd, String project, String member) throws APIException {
      boolean sandboxAssumed = true;
      if (member != null && member.length() != 0) {
         if (project == null) {
            File memberFile = new File(cwd, member);
            if (!memberFile.isAbsolute()) {
               throw new InvalidCommandOptionException("SICommands: parameter 'cwd' cannot be null or empty.");
            }

            member = memberFile.getAbsolutePath();
         }

         if (project != null) {
            cmd.addOption(new Option("P", project));
            sandboxAssumed = false;
         }

         cmd.addSelection(member);
         return sandboxAssumed;
      } else {
         throw new InvalidCommandSelectionException("SICommands: parameter 'member' cannot be null or empty.");
      }
   }

   public void siSandboxMemberHistoryView(String cwd, String member) throws APIException {
      this.siMemberHistoryView(cwd, (String)null, member);
   }

   public void siMemberHistoryView(String project, String member) throws APIException {
      this.siMemberHistoryView((String)null, project, member);
   }

   private void siMemberHistoryView(String cwd, String project, String member) throws APIException {
      Command cmd = new Command("si", "viewhistory");
      cmd.addOption(new Option("g"));
      this.resolveLocalMemberAccess(cmd, cwd, project, member);
      this.runAPICommand(cmd);
   }

   public void siSandboxMemberInformationView(String cwd, String member) throws APIException {
      Command cmd = new Command("si", "memberinfo");
      cmd.addOption(new Option("g"));
      this.resolveLocalMemberAccess(cmd, cwd, (String)null, member);
      this.runAPICommand(cmd);
   }

   public void siSandboxMemberDifferences(String cwd, String member) throws APIException {
      this.siMemberDifferences(cwd, (String)null, member, (String)null, (String)null);
   }

   public void siMemberDifferences(String project, String member, String revision1, String revision2) throws APIException {
      this.siMemberDifferences((String)null, project, member, revision1, revision2);
   }

   private void siMemberDifferences(String cwd, String project, String member, String revision1, String revision2) throws APIException {
      Command cmd = new Command("si", "diff");
      cmd.addOption(new Option("g"));
      if (revision1 != null) {
         cmd.addOption(new Option("r", revision1));
      }

      if (revision2 != null) {
         cmd.addOption(new Option("r", revision2));
      }

      this.resolveLocalMemberAccess(cmd, cwd, project, member);
      this.runAPICommand(cmd);
   }

   public boolean isSandboxMember(String cwd, String filename) {
      try {
         String[] members = new String[]{filename};
         OptionList options = new OptionList();
         options.add(new Option("fields", "name"));
         Response response = this.getSandboxMemberStatus(cwd, members, options);
         Item item = response.getWorkItems().next();
         return item != null && item.getModelType().equalsIgnoreCase("si.Member");
      } catch (APIException var7) {
         return false;
      }
   }

   public Response siCheckOut(String cwd, String[] members) throws APIException {
      return this.siCheckOut(cwd, members, (OptionList)null);
   }

   public Response siCheckOut(String cwd, String[] members, OptionList options) throws APIException {
      if (members != null && members.length != 0) {
         Command cmd = new Command("ii", "checkout");
         if (this.isInteractive) {
            cmd.addOption(new Option("g"));
         }

         if (cwd != null) {
            cmd.addOption(new Option("cwd", cwd));
         }

         if (members != null && members.length > 0) {
            for(int i = 0; i < members.length; ++i) {
               cmd.addSelection(members[i]);
            }

            cmd.addOption(new Option("recurse"));
         }

         if (options != null) {
            Iterator it = options.getOptions();

            while(it.hasNext()) {
               cmd.addOption((Option)it.next());
            }
         }

         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("SICommands.siCheckOut: parameter 'members' cannot be null or empty.");
      }
   }

   public Response siCheckIn(String cwd, String[] members) throws APIException {
      return this.siCheckIn(cwd, members, (OptionList)null);
   }

   public Response siCheckIn(String cwd, String[] members, OptionList options) throws APIException {
      SICheckinCommand siCheckin = new SICheckinCommand(this.getCmdRunnerCreator());
      siCheckin.setCwd(cwd);
      siCheckin.addOptionList(options);
      return siCheckin.execute(members, this.isInteractive);
   }

   public Response siSubmitMembers(String cwd, String[] members) throws APIException {
      return this.siSubmitMembers(cwd, members, (OptionList)null);
   }

   public Response siSubmitMembers(String cwd, String[] members, OptionList options) throws APIException {
      if (members != null && members.length != 0) {
         Command cmd = new Command("si", "submit");
         if (this.isInteractive) {
            cmd.addOption(new Option("g"));
         }

         if (cwd != null) {
            cmd.addOption(new Option("cwd", cwd));
         }

         if (members != null && members.length > 0) {
            for(int i = 0; i < members.length; ++i) {
               cmd.addSelection(members[i]);
            }

            cmd.addOption(new Option("recurse"));
         }

         if (options != null) {
            Iterator it = options.getOptions();

            while(it.hasNext()) {
               cmd.addOption((Option)it.next());
            }
         }

         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("SICommands.siCheckIn: parameter 'members' cannot be null or empty.");
      }
   }

   public Response siAddMembers(String cwd, String[] nonMembers) throws APIException {
      return this.siAddMembers(cwd, nonMembers, (OptionList)null);
   }

   public Response siAddMembers(String cwd, String[] nonMembers, OptionList options) throws APIException {
      SIAddCommand siAdd = new SIAddCommand(this.getCmdRunnerCreator());
      siAdd.setCwd(cwd);
      siAdd.addOptionList(options);
      return siAdd.execute(nonMembers, this.isInteractive);
   }

   public Response siAddSharedMember(String cwd, String member, String archive) throws APIException {
      if (archive != null && archive.length() != 0) {
         Command cmd = new Command("si", "add");
         if (this.isInteractive) {
            cmd.addOption(new Option("g"));
         }

         if (cwd != null) {
            cmd.addOption(new Option("cwd", cwd));
         }

         cmd.addOption(new Option("archive", archive));
         cmd.addOption(new Option("revision", ":head"));
         this.resolveLocalMemberAccess(cmd, cwd, (String)null, member);
         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandOptionException("SICommands.siAddMembers: parameter 'archive' cannot be null or empty.");
      }
   }

   public String getSandboxMemberArchive(String cwd, String member) throws APIException {
      Response response = this.getSandboxMemberArchiveInfo(cwd, member);
      return response.getWorkItems().next().getField("archiveName").getValueAsString();
   }

   public Response getSandboxMemberArchiveInfo(String cwd, String member) throws APIException {
      Command cmd = new Command("si", "archiveinfo");
      this.resolveLocalMemberAccess(cmd, cwd, (String)null, member);
      return this.runAPICommand(cmd);
   }

   public void siDropSandboxMembers(String cwd, String[] members) throws APIException {
      this.siDropSandboxMembers(cwd, members, (OptionList)null);
   }

   public void siDropSandboxMembers(String cwd, String[] members, OptionList options) throws APIException {
      SIDropCommand siDrop = new SIDropCommand(this.getCmdRunnerCreator());
      siDrop.setCwd(cwd);
      siDrop.addOptionList(options);
      siDrop.execute(members, this.isInteractive);
   }

   public void siMoveSandboxMembers(String cwd, String[] members, OptionList options) throws APIException {
      SIMoveCommand siMove = new SIMoveCommand(this.getCmdRunnerCreator());
      siMove.setCwd(cwd);
      siMove.addOptionList(options);
      siMove.execute(members, this.isInteractive);
   }

   public Response siRenameSandboxMember(String cwd, String member, String newName) throws APIException {
      return this.siRenameMember(cwd, (String)null, member, newName, new OptionList());
   }

   public Response siRenameMember(String project, String member, String newName) throws APIException {
      return this.siRenameMember((String)null, project, member, newName, new OptionList());
   }

   public Response siRenameMember(String cwd, String member, String newName, OptionList options) throws APIException {
      return this.siRenameMember(cwd, (String)null, member, newName, options);
   }

   private Response siRenameMember(String cwd, String project, String member, String newName, OptionList options) throws APIException {
      Command cmd = new Command("si", "rename");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      cmd.addOption(new Option("newName", newName));
      if (options != null) {
         Iterator it = options.getOptions();

         while(it.hasNext()) {
            cmd.addOption((Option)it.next());
         }
      }

      cwd = this.validateSandboxCWD(cwd, new String[]{member});
      String memberName = (new File(member)).getName();
      this.resolveLocalMemberAccess(cmd, cwd, project, memberName);
      return this.runAPICommand(cmd);
   }

   public Response siResync(String cwd, String[] members, boolean recurse) throws APIException {
      SIResyncCommand cmd = new SIResyncCommand(this.getCmdRunnerCreator());
      cmd.setCwd(cwd);
      cmd.setRecurse(recurse);
      return cmd.execute(members, true);
   }

   public Response siResync(String cwd, String[] members) throws APIException {
      return this.siResync(cwd, members, false);
   }

   public Response siRevertMembers(String cwd, String[] members) throws APIException {
      SIRevertCommand siRevert = new SIRevertCommand(this.getCmdRunnerCreator());
      siRevert.setCwd(cwd);
      return siRevert.execute(members, this.isInteractive);
   }

   private void siRetrieveMember(String cwd, String project, String member, String revision, String targetFile) throws APIException {
      if (targetFile != null && targetFile.length() != 0) {
         if (revision != null && revision.length() != 0) {
            Command cmd = new Command("si", "projectco");
            cmd.addOption(new Option("nolock"));
            if (revision != null) {
               cmd.addOption(new Option("revision", revision));
            }

            cmd.addOption(new Option("overwriteExisting"));
            File target = new File(targetFile);
            if (!target.exists()) {
               try {
                  target.createNewFile();
               } catch (IOException var9) {
               }
            }

            cmd.addOption(new FileOption("targetFile", targetFile));
            this.resolveLocalMemberAccess(cmd, cwd, project, member);
            this.runAPICommand(cmd);
         } else {
            throw new InvalidCommandOptionException("SICommands.siProjectCheckout: parameter 'revision' cannot be null or empty.");
         }
      } else {
         throw new InvalidCommandOptionException("SICommands.siProjectCheckout: parameter 'targetFile' cannot be null or empty.");
      }
   }

   public void siRetrieveMember(String project, String member, String revision, String targetFile) throws APIException {
      this.siRetrieveMember((String)null, project, member, revision, targetFile);
   }

   public void siRetrieveSandboxMember(String cwd, String member, String revision, String targetFile) throws APIException {
      this.siRetrieveMember(cwd, (String)null, member, revision, targetFile);
   }

   public Response getSandboxMemberStatus(String cwd, String[] members) throws APIException {
      List fields = new ArrayList();
      fields.add("lockrecord");
      fields.add("memberarchive");
      fields.add("memberrev");
      fields.add("name");
      fields.add("newrevdelta");
      fields.add("revsyncdelta");
      fields.add("type");
      fields.add("wfdelta");
      fields.add("workingrev");
      fields.add("merge");
      fields.add("frozen");
      fields.add("archiveshared");
      fields.add("workingcpid");
      fields.add("context");
      return this.getSandboxMemberStatus(cwd, members, (List)fields);
   }

   public Response getSandboxMemberStatus(String cwd, String[] members, List statusFields) throws APIException {
      OptionList options = new OptionList();
      StringBuffer sb = null;

      for(Iterator it = statusFields.iterator(); it.hasNext(); sb.append(it.next())) {
         if (sb == null) {
            sb = new StringBuffer();
         } else {
            sb.append(',');
         }
      }

      if (sb != null) {
         options.add(new Option("fields", sb.toString()));
      }

      return this.getSandboxMemberStatus(cwd, members, options);
   }

   public Response getSandboxMemberStatus(String cwd, String[] members, OptionList options) throws APIException {
      return this.getSandboxMemberStatus(cwd, members, options, false);
   }

   public Response getSandboxMemberStatus(String cwd, String[] members, OptionList options, boolean generateStreamedResponse) throws APIException {
      Command cmd = new Command("si", "viewsandbox");
      if (this.isInteractive) {
         cmd.addOption(new Option("settingsUI", "gui"));
      }

      cmd.addOption(new Option("cwd", this.validateSandboxCWD(cwd, members)));
      cmd.addOption(new Option("norecurse"));
      if (members != null && members.length > 0) {
         for(int i = 0; i < members.length; ++i) {
            cmd.addSelection(members[i]);
         }
      }

      if (options != null) {
         Iterator it = options.getOptions();

         while(it.hasNext()) {
            cmd.addOption((Option)it.next());
         }
      }

      return this.runAPICommand(cmd, generateStreamedResponse);
   }

   public Response getSandboxMembers(File sandbox, OptionList options, boolean generateStreamedResponse) throws APIException {
      Command cmd = new Command("si", "viewsandbox");
      cmd.addOption(new Option("sandbox", sandbox.toString()));
      if (options != null) {
         Iterator it = options.getOptions();

         while(it.hasNext()) {
            cmd.addOption((Option)it.next());
         }
      }

      return this.runAPICommand(cmd, generateStreamedResponse);
   }

   public Response getSandboxMemberDifferenceStatus(String cwd, String[] members, OptionList options) throws APIException {
      Command cmd = new Command("si", "diff");
      if (this.isInteractive) {
         cmd.addOption(new Option("settingsUI", "gui"));
      }

      cmd.addOption(new Option("cwd", this.validateSandboxCWD(cwd, members)));
      if (members != null && members.length > 0) {
         for(int i = 0; i < members.length; ++i) {
            cmd.addSelection(members[i]);
         }
      }

      if (options != null) {
         Iterator it = options.getOptions();

         while(it.hasNext()) {
            cmd.addOption((Option)it.next());
         }
      }

      return this.runAPICommand(cmd);
   }

   public void siLabelSandboxMember(String cwd, String[] members, String label, String revision) throws APIException {
      Command cmd = new Command("si", "addlabel");
      if (this.isInteractive) {
         cmd.addOption(new Option("gui"));
      }

      if (cwd != null) {
         cmd.addOption(new Option("cwd", cwd));
      }

      if (label != null) {
         cmd.addOption(new Option("label", label));
         cmd.addOption(new Option("moveLabel"));
      }

      if (revision != null) {
         cmd.addOption(new Option("revision", revision));
      }

      cmd.addOption(new Option("recurse"));

      for(int i = 0; i < members.length; ++i) {
         cmd.addSelection(members[i]);
      }

      this.runAPICommand(cmd);
   }

   public Response siMakeSandboxMemberWritable(String cwd, String[] members) throws APIException {
      Command cmd = new Command("si", "makewritable");
      if (cwd != null) {
         cmd.addOption(new Option("cwd", cwd));
      }

      cmd.addOption(new Option("recurse"));

      for(int i = 0; i < members.length; ++i) {
         cmd.addSelection(members[i]);
      }

      return this.runAPICommand(cmd);
   }

   public Response siLockSandboxMembers(String cwd, String[] members, String cpid, boolean allowPrompting, boolean isUpgrade) throws APIException {
      SILockCommand siLock = new SILockCommand(this.getCmdRunnerCreator());
      siLock.setCwd(cwd);
      if (cpid != null) {
         siLock.setCpid(cpid);
      }

      siLock.setAllowPrompting(allowPrompting);
      siLock.setLockType("auto");
      if (isUpgrade) {
         siLock.setAction("upgrade");
      }

      return siLock.execute(members, this.isInteractive);
   }

   public Response siDowngradeLockMembers(String cwd, String[] members) throws APIException {
      return this.siUnlockMembers(cwd, members, true);
   }

   public Response siUnlockMembers(String cwd, String[] members, boolean downgrade) throws APIException {
      SIUnlockCommand siUnlock = new SIUnlockCommand(this.getCmdRunnerCreator());
      siUnlock.setCwd(cwd);
      if (downgrade) {
         siUnlock.setAction("downgrade");
      } else {
         siUnlock.setAction("unlock");
      }

      return siUnlock.execute(members, this.isInteractive);
   }

   public Response siCreateProject(String project) throws APIException {
      Command cmd = new Command("si", "createproject");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      if (project != null) {
         cmd.addSelection(project);
      }

      return this.runAPICommand(cmd);
   }

   public Response siCreateSubproject(String project, String subproject) throws APIException {
      Command cmd = new Command("si", "createsubproject");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      if (project != null && project.length() > 0) {
         cmd.addOption(new Option("project", project));
      }

      if (subproject != null) {
         cmd.addSelection(subproject);
      }

      return this.runAPICommand(cmd);
   }

   public Response runCreateProjectWizard() throws APIException {
      Command cmd = new Command("ii", "createprojectwizard");
      cmd.addOption(new Option("g"));
      return this.runAPICommand(cmd);
   }

   public Response getProjectInfo(String project) throws APIException {
      return this.getProjectInfo(project, false);
   }

   public Response getProjectInfo(String project, boolean showDevPaths) throws APIException {
      if (project != null && project.length() != 0) {
         Command cmd = new Command("si", "projectinfo");
         cmd.addOption(new Option("noassociatedIssues"));
         cmd.addOption(new Option("noacl"));
         cmd.addOption(new Option("noshowCheckpointDescription"));
         cmd.addOption(new Option("noattributes"));
         if (showDevPaths) {
            cmd.addOption(new Option("devpaths"));
         } else {
            cmd.addOption(new Option("nodevpaths"));
         }

         cmd.addOption(new Option("project", project));
         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandOptionException("SICommands: parameter 'project' cannot be null or empty.");
      }
   }

   static String getMemberListCWD(String[] memberlist) throws APIException {
      String cwd = null;
      if (memberlist != null) {
         for(int i = 0; i < memberlist.length; ++i) {
            File memberFile = new File(memberlist[i]);
            if (memberFile.isAbsolute()) {
               try {
                  File dir;
                  if (!memberFile.isDirectory()) {
                     dir = memberFile.getParentFile();
                  } else {
                     dir = memberFile;
                  }

                  if (dir != null) {
                     if (cwd == null) {
                        cwd = dir.getCanonicalPath();
                     } else {
                        String dirpath = dir.getCanonicalPath();
                        if (cwd.startsWith(dirpath)) {
                           cwd = dirpath;
                        } else {
                           while(!dirpath.startsWith(cwd)) {
                              cwd = (new File(cwd)).getParent();
                              if (cwd == null) {
                                 throw new InvalidCommandSelectionException("SICommands: selection of member cannot span devices");
                              }
                           }
                        }
                     }
                  }
               } catch (IOException var6) {
                  throw new InvalidCommandSelectionException(var6);
               }
            }
         }
      }

      return cwd;
   }

   protected String validateSandboxCWD(String cwd, String[] memberlist) throws APIException {
      String rootdir = getMemberListCWD(memberlist);
      if (rootdir != null) {
         if (cwd == null) {
            cwd = rootdir;
         } else {
            String canonicalCWD = null;

            try {
               canonicalCWD = (new File(cwd)).getCanonicalPath();
            } catch (IOException var6) {
               throw new InvalidCommandOptionException(var6);
            }

            if (!rootdir.startsWith(canonicalCWD)) {
               throw new InvalidCommandOptionException("SICommands: invalid 'cwd' in relation to its member list");
            }
         }
      }

      if (cwd != null && cwd.length() != 0) {
         return cwd;
      } else {
         throw new InvalidCommandOptionException("SICommands: parameter 'cwd' cannot be null or empty.");
      }
   }

   public Response getSandboxInfo(String cwd) throws APIException {
      if (cwd != null && cwd.length() != 0) {
         Command cmd = new Command("si", "sandboxinfo");
         cmd.addOption(new Option("noassociatedIssues"));
         cmd.addOption(new Option("noattributes"));
         cmd.addOption(new Option("noshowCheckpointDescription"));
         cmd.addOption(new Option("cwd", cwd));
         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandOptionException("SICommands: parameter 'cwd' cannot be null or empty.");
      }
   }

   public Response getSandboxInfoFromSandbox(String sandboxFile) throws APIException {
      if (sandboxFile != null && sandboxFile.length() != 0) {
         Command cmd = new Command("si", "sandboxinfo");
         cmd.addOption(new Option("noassociatedIssues"));
         cmd.addOption(new Option("noattributes"));
         cmd.addOption(new Option("noshowCheckpointDescription"));
         cmd.addOption(new Option("sandbox", sandboxFile));
         return this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandOptionException("SICommands: parameter 'sandboxFile' cannot be null or empty.");
      }
   }

   public String getSandboxName(String cwd) throws APIException {
      try {
         Response response = this.getSandboxInfo(cwd);
         WorkItem sandbox = response.getWorkItems().next();
         return sandbox.getField("sandboxName").getValueAsString();
      } catch (NoSuchElementException var4) {
         throw new APIException(var4);
      }
   }

   public String siCreateSandbox(String project, String sandboxDir) throws APIException {
      return this.siCreateSandbox(project, (String)null, (String)null, sandboxDir);
   }

   public String siCreateVariantSandbox(String project, String devPath, String sandboxDir) throws APIException {
      return this.siCreateSandbox(project, devPath, (String)null, sandboxDir);
   }

   public String siCreateBuildSandbox(String project, String projectRevision, String sandboxDir) throws APIException {
      return this.siCreateSandbox(project, (String)null, projectRevision, sandboxDir);
   }

   private String siCreateSandbox(String project, String devPath, String projectRevision, String sandboxDir) throws APIException {
      Command cmd = new Command("si", "createsandbox");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      cmd.addOption(new Option("noopenView"));
      cmd.addOption(new Option("R"));
      if (project != null && project.length() > 0) {
         cmd.addOption(new Option("project", project));
         if (devPath != null && devPath.length() > 0) {
            cmd.addOption(new Option("devpath", devPath));
         }

         if (projectRevision != null && projectRevision.length() > 0) {
            cmd.addOption(new Option("projectRevision", projectRevision));
         }

         if (sandboxDir != null && sandboxDir.length() > 0) {
            cmd.addSelection(sandboxDir);
         }

         Response response = this.runAPICommand(cmd);
         return response.getResult().getPrimaryValue().getId();
      } else {
         throw new InvalidCommandOptionException("SICommands.siCreateSandbox: parameter 'project' cannot be null or empty.");
      }
   }

   public void siSandboxView(String cwd) throws APIException {
      this.siSandboxView(cwd, (String[])null);
   }

   public void siSandboxView(String cwd, String[] members) throws APIException {
      Command cmd = new Command("si", "viewsandbox");
      cmd.addOption(new Option("cwd", this.validateSandboxCWD(cwd, members)));
      cmd.addOption(new Option("g"));
      if (members != null && members.length > 0) {
         for(int i = 0; i < members.length; ++i) {
            cmd.addSelection(members[i]);
         }
      }

      this.runAPICommand(cmd);
   }

   public void siDeleteSandbox(String sandboxFile) throws APIException {
      if (sandboxFile != null) {
         Command cmd = new Command("si", "dropsandbox");
         cmd.addOption(new Option("delete", "all"));
         cmd.addSelection(sandboxFile);
         this.runAPICommand(cmd);
      }

   }

   public Response getSandboxes(boolean includeSubs) throws APIException {
      Command cmd = new Command("si", "sandboxes");
      if (includeSubs) {
         cmd.addOption(new Option("displaySubs"));
      }

      return this.runAPICommand(cmd);
   }

   public String siCreateChangePackage() throws APIException {
      return this.siCreateChangePackage((Integer)null, (String)null);
   }

   public String siCreateChangePackage(String summary) throws APIException {
      return this.siCreateChangePackage((Integer)null, summary);
   }

   public String siCreateChangePackage(int issueId) throws APIException {
      return this.siCreateChangePackage(new Integer(issueId), (String)null);
   }

   private String siCreateChangePackage(Integer issueId, String summary) throws APIException {
      Command cmd = new Command("si", "createcp");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      if (issueId != null) {
         cmd.addOption(new Option("issueID", String.valueOf(issueId)));
      }

      if (summary != null) {
         cmd.addOption(new Option("summary", summary));
      }

      Response response = this.runAPICommand(cmd);
      Result result = response.getResult();
      String cpid = result.getPrimaryValue().getId();
      this.setActiveChangePackage(cpid);
      return cpid;
   }

   public void siSubmitChangePackage(String cpID) throws APIException {
      Command cmd = new Command("si", "submitcp");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      if (cpID != null && cpID.length() > 0) {
         cmd.addSelection(cpID);
         this.runAPICommand(cmd);
         if (this.getActiveChangePackage().equalsIgnoreCase(cpID)) {
            this.setActiveChangePackage(":none");
         }

      } else {
         throw new InvalidCommandSelectionException("SICommands.siSubmitCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public void siCloseChangePackage(String cpID) throws APIException {
      Command cmd = new Command("si", "closecp");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      }

      if (cpID != null && cpID.length() > 0) {
         cmd.addSelection(cpID);
         this.runAPICommand(cmd);
         if (this.getActiveChangePackage().equalsIgnoreCase(cpID)) {
            this.setActiveChangePackage(":none");
         }

      } else {
         throw new InvalidCommandSelectionException("SICommands.siCloseCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public void siDiscardChangePackage(String cpid) throws APIException {
      this.siDiscardChangePackage(cpid, (OptionList)null);
   }

   public void siDiscardChangePackage(String cpid, OptionList options) throws APIException {
      Command cmd = new Command("si", "discardcp");
      if (this.isInteractive) {
         cmd.addOption(new Option("g"));
      } else {
         cmd.addOption(new Option("noconfirm"));
      }

      if (cpid != null && cpid.length() > 0) {
         cmd.addSelection(cpid);
         this.runAPICommand(cmd);
         if (this.getActiveChangePackage().equalsIgnoreCase(cpid)) {
            this.setActiveChangePackage(":none");
         }

      } else {
         throw new InvalidCommandSelectionException("SICommands.siCloseCP: parameter 'cpid' cannot be null or empty.");
      }
   }

   public void siChangePackageView(String cpID) throws APIException {
      Command cmd = new Command("si", "viewcp");
      cmd.addOption(new Option("g"));
      if (cpID != null && cpID.length() > 0) {
         cmd.addSelection(cpID);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("SICommands.siViewCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public String getChangePackageSummary(String cpID) throws APIException {
      Command cmd = new Command("si", "viewcps");
      if (this.isInteractive) {
         cmd.addOption(new Option("settingsUI", "gui"));
      }

      cmd.addOption(new Option("fields=summary"));
      if (cpID != null && cpID.length() > 0) {
         cmd.addSelection(cpID);
         Response response = this.runAPICommand(cmd);
         return response.getWorkItems().next().getField("summary").getValueAsString();
      } else {
         throw new InvalidCommandSelectionException("SICommands.siSubmitCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public void setActiveChangePackage(String cpID) throws APIException {
      if (cpID != null && cpID.length() != 0) {
         Command cmd = new Command("si", "setactivecp");
         if (this.isInteractive) {
            cmd.addOption(new Option("settingsUI", "gui"));
         }

         cmd.addSelection(cpID);
         this.runAPICommand(cmd);
      } else {
         throw new InvalidCommandSelectionException("SICommands.siSetActiveCP: parameter 'cpId' cannot be null or empty.");
      }
   }

   public String getActiveChangePackage() throws APIException {
      Command cmd = new Command("si", "viewactivecp");
      if (this.isInteractive) {
         cmd.addOption(new Option("settingsUI", "gui"));
      }

      Response response = this.runAPICommand(cmd);
      return response.getWorkItems().next().getId();
   }

   public Item[] getChangePackages(int issueID) throws APIException {
      WorkItem[] cps = new WorkItem[0];
      Command cmd = new Command("si", "viewcp");
      cmd.addOption(new Option("showCommitted"));
      cmd.addOption(new Option("showPending"));
      cmd.addOption(new Option("showUncommitted"));
      cmd.addOption(new Option("fields=id,summary,state,archive,isclosed,member,project,revision,sandbox,server,siserver,type,user,variant"));
      cmd.addSelection(String.valueOf(issueID));
      Response response = this.runAPICommand(cmd);
      cps = new WorkItem[response.getWorkItemListSize()];
      int index = 0;

      for(WorkItemIterator items = response.getWorkItems(); items.hasNext(); ++index) {
         cps[index] = items.next();
      }

      return cps;
   }

   public Item getChangePackage(String cpID) throws APIException {
      Command cmd = new Command("si", "viewcp");
      cmd.addOption(new Option("showCommitted"));
      cmd.addOption(new Option("showPending"));
      cmd.addOption(new Option("showUncommitted"));
      cmd.addOption(new Option("fields=id,summary,state,archive,isclosed,member,project,revision,sandbox,server,siserver,type,user,variant"));
      cmd.addSelection(cpID);
      Response response = this.runAPICommand(cmd);
      return response.getWorkItems().next();
   }

   public Item[] getMyOpenChangePackages(String fields) throws APIException {
      WorkItem[] cps = new WorkItem[0];
      Command cmd = new Command("si", "viewcps");
      if (this.isInteractive) {
         cmd.addOption(new Option("settingsUI", "gui"));
      }

      if (fields != null) {
         cmd.addOption(new Option("fields=" + fields));
      }

      Response response = this.runAPICommand(cmd);
      cps = new WorkItem[response.getWorkItemListSize()];
      int index = 0;

      for(WorkItemIterator items = response.getWorkItems(); items.hasNext(); ++index) {
         cps[index] = items.next();
      }

      return cps;
   }
}
