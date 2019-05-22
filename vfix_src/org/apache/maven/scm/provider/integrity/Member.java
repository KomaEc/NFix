package org.apache.maven.scm.provider.integrity;

import com.mks.api.Command;
import com.mks.api.FileOption;
import com.mks.api.Option;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import java.io.File;
import java.util.Date;

public class Member {
   private String memberID;
   private String memberName;
   private Date memberTimestamp;
   private String memberDescription;
   private String projectConfigPath;
   private String memberRev;
   private File targetFile;
   private String relativeFile;
   private String lineTerminator;
   private String overwriteExisting;
   private String restoreTimestamp;

   public Member(WorkItem wi, String configPath, String projectRoot, String workspaceDir) {
      this.projectConfigPath = configPath;
      this.memberID = wi.getId();
      this.memberName = wi.getField("name").getValueAsString();
      this.memberRev = wi.getField("memberrev").getItem().getId();
      this.memberTimestamp = wi.getField("membertimestamp").getDateTime();
      if (null != wi.getField("memberdescription") && null != wi.getField("memberdescription").getValueAsString()) {
         this.memberDescription = wi.getField("memberdescription").getValueAsString();
      } else {
         this.memberDescription = new String("");
      }

      this.lineTerminator = "native";
      this.overwriteExisting = "overwriteExisting";
      this.restoreTimestamp = "restoreTimestamp";
      this.relativeFile = this.memberName.substring(projectRoot.length());
      this.targetFile = new File(workspaceDir + this.relativeFile);
   }

   public String getTargetFilePath() {
      return this.targetFile.getAbsolutePath();
   }

   public String getRevision() {
      return this.memberRev;
   }

   public Date getTimestamp() {
      return this.memberTimestamp;
   }

   public String getDescription() {
      return this.memberDescription;
   }

   public String getMemberName() {
      return this.memberName;
   }

   public String getName() {
      if (this.memberID.indexOf(47) > 0) {
         return this.memberID.substring(this.memberID.lastIndexOf(47) + 1);
      } else {
         return this.memberID.indexOf(92) > 0 ? this.memberID.substring(this.memberID.lastIndexOf(92) + 1) : this.memberID;
      }
   }

   public void setLineTerminator(String lineTerminator) {
      this.lineTerminator = lineTerminator;
   }

   public void setOverwriteExisting(String overwriteExisting) {
      this.overwriteExisting = overwriteExisting;
   }

   public void setRestoreTimestamp(boolean restoreTime) {
      if (restoreTime) {
         this.restoreTimestamp = "restoreTimestamp";
      } else {
         this.restoreTimestamp = "norestoreTimestamp";
      }

   }

   public boolean checkout(APISession api) throws APIException {
      if (!this.targetFile.getParentFile().isDirectory()) {
         this.targetFile.getParentFile().mkdirs();
      }

      Command coCMD = new Command("si", "projectco");
      coCMD.addOption(new Option(this.overwriteExisting));
      coCMD.addOption(new Option("nolock"));
      coCMD.addOption(new Option("project", this.projectConfigPath));
      coCMD.addOption(new FileOption("targetFile", this.targetFile));
      coCMD.addOption(new Option(this.restoreTimestamp));
      coCMD.addOption(new Option("lineTerminator", this.lineTerminator));
      coCMD.addOption(new Option("revision", this.memberRev));
      coCMD.addSelection(this.memberID);
      Response res = api.runCommand(coCMD);
      return res.getExitCode() == 0;
   }

   public boolean equals(Object o) {
      return o instanceof Member && null != o ? ((Member)o).getMemberName().equals(this.getMemberName()) : false;
   }

   public int hashCode() {
      return this.getMemberName().hashCode();
   }
}
