package org.netbeans.lib.cvsclient.command.log;

import java.io.File;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;

public class LogCommand extends BasicCommand {
   protected EventManager eventManager;
   private boolean defaultBranch;
   private String dateFilter;
   private boolean headerOnly;
   private boolean noTags;
   private String revisionFilter;
   private String stateFilter;
   private String userFilter;
   private boolean headerAndDescOnly;

   public LogCommand() {
      this.resetCVSCommand();
   }

   public Builder createBuilder(EventManager var1) {
      return new LogBuilder(var1, this);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.eventManager = var2;
      super.execute(var1, var2);

      try {
         if (this.defaultBranch) {
            this.requests.add(1, new ArgumentRequest("-b"));
         }

         if (this.headerAndDescOnly) {
            this.requests.add(1, new ArgumentRequest("-t"));
         }

         if (this.headerOnly) {
            this.requests.add(1, new ArgumentRequest("-h"));
         }

         if (this.noTags) {
            this.requests.add(1, new ArgumentRequest("-N"));
         }

         if (this.userFilter != null) {
            this.requests.add(1, new ArgumentRequest("-w" + this.userFilter));
         }

         if (this.revisionFilter != null) {
            this.requests.add(1, new ArgumentRequest("-r" + this.revisionFilter));
         }

         if (this.stateFilter != null) {
            this.requests.add(1, new ArgumentRequest("-s" + this.stateFilter));
         }

         if (this.dateFilter != null) {
            this.requests.add(1, new ArgumentRequest("-d" + this.dateFilter));
         }

         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.LOG);
         var1.processRequests(this.requests);
      } catch (CommandException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new CommandException(var10, var10.getLocalizedMessage());
      } finally {
         this.requests.clear();
         if (!this.isBuilderSet()) {
            this.builder = null;
         }

      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public boolean isDefaultBranch() {
      return this.defaultBranch;
   }

   public void setDefaultBranch(boolean var1) {
      this.defaultBranch = var1;
   }

   public String getDateFilter() {
      return this.dateFilter;
   }

   public void setDateFilter(String var1) {
      this.dateFilter = var1;
   }

   public boolean isHeaderOnly() {
      return this.headerOnly;
   }

   public void setHeaderOnly(boolean var1) {
      this.headerOnly = var1;
   }

   public boolean isNoTags() {
      return this.noTags;
   }

   public void setNoTags(boolean var1) {
      this.noTags = var1;
   }

   public String getRevisionFilter() {
      return this.revisionFilter;
   }

   public void setRevisionFilter(String var1) {
      this.revisionFilter = var1;
   }

   public String getStateFilter() {
      return this.stateFilter;
   }

   public void setStateFilter(String var1) {
      this.stateFilter = var1;
   }

   public String getUserFilter() {
      return this.userFilter;
   }

   public void setUserFilter(String var1) {
      this.userFilter = var1;
   }

   public boolean isHeaderAndDescOnly() {
      return this.headerAndDescOnly;
   }

   public void setHeaderAndDescOnly(boolean var1) {
      this.headerAndDescOnly = var1;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("log ");
      var1.append(this.getCVSArguments());
      File[] var2 = this.getFiles();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(var2[var3].getName());
            var1.append(' ');
         }
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'b') {
         this.setDefaultBranch(true);
      } else if (var1 == 'h') {
         this.setHeaderOnly(true);
      } else if (var1 == 't') {
         this.setHeaderAndDescOnly(true);
      } else if (var1 == 'N') {
         this.setNoTags(true);
      } else if (var1 == 'd') {
         this.setDateFilter(var2);
      } else if (var1 == 'r') {
         this.setRevisionFilter(var2 == null ? "" : var2);
      } else if (var1 == 's') {
         this.setStateFilter(var2);
      } else {
         if (var1 != 'w') {
            return false;
         }

         this.setUserFilter(var2 == null ? "" : var2);
      }

      return true;
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setDefaultBranch(false);
      this.setHeaderOnly(false);
      this.setHeaderAndDescOnly(false);
      this.setNoTags(false);
      this.setDateFilter((String)null);
      this.setRevisionFilter((String)null);
      this.setStateFilter((String)null);
      this.setUserFilter((String)null);
   }

   public String getOptString() {
      return "RlbhtNd:r:s:w:";
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.isDefaultBranch()) {
         var1.append("-b ");
      }

      if (this.isHeaderAndDescOnly()) {
         var1.append("-t ");
      }

      if (this.isHeaderOnly()) {
         var1.append("-h ");
      }

      if (this.isNoTags()) {
         var1.append("-N ");
      }

      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.userFilter != null) {
         var1.append("-w");
         var1.append(this.userFilter);
         var1.append(' ');
      }

      if (this.revisionFilter != null) {
         var1.append("-r");
         var1.append(this.revisionFilter);
         var1.append(' ');
      }

      if (this.stateFilter != null) {
         var1.append("-s");
         var1.append(this.stateFilter);
         var1.append(' ');
      }

      if (this.dateFilter != null) {
         var1.append("-d");
         var1.append(this.dateFilter);
         var1.append(' ');
      }

      return var1.toString();
   }
}
