package org.netbeans.lib.cvsclient.command.watch;

import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.Watch;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;

public class WatchCommand extends BasicCommand {
   private WatchMode watchMode;
   private Watch watch;

   public WatchCommand() {
      this.resetCVSCommand();
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      this.checkState();
      var1.ensureConnection();

      try {
         super.execute(var1, var2);
         if (this.getWatchMode().isWatchOptionAllowed()) {
            String[] var3 = this.getWatchNotNull().getArguments();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               this.addRequest(new ArgumentRequest("-a"));
               this.addRequest(new ArgumentRequest(var3[var4]));
            }
         }

         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(this.getWatchMode().getCommand());
         var1.processRequests(this.requests);
      } catch (CommandException var9) {
         throw var9;
      } catch (Exception var10) {
         throw new CommandException(var10, var10.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

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
      this.setWatch((Watch)null);
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("watch ");
      var1.append(this.getCVSArguments());
      this.appendFileArguments(var1);
      return var1.toString();
   }

   public String getCVSArguments() {
      this.checkState();
      StringBuffer var1 = new StringBuffer();
      var1.append(this.getWatchMode().toString());
      var1.append(' ');
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.getWatchMode().isWatchOptionAllowed()) {
         var1.append("-a ");
         var1.append(this.getWatchNotNull().toString());
      }

      return var1.toString();
   }

   public WatchMode getWatchMode() {
      return this.watchMode;
   }

   public void setWatchMode(WatchMode var1) {
      this.watchMode = var1;
   }

   public Watch getWatch() {
      return this.watch;
   }

   private Watch getWatchNotNull() {
      return this.watch == null ? Watch.ALL : this.watch;
   }

   public void setWatch(Watch var1) {
      this.watch = var1;
   }

   private void checkState() {
      if (this.getWatchMode() == null) {
         throw new IllegalStateException("Watch mode expected!");
      }
   }
}
