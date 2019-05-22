package org.netbeans.lib.cvsclient.command.checkout;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.command.PipedFilesBuilder;
import org.netbeans.lib.cvsclient.command.TemporaryFileCreator;
import org.netbeans.lib.cvsclient.command.update.UpdateBuilder;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.ExpandModulesRequest;
import org.netbeans.lib.cvsclient.request.RootRequest;

public class CheckoutCommand extends BasicCommand implements TemporaryFileCreator {
   private static final String UPDATING = ": Updating ";
   private final Set emptyDirectories = new HashSet();
   private final List modules = new LinkedList();
   private final List expandedModules = new LinkedList();
   private boolean showModules;
   private boolean showModulesWithStatus;
   private boolean pipeToOutput;
   private boolean pruneDirectories;
   private boolean resetStickyOnes;
   private boolean useHeadIfNotFound;
   private boolean notShortenPaths;
   private boolean isNotShortenSet;
   private String checkoutByDate;
   private String checkoutByRevision;
   private String checkoutDirectory;
   private KeywordSubstitutionOptions keywordSubst;
   private boolean notRunModuleProgram;
   private ClientServices client;

   public CheckoutCommand(boolean var1, String[] var2) {
      this.resetCVSCommand();
      this.setRecursive(var1);
      this.setModules(var2);
   }

   public CheckoutCommand(boolean var1, String var2) {
      this.resetCVSCommand();
      this.setRecursive(var1);
      this.setModule(var2);
   }

   public CheckoutCommand() {
      this.resetCVSCommand();
      this.setRecursive(true);
   }

   public void setModule(String var1) {
      this.modules.add(var1);
   }

   public void clearModules() {
      this.modules.clear();
   }

   public void setModules(String[] var1) {
      this.clearModules();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         this.modules.add(var3);
      }

   }

   public String[] getModules() {
      String[] var1 = new String[this.modules.size()];
      var1 = (String[])this.modules.toArray(var1);
      return var1;
   }

   private void processExistingModules(String var1) {
      if (this.expandedModules.size() != 0) {
         ArrayList var2 = new ArrayList(this.expandedModules.size());
         Iterator var3 = this.expandedModules.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var4.equals(".")) {
               var2.add(new File(var1));
               break;
            }

            File var5 = null;
            File var6 = new File(var1, var4);
            if (var6.isFile()) {
               var5 = var6.getParentFile();
            } else {
               var5 = var6;
            }

            File var7 = new File(var5, "CVS/Repository");
            if (var7.exists()) {
               var2.add(var6);
            }
         }

         File[] var8 = new File[var2.size()];
         var8 = (File[])var2.toArray(var8);
         this.setFiles(var8);
      }
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      this.client = var1;

      try {
         this.requests = new LinkedList();
         if (var1.isFirstCommand()) {
            this.requests.add(new RootRequest(var1.getRepository()));
         }

         if (this.showModules || this.showModulesWithStatus) {
            if (this.builder == null && !this.isBuilderSet()) {
               this.builder = this.createBuilder(var2);
            }

            if (this.showModules) {
               this.requests.add(new ArgumentRequest("-c"));
            }

            if (this.showModulesWithStatus) {
               this.requests.add(new ArgumentRequest("-s"));
            }

            this.requests.add(CommandRequest.CHECKOUT);

            try {
               var1.processRequests(this.requests);
               this.requests.clear();
            } catch (CommandException var14) {
               throw var14;
            } catch (EOFException var15) {
               throw new CommandException(var15, CommandException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
            } catch (Exception var16) {
               throw new CommandException(var16, var16.getLocalizedMessage());
            }
         } else {
            Iterator var3 = this.modules.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               this.requests.add(new ArgumentRequest(var4));
            }

            this.expandedModules.clear();
            this.requests.add(new DirectoryRequest(".", var1.getRepository()));
            this.requests.add(new RootRequest(var1.getRepository()));
            this.requests.add(new ExpandModulesRequest());

            try {
               var1.processRequests(this.requests);
            } catch (CommandException var12) {
               throw var12;
            } catch (Exception var13) {
               throw new CommandException(var13, var13.getLocalizedMessage());
            }

            this.requests.clear();
            this.postExpansionExecute(var1, var2);
         }
      } finally {
         this.client = null;
      }
   }

   protected boolean assumeLocalPathWhenUnspecified() {
      return false;
   }

   public void moduleExpanded(ModuleExpansionEvent var1) {
      this.expandedModules.add(var1.getModule());
   }

   private void postExpansionExecute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      this.processExistingModules(var1.getLocalPath());
      super.execute(var1, var2);
      int var3 = this.requests.size();
      if (!this.isRecursive()) {
         this.requests.add(0, new ArgumentRequest("-l"));
      }

      if (this.pipeToOutput) {
         this.requests.add(0, new ArgumentRequest("-p"));
      }

      if (this.resetStickyOnes) {
         this.requests.add(0, new ArgumentRequest("-A"));
      }

      if (this.useHeadIfNotFound) {
         this.requests.add(0, new ArgumentRequest("-f"));
      }

      if (this.isNotShortenPaths()) {
         this.requests.add(0, new ArgumentRequest("-N"));
      }

      if (this.notRunModuleProgram) {
         this.requests.add(0, new ArgumentRequest("-n"));
      }

      if (this.checkoutByDate != null && this.checkoutByDate.length() > 0) {
         this.requests.add(0, new ArgumentRequest("-D"));
         this.requests.add(1, new ArgumentRequest(this.getCheckoutByDate()));
      }

      if (this.checkoutByRevision != null && this.checkoutByRevision.length() > 0) {
         this.requests.add(0, new ArgumentRequest("-r"));
         this.requests.add(1, new ArgumentRequest(this.getCheckoutByRevision()));
      }

      if (this.checkoutDirectory != null && !this.checkoutDirectory.equals("")) {
         this.requests.add(0, new ArgumentRequest("-d"));
         this.requests.add(1, new ArgumentRequest(this.getCheckoutDirectory()));
      }

      if (this.getKeywordSubst() != null) {
         this.requests.add(0, new ArgumentRequest("-k" + this.getKeywordSubst()));
      }

      var3 = this.requests.size() - var3;
      this.requests.add(var3++, new ArgumentRequest("--"));
      Iterator var4 = this.modules.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         this.requests.add(var3++, new ArgumentRequest(var5));
      }

      this.requests.add(new DirectoryRequest(".", var1.getRepository()));
      this.requests.add(CommandRequest.CHECKOUT);

      try {
         var1.processRequests(this.requests);
         if (this.pruneDirectories) {
            this.pruneEmptyDirectories();
         }

         this.requests.clear();
      } catch (CommandException var6) {
         throw var6;
      } catch (Exception var7) {
         throw new CommandException(var7, var7.getLocalizedMessage());
      }
   }

   public boolean isShowModules() {
      return this.showModules;
   }

   public void setShowModules(boolean var1) {
      this.showModules = var1;
   }

   public boolean isShowModulesWithStatus() {
      return this.showModulesWithStatus;
   }

   public void setShowModulesWithStatus(boolean var1) {
      this.showModulesWithStatus = var1;
   }

   public void setPruneDirectories(boolean var1) {
      this.pruneDirectories = var1;
   }

   public boolean getPruneDirectories() {
      return this.pruneDirectories;
   }

   public boolean isPipeToOutput() {
      return this.pipeToOutput;
   }

   public void setPipeToOutput(boolean var1) {
      this.pipeToOutput = var1;
   }

   public boolean isResetStickyOnes() {
      return this.resetStickyOnes;
   }

   public void setResetStickyOnes(boolean var1) {
      this.resetStickyOnes = var1;
   }

   public boolean isUseHeadIfNotFound() {
      return this.useHeadIfNotFound;
   }

   public void setUseHeadIfNotFound(boolean var1) {
      this.useHeadIfNotFound = var1;
   }

   public boolean isNotShortenPaths() {
      return this.notShortenPaths || !this.isNotShortenSet && this.checkoutDirectory == null;
   }

   public void setNotShortenPaths(boolean var1) {
      this.notShortenPaths = var1;
      this.isNotShortenSet = true;
   }

   public boolean isNotRunModuleProgram() {
      return this.notRunModuleProgram;
   }

   public void setNotRunModuleProgram(boolean var1) {
      this.notRunModuleProgram = var1;
   }

   public String getCheckoutByDate() {
      return this.checkoutByDate;
   }

   public void setCheckoutByDate(String var1) {
      this.checkoutByDate = var1;
   }

   public String getCheckoutByRevision() {
      return this.checkoutByRevision;
   }

   public void setCheckoutByRevision(String var1) {
      this.checkoutByRevision = var1;
   }

   public String getCheckoutDirectory() {
      return this.checkoutDirectory;
   }

   public void setCheckoutDirectory(String var1) {
      this.checkoutDirectory = var1;
   }

   public KeywordSubstitutionOptions getKeywordSubst() {
      return this.keywordSubst;
   }

   public void setKeywordSubst(KeywordSubstitutionOptions var1) {
      this.keywordSubst = var1;
   }

   public Builder createBuilder(EventManager var1) {
      if (!this.isShowModules() && !this.isShowModulesWithStatus()) {
         return (Builder)(this.isPipeToOutput() ? new PipedFilesBuilder(var1, this, this) : new UpdateBuilder(var1, this.getLocalDirectory()));
      } else {
         return new ModuleListBuilder(var1, this);
      }
   }

   public File createTempFile(String var1) throws IOException {
      File var2 = File.createTempFile("cvs", ".dff", this.getGlobalOptions().getTempDir());
      return var2;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("checkout ");
      var1.append(this.getCVSArguments());
      if (!this.isShowModules() && !this.isShowModulesWithStatus()) {
         Iterator var2 = this.modules.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append(var3);
            var1.append(' ');
         }
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'c') {
         this.setShowModules(true);
      } else if (var1 == 's') {
         this.setShowModulesWithStatus(true);
      } else if (var1 == 'p') {
         this.setPipeToOutput(true);
      } else if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'A') {
         this.setResetStickyOnes(true);
      } else if (var1 == 'f') {
         this.setUseHeadIfNotFound(true);
      } else if (var1 == 'P') {
         this.setPruneDirectories(true);
      } else if (var1 == 'D') {
         this.setCheckoutByDate(var2.trim());
      } else if (var1 == 'r') {
         this.setCheckoutByRevision(var2.trim());
      } else if (var1 == 'd') {
         this.setCheckoutDirectory(var2);
      } else if (var1 == 'N') {
         this.setNotShortenPaths(true);
      } else if (var1 == 'n') {
         this.setNotRunModuleProgram(true);
      } else {
         if (var1 != 'k') {
            return false;
         }

         KeywordSubstitutionOptions var3 = KeywordSubstitutionOptions.findKeywordSubstOption(var2);
         this.setKeywordSubst(var3);
      }

      return true;
   }

   public String getOptString() {
      return "cnpslNPRAD:r:fk:d:";
   }

   public void resetCVSCommand() {
      this.setShowModules(false);
      this.setShowModulesWithStatus(false);
      this.setPipeToOutput(false);
      this.setRecursive(true);
      this.setResetStickyOnes(false);
      this.setUseHeadIfNotFound(false);
      this.setCheckoutByDate((String)null);
      this.setCheckoutByRevision((String)null);
      this.setKeywordSubst((KeywordSubstitutionOptions)null);
      this.setPruneDirectories(false);
      this.setNotShortenPaths(false);
      this.isNotShortenSet = false;
      this.setNotRunModuleProgram(false);
      this.setCheckoutDirectory((String)null);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.isShowModules()) {
         var1.append("-c ");
      }

      if (this.isShowModulesWithStatus()) {
         var1.append("-s ");
      }

      if (this.isPipeToOutput()) {
         var1.append("-p ");
      }

      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.isResetStickyOnes()) {
         var1.append("-A ");
      }

      if (this.isUseHeadIfNotFound()) {
         var1.append("-f ");
      }

      if (this.getPruneDirectories()) {
         var1.append("-P ");
      }

      if (this.isNotShortenPaths()) {
         var1.append("-N ");
      }

      if (this.isNotRunModuleProgram()) {
         var1.append("-n ");
      }

      if (this.getKeywordSubst() != null) {
         var1.append("-k");
         var1.append(this.getKeywordSubst());
         var1.append(' ');
      }

      if (this.getCheckoutByRevision() != null && this.getCheckoutByRevision().length() > 0) {
         var1.append("-r ");
         var1.append(this.getCheckoutByRevision());
         var1.append(' ');
      }

      if (this.getCheckoutByDate() != null && this.getCheckoutByDate().length() > 0) {
         var1.append("-D ");
         var1.append(this.getCheckoutByDate());
         var1.append(' ');
      }

      if (this.getCheckoutDirectory() != null) {
         var1.append("-d ");
         var1.append(this.getCheckoutDirectory());
         var1.append(" ");
      }

      return var1.toString();
   }

   public void messageSent(MessageEvent var1) {
      super.messageSent(var1);
      if (this.pruneDirectories && var1.getMessage().indexOf(": Updating ") > 0) {
         File var2 = new File(this.getLocalDirectory(), var1.getMessage().substring(var1.getMessage().indexOf(": Updating ") + ": Updating ".length()));
         this.emptyDirectories.add(var2);
      }

   }

   private boolean pruneEmptyDirectory(File var1) throws IOException {
      boolean var2 = true;
      File[] var3 = var1.listFiles();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].isFile()) {
               var2 = false;
            } else if (!var3[var4].getName().equals("CVS")) {
               var2 = this.pruneEmptyDirectory(var3[var4]);
            }

            if (!var2) {
               break;
            }
         }

         if (var2) {
            File var8 = new File(var1, "CVS/Entries");
            if (var8.exists()) {
               File var5 = new File(var1, "CVS");
               File[] var6 = var5.listFiles();

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  var6[var7].delete();
               }

               var5.delete();
               var1.delete();
               this.client.removeEntry(var1);
            }
         }
      }

      return var2;
   }

   private void pruneEmptyDirectories() throws IOException {
      Iterator var1 = this.emptyDirectories.iterator();

      while(var1.hasNext()) {
         File var2 = (File)var1.next();
         if (var2.exists()) {
            this.pruneEmptyDirectory(var2);
         }
      }

      this.emptyDirectories.clear();
   }
}
