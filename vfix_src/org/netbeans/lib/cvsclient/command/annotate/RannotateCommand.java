package org.netbeans.lib.cvsclient.command.annotate;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.ExpandModulesRequest;
import org.netbeans.lib.cvsclient.request.RootRequest;

public class RannotateCommand extends BasicCommand {
   private final List modules = new LinkedList();
   private final List expandedModules = new LinkedList();
   private boolean useHeadIfNotFound;
   private String annotateByDate;
   private String annotateByRevision;
   private boolean headerAndDescOnly;

   public RannotateCommand() {
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

   private void processExistingModules(String var1) {
      if (this.expandedModules.size() != 0) {
         String[] var2 = new String[this.expandedModules.size()];
         var2 = (String[])this.expandedModules.toArray(var2);
         this.setModules(var2);
      }
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.requests = new LinkedList();
      if (var1.isFirstCommand()) {
         this.requests.add(new RootRequest(var1.getRepository()));
      }

      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.requests.add(new ArgumentRequest(var4));
      }

      this.expandedModules.clear();
      this.requests.add(new DirectoryRequest(".", var1.getRepository()));
      this.requests.add(new ExpandModulesRequest());

      try {
         var1.processRequests(this.requests);
      } catch (CommandException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new CommandException(var6, var6.getLocalizedMessage());
      }

      this.requests.clear();
      this.postExpansionExecute(var1, var2);
   }

   public void moduleExpanded(ModuleExpansionEvent var1) {
      this.expandedModules.add(var1.getModule());
   }

   private void postExpansionExecute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      super.execute(var1, var2);
      if (!this.isRecursive()) {
         this.requests.add(1, new ArgumentRequest("-l"));
      }

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

      Iterator var3 = this.modules.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.requests.add(new ArgumentRequest(var4));
      }

      this.requests.add(new DirectoryRequest(".", var1.getRepository()));
      this.requests.add(CommandRequest.RANNOTATE);

      try {
         var1.processRequests(this.requests);
         this.requests.clear();
      } catch (CommandException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new CommandException(var6, var6.getLocalizedMessage());
      }
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("rannotate ");
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

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setAnnotateByDate((String)null);
      this.setAnnotateByRevision((String)null);
      this.setUseHeadIfNotFound(false);
   }

   public String getOptString() {
      return "Rlr:D:f";
   }

   public Builder createBuilder(EventManager var1) {
      return new AnnotateBuilder(var1, this);
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
}
