package org.netbeans.lib.cvsclient.command.log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandUtils;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class LogBuilder implements Builder {
   private static final String LOGGING_DIR = ": Logging ";
   private static final String RCS_FILE = "RCS file: ";
   private static final String WORK_FILE = "Working file: ";
   private static final String REV_HEAD = "head: ";
   private static final String BRANCH = "branch: ";
   private static final String LOCKS = "locks: ";
   private static final String ACCESS_LIST = "access list: ";
   private static final String SYM_NAME = "symbolic names:";
   private static final String KEYWORD_SUBST = "keyword substitution: ";
   private static final String TOTAL_REV = "total revisions: ";
   private static final String SEL_REV = ";\tselected revisions: ";
   private static final String DESCRIPTION = "description:";
   private static final String REVISION = "revision ";
   private static final String DATE = "date: ";
   private static final String BRANCHES = "branches: ";
   private static final String AUTHOR = "author: ";
   private static final String STATE = "state: ";
   private static final String LINES = "lines: ";
   private static final String COMMITID = "commitid: ";
   private static final String SPLITTER = "----------------------------";
   private static final String FINAL_SPLIT = "=============================================================================";
   private static final String ERROR = ": nothing known about ";
   private static final String NO_FILE = "no file";
   protected EventManager eventManager;
   protected BasicCommand logCommand;
   protected LogInformation logInfo;
   protected LogInformation.Revision revision;
   protected String fileDirectory;
   private boolean addingSymNames;
   private boolean addingDescription;
   private boolean addingLogMessage;
   private StringBuffer tempBuffer = null;
   private List messageList;

   public LogBuilder(EventManager var1, BasicCommand var2) {
      this.logCommand = var2;
      this.eventManager = var1;
      this.addingSymNames = false;
      this.addingDescription = false;
      this.addingLogMessage = false;
      this.logInfo = null;
      this.revision = null;
      this.messageList = new ArrayList(500);
   }

   public void outputDone() {
      if (this.logInfo != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.logInfo));
         this.logInfo = null;
         this.messageList = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      if (var1.equals("=============================================================================")) {
         if (this.addingDescription) {
            this.addingDescription = false;
            this.logInfo.setDescription(this.tempBuffer.toString());
         }

         if (this.addingLogMessage) {
            this.addingLogMessage = false;
            this.revision.setMessage(CommandUtils.findUniqueString(this.tempBuffer.toString(), this.messageList));
         }

         if (this.revision != null) {
            this.logInfo.addRevision(this.revision);
            this.revision = null;
         }

         if (this.logInfo != null) {
            this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.logInfo));
            this.logInfo = null;
            this.tempBuffer = null;
         }

      } else {
         if (this.addingLogMessage) {
            if (!var1.startsWith("branches: ")) {
               this.processLogMessage(var1);
               return;
            }

            this.processBranches(var1.substring("branches: ".length()));
         }

         if (this.addingSymNames) {
            this.processSymbolicNames(var1);
         }

         if (this.addingDescription) {
            this.processDescription(var1);
         }

         if (var1.startsWith("revision ")) {
            this.processRevisionStart(var1);
         }

         if (var1.startsWith("date: ")) {
            this.processRevisionDate(var1);
         }

         if (var1.startsWith("keyword substitution: ")) {
            this.logInfo.setKeywordSubstitution(var1.substring("keyword substitution: ".length()).trim().intern());
            this.addingSymNames = false;
         } else {
            if (var1.startsWith("description:")) {
               this.tempBuffer = new StringBuffer(var1.substring("description:".length()));
               this.addingDescription = true;
            }

            if (var1.indexOf(": Logging ") >= 0) {
               this.fileDirectory = var1.substring(var1.indexOf(": Logging ") + ": Logging ".length()).trim();
            } else if (var1.startsWith("RCS file: ")) {
               this.processRcsFile(var1.substring("RCS file: ".length()));
            } else if (var1.startsWith("Working file: ")) {
               this.processWorkingFile(var1.substring("Working file: ".length()));
            } else if (var1.startsWith("head: ")) {
               this.logInfo.setHeadRevision(var1.substring("head: ".length()).trim().intern());
            } else {
               if (var1.startsWith("branch: ")) {
                  this.logInfo.setBranch(var1.substring("branch: ".length()).trim().intern());
               }

               if (var1.startsWith("locks: ")) {
                  this.logInfo.setLocks(var1.substring("locks: ".length()).trim().intern());
               }

               if (var1.startsWith("access list: ")) {
                  this.logInfo.setAccessList(var1.substring("access list: ".length()).trim().intern());
               }

               if (var1.startsWith("symbolic names:")) {
                  this.addingSymNames = true;
               }

               if (var1.startsWith("total revisions: ")) {
                  int var3 = var1.indexOf(59);
                  if (var3 < 0) {
                     this.logInfo.setTotalRevisions(var1.substring("total revisions: ".length()).trim().intern());
                     this.logInfo.setSelectedRevisions("0");
                  } else {
                     String var4 = var1.substring(0, var3);
                     String var5 = var1.substring(var3, var1.length());
                     this.logInfo.setTotalRevisions(var4.substring("total revisions: ".length()).trim().intern());
                     this.logInfo.setSelectedRevisions(var5.substring(";\tselected revisions: ".length()).trim().intern());
                  }
               }

            }
         }
      }
   }

   private String findUniqueString(String var1, List var2) {
      if (var1 == null) {
         return null;
      } else {
         int var3 = var2.indexOf(var1);
         if (var3 >= 0) {
            return (String)var2.get(var3);
         } else {
            var2.add(var1);
            return var1;
         }
      }
   }

   private void processRcsFile(String var1) {
      if (this.logInfo != null) {
      }

      this.logInfo = new LogInformation();
      this.logInfo.setRepositoryFilename(var1.trim());
   }

   private void processWorkingFile(String var1) {
      String var2 = var1.trim();
      if (var2.startsWith("no file")) {
         var2 = var2.substring(8);
      }

      this.logInfo.setFile(this.createFile(var1));
   }

   private void processBranches(String var1) {
      int var2 = var1.lastIndexOf(59);
      if (var2 > 0) {
         var1 = var1.substring(0, var2);
      }

      this.revision.setBranches(var1.trim());
   }

   private void processLogMessage(String var1) {
      if (var1.startsWith("----------------------------")) {
         this.addingLogMessage = false;
         this.revision.setMessage(this.findUniqueString(this.tempBuffer.toString(), this.messageList));
      } else {
         this.tempBuffer.append(var1 + "\n");
      }
   }

   private void processSymbolicNames(String var1) {
      if (!var1.startsWith("keyword substitution: ")) {
         var1 = var1.trim();
         int var2 = var1.indexOf(58);
         if (var2 > 0) {
            String var3 = var1.substring(0, var2).trim();
            String var4 = var1.substring(var2 + 1, var1.length()).trim();
            this.logInfo.addSymbolicName(var3.intern(), var4.intern());
         }
      }

   }

   private void processDescription(String var1) {
      if (var1.startsWith("----------------------------")) {
         this.addingDescription = false;
         this.logInfo.setDescription(this.tempBuffer.toString());
      } else {
         this.tempBuffer.append(var1);
      }
   }

   private void processRevisionStart(String var1) {
      if (this.revision != null) {
         this.logInfo.addRevision(this.revision);
      }

      this.revision = this.logInfo.createNewRevision(var1.substring("revision ".length()).intern());
   }

   private void processRevisionDate(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, ";", false);

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken().trim();
         if (var3.startsWith("date: ")) {
            this.revision.setDateString(var3.substring("date: ".length()));
         } else if (var3.startsWith("author: ")) {
            this.revision.setAuthor(var3.substring("author: ".length()));
         } else if (var3.startsWith("state: ")) {
            this.revision.setState(var3.substring("state: ".length()));
         } else if (var3.startsWith("lines: ")) {
            this.revision.setLines(var3.substring("lines: ".length()));
         } else if (var3.startsWith("commitid: ")) {
            this.revision.setCommitID(var3.substring("commitid: ".length()));
         }
      }

      this.addingLogMessage = true;
      this.tempBuffer = new StringBuffer();
   }

   protected File createFile(String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.logCommand.getLocalDirectory());
      var2.append(File.separator);
      var2.append(var1.replace('/', File.separatorChar));
      return new File(var2.toString());
   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}
