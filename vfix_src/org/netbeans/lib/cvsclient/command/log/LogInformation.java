package org.netbeans.lib.cvsclient.command.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;
import org.netbeans.lib.cvsclient.util.BugLog;

public class LogInformation extends FileInfoContainer {
   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
   private File file;
   private String repositoryFilename;
   private String headRevision;
   private String branch;
   private String accessList;
   private String keywordSubstitution;
   private String totalRevisions;
   private String selectedRevisions;
   private String description;
   private String locks;
   private final List revisions = new ArrayList();
   private final List symbolicNames = new ArrayList();
   private StringBuffer symNamesBuffer;

   public File getFile() {
      return this.file;
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public String getRepositoryFilename() {
      return this.repositoryFilename;
   }

   public void setRepositoryFilename(String var1) {
      this.repositoryFilename = var1;
   }

   public String getHeadRevision() {
      return this.headRevision;
   }

   public void setHeadRevision(String var1) {
      this.headRevision = var1;
   }

   public String getBranch() {
      return this.branch;
   }

   public void setBranch(String var1) {
      this.branch = var1;
   }

   public String getAccessList() {
      return this.accessList;
   }

   public void setAccessList(String var1) {
      this.accessList = var1;
   }

   public String getKeywordSubstitution() {
      return this.keywordSubstitution;
   }

   public void setKeywordSubstitution(String var1) {
      this.keywordSubstitution = var1;
   }

   public String getTotalRevisions() {
      return this.totalRevisions;
   }

   public void setTotalRevisions(String var1) {
      this.totalRevisions = var1;
   }

   public String getSelectedRevisions() {
      return this.selectedRevisions;
   }

   public void setSelectedRevisions(String var1) {
      this.selectedRevisions = var1;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public String getLocks() {
      return this.locks;
   }

   public void setLocks(String var1) {
      this.locks = var1;
   }

   public void addRevision(LogInformation.Revision var1) {
      this.revisions.add(var1);
   }

   public List getRevisionList() {
      return this.revisions;
   }

   public LogInformation.Revision getRevision(String var1) {
      Iterator var2 = this.revisions.iterator();

      LogInformation.Revision var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LogInformation.Revision)var2.next();
      } while(!var3.getNumber().equals(var1));

      return var3;
   }

   public void addSymbolicName(String var1, String var2) {
      LogInformation.SymName var3 = new LogInformation.SymName();
      var3.setName(var1);
      var3.setRevision(var2);
      this.symbolicNames.add(var3);
   }

   public List getAllSymbolicNames() {
      return this.symbolicNames;
   }

   public List getSymNamesForRevision(String var1) {
      Iterator var2 = this.symbolicNames.iterator();
      LinkedList var3 = new LinkedList();

      while(var2.hasNext()) {
         LogInformation.SymName var4 = (LogInformation.SymName)var2.next();
         if (var4.getRevision().equals(var1)) {
            var3.add(var4);
         }
      }

      return var3;
   }

   public LogInformation.SymName getSymName(String var1) {
      Iterator var2 = this.symbolicNames.iterator();

      LogInformation.SymName var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (LogInformation.SymName)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public LogInformation.Revision createNewRevision(String var1) {
      LogInformation.Revision var2 = new LogInformation.Revision();
      var2.setNumber(var1);
      return var2;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(30);
      var1.append("\nFile: " + (this.file != null ? this.file.getAbsolutePath() : "null"));
      var1.append("\nRepositoryFile: " + this.repositoryFilename);
      var1.append("\nHead revision: " + this.headRevision);
      return var1.toString();
   }

   public class Revision {
      private String number;
      private Date date;
      private String dateString;
      private String author;
      private String state;
      private String lines;
      private String commitID;
      private String message;
      private String branches;

      public LogInformation getLogInfoHeader() {
         return LogInformation.this;
      }

      public String getNumber() {
         return this.number;
      }

      public void setNumber(String var1) {
         this.number = var1;
      }

      public Date getDate() {
         return this.date;
      }

      public String getDateString() {
         return this.dateString;
      }

      public void setDateString(String var1) {
         this.dateString = var1;
         if (var1 == null) {
            this.date = null;
         } else {
            try {
               var1 = var1.replace('/', '-') + " +0000";
               this.date = LogInformation.DATE_FORMAT.parse(var1);
            } catch (Exception var3) {
               BugLog.getInstance().bug("Couldn't parse date " + var1);
            }

         }
      }

      public String getAuthor() {
         return this.author;
      }

      public void setAuthor(String var1) {
         this.author = var1;
      }

      public String getState() {
         return this.state;
      }

      public void setState(String var1) {
         this.state = var1;
      }

      public String getLines() {
         return this.lines;
      }

      public void setLines(String var1) {
         this.lines = var1;
      }

      public String getCommitID() {
         return this.commitID;
      }

      public void setCommitID(String var1) {
         this.commitID = var1;
      }

      public int getAddedLines() {
         if (this.lines != null) {
            int var1 = this.lines.indexOf(43);
            int var2 = this.lines.indexOf(32);
            if (var1 >= 0 && var2 > var1) {
               String var3 = this.lines.substring(var1 + 1, var2);

               try {
                  int var4 = Integer.parseInt(var3);
                  return var4;
               } catch (NumberFormatException var5) {
               }
            }
         }

         return 0;
      }

      public int getRemovedLines() {
         if (this.lines != null) {
            int var1 = this.lines.indexOf(45);
            if (var1 >= 0) {
               String var2 = this.lines.substring(var1 + 1);

               try {
                  int var3 = Integer.parseInt(var2);
                  return var3;
               } catch (NumberFormatException var4) {
               }
            }
         }

         return 0;
      }

      public String getMessage() {
         return this.message;
      }

      public void setMessage(String var1) {
         this.message = var1;
      }

      public String getBranches() {
         return this.branches;
      }

      public void setBranches(String var1) {
         this.branches = var1;
      }
   }

   public class SymName {
      private String name;
      private String revision;

      public String getName() {
         return this.name;
      }

      public void setName(String var1) {
         this.name = var1;
      }

      public void setRevision(String var1) {
         this.revision = var1;
      }

      public String getRevision() {
         return this.revision;
      }

      public final boolean isBranch() {
         boolean var1 = false;
         String[] var2 = this.revision.split("\\.");
         if (var2.length > 2 && var2.length % 2 == 0) {
            String var3 = var2[var2.length - 2];
            var1 = "0".equals(var3);
         }

         return var1;
      }
   }
}
