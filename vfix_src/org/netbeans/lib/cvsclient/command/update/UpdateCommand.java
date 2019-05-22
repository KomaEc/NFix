package org.netbeans.lib.cvsclient.command.update;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.CommandUtils;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.command.PipedFilesBuilder;
import org.netbeans.lib.cvsclient.command.TemporaryFileCreator;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.file.FileUtils;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.EntryRequest;
import org.netbeans.lib.cvsclient.request.UnchangedRequest;

public class UpdateCommand extends BasicCommand implements TemporaryFileCreator {
   private static final String RENAME_FORMAT = "{0}/.#{1}.{2}";
   private static final Object[] FORMAT_PARAMETER = new Object[3];
   private final Set emptyDirectories = new HashSet();
   private boolean buildDirectories;
   private boolean cleanCopy;
   private boolean pruneDirectories;
   private boolean pipeToOutput;
   private boolean resetStickyOnes;
   private boolean useHeadIfNotFound;
   private String updateByDate;
   private String updateByRevision;
   private KeywordSubstitutionOptions keywordSubst;
   private String mergeRevision1;
   private String mergeRevision2;

   public UpdateCommand() {
      this.resetCVSCommand();
   }

   public Builder createBuilder(EventManager var1) {
      return (Builder)(this.isPipeToOutput() ? new PipedFilesBuilder(var1, this, this) : new UpdateBuilder(var1, this.getLocalDirectory()));
   }

   protected void sendEntryAndModifiedRequests(Entry var1, File var2) {
      if (this.isCleanCopy() && var2 != null && var1 != null) {
         if (!this.isPipeToOutput()) {
            FORMAT_PARAMETER[0] = var2.getParent();
            FORMAT_PARAMETER[1] = var2.getName();
            FORMAT_PARAMETER[2] = var1.getRevision();
            String var3 = MessageFormat.format("{0}/.#{1}.{2}", FORMAT_PARAMETER);

            try {
               FileUtils.copyFile(var2, new File(var3));
            } catch (IOException var5) {
            }
         }

         var2 = null;
      }

      super.sendEntryAndModifiedRequests(var1, var2);
   }

   public void setBuildDirectories(boolean var1) {
      this.buildDirectories = var1;
   }

   public boolean isBuildDirectories() {
      return this.buildDirectories;
   }

   public void setCleanCopy(boolean var1) {
      this.cleanCopy = var1;
   }

   public boolean isCleanCopy() {
      return this.cleanCopy;
   }

   public void setPruneDirectories(boolean var1) {
      this.pruneDirectories = var1;
   }

   public boolean isPruneDirectories() {
      return this.pruneDirectories;
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      super.execute(var1, var2);
      this.emptyDirectories.clear();

      try {
         if (!this.isRecursive()) {
            this.requests.add(1, new ArgumentRequest("-l"));
         }

         if (this.isBuildDirectories()) {
            this.requests.add(1, new ArgumentRequest("-d"));
         }

         if (this.isCleanCopy() && !this.isPipeToOutput()) {
            this.requests.add(1, new ArgumentRequest("-C"));
         }

         if (this.isPipeToOutput()) {
            this.requests.add(1, new ArgumentRequest("-p"));
         }

         if (this.isResetStickyOnes()) {
            this.requests.add(1, new ArgumentRequest("-A"));
         }

         if (this.isUseHeadIfNotFound()) {
            this.requests.add(1, new ArgumentRequest("-f"));
         }

         if (this.getUpdateByDate() != null) {
            this.requests.add(1, new ArgumentRequest("-D"));
            this.requests.add(2, new ArgumentRequest(this.getUpdateByDate()));
         } else if (this.getUpdateByRevision() != null) {
            this.requests.add(1, new ArgumentRequest("-r"));
            this.requests.add(2, new ArgumentRequest(this.getUpdateByRevision()));
         }

         if (this.getMergeRevision1() != null) {
            this.requests.add(1, new ArgumentRequest("-j"));
            this.requests.add(2, new ArgumentRequest(this.getMergeRevision1()));
            if (this.getMergeRevision2() != null) {
               this.requests.add(3, new ArgumentRequest("-j"));
               this.requests.add(4, new ArgumentRequest(this.getMergeRevision2()));
            }
         }

         if (this.getKeywordSubst() != null) {
            this.requests.add(1, new ArgumentRequest("-k"));
            this.requests.add(2, new ArgumentRequest(this.getKeywordSubst().toString()));
         }

         this.requests.add(1, new ArgumentRequest("-u"));
         this.addRequestForWorkingDirectory(var1);
         this.addArgumentRequests();
         this.addRequest(CommandRequest.UPDATE);
         if (this.isPipeToOutput() && (this.getUpdateByRevision() != null || this.getUpdateByDate() != null)) {
            ListIterator var3 = this.requests.listIterator();

            while(var3.hasNext()) {
               Object var4 = var3.next();
               if (var4 instanceof EntryRequest) {
                  EntryRequest var5 = (EntryRequest)var4;
                  Entry var6 = var5.getEntry();
                  if (var6.getRevision().startsWith("-")) {
                     var6.setRevision(var6.getRevision().substring(1));
                  }

                  var3.set(new EntryRequest(var6));
                  var3.add(new UnchangedRequest(var6.getName()));
               }
            }
         }

         var1.processRequests(this.requests);
         if (this.pruneDirectories && (this.getGlobalOptions() == null || !this.getGlobalOptions().isDoNoChanges())) {
            this.pruneEmptyDirectories(var1);
         }
      } catch (CommandException var12) {
         throw var12;
      } catch (EOFException var13) {
         throw new CommandException(var13, CommandException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (Exception var14) {
         throw new CommandException(var14, var14.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

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

   public String getUpdateByDate() {
      return this.updateByDate;
   }

   public void setUpdateByDate(String var1) {
      this.updateByDate = getTrimmedString(var1);
   }

   public String getUpdateByRevision() {
      return this.updateByRevision;
   }

   public void setUpdateByRevision(String var1) {
      this.updateByRevision = getTrimmedString(var1);
   }

   public KeywordSubstitutionOptions getKeywordSubst() {
      return this.keywordSubst;
   }

   public void setKeywordSubst(KeywordSubstitutionOptions var1) {
      this.keywordSubst = var1;
   }

   public File createTempFile(String var1) throws IOException {
      File var2 = File.createTempFile("cvs", ".dff", this.getGlobalOptions().getTempDir());
      return var2;
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("update ");
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

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.isPipeToOutput()) {
         var1.append("-p ");
      }

      if (this.isCleanCopy()) {
         var1.append("-C ");
      }

      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.isBuildDirectories()) {
         var1.append("-d ");
      }

      if (this.isPruneDirectories()) {
         var1.append("-P ");
      }

      if (this.isResetStickyOnes()) {
         var1.append("-A ");
      }

      if (this.isUseHeadIfNotFound()) {
         var1.append("-f ");
      }

      if (this.getKeywordSubst() != null) {
         var1.append("-k");
         var1.append(this.getKeywordSubst().toString());
         var1.append(' ');
      }

      if (this.getUpdateByRevision() != null) {
         var1.append("-r ");
         var1.append(this.getUpdateByRevision());
         var1.append(' ');
      }

      if (this.getUpdateByDate() != null) {
         var1.append("-D ");
         var1.append(this.getUpdateByDate());
         var1.append(' ');
      }

      if (this.getMergeRevision1() != null) {
         var1.append("-j ");
         var1.append(this.getMergeRevision1());
         var1.append(' ');
         if (this.getMergeRevision2() != null) {
            var1.append("-j ");
            var1.append(this.getMergeRevision2());
            var1.append(' ');
         }
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'C') {
         this.setCleanCopy(true);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'd') {
         this.setBuildDirectories(true);
      } else if (var1 == 'P') {
         this.setPruneDirectories(true);
      } else if (var1 == 'A') {
         this.setResetStickyOnes(true);
      } else if (var1 == 'f') {
         this.setUseHeadIfNotFound(true);
      } else if (var1 == 'D') {
         this.setUpdateByDate(var2.trim());
      } else if (var1 == 'r') {
         this.setUpdateByRevision(var2.trim());
      } else if (var1 == 'k') {
         KeywordSubstitutionOptions var3 = KeywordSubstitutionOptions.findKeywordSubstOption(var2);
         this.setKeywordSubst(var3);
      } else if (var1 == 'p') {
         this.setPipeToOutput(true);
      } else {
         if (var1 != 'j') {
            return false;
         }

         if (this.getMergeRevision1() == null) {
            this.setMergeRevision1(var2);
         } else {
            this.setMergeRevision2(var2);
         }
      }

      return true;
   }

   public void resetCVSCommand() {
      this.setRecursive(true);
      this.setCleanCopy(false);
      this.setBuildDirectories(false);
      this.setPruneDirectories(false);
      this.setResetStickyOnes(false);
      this.setUseHeadIfNotFound(false);
      this.setUpdateByDate((String)null);
      this.setUpdateByRevision((String)null);
      this.setKeywordSubst((KeywordSubstitutionOptions)null);
      this.setPipeToOutput(false);
      this.setMergeRevision1((String)null);
      this.setMergeRevision2((String)null);
   }

   public void messageSent(MessageEvent var1) {
      super.messageSent(var1);
      if (this.pruneDirectories) {
         String var2 = CommandUtils.getExaminedDirectory(var1.getMessage(), ": Updating");
         if (var2 != null) {
            if (!var2.equals(".")) {
               this.emptyDirectories.add(new File(this.getLocalDirectory(), var2));
            }
         }
      }
   }

   private boolean pruneEmptyDirectory(File var1, ClientServices var2) throws IOException {
      File[] var3 = var1.listFiles();
      if (var3 == null) {
         return true;
      } else {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].isFile()) {
               return false;
            }

            if (!var3[var4].getName().equals("CVS") && !this.pruneEmptyDirectory(var3[var4], var2)) {
               return false;
            }
         }

         if ((new File(var1, "CVS/Entries")).isFile() && (new File(var1, "CVS/Repository")).isFile()) {
            File var7 = new File(var1, "CVS");
            Iterator var5 = this.clientServices.getEntries(var1);

            Entry var6;
            do {
               if (!var5.hasNext()) {
                  this.deleteRecursively(var7);
                  var1.delete();
                  if (!var2.exists(var1)) {
                     var2.removeEntry(var1);
                  }

                  return true;
               }

               var6 = (Entry)var5.next();
            } while(var6.getName() == null || !var6.isUserFileToBeRemoved());

            return false;
         } else {
            return false;
         }
      }
   }

   private void deleteRecursively(File var1) {
      File[] var2 = var1.listFiles();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         if (var4.isDirectory()) {
            this.deleteRecursively(var4);
         } else {
            var4.delete();
         }
      }

      var1.delete();
   }

   private void pruneEmptyDirectories(ClientServices var1) throws IOException {
      Iterator var2 = this.emptyDirectories.iterator();

      while(var2.hasNext()) {
         File var3 = (File)var2.next();
         if (var3.exists()) {
            this.pruneEmptyDirectory(var3, var1);
         }
      }

      this.emptyDirectories.clear();
   }

   public String getOptString() {
      return "RCnldPAfD:r:pj:k:";
   }

   public String getMergeRevision1() {
      return this.mergeRevision1;
   }

   public void setMergeRevision1(String var1) {
      this.mergeRevision1 = getTrimmedString(var1);
   }

   public String getMergeRevision2() {
      return this.mergeRevision2;
   }

   public void setMergeRevision2(String var1) {
      this.mergeRevision2 = getTrimmedString(var1);
   }
}
