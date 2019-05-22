package org.netbeans.lib.cvsclient.command.export;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.command.RepositoryCommand;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;

public class ExportCommand extends RepositoryCommand {
   private final Set emptyDirectories = new HashSet();
   private boolean pruneDirectories;
   private KeywordSubstitutionOptions keywordSubstitutionOptions;
   private String exportByDate;
   private String exportByRevision;
   private String exportDirectory;
   private boolean useHeadIfNotFound;
   private boolean notShortenPaths;
   private boolean notRunModuleProgram;

   public ExportCommand() {
      this.resetCVSCommand();
   }

   public KeywordSubstitutionOptions getKeywordSubstitutionOptions() {
      return this.keywordSubstitutionOptions;
   }

   public void setKeywordSubstitutionOptions(KeywordSubstitutionOptions var1) {
      this.keywordSubstitutionOptions = var1;
   }

   public void setPruneDirectories(boolean var1) {
      this.pruneDirectories = var1;
   }

   public boolean isPruneDirectories() {
      return this.pruneDirectories;
   }

   protected void postExpansionExecute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      if (!this.isRecursive()) {
         this.requests.add(0, new ArgumentRequest("-l"));
      }

      if (this.useHeadIfNotFound) {
         this.requests.add(0, new ArgumentRequest("-f"));
      }

      if (this.exportDirectory != null && !this.exportDirectory.equals("")) {
         this.requests.add(0, new ArgumentRequest("-d"));
         this.requests.add(1, new ArgumentRequest(this.getExportDirectory()));
      }

      if (this.exportByDate != null && this.exportByDate.length() > 0) {
         this.requests.add(0, new ArgumentRequest("-D"));
         this.requests.add(1, new ArgumentRequest(this.getExportByDate()));
      }

      if (this.exportByRevision != null && this.exportByRevision.length() > 0) {
         this.requests.add(0, new ArgumentRequest("-r"));
         this.requests.add(1, new ArgumentRequest(this.getExportByRevision()));
      }

      if (this.notShortenPaths) {
         this.requests.add(0, new ArgumentRequest("-N"));
      }

      if (this.notRunModuleProgram) {
         this.requests.add(0, new ArgumentRequest("-n"));
      }

      if (this.getKeywordSubstitutionOptions() != null) {
         this.requests.add(new ArgumentRequest("-k" + this.getKeywordSubstitutionOptions()));
      }

      this.addArgumentRequests();
      this.requests.add(new DirectoryRequest(".", var1.getRepository()));
      this.requests.add(CommandRequest.EXPORT);

      try {
         var1.processRequests(this.requests);
         if (this.pruneDirectories) {
            this.pruneEmptyDirectories();
         }

         this.requests.clear();
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.removeAllCVSAdminFiles();
      }

   }

   private void removeAllCVSAdminFiles() {
      File var1 = null;
      if (this.getExportDirectory() != null) {
         var1 = new File(this.getLocalDirectory(), this.getExportDirectory());
         this.deleteCVSSubDirs(var1);
      } else {
         var1 = new File(this.getLocalDirectory());
         Iterator var2 = this.expandedModules.iterator();

         while(var2.hasNext()) {
            String var3 = var2.next().toString();
            File var4 = new File(var1.getAbsolutePath(), var3);
            this.deleteCVSSubDirs(var4);
         }
      }

   }

   private void deleteCVSSubDirs(File var1) {
      if (var1.isDirectory()) {
         File[] var2 = var1.listFiles();
         if (var2 == null) {
            return;
         }

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var2[var3].isDirectory()) {
               if (!var2[var3].getName().equalsIgnoreCase("CVS")) {
                  this.deleteCVSSubDirs(var2[var3]);
               } else {
                  File[] var4 = var2[var3].listFiles();

                  for(int var5 = 0; var5 < var4.length; ++var5) {
                     var4[var5].delete();
                  }

                  var2[var3].delete();
               }
            }
         }
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("export ");
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

      if (this.isUseHeadIfNotFound()) {
         var1.append("-f ");
      }

      if (this.getExportByDate() != null) {
         var1.append("-D ");
         var1.append(this.getExportByDate());
         var1.append(" ");
      }

      if (this.getExportByRevision() != null) {
         var1.append("-r ");
         var1.append(this.getExportByRevision());
         var1.append(" ");
      }

      if (this.isPruneDirectories()) {
         var1.append("-P ");
      }

      if (this.isNotShortenPaths()) {
         var1.append("-N ");
      }

      if (this.isNotRunModuleProgram()) {
         var1.append("-n ");
      }

      if (this.getExportDirectory() != null) {
         var1.append("-d ");
         var1.append(this.getExportDirectory());
         var1.append(" ");
      }

      if (this.getKeywordSubstitutionOptions() != null) {
         var1.append("-k");
         var1.append(this.getKeywordSubstitutionOptions().toString());
         var1.append(" ");
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'k') {
         this.setKeywordSubstitutionOptions(KeywordSubstitutionOptions.findKeywordSubstOption(var2));
      } else if (var1 == 'r') {
         this.setExportByRevision(var2);
      } else if (var1 == 'f') {
         this.setUseHeadIfNotFound(true);
      } else if (var1 == 'D') {
         this.setExportByDate(var2);
      } else if (var1 == 'd') {
         this.setExportDirectory(var2);
      } else if (var1 == 'P') {
         this.setPruneDirectories(true);
      } else if (var1 == 'N') {
         this.setNotShortenPaths(true);
      } else if (var1 == 'n') {
         this.setNotRunModuleProgram(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else {
         if (var1 != 'R') {
            return false;
         }

         this.setRecursive(true);
      }

      return true;
   }

   public void resetCVSCommand() {
      this.setModules((String[])null);
      this.setKeywordSubstitutionOptions((KeywordSubstitutionOptions)null);
      this.setPruneDirectories(false);
      this.setRecursive(true);
      this.setExportByDate((String)null);
      this.setExportByRevision((String)null);
      this.setExportDirectory((String)null);
      this.setUseHeadIfNotFound(false);
      this.setNotShortenPaths(false);
      this.setNotRunModuleProgram(false);
   }

   public String getOptString() {
      return "k:r:D:NPlRnd:f";
   }

   public Builder createBuilder(EventManager var1) {
      return new ExportBuilder(var1, this);
   }

   public void messageSent(MessageEvent var1) {
      super.messageSent(var1);
      if (this.pruneDirectories && var1.getMessage().indexOf(": Exporting ") > 0) {
         File var2 = new File(this.getLocalDirectory(), var1.getMessage().substring(21));
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

   public String getExportByDate() {
      return this.exportByDate;
   }

   public void setExportByDate(String var1) {
      this.exportByDate = var1;
   }

   public String getExportByRevision() {
      return this.exportByRevision;
   }

   public void setExportByRevision(String var1) {
      this.exportByRevision = var1;
   }

   public String getExportDirectory() {
      return this.exportDirectory;
   }

   public void setExportDirectory(String var1) {
      this.exportDirectory = var1;
   }

   public boolean isUseHeadIfNotFound() {
      return this.useHeadIfNotFound;
   }

   public void setUseHeadIfNotFound(boolean var1) {
      this.useHeadIfNotFound = var1;
   }

   public boolean isNotShortenPaths() {
      return this.notShortenPaths;
   }

   public void setNotShortenPaths(boolean var1) {
      this.notShortenPaths = var1;
   }

   public boolean isNotRunModuleProgram() {
      return this.notRunModuleProgram;
   }

   public void setNotRunModuleProgram(boolean var1) {
      this.notRunModuleProgram = var1;
   }
}
