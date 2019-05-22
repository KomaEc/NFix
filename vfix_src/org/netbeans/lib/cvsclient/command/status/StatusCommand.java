package org.netbeans.lib.cvsclient.command.status;

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

public class StatusCommand extends BasicCommand {
   private EventManager eventManager;
   private boolean includeTags;

   public Builder createBuilder(EventManager var1) {
      return new StatusBuilder(var1, this);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.eventManager = var2;
      super.execute(var1, var2);

      try {
         if (this.includeTags) {
            this.requests.add(1, new ArgumentRequest("-v"));
         }

         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.STATUS);
         var1.processRequests(this.requests);
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   public boolean isIncludeTags() {
      return this.includeTags;
   }

   public void setIncludeTags(boolean var1) {
      this.includeTags = var1;
   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("status ");
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
      } else {
         if (var1 != 'v') {
            return false;
         }

         this.setIncludeTags(true);
      }

      return true;
   }

   public String getOptString() {
      return "Rlv";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setIncludeTags(false);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.isIncludeTags()) {
         var1.append("-v ");
      }

      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      return var1.toString();
   }
}
