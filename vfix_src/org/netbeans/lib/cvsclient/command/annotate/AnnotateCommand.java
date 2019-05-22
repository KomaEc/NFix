package org.netbeans.lib.cvsclient.command.annotate;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.EntryRequest;

public class AnnotateCommand extends BasicCommand {
   protected EventManager eventManager;
   private boolean useHeadIfNotFound;
   private String annotateByDate;
   private String annotateByRevision;

   public Builder createBuilder(EventManager var1) {
      return new AnnotateBuilder(var1, this);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      this.eventManager = var2;
      var1.ensureConnection();
      super.execute(var1, var2);
      this.excludeBinaryFiles(this.requests);

      try {
         if (this.useHeadIfNotFound) {
            this.requests.add(1, new ArgumentRequest("-f"));
         }

         if (this.annotateByDate != null && this.annotateByDate.length() > 0) {
            this.requests.add(1, new ArgumentRequest("-D"));
            this.requests.add(2, new ArgumentRequest(this.getAnnotateByDate()));
         }

         if (this.annotateByRevision != null && this.annotateByRevision.length() > 0) {
            this.requests.add(1, new ArgumentRequest("-r"));
            this.requests.add(2, new ArgumentRequest(this.getAnnotateByRevision()));
         }

         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.ANNOTATE);
         var1.processRequests(this.requests);
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   private void excludeBinaryFiles(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 instanceof EntryRequest) {
            EntryRequest var4 = (EntryRequest)var3;
            if (var4.getEntry().isBinary()) {
               var2.remove();
               if (var2.hasNext()) {
                  var2.next();
                  var2.remove();
               }
            }
         }
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.builder != null) {
         this.builder.outputDone();
      }

   }

   public boolean isUseHeadIfNotFound() {
      return this.useHeadIfNotFound;
   }

   public void setUseHeadIfNotFound(boolean var1) {
      this.useHeadIfNotFound = var1;
   }

   public String getAnnotateByDate() {
      return this.annotateByDate;
   }

   public void setAnnotateByDate(String var1) {
      this.annotateByDate = var1;
   }

   public String getAnnotateByRevision() {
      return this.annotateByRevision;
   }

   public void setAnnotateByRevision(String var1) {
      this.annotateByRevision = var1;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("annotate ");
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
         this.setAnnotateByRevision(var2);
      } else if (var1 == 'D') {
         this.setAnnotateByDate(var2);
      } else {
         if (var1 != 'f') {
            return false;
         }

         this.setUseHeadIfNotFound(true);
      }

      return true;
   }

   public String getOptString() {
      return "Rlr:D:f";
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setAnnotateByDate((String)null);
      this.setAnnotateByRevision((String)null);
      this.setUseHeadIfNotFound(false);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.getAnnotateByRevision() != null) {
         var1.append("-r ");
         var1.append(this.getAnnotateByRevision());
         var1.append(" ");
      }

      if (this.getAnnotateByDate() != null) {
         var1.append("-D ");
         var1.append(this.getAnnotateByDate());
         var1.append(" ");
      }

      if (this.isUseHeadIfNotFound()) {
         var1.append("-f ");
      }

      return var1.toString();
   }
}
