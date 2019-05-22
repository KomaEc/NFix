package org.netbeans.lib.cvsclient.command;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.ExpandModulesRequest;
import org.netbeans.lib.cvsclient.request.Request;
import org.netbeans.lib.cvsclient.request.RootRequest;

public abstract class RepositoryCommand extends BuildableCommand {
   protected List requests = new LinkedList();
   protected ClientServices clientServices;
   private boolean recursive = true;
   protected final List modules = new LinkedList();
   protected final List expandedModules = new LinkedList();

   public boolean isRecursive() {
      return this.recursive;
   }

   public void setRecursive(boolean var1) {
      this.recursive = var1;
   }

   public void addModule(String var1) {
      this.modules.add(var1);
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

   public void clearModules() {
      this.modules.clear();
   }

   protected final void addArgumentRequests() {
      if (this.expandedModules.size() != 0) {
         Iterator var1 = this.expandedModules.iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            this.addRequest(new ArgumentRequest(var2));
         }

      }
   }

   public final void moduleExpanded(ModuleExpansionEvent var1) {
      this.expandedModules.add(var1.getModule());
   }

   public final void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.requests.clear();
      super.execute(var1, var2);
      this.clientServices = var1;
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

   protected abstract void postExpansionExecute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException;

   protected final void addRequest(Request var1) {
      this.requests.add(var1);
   }

   protected final void addRequestForWorkingDirectory(ClientServices var1) throws IOException {
      this.addRequest(new DirectoryRequest(".", var1.getRepositoryForDirectory(this.getLocalDirectory())));
   }

   protected final void addArgumentRequest(boolean var1, String var2) {
      if (var1) {
         this.addRequest(new ArgumentRequest(var2));
      }
   }

   protected final void appendModuleArguments(StringBuffer var1) {
      if (this.expandedModules.size() != 0) {
         Iterator var2 = this.expandedModules.iterator();
         var1.append((String)var2.next());

         while(var2.hasNext()) {
            var1.append(' ');
            var1.append((String)var2.next());
         }

      }
   }
}
