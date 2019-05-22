package org.netbeans.lib.cvsclient.command.edit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.Watch;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.file.FileUtils;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.NotifyRequest;

public class EditCommand extends BasicCommand {
   private boolean checkThatUnedited;
   private boolean forceEvenIfEdited;
   private Watch temporaryWatch;
   private transient ClientServices clientServices;

   public static File getEditBackupFile(File var0) {
      return new File(var0.getParent(), "CVS/Base/" + var0.getName());
   }

   public EditCommand() {
      this.resetCVSCommand();
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException {
      this.clientServices = var1;

      try {
         var1.ensureConnection();
         super.execute(var1, var2);
         this.addArgumentRequest(this.isCheckThatUnedited(), "-c");
         this.addArgumentRequest(this.isForceEvenIfEdited(), "-f");
         this.addRequestForWorkingDirectory(var1);
         this.addRequest(CommandRequest.NOOP);
         var1.processRequests(this.requests);
      } catch (AuthenticationException var10) {
      } catch (CommandException var11) {
         throw var11;
      } catch (EOFException var12) {
         throw new CommandException(var12, CommandException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (Exception var13) {
         throw new CommandException(var13, var13.getLocalizedMessage());
      } finally {
         this.requests.clear();
         this.clientServices = null;
      }

   }

   protected void addRequestForFile(File var1, Entry var2) {
      String var3 = Watch.getWatchString(this.getTemporaryWatch());
      this.requests.add(new NotifyRequest(var1, "E", var3));

      try {
         this.editFile(this.clientServices, var1);
      } catch (IOException var5) {
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("edit ");
      var1.append(this.getCVSArguments());
      this.appendFileArguments(var1);
      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'R') {
         this.setRecursive(true);
      } else {
         if (var1 != 'l') {
            return false;
         }

         this.setRecursive(false);
      }

      return true;
   }

   public String getOptString() {
      return "Rl";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setCheckThatUnedited(false);
      this.setForceEvenIfEdited(true);
      this.setTemporaryWatch((Watch)null);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer();
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      return var1.toString();
   }

   public boolean isCheckThatUnedited() {
      return this.checkThatUnedited;
   }

   public void setCheckThatUnedited(boolean var1) {
      this.checkThatUnedited = var1;
   }

   public boolean isForceEvenIfEdited() {
      return this.forceEvenIfEdited;
   }

   public void setForceEvenIfEdited(boolean var1) {
      this.forceEvenIfEdited = var1;
   }

   public Watch getTemporaryWatch() {
      return this.temporaryWatch;
   }

   public void setTemporaryWatch(Watch var1) {
      this.temporaryWatch = var1;
   }

   private void editFile(ClientServices var1, File var2) throws IOException {
      this.addBaserevEntry(var1, var2);
      FileUtils.copyFile(var2, getEditBackupFile(var2));
      FileUtils.setFileReadOnly(var2, false);
   }

   private void addBaserevEntry(ClientServices var1, File var2) throws IOException {
      Entry var3 = var1.getEntry(var2);
      if (var3 != null && var3.getRevision() != null && !var3.isNewUserFile() && !var3.isUserFileToBeRemoved()) {
         File var4 = new File(var2.getParentFile(), "CVS/Baserev");
         File var5 = new File(var4.getAbsolutePath() + '~');
         BufferedReader var6 = null;
         BufferedWriter var7 = null;
         boolean var8 = true;
         boolean var9 = true;
         String var10 = 'B' + var2.getName() + '/';

         try {
            var7 = new BufferedWriter(new FileWriter(var5));
            var9 = false;
            var6 = new BufferedReader(new FileReader(var4));

            for(String var11 = var6.readLine(); var11 != null; var11 = var6.readLine()) {
               if (var11.startsWith(var10)) {
                  var8 = false;
               }

               var9 = true;
               var7.write(var11);
               var7.newLine();
               var9 = false;
            }
         } catch (IOException var70) {
            if (var9) {
               throw var70;
            }
         } finally {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (IOException var69) {
               }
            }

            if (var7 != null) {
               try {
                  if (var8 && !var9) {
                     var7.write(var10 + var3.getRevision() + '/');
                     var7.newLine();
                  }
               } finally {
                  try {
                     var7.close();
                  } catch (IOException var67) {
                  }

               }
            }

         }

         var4.delete();
         var5.renameTo(var4);
      } else {
         throw new IllegalArgumentException("File does not have an Entry or Entry is invalid!");
      }
   }
}
