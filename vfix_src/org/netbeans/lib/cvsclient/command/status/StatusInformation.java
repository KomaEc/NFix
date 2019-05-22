package org.netbeans.lib.cvsclient.command.status;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;
import org.netbeans.lib.cvsclient.file.FileStatus;

public class StatusInformation extends FileInfoContainer {
   private File file;
   private FileStatus status;
   private String workingRevision;
   private String repositoryRevision;
   private String repositoryFileName;
   private String stickyDate;
   private String stickyOptions;
   private String stickyTag;
   private List tags;
   private StringBuffer symNamesBuffer;

   public StatusInformation() {
      this.setAllExistingTags((List)null);
   }

   public File getFile() {
      return this.file;
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public FileStatus getStatus() {
      return this.status;
   }

   public void setStatus(FileStatus var1) {
      this.status = var1;
   }

   public String getStatusString() {
      return this.status == null ? null : this.status.toString();
   }

   public void setStatusString(String var1) {
      this.setStatus(FileStatus.getStatusForString(var1));
   }

   public String getWorkingRevision() {
      return this.workingRevision;
   }

   public void setWorkingRevision(String var1) {
      this.workingRevision = var1;
   }

   public String getRepositoryRevision() {
      return this.repositoryRevision;
   }

   public void setRepositoryRevision(String var1) {
      this.repositoryRevision = var1;
   }

   public String getRepositoryFileName() {
      return this.repositoryFileName;
   }

   public void setRepositoryFileName(String var1) {
      this.repositoryFileName = var1;
   }

   public String getStickyTag() {
      return this.stickyTag;
   }

   public void setStickyTag(String var1) {
      this.stickyTag = var1;
   }

   public String getStickyDate() {
      return this.stickyDate;
   }

   public void setStickyDate(String var1) {
      this.stickyDate = var1;
   }

   public String getStickyOptions() {
      return this.stickyOptions;
   }

   public void setStickyOptions(String var1) {
      this.stickyOptions = var1;
   }

   public void addExistingTag(String var1, String var2) {
      if (this.symNamesBuffer == null) {
         this.symNamesBuffer = new StringBuffer();
      }

      this.symNamesBuffer.append(var1);
      this.symNamesBuffer.append(" ");
      this.symNamesBuffer.append(var2);
      this.symNamesBuffer.append("\n");
   }

   private void createSymNames() {
      this.tags = new LinkedList();
      if (this.symNamesBuffer != null) {
         int var1 = 0;
         int var2 = 0;

         while(var1 < this.symNamesBuffer.length()) {
            while(var1 < this.symNamesBuffer.length() && this.symNamesBuffer.charAt(var1) != '\n') {
               ++var1;
            }

            if (var1 > var2) {
               String var3 = this.symNamesBuffer.substring(var2, var1);
               String var4 = var3.substring(0, var3.indexOf(32));
               String var5 = var3.substring(var3.indexOf(32) + 1);
               StatusInformation.SymName var6 = new StatusInformation.SymName();
               var6.setTag(var4);
               var6.setRevision(var5);
               this.tags.add(var6);
               var2 = var1 + 1;
               ++var1;
            }
         }

         this.symNamesBuffer = null;
      }
   }

   public List getAllExistingTags() {
      if (this.tags == null) {
         this.createSymNames();
      }

      return this.tags;
   }

   public void setAllExistingTags(List var1) {
      this.tags = var1;
   }

   public List getSymNamesForRevision(String var1) {
      if (this.tags == null) {
         this.createSymNames();
      }

      LinkedList var2 = new LinkedList();
      Iterator var3 = this.tags.iterator();

      while(var3.hasNext()) {
         StatusInformation.SymName var4 = (StatusInformation.SymName)var3.next();
         if (var4.getRevision().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public StatusInformation.SymName getSymNameForTag(String var1) {
      if (this.tags == null) {
         this.createSymNames();
      }

      Iterator var2 = this.tags.iterator();

      StatusInformation.SymName var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (StatusInformation.SymName)var2.next();
      } while(!var3.getTag().equals(var1));

      return var3;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("\nFile: ");
      var1.append(this.file != null ? this.file.getAbsolutePath() : "null");
      var1.append("\nStatus is: ");
      var1.append(this.getStatusString());
      var1.append("\nWorking revision: ");
      var1.append(this.workingRevision);
      var1.append("\nRepository revision: ");
      var1.append("\nSticky date: ");
      var1.append(this.stickyDate);
      var1.append("\nSticky options: ");
      var1.append(this.stickyOptions);
      var1.append("\nSticky tag: ");
      var1.append(this.stickyTag);
      if (this.tags != null && this.tags.size() > 0) {
         var1.append("\nExisting Tags:");
         Iterator var2 = this.tags.iterator();

         while(var2.hasNext()) {
            var1.append("\n  ");
            var1.append(var2.next().toString());
         }
      }

      return var1.toString();
   }

   public static class SymName {
      private String tag;
      private String revision;

      public String getTag() {
         return this.tag;
      }

      public void setTag(String var1) {
         this.tag = var1;
      }

      public void setRevision(String var1) {
         this.revision = var1;
      }

      public String getRevision() {
         return this.revision;
      }

      public String toString() {
         return this.getTag() + " : " + this.getRevision();
      }
   }
}
