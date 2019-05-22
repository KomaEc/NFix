package org.netbeans.lib.cvsclient.command.tag;

import java.io.EOFException;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.RepositoryCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;

public class RtagCommand extends RepositoryCommand {
   private EventManager eventManager;
   private boolean clearFromRemoved;
   private boolean deleteTag;
   private boolean makeBranchTag;
   private boolean overrideExistingTag;
   private boolean matchHeadIfRevisionNotFound;
   private boolean noExecTagProgram;
   private String tag;
   private String tagByDate;
   private String tagByRevision;

   public Builder createBuilder(EventManager var1) {
      return new TagBuilder(var1, this.getLocalDirectory());
   }

   public boolean isClearFromRemoved() {
      return this.clearFromRemoved;
   }

   public void setClearFromRemoved(boolean var1) {
      this.clearFromRemoved = var1;
   }

   public boolean isDeleteTag() {
      return this.deleteTag;
   }

   public void setDeleteTag(boolean var1) {
      this.deleteTag = var1;
   }

   public boolean isMakeBranchTag() {
      return this.makeBranchTag;
   }

   public void setMakeBranchTag(boolean var1) {
      this.makeBranchTag = var1;
   }

   public boolean isOverrideExistingTag() {
      return this.overrideExistingTag;
   }

   public void setOverrideExistingTag(boolean var1) {
      this.overrideExistingTag = var1;
   }

   public boolean isMatchHeadIfRevisionNotFound() {
      return this.matchHeadIfRevisionNotFound;
   }

   public void setMatchHeadIfRevisionNotFound(boolean var1) {
      this.matchHeadIfRevisionNotFound = var1;
   }

   public boolean isNoExecTagProgram() {
      return this.noExecTagProgram;
   }

   public void setNoExecTagProgram(boolean var1) {
      this.noExecTagProgram = var1;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }

   public String getTagByDate() {
      return this.tagByDate;
   }

   public void setTagByDate(String var1) {
      this.tagByDate = var1;
   }

   public String getTagByRevision() {
      return this.tagByRevision;
   }

   public void setTagByRevision(String var1) {
      this.tagByRevision = var1;
   }

   protected void postExpansionExecute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.eventManager = var2;

      try {
         if (this.clearFromRemoved) {
            this.requests.add(new ArgumentRequest("-a"));
         }

         if (this.overrideExistingTag) {
            this.requests.add(new ArgumentRequest("-F"));
         }

         if (this.matchHeadIfRevisionNotFound) {
            this.requests.add(new ArgumentRequest("-f"));
         }

         if (this.makeBranchTag) {
            this.requests.add(new ArgumentRequest("-b"));
         }

         if (this.deleteTag) {
            this.requests.add(new ArgumentRequest("-d"));
         }

         if (this.noExecTagProgram) {
            this.requests.add(new ArgumentRequest("-n "));
         }

         if (this.tagByDate != null && this.tagByDate.length() > 0) {
            this.requests.add(new ArgumentRequest("-D"));
            this.requests.add(new ArgumentRequest(this.getTagByDate()));
         }

         if (this.tagByRevision != null && this.tagByRevision.length() > 0) {
            this.requests.add(new ArgumentRequest("-r"));
            this.requests.add(new ArgumentRequest(this.getTagByRevision()));
         }

         this.requests.add(new ArgumentRequest(this.getTag()));
         this.addArgumentRequests();
         this.addRequest(CommandRequest.RTAG);
         var1.processRequests(this.requests);
      } catch (CommandException var9) {
         throw var9;
      } catch (EOFException var10) {
         throw new CommandException(var10, CommandException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (Exception var11) {
         throw new CommandException(var11, var11.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("rtag ");
      var1.append(this.getCVSArguments());
      if (this.getTag() != null) {
         var1.append(this.getTag());
         var1.append(" ");
      }

      this.appendModuleArguments(var1);
      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'a') {
         this.setClearFromRemoved(true);
      } else if (var1 == 'd') {
         this.setDeleteTag(true);
      } else if (var1 == 'F') {
         this.setOverrideExistingTag(true);
      } else if (var1 == 'f') {
         this.setMatchHeadIfRevisionNotFound(true);
      } else if (var1 == 'b') {
         this.setMakeBranchTag(true);
      } else if (var1 == 'n') {
         this.setNoExecTagProgram(true);
      } else if (var1 == 'D') {
         this.setTagByDate(var2.trim());
      } else {
         if (var1 != 'r') {
            return false;
         }

         this.setTagByRevision(var2.trim());
      }

      return true;
   }

   public String getOptString() {
      return "RlaFfbdnD:r:";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setClearFromRemoved(false);
      this.setDeleteTag(false);
      this.setMakeBranchTag(false);
      this.setOverrideExistingTag(false);
      this.setMatchHeadIfRevisionNotFound(false);
      this.setNoExecTagProgram(false);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer();
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.isClearFromRemoved()) {
         var1.append("-a ");
      }

      if (this.isOverrideExistingTag()) {
         var1.append("-F ");
      }

      if (this.isMatchHeadIfRevisionNotFound()) {
         var1.append("-f ");
      }

      if (this.isMakeBranchTag()) {
         var1.append("-b ");
      }

      if (this.isDeleteTag()) {
         var1.append("-d ");
      }

      if (this.isNoExecTagProgram()) {
         var1.append("-n ");
      }

      if (this.getTagByRevision() != null && this.getTagByRevision().length() > 0) {
         var1.append("-r ");
         var1.append(this.getTagByRevision());
         var1.append(" ");
      }

      if (this.getTagByDate() != null && this.getTagByDate().length() > 0) {
         var1.append("-D ");
         var1.append(this.getTagByDate());
         var1.append(" ");
      }

      return var1.toString();
   }
}
