package org.netbeans.lib.cvsclient.command.unedit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.Watch;
import org.netbeans.lib.cvsclient.command.edit.EditCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.file.FileUtils;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.NotifyRequest;

public class UneditCommand extends BasicCommand {
   private Watch temporaryWatch;

   public UneditCommand() {
      this.resetCVSCommand();
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();

      try {
         super.execute(var1, var2);
         this.addRequestForWorkingDirectory(var1);
         this.addRequest(CommandRequest.NOOP);
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

   protected void addRequestForFile(File var1, Entry var2) {
      String var3 = Watch.getWatchString(this.getTemporaryWatch());
      this.requests.add(new NotifyRequest(var1, "U", var3));

      try {
         this.uneditFile(var1);
      } catch (IOException var5) {
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("unedit ");
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
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer();
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      return var1.toString();
   }

   public Watch getTemporaryWatch() {
      return this.temporaryWatch;
   }

   public void setTemporaryWatch(Watch var1) {
      this.temporaryWatch = var1;
   }

   private void uneditFile(File var1) throws IOException {
      this.removeBaserevEntry(var1);
      EditCommand.getEditBackupFile(var1).delete();
      FileUtils.setFileReadOnly(var1, true);
   }

   private void removeBaserevEntry(File var1) throws IOException {
      File var2 = new File(var1.getParentFile(), "CVS/Baserev");
      File var3 = new File(var2.getAbsolutePath() + '~');
      BufferedReader var4 = null;
      BufferedWriter var5 = null;
      String var6 = 'B' + var1.getName() + '/';

      try {
         var5 = new BufferedWriter(new FileWriter(var3));
         var4 = new BufferedReader(new FileReader(var2));

         for(String var7 = var4.readLine(); var7 != null; var7 = var4.readLine()) {
            if (!var7.startsWith(var6)) {
               var5.write(var7);
               var5.newLine();
            }
         }
      } catch (FileNotFoundException var21) {
      } finally {
         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var20) {
            }
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var19) {
            }
         }

      }

      var2.delete();
      if (var3.length() > 0L) {
         var3.renameTo(var2);
      } else {
         var3.delete();
      }

   }
}
