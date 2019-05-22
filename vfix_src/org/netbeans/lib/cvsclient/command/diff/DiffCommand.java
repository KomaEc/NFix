package org.netbeans.lib.cvsclient.command.diff;

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

public class DiffCommand extends BasicCommand {
   protected EventManager eventManager;
   private String beforeDate1;
   private String revision1;
   private String revision2;
   private String beforeDate2;
   private String keywordSubst;
   private boolean ignoreAllWhitespace;
   private boolean ignoreBlankLines;
   private boolean ignoreCase;
   private boolean ignoreSpaceChange;
   private boolean contextDiff;
   private boolean unifiedDiff;

   public Builder createBuilder(EventManager var1) {
      return !this.isContextDiff() && !this.isUnifiedDiff() ? new SimpleDiffBuilder(var1, this) : null;
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.eventManager = var2;
      super.execute(var1, var2);

      try {
         this.addRDSwitches();
         if (this.getKeywordSubst() != null && !this.getKeywordSubst().equals("")) {
            this.requests.add(new ArgumentRequest("-k" + this.getKeywordSubst()));
         }

         this.addArgumentRequest(this.isIgnoreAllWhitespace(), "-w");
         this.addArgumentRequest(this.isIgnoreBlankLines(), "-B");
         this.addArgumentRequest(this.isIgnoreSpaceChange(), "-b");
         this.addArgumentRequest(this.isIgnoreCase(), "-i");
         this.addArgumentRequest(this.isContextDiff(), "-c");
         this.addArgumentRequest(this.isUnifiedDiff(), "-u");
         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.DIFF);
         var1.processRequests(this.requests);
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   private void addRDSwitches() {
      if (this.getRevision2() != null) {
         this.requests.add(1, new ArgumentRequest("-r"));
         this.requests.add(2, new ArgumentRequest(this.getRevision2()));
      } else if (this.getBeforeDate2() != null) {
         this.requests.add(1, new ArgumentRequest("-D " + this.getBeforeDate2()));
      }

      if (this.getRevision1() != null) {
         this.requests.add(1, new ArgumentRequest("-r"));
         this.requests.add(2, new ArgumentRequest(this.getRevision1()));
      } else {
         if (this.getBeforeDate1() == null) {
            return;
         }

         this.requests.add(1, new ArgumentRequest("-D " + this.getBeforeDate1()));
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public String getBeforeDate1() {
      return this.beforeDate1;
   }

   public void setBeforeDate1(String var1) {
      this.beforeDate1 = var1;
   }

   public String getRevision1() {
      return this.revision1;
   }

   public void setRevision1(String var1) {
      this.revision1 = var1;
   }

   public String getRevision2() {
      return this.revision2;
   }

   public void setRevision2(String var1) {
      this.revision2 = var1;
   }

   public String getBeforeDate2() {
      return this.beforeDate2;
   }

   public void setBeforeDate2(String var1) {
      this.beforeDate2 = var1;
   }

   public String getKeywordSubst() {
      return this.keywordSubst;
   }

   public void setKeywordSubst(String var1) {
      this.keywordSubst = var1;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("diff ");
      var1.append(this.getCVSArguments());
      File[] var2 = this.getFiles();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1.append(var2[var3].getName() + " ");
         }
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'r') {
         if (this.getRevision1() == null) {
            this.setRevision1(var2);
         } else {
            this.setRevision2(var2);
         }
      } else if (var1 == 'D') {
         if (this.getBeforeDate1() == null) {
            this.setBeforeDate1(var2);
         } else {
            this.setBeforeDate2(var2);
         }
      } else if (var1 == 'k') {
         this.setKeywordSubst(var2);
      } else if (var1 == 'w') {
         this.setIgnoreAllWhitespace(true);
      } else if (var1 == 'b') {
         this.setIgnoreSpaceChange(true);
      } else if (var1 == 'B') {
         this.setIgnoreBlankLines(true);
      } else if (var1 == 'i') {
         this.setIgnoreCase(true);
      } else if (var1 == 'c') {
         this.setContextDiff(true);
      } else {
         if (var1 != 'u') {
            return false;
         }

         this.setUnifiedDiff(true);
      }

      return true;
   }

   public String getOptString() {
      return "Rlr:D:k:wBbicu";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setRevision1((String)null);
      this.setRevision2((String)null);
      this.setBeforeDate1((String)null);
      this.setBeforeDate2((String)null);
      this.setKeywordSubst((String)null);
      this.setIgnoreAllWhitespace(false);
      this.setIgnoreBlankLines(false);
      this.setIgnoreCase(false);
      this.setIgnoreSpaceChange(false);
      this.setContextDiff(false);
      this.setUnifiedDiff(false);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.getKeywordSubst() != null && this.getKeywordSubst().length() > 0) {
         var1.append("-k" + this.getKeywordSubst() + " ");
      }

      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.getRevision1() != null) {
         var1.append("-r " + this.getRevision1() + " ");
      }

      if (this.getBeforeDate1() != null) {
         var1.append("-D " + this.getBeforeDate1() + " ");
      }

      if (this.getRevision2() != null) {
         var1.append("-r " + this.getRevision2() + " ");
      }

      if (this.getBeforeDate2() != null) {
         var1.append("-D " + this.getBeforeDate2() + " ");
      }

      if (this.isIgnoreAllWhitespace()) {
         var1.append("-w ");
      }

      if (this.isIgnoreBlankLines()) {
         var1.append("-B ");
      }

      if (this.isIgnoreCase()) {
         var1.append("-i ");
      }

      if (this.isIgnoreSpaceChange()) {
         var1.append("-b ");
      }

      if (this.isContextDiff()) {
         var1.append("-c ");
      }

      if (this.isUnifiedDiff()) {
         var1.append("-u ");
      }

      return var1.toString();
   }

   public boolean isIgnoreAllWhitespace() {
      return this.ignoreAllWhitespace;
   }

   public void setIgnoreAllWhitespace(boolean var1) {
      this.ignoreAllWhitespace = var1;
   }

   public boolean isIgnoreBlankLines() {
      return this.ignoreBlankLines;
   }

   public void setIgnoreBlankLines(boolean var1) {
      this.ignoreBlankLines = var1;
   }

   public boolean isIgnoreCase() {
      return this.ignoreCase;
   }

   public void setIgnoreCase(boolean var1) {
      this.ignoreCase = var1;
   }

   public boolean isIgnoreSpaceChange() {
      return this.ignoreSpaceChange;
   }

   public void setIgnoreSpaceChange(boolean var1) {
      this.ignoreSpaceChange = var1;
   }

   public boolean isContextDiff() {
      return this.contextDiff;
   }

   public void setContextDiff(boolean var1) {
      this.contextDiff = var1;
   }

   public boolean isUnifiedDiff() {
      return this.unifiedDiff;
   }

   public void setUnifiedDiff(boolean var1) {
      this.unifiedDiff = var1;
   }
}
