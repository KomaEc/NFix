package org.netbeans.lib.cvsclient.command.log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;

public class RlogCommand extends BasicCommand {
   private final List modules = new LinkedList();
   private boolean defaultBranch;
   private String dateFilter;
   private boolean headerOnly;
   private boolean suppressHeader;
   private boolean noTags;
   private String revisionFilter;
   private String stateFilter;
   private String userFilter;
   private boolean headerAndDescOnly;

   public RlogCommand() {
      this.resetCVSCommand();
   }

   public void setModule(String var1) {
      this.modules.add(var1);
   }

   public void clearModules() {
      this.modules.clear();
   }

   public void setModules(String[] var1) {
      this.clearModules();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2];
            this.modules.add(var3);
         }

      }
   }

   public String[] getModules() {
      String[] var1 = new String[this.modules.size()];
      var1 = (String[])this.modules.toArray(var1);
      return var1;
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

   public boolean isSuppressHeader() {
      return this.suppressHeader;
   }

   public void setSuppressHeader(boolean var1) {
      this.suppressHeader = var1;
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

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      super.execute(var1, var2);
      if (!this.isRecursive()) {
         this.requests.add(new ArgumentRequest("-l"));
      }

      if (this.defaultBranch) {
         this.requests.add(new ArgumentRequest("-b"));
      }

      if (this.headerAndDescOnly) {
         this.requests.add(new ArgumentRequest("-t"));
      }

      if (this.headerOnly) {
         this.requests.add(new ArgumentRequest("-h"));
      }

      if (this.suppressHeader) {
         this.requests.add(new ArgumentRequest("-S"));
      }

      if (this.noTags) {
         this.requests.add(new ArgumentRequest("-N"));
      }

      if (this.userFilter != null) {
         this.requests.add(new ArgumentRequest("-w" + this.userFilter));
      }

      if (this.revisionFilter != null) {
         this.requests.add(new ArgumentRequest("-r" + this.revisionFilter));
      }

      if (this.stateFilter != null) {
         this.requests.add(new ArgumentRequest("-s" + this.stateFilter));
      }

      if (this.dateFilter != null) {
         this.requests.add(new ArgumentRequest("-d" + this.dateFilter));
      }

      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.requests.add(new ArgumentRequest(var4));
      }

      this.requests.add(CommandRequest.RLOG);

      try {
         var1.processRequests(this.requests);
         this.requests.clear();
      } catch (CommandException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new CommandException(var6, var6.getLocalizedMessage());
      }
   }

   protected boolean assumeLocalPathWhenUnspecified() {
      return false;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("rlog ");
      var1.append(this.getCVSArguments());
      if (this.modules != null && this.modules.size() > 0) {
         Iterator var4 = this.modules.iterator();

         while(var4.hasNext()) {
            String var3 = (String)var4.next();
            var1.append(var3);
            var1.append(' ');
         }
      } else {
         String var2 = CommandException.getLocalMessage("ExportCommand.moduleEmpty.text");
         var1.append(" ");
         var1.append(var2);
      }

      return var1.toString();
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

      if (this.isSuppressHeader()) {
         var1.append("-S ");
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
      } else if (var1 == 'S') {
         this.setSuppressHeader(true);
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
      this.setSuppressHeader(false);
      this.setNoTags(false);
      this.setDateFilter((String)null);
      this.setRevisionFilter((String)null);
      this.setStateFilter((String)null);
      this.setUserFilter((String)null);
   }

   public String getOptString() {
      return "RlbhStNd:r:s:w:";
   }

   public Builder createBuilder(EventManager var1) {
      return new LogBuilder(var1, this);
   }
}
