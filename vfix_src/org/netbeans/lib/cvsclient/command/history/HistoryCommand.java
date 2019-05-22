package org.netbeans.lib.cvsclient.command.history;

import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.RootRequest;
import org.netbeans.lib.cvsclient.request.UseUnchangedRequest;

public class HistoryCommand extends Command {
   private final List requests = new LinkedList();
   private EventManager eventManager;
   private boolean forAllUsers;
   private String showBackToRecordContaining;
   private boolean reportCommits;
   private String sinceDate;
   private boolean reportEverything;
   private boolean lastEventOfProject;
   private boolean reportCheckouts;
   private String sinceRevision;
   private boolean reportTags;
   private String sinceTag;
   private boolean forWorkingDirectory;
   private String reportEventType;
   private String timeZone;
   private String[] lastEventForFile;
   private String[] reportOnModule;
   private String[] reportLastEventForModule;
   private String[] forUsers;

   public Builder createBuilder(EventManager var1) {
      return null;
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.eventManager = var2;
      this.requests.clear();
      super.execute(var1, var2);

      try {
         if (var1.isFirstCommand()) {
            this.requests.add(new RootRequest(var1.getRepository()));
            this.requests.add(new UseUnchangedRequest());
         }

         this.addBooleanArgument(this.requests, this.isForAllUsers(), "-a");
         this.addBooleanArgument(this.requests, this.isForWorkingDirectory(), "-w");
         this.addBooleanArgument(this.requests, this.isLastEventOfProject(), "-l");
         this.addBooleanArgument(this.requests, this.isReportCheckouts(), "-o");
         this.addBooleanArgument(this.requests, this.isReportCommits(), "-c");
         this.addBooleanArgument(this.requests, this.isReportEverything(), "-e");
         this.addBooleanArgument(this.requests, this.isReportTags(), "-T");
         this.addStringArgument(this.requests, this.getReportEventType(), "-x");
         this.addStringArgument(this.requests, this.getShowBackToRecordContaining(), "-b");
         this.addStringArgument(this.requests, this.getSinceDate(), "-D");
         this.addStringArgument(this.requests, this.getSinceRevision(), "-r");
         this.addStringArgument(this.requests, this.getSinceTag(), "-t");
         this.addStringArrayArgument(this.requests, this.getForUsers(), "-u");
         this.addStringArrayArgument(this.requests, this.getReportLastEventForModule(), "-n");
         this.addStringArrayArgument(this.requests, this.getReportOnModule(), "-m");
         this.addStringArrayArgument(this.requests, this.getLastEventForFile(), "-f");
         if (!this.isReportCheckouts() && !this.isReportCommits() && !this.isReportTags() && !this.isReportEverything() && this.getReportEventType() == null && this.getReportOnModule() == null) {
            this.addBooleanArgument(this.requests, true, "-c");
         }

         if (this.getTimeZone() != null) {
            this.addStringArgument(this.requests, this.getTimeZone(), "-z");
         } else {
            this.addStringArgument(this.requests, "+0000", "-z");
         }

         this.requests.add(CommandRequest.HISTORY);
         var1.processRequests(this.requests);
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   private void addStringArgument(List var1, String var2, String var3) {
      if (var2 != null) {
         var1.add(new ArgumentRequest(var3));
         var1.add(new ArgumentRequest(var2));
      }

   }

   private void addStringArrayArgument(List var1, String[] var2, String var3) {
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            var1.add(new ArgumentRequest(var3));
            var1.add(new ArgumentRequest(var2[var4]));
         }
      }

   }

   private void addBooleanArgument(List var1, boolean var2, String var3) {
      if (var2) {
         var1.add(new ArgumentRequest(var3));
      }

   }

   public void commandTerminated(TerminationEvent var1) {
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("history ");
      var1.append(this.getCVSArguments());
      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'a') {
         this.setForAllUsers(true);
      } else if (var1 == 'b') {
         this.setShowBackToRecordContaining(var2);
      } else if (var1 == 'c') {
         this.setReportCommits(true);
      } else if (var1 == 'D') {
         this.setSinceDate(var2);
      } else if (var1 == 'e') {
         this.setReportEverything(true);
      } else if (var1 == 'l') {
         this.setLastEventOfProject(true);
      } else if (var1 == 'o') {
         this.setReportCheckouts(true);
      } else if (var1 == 'r') {
         this.setSinceRevision(var2);
      } else if (var1 == 'T') {
         this.setReportTags(true);
      } else if (var1 == 't') {
         this.setSinceTag(var2);
      } else if (var1 == 'w') {
         this.setForWorkingDirectory(true);
      } else if (var1 == 'x') {
         this.setReportEventType(var2);
      } else if (var1 == 'z') {
         this.setTimeZone(var2);
      } else if (var1 == 'f') {
         this.addLastEventForFile(var2);
      } else if (var1 == 'm') {
         this.addReportOnModule(var2);
      } else if (var1 == 'n') {
         this.addReportLastEventForModule(var2);
      } else {
         if (var1 != 'u') {
            return false;
         }

         this.addForUsers(var2);
      }

      return true;
   }

   public String getOptString() {
      return "ab:cD:ef:lm:n:or:Tt:u:wx:z:";
   }

   public void resetCVSCommand() {
      this.setForAllUsers(false);
      this.setForUsers((String[])null);
      this.setForWorkingDirectory(false);
      this.setLastEventForFile((String[])null);
      this.setLastEventOfProject(false);
      this.setReportCheckouts(false);
      this.setReportCommits(false);
      this.setReportEventType((String)null);
      this.setReportEverything(false);
      this.setReportLastEventForModule((String[])null);
      this.setReportOnModule((String[])null);
      this.setReportTags(false);
      this.setShowBackToRecordContaining((String)null);
      this.setSinceDate((String)null);
      this.setSinceRevision((String)null);
      this.setSinceTag((String)null);
      this.setTimeZone((String)null);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.isForAllUsers()) {
         var1.append("-a ");
      }

      if (this.isForWorkingDirectory()) {
         var1.append("-w ");
      }

      if (this.isLastEventOfProject()) {
         var1.append("-l ");
      }

      if (this.isReportCheckouts()) {
         var1.append("-o ");
      }

      if (this.isReportCommits()) {
         var1.append("-c ");
      }

      if (this.isReportEverything()) {
         var1.append("-e ");
      }

      if (this.isReportTags()) {
         var1.append("-T ");
      }

      if (this.getForUsers() != null) {
         this.appendArrayToSwitches(var1, this.getForUsers(), "-u ");
      }

      if (this.getLastEventForFile() != null) {
         this.appendArrayToSwitches(var1, this.getLastEventForFile(), "-f ");
      }

      if (this.getReportEventType() != null) {
         var1.append("-x ");
         var1.append(this.getReportEventType());
         var1.append(" ");
      }

      if (this.getReportLastEventForModule() != null) {
         this.appendArrayToSwitches(var1, this.getReportLastEventForModule(), "-n ");
      }

      if (this.getReportOnModule() != null) {
         this.appendArrayToSwitches(var1, this.getReportOnModule(), "-m ");
      }

      if (this.getShowBackToRecordContaining() != null) {
         var1.append("-b ");
         var1.append(this.getShowBackToRecordContaining());
         var1.append(" ");
      }

      if (this.getSinceDate() != null) {
         var1.append("-D ");
         var1.append(this.getSinceDate());
         var1.append(" ");
      }

      if (this.getSinceRevision() != null) {
         var1.append("-r ");
         var1.append(this.getSinceRevision());
         var1.append(" ");
      }

      if (this.getSinceTag() != null) {
         var1.append("-t ");
         var1.append(this.getSinceTag());
         var1.append(" ");
      }

      if (this.getTimeZone() != null) {
         var1.append("-z ");
         var1.append(this.getTimeZone());
         var1.append(" ");
      }

      return var1.toString();
   }

   private void appendArrayToSwitches(StringBuffer var1, String[] var2, String var3) {
      if (var2 != null) {
         for(int var4 = 0; var4 < var2.length; ++var4) {
            var1.append(var3);
            var1.append(var2[var4]);
            var1.append(" ");
         }

      }
   }

   public boolean isForAllUsers() {
      return this.forAllUsers;
   }

   public void setForAllUsers(boolean var1) {
      this.forAllUsers = var1;
   }

   public String getShowBackToRecordContaining() {
      return this.showBackToRecordContaining;
   }

   public void setShowBackToRecordContaining(String var1) {
      this.showBackToRecordContaining = var1;
   }

   public boolean isReportCommits() {
      return this.reportCommits;
   }

   public void setReportCommits(boolean var1) {
      this.reportCommits = var1;
   }

   public String getSinceDate() {
      return this.sinceDate;
   }

   public void setSinceDate(String var1) {
      this.sinceDate = var1;
   }

   public boolean isReportEverything() {
      return this.reportEverything;
   }

   public void setReportEverything(boolean var1) {
      this.reportEverything = var1;
   }

   public boolean isLastEventOfProject() {
      return this.lastEventOfProject;
   }

   public void setLastEventOfProject(boolean var1) {
      this.lastEventOfProject = var1;
   }

   public boolean isReportCheckouts() {
      return this.reportCheckouts;
   }

   public void setReportCheckouts(boolean var1) {
      this.reportCheckouts = var1;
   }

   public String getSinceRevision() {
      return this.sinceRevision;
   }

   public void setSinceRevision(String var1) {
      this.sinceRevision = var1;
   }

   public boolean isReportTags() {
      return this.reportTags;
   }

   public void setReportTags(boolean var1) {
      this.reportTags = var1;
   }

   public String getSinceTag() {
      return this.sinceTag;
   }

   public void setSinceTag(String var1) {
      this.sinceTag = var1;
   }

   public boolean isForWorkingDirectory() {
      return this.forWorkingDirectory;
   }

   public void setForWorkingDirectory(boolean var1) {
      this.forWorkingDirectory = var1;
   }

   public String getReportEventType() {
      return this.reportEventType;
   }

   public void setReportEventType(String var1) {
      this.reportEventType = var1;
   }

   public String getTimeZone() {
      return this.timeZone;
   }

   public void setTimeZone(String var1) {
      this.timeZone = var1;
   }

   public String[] getLastEventForFile() {
      return this.lastEventForFile;
   }

   public void setLastEventForFile(String[] var1) {
      this.lastEventForFile = var1;
   }

   public void addLastEventForFile(String var1) {
      this.lastEventForFile = this.addNewValue(this.lastEventForFile, var1);
   }

   public String[] getReportOnModule() {
      return this.reportOnModule;
   }

   public void setReportOnModule(String[] var1) {
      this.reportOnModule = var1;
   }

   public void addReportOnModule(String var1) {
      this.reportOnModule = this.addNewValue(this.reportOnModule, var1);
   }

   public String[] getReportLastEventForModule() {
      return this.reportLastEventForModule;
   }

   public void setReportLastEventForModule(String[] var1) {
      this.reportLastEventForModule = var1;
   }

   public void addReportLastEventForModule(String var1) {
      this.reportLastEventForModule = this.addNewValue(this.reportLastEventForModule, var1);
   }

   public String[] getForUsers() {
      return this.forUsers;
   }

   public void setForUsers(String[] var1) {
      this.forUsers = var1;
   }

   public void addForUsers(String var1) {
      this.forUsers = this.addNewValue(this.forUsers, var1);
   }

   private String[] addNewValue(String[] var1, String var2) {
      if (var1 == null) {
         var1 = new String[]{var2};
         return var1;
      } else {
         String[] var3 = new String[var1.length + 1];

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var3[var4] = var1[var4];
         }

         var3[var3.length] = var2;
         return var3;
      }
   }
}
