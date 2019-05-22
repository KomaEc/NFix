package org.apache.maven.scm.provider.integrity;

import com.mks.api.Command;
import com.mks.api.MultiValue;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;

public class Project {
   public static final String NORMAL_PROJECT = "Normal";
   public static final String VARIANT_PROJECT = "Variant";
   public static final String BUILD_PROJECT = "Build";
   private String projectName;
   private String projectType;
   private String projectRevision;
   private String fullConfigSyntax;
   private Date lastCheckpoint;
   private APISession api;
   public static final Comparator<Member> FILES_ORDER = new Comparator<Member>() {
      public int compare(Member cmm1, Member cmm2) {
         return cmm1.getMemberName().compareToIgnoreCase(cmm2.getMemberName());
      }
   };

   public static void validateTag(String tagName) throws Exception {
      if (tagName != null && tagName.length() != 0) {
         char ch = tagName.charAt(0);
         if (('A' > ch || ch > 'Z') && ('a' > ch || ch > 'z')) {
            throw new Exception("The checkpoint label must start with an alpha character!");
         } else {
            char[] arr$ = "$,.:;/\\@".toCharArray();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               char invalid = arr$[i$];
               if (tagName.indexOf(invalid) >= 0) {
                  throw new Exception("The checkpoint label may cannot contain one of the following characters: $ , . : ; / \\ @");
               }
            }

         }
      } else {
         throw new Exception("The checkpoint label string is empty!");
      }
   }

   public Project(APISession api, String configPath) throws APIException {
      this.api = api;

      try {
         Command siProjectInfoCmd = new Command("si", "projectinfo");
         siProjectInfoCmd.addOption(new Option("project", configPath));
         api.getLogger().info("Preparing to execute si projectinfo for " + configPath);
         Response infoRes = api.runCommand(siProjectInfoCmd);
         WorkItem wi = infoRes.getWorkItems().next();
         Field pjNameFld = wi.getField("projectName");
         Field pjTypeFld = wi.getField("projectType");
         Field pjCfgPathFld = wi.getField("fullConfigSyntax");
         Field pjChkptFld = wi.getField("lastCheckpoint");
         if (null != pjNameFld && null != pjNameFld.getValueAsString()) {
            this.projectName = pjNameFld.getValueAsString();
         } else {
            api.getLogger().warn("Project info did not provide a value for the 'projectName' field!");
            this.projectName = "";
         }

         if (null != pjTypeFld && null != pjTypeFld.getValueAsString()) {
            this.projectType = pjTypeFld.getValueAsString();
            if (this.isBuild()) {
               Field pjRevFld = wi.getField("revision");
               if (null != pjRevFld && null != pjRevFld.getItem()) {
                  this.projectRevision = pjRevFld.getItem().getId();
               } else {
                  this.projectRevision = "";
                  api.getLogger().warn("Project info did not provide a vale for the 'revision' field!");
               }
            }
         } else {
            api.getLogger().warn("Project info did not provide a value for the 'projectType' field!");
            this.projectType = "";
         }

         if (null != pjCfgPathFld && null != pjCfgPathFld.getValueAsString()) {
            this.fullConfigSyntax = pjCfgPathFld.getValueAsString();
         } else {
            api.getLogger().error("Project info did not provide a value for the 'fullConfigSyntax' field!");
            this.fullConfigSyntax = "";
         }

         if (null != pjChkptFld && null != pjChkptFld.getDateTime()) {
            this.lastCheckpoint = pjChkptFld.getDateTime();
         } else {
            api.getLogger().warn("Project info did not provide a value for the 'lastCheckpoint' field!");
            this.lastCheckpoint = Calendar.getInstance().getTime();
         }
      } catch (NoSuchElementException var11) {
         api.getLogger().error("Project info did not provide a value for field " + var11.getMessage());
      }

   }

   public String getProjectName() {
      return this.projectName;
   }

   public String getProjectRevision() {
      return this.projectRevision;
   }

   public boolean isNormal() {
      return this.projectType.equalsIgnoreCase("Normal");
   }

   public boolean isVariant() {
      return this.projectType.equalsIgnoreCase("Variant");
   }

   public boolean isBuild() {
      return this.projectType.equalsIgnoreCase("Build");
   }

   public String getConfigurationPath() {
      return this.fullConfigSyntax;
   }

   public Date getLastCheckpointDate() {
      return this.lastCheckpoint;
   }

   public List<Member> listFiles(String workspaceDir) throws APIException {
      List<Member> memberList = new ArrayList();
      Hashtable<String, String> pjConfigHash = new Hashtable();
      pjConfigHash.put(this.projectName, this.fullConfigSyntax);
      String projectRoot = this.projectName.substring(0, this.projectName.lastIndexOf(47));
      Command siViewProjectCmd = new Command("si", "viewproject");
      siViewProjectCmd.addOption(new Option("recurse"));
      siViewProjectCmd.addOption(new Option("project", this.fullConfigSyntax));
      MultiValue mvFields = new MultiValue(",");
      mvFields.add("name");
      mvFields.add("context");
      mvFields.add("memberrev");
      mvFields.add("membertimestamp");
      mvFields.add("memberdescription");
      siViewProjectCmd.addOption(new Option("fields", mvFields));
      this.api.getLogger().info("Preparing to execute si viewproject for " + this.fullConfigSyntax);
      Response viewRes = this.api.runCommand(siViewProjectCmd);
      WorkItemIterator wit = viewRes.getWorkItems();

      while(wit.hasNext()) {
         WorkItem wi = wit.next();
         if (wi.getModelType().equals("si.Subproject")) {
            pjConfigHash.put(wi.getField("name").getValueAsString(), wi.getId());
         } else if (wi.getModelType().equals("si.Member")) {
            String parentProject = wi.getField("parent").getValueAsString();
            Member iCMMember = new Member(wi, (String)pjConfigHash.get(parentProject), projectRoot, workspaceDir);
            memberList.add(iCMMember);
         } else {
            this.api.getLogger().warn("View project output contains an invalid model type: " + wi.getModelType());
         }
      }

      Collections.sort(memberList, FILES_ORDER);
      return memberList;
   }

   public Response checkpoint(String message, String tag) throws APIException {
      this.api.getLogger().debug("Checkpointing project " + this.fullConfigSyntax + " with label '" + tag + "'");
      Command siCheckpoint = new Command("si", "checkpoint");
      siCheckpoint.addOption(new Option("recurse"));
      siCheckpoint.addOption(new Option("project", this.fullConfigSyntax));
      siCheckpoint.addOption(new Option("label", tag));
      if (null != message && message.length() > 0) {
         siCheckpoint.addOption(new Option("description", message));
      }

      return this.api.runCommand(siCheckpoint);
   }

   public Response createDevPath(String devPath) throws APIException {
      String chkpt = this.projectRevision;
      if (!this.isBuild()) {
         Response chkptRes = this.checkpoint("Pre-checkpoint for development path " + devPath, devPath + " Baseline");
         WorkItem wi = chkptRes.getWorkItem(this.fullConfigSyntax);
         chkpt = wi.getResult().getField("resultant").getItem().getId();
      }

      this.api.getLogger().debug("Creating development path '" + devPath + "' for project " + this.projectName + " at revision '" + chkpt + "'");
      Command siCreateDevPath = new Command("si", "createdevpath");
      siCreateDevPath.addOption(new Option("devpath", devPath));
      siCreateDevPath.addOption(new Option("project", this.projectName));
      siCreateDevPath.addOption(new Option("projectRevision", chkpt));
      return this.api.runCommand(siCreateDevPath);
   }
}
