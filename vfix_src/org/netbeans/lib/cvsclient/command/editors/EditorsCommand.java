package org.netbeans.lib.cvsclient.command.editors;

import java.io.EOFException;
import java.io.File;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.CommandRequest;

public class EditorsCommand extends BasicCommand {
   public EditorsCommand() {
      this.resetCVSCommand();
   }

   public Builder createBuilder(EventManager var1) {
      return new EditorsBuilder(var1);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      super.execute(var1, var2);

      try {
         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.EDITORS);
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
      StringBuffer var1 = new StringBuffer("editors ");
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
}
