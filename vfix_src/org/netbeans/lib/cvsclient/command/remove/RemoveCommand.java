package org.netbeans.lib.cvsclient.command.remove;

import java.io.File;
import java.io.IOException;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.util.BugLog;

public class RemoveCommand extends BasicCommand {
   private boolean deleteBeforeRemove;
   private boolean ignoreLocallyExistingFiles;

   public boolean isDeleteBeforeRemove() {
      return this.deleteBeforeRemove;
   }

   public void setDeleteBeforeRemove(boolean var1) {
      this.deleteBeforeRemove = var1;
   }

   /** @deprecated */
   public boolean doesIgnoreLocallyExistingFiles() {
      return this.ignoreLocallyExistingFiles;
   }

   public boolean isIgnoreLocallyExistingFiles() {
      return this.ignoreLocallyExistingFiles;
   }

   public void setIgnoreLocallyExistingFiles(boolean var1) {
      this.ignoreLocallyExistingFiles = var1;
   }

   public Builder createBuilder(EventManager var1) {
      return new RemoveBuilder(var1, this);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      if (this.files != null && this.files.length != 0) {
         var1.ensureConnection();
         if (this.isDeleteBeforeRemove()) {
            this.removeAll(this.files);
         }

         super.execute(var1, var2);

         try {
            this.addRequestForWorkingDirectory(var1);
            this.addArgumentRequests();
            this.addRequest(CommandRequest.REMOVE);
            var1.processRequests(this.requests);
         } catch (CommandException var8) {
            throw var8;
         } catch (Exception var9) {
            throw new CommandException(var9, var9.getLocalizedMessage());
         } finally {
            this.requests.clear();
         }

      } else {
         throw new CommandException("No files have been specified for removal.", CommandException.getLocalMessage("RemoveCommand.noFilesSpecified", (Object[])null));
      }
   }

   protected void sendEntryAndModifiedRequests(Entry var1, File var2) {
      super.sendEntryAndModifiedRequests(var1, this.isIgnoreLocallyExistingFiles() ? null : var2);
      if (var1.getRevision().equals("0")) {
         try {
            this.clientServices.removeEntry(var2);
         } catch (IOException var4) {
            BugLog.getInstance().showException(var4);
         }
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("remove ");
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
      if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'R') {
         this.setRecursive(true);
      } else {
         if (var1 != 'f') {
            return false;
         }

         this.setDeleteBeforeRemove(true);
      }

      return true;
   }

   private void removeAll(File[] var1) throws CommandException {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            File var3 = var1[var2];
            if (var3.exists() && var3.isFile()) {
               if (!var3.delete()) {
                  throw new CommandException("Cannot delete file " + var3.getAbsolutePath(), CommandException.getLocalMessage("RemoveCommand.cannotDelete", new Object[]{var3.getAbsolutePath()}));
               }
            } else if (this.isRecursive() && !var3.getName().equalsIgnoreCase("CVS")) {
               this.removeAll(var3.listFiles());
            }
         }

      }
   }

   public String getOptString() {
      return "flR";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setDeleteBeforeRemove(false);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.isDeleteBeforeRemove()) {
         var1.append("-f ");
      }

      return var1.toString();
   }
}
