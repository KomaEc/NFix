package org.netbeans.lib.cvsclient.command.importcmd;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.command.BuildableCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.ArgumentxRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.ModifiedRequest;
import org.netbeans.lib.cvsclient.response.WrapperSendResponse;
import org.netbeans.lib.cvsclient.util.SimpleStringPattern;
import org.netbeans.lib.cvsclient.util.StringPattern;

public class ImportCommand extends BuildableCommand {
   private Map wrapperMap = new HashMap();
   private String logMessage;
   private String module;
   private String releaseTag;
   private String vendorBranch;
   private String vendorTag;
   private String importDirectory;
   private KeywordSubstitutionOptions keywordSubstitutionOptions;
   private boolean useFileModifTime;
   private List ignoreList = new LinkedList();
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ImportCommand() {
      this.resetCVSCommand();
   }

   public void addWrapper(String var1, KeywordSubstitutionOptions var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("keywordSubstitutionOptions must not be null");
      } else {
         this.wrapperMap.put(new SimpleStringPattern(var1), var2);
      }
   }

   public void addWrapper(StringPattern var1, KeywordSubstitutionOptions var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("keywordSubstitutionOptions must not be null");
      } else {
         this.wrapperMap.put(var1, var2);
      }
   }

   public void setWrappers(Map var1) {
      this.wrapperMap = var1;
   }

   public Map getWrappers() {
      return this.wrapperMap;
   }

   public KeywordSubstitutionOptions getKeywordSubstitutionOptions() {
      return this.keywordSubstitutionOptions;
   }

   public void setKeywordSubstitutionOptions(KeywordSubstitutionOptions var1) {
      this.keywordSubstitutionOptions = var1;
   }

   public String getReleaseTag() {
      return this.releaseTag;
   }

   public void setReleaseTag(String var1) {
      this.releaseTag = getTrimmedString(var1);
   }

   public String getLogMessage() {
      return this.logMessage;
   }

   public void setLogMessage(String var1) {
      this.logMessage = getTrimmedString(var1);
   }

   public String getModule() {
      return this.module;
   }

   public void setModule(String var1) {
      this.module = getTrimmedString(var1);
   }

   public void setImportDirectory(String var1) {
      this.importDirectory = var1;
   }

   public String getImportDirectory() {
      return this.importDirectory;
   }

   public String getVendorBranch() {
      return this.vendorBranch;
   }

   private String getVendorBranchNotNull() {
      return this.vendorBranch == null ? "1.1.1" : this.vendorBranch;
   }

   public void setVendorBranch(String var1) {
      this.vendorBranch = getTrimmedString(var1);
   }

   public String getVendorTag() {
      return this.vendorTag;
   }

   public void setVendorTag(String var1) {
      this.vendorTag = getTrimmedString(var1);
   }

   public boolean isUseFileModifTime() {
      return this.useFileModifTime;
   }

   public void setUseFileModifTime(boolean var1) {
      this.useFileModifTime = var1;
   }

   public List getIgnoreFiles() {
      return Collections.unmodifiableList(this.ignoreList);
   }

   public void addIgnoredFile(String var1) {
      this.ignoreList.add(var1);
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      String var10;
      if (this.getLogMessage() == null) {
         var10 = CommandException.getLocalMessage("ImportCommand.messageEmpty");
         throw new CommandException("message may not be null nor empty", var10);
      } else if (this.getModule() == null) {
         var10 = CommandException.getLocalMessage("ImportCommand.moduleEmpty");
         throw new CommandException("module may not be null nor empty", var10);
      } else if (this.getReleaseTag() == null) {
         var10 = CommandException.getLocalMessage("ImportCommand.releaseTagEmpty");
         throw new CommandException("release tag may not be null nor empty", var10);
      } else if (this.getVendorTag() == null) {
         var10 = CommandException.getLocalMessage("ImportCommand.vendorTagEmpty");
         throw new CommandException("vendor tag may not be null nor empty", var10);
      } else {
         var1.ensureConnection();
         HashMap var3 = new HashMap(var1.getWrappersMap());
         var3.putAll(this.getWrappers());
         this.setWrappers(var3);
         super.execute(var1, var2);
         if (!$assertionsDisabled && this.getLocalDirectory() == null) {
            throw new AssertionError("local directory may not be null");
         } else {
            ArrayList var4 = new ArrayList();

            try {
               var4.add(new ArgumentRequest("-b"));
               var4.add(new ArgumentRequest(this.getVendorBranchNotNull()));
               if (this.getKeywordSubstitutionOptions() != null) {
                  var4.add(new ArgumentRequest("-k"));
                  var4.add(new ArgumentRequest(this.getKeywordSubstitutionOptions().toString()));
               }

               this.addMessageRequests(var4, this.getLogMessage());
               this.addWrapperRequests(var4, this.wrapperMap);
               if (this.isUseFileModifTime()) {
                  var4.add(new ArgumentRequest("-d"));
               }

               for(int var5 = 0; var5 < this.ignoreList.size(); ++var5) {
                  var4.add(new ArgumentRequest("-I"));
                  var4.add(new ArgumentRequest((String)this.ignoreList.get(var5)));
               }

               var4.add(new ArgumentRequest(this.getModule()));
               var4.add(new ArgumentRequest(this.getVendorTag()));
               var4.add(new ArgumentRequest(this.getReleaseTag()));
               this.addFileRequests(new File(this.getLocalDirectory()), var4, var1);
               var4.add(new DirectoryRequest(".", this.getRepositoryRoot(var1)));
               var4.add(CommandRequest.IMPORT);
               var1.processRequests(var4);
            } catch (CommandException var7) {
               throw var7;
            } catch (EOFException var8) {
               String var6 = CommandException.getLocalMessage("CommandException.EndOfFile", (Object[])null);
               throw new CommandException(var8, var6);
            } catch (Exception var9) {
               throw new CommandException(var9, var9.getLocalizedMessage());
            }
         }
      }
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("import ");
      var1.append(this.getCVSArguments());
      String var2;
      if (this.getModule() != null) {
         var1.append(" ");
         var1.append(this.getModule());
      } else {
         var2 = CommandException.getLocalMessage("ImportCommand.moduleEmpty.text");
         var1.append(" ");
         var1.append(var2);
      }

      if (this.getVendorTag() != null) {
         var1.append(" ");
         var1.append(this.getVendorTag());
      } else {
         var2 = CommandException.getLocalMessage("ImportCommand.vendorTagEmpty.text");
         var1.append(" ");
         var1.append(var2);
      }

      if (this.getReleaseTag() != null) {
         var1.append(" ");
         var1.append(this.getReleaseTag());
      } else {
         var2 = CommandException.getLocalMessage("ImportCommand.releaseTagEmpty.text");
         var1.append(" ");
         var1.append(var2);
      }

      return var1.toString();
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer("");
      if (this.getLogMessage() != null) {
         var1.append("-m \"");
         var1.append(this.getLogMessage());
         var1.append("\" ");
      }

      if (this.getKeywordSubstitutionOptions() != null) {
         var1.append("-k");
         var1.append(this.getKeywordSubstitutionOptions().toString());
         var1.append(" ");
      }

      if (this.getVendorBranch() != null) {
         var1.append("-b ");
         var1.append(this.getVendorBranch());
         var1.append(" ");
      }

      if (this.isUseFileModifTime()) {
         var1.append("-d ");
      }

      Iterator var2;
      if (this.wrapperMap.size() > 0) {
         var2 = this.wrapperMap.keySet().iterator();

         while(var2.hasNext()) {
            StringPattern var3 = (StringPattern)var2.next();
            KeywordSubstitutionOptions var4 = (KeywordSubstitutionOptions)this.wrapperMap.get(var3);
            var1.append("-W ");
            var1.append(var3.toString());
            var1.append(" -k '");
            var1.append(var4.toString());
            var1.append("' ");
         }
      }

      var2 = this.ignoreList.iterator();

      while(var2.hasNext()) {
         var1.append("-I ");
         var1.append((String)var2.next());
         var1.append(" ");
      }

      return var1.toString();
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'b') {
         this.setVendorBranch(var2);
      } else if (var1 == 'm') {
         this.setLogMessage(var2);
      } else if (var1 == 'k') {
         this.setKeywordSubstitutionOptions(KeywordSubstitutionOptions.findKeywordSubstOption(var2));
      } else if (var1 == 'W') {
         Map var3 = WrapperSendResponse.parseWrappers(var2);
         Iterator var4 = var3.keySet().iterator();

         while(var4.hasNext()) {
            StringPattern var5 = (StringPattern)var4.next();
            KeywordSubstitutionOptions var6 = (KeywordSubstitutionOptions)var3.get(var5);
            this.addWrapper(var5, var6);
         }
      } else if (var1 == 'd') {
         this.setUseFileModifTime(true);
      } else {
         if (var1 != 'I') {
            return false;
         }

         this.addIgnoredFile(var2);
      }

      return true;
   }

   public void resetCVSCommand() {
      this.setLogMessage((String)null);
      this.setModule((String)null);
      this.setReleaseTag((String)null);
      this.setVendorTag((String)null);
      this.setVendorBranch((String)null);
      this.setUseFileModifTime(false);
      this.ignoreList.clear();
      this.wrapperMap.clear();
   }

   public String getOptString() {
      return "m:W:b:k:dI:";
   }

   private void addMessageRequests(List var1, String var2) {
      var1.add(new ArgumentRequest("-m"));
      StringTokenizer var3 = new StringTokenizer(var2, "\n", false);
      boolean var4 = true;

      while(var3.hasMoreTokens()) {
         if (var4) {
            var1.add(new ArgumentRequest(var3.nextToken()));
            var4 = false;
         } else {
            var1.add(new ArgumentxRequest(var3.nextToken()));
         }
      }

   }

   private void addWrapperRequests(List var1, Map var2) {
      Iterator var3 = var2.keySet().iterator();

      while(var3.hasNext()) {
         StringPattern var4 = (StringPattern)var3.next();
         KeywordSubstitutionOptions var5 = (KeywordSubstitutionOptions)var2.get(var4);
         StringBuffer var6 = new StringBuffer();
         var6.append(var4.toString());
         var6.append(" -k '");
         var6.append(var5.toString());
         var6.append("'");
         var1.add(new ArgumentRequest("-W"));
         var1.add(new ArgumentRequest(var6.toString()));
      }

   }

   private void addFileRequests(File var1, List var2, ClientServices var3) throws IOException {
      String var4 = this.getRelativeToLocalPathInUnixStyle(var1);
      String var5 = this.getRepositoryRoot(var3);
      if (!var4.equals(".")) {
         var5 = var5 + '/' + var4;
      }

      var2.add(new DirectoryRequest(var4, var5));
      File[] var6 = var1.listFiles();
      if (var6 != null) {
         LinkedList var7 = null;

         File var9;
         for(int var8 = 0; var8 < var6.length; ++var8) {
            var9 = var6[var8];
            String var10 = var9.getName();
            if (!var3.shouldBeIgnored(var1, var10)) {
               if (var9.isDirectory()) {
                  if (var7 == null) {
                     var7 = new LinkedList();
                  }

                  var7.add(var9);
               } else {
                  boolean var11 = this.isBinary(var10);
                  var2.add(new ModifiedRequest(var9, var11));
               }
            }
         }

         if (var7 != null) {
            Iterator var12 = var7.iterator();

            while(var12.hasNext()) {
               var9 = (File)var12.next();
               this.addFileRequests(var9, var2, var3);
            }
         }

      }
   }

   private String getRepositoryRoot(ClientServices var1) {
      String var2 = var1.getRepository() + '/' + this.getModule();
      return var2;
   }

   private boolean isBinary(String var1) {
      KeywordSubstitutionOptions var2 = this.getKeywordSubstitutionOptions();
      Iterator var3 = this.wrapperMap.keySet().iterator();

      while(var3.hasNext()) {
         StringPattern var4 = (StringPattern)var3.next();
         if (var4.doesMatch(var1)) {
            var2 = (KeywordSubstitutionOptions)this.wrapperMap.get(var4);
            break;
         }
      }

      return var2 == KeywordSubstitutionOptions.BINARY;
   }

   public Builder createBuilder(EventManager var1) {
      return new ImportBuilder(var1, this);
   }

   static {
      $assertionsDisabled = !ImportCommand.class.desiredAssertionStatus();
   }
}
