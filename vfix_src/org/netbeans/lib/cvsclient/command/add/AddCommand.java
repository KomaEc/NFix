package org.netbeans.lib.cvsclient.command.add;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BuildableCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.command.WrapperUtils;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.ArgumentxRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.EntryRequest;
import org.netbeans.lib.cvsclient.request.IsModifiedRequest;
import org.netbeans.lib.cvsclient.request.KoptRequest;
import org.netbeans.lib.cvsclient.request.RootRequest;
import org.netbeans.lib.cvsclient.request.StickyRequest;
import org.netbeans.lib.cvsclient.util.SimpleStringPattern;

public class AddCommand extends BuildableCommand {
   private static final String DIR_ADDED = " added to the repository";
   private static final String DIRECTORY = "Directory ";
   private List requests;
   private final List argumentRequests = new LinkedList();
   private final List newDirList = new LinkedList();
   private ClientServices clientServices;
   private File[] files;
   private String message;
   private KeywordSubstitutionOptions keywordSubst;
   private Map wrapperMap;
   private HashMap dir2WrapperMap = new HashMap(16);
   private static final Map EMPTYWRAPPER = new HashMap(1);

   public AddCommand() {
      this.resetCVSCommand();
   }

   public void setFiles(File[] var1) {
      this.files = var1;
      if (var1 != null) {
         this.files = new File[var1.length];
         int var2 = 0;
         int var3 = 0;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var1[var5];
            if (var6.isDirectory()) {
               this.files[var2] = var6;
               ++var2;
            } else {
               this.files[var4 - (1 + var3)] = var6;
               ++var3;
            }
         }

      }
   }

   public File[] getFiles() {
      return this.files;
   }

   public File getFileEndingWith(String var1) {
      String var2 = var1.replace('\\', '/');
      String var3 = this.getLocalDirectory().replace('\\', '/');
      boolean var4 = false;

      for(int var7 = 0; var7 < this.files.length; ++var7) {
         String var5 = this.files[var7].getAbsolutePath();
         String var6 = this.files[var7].getParentFile().getAbsolutePath().replace('\\', '/');
         var5 = var5.replace('\\', '/');
         if (var5.endsWith(var2) && var2.indexOf(47) >= 0 || this.files[var7].getName().equals(var2) && var6.equals(var3)) {
            return this.files[var7];
         }
      }

      return null;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public KeywordSubstitutionOptions getKeywordSubst() {
      return this.keywordSubst;
   }

   public void setKeywordSubst(KeywordSubstitutionOptions var1) {
      this.keywordSubst = var1;
   }

   protected void addRequests(File var1) throws IOException, CommandException {
      if (var1.isDirectory()) {
         this.addRequestsForDirectory(var1, false);
      } else {
         this.addRequestsForFile(var1);
      }

   }

   private void addRequestsForDirectory(File var1, boolean var2) throws IOException {
      File var3 = var1.getParentFile();
      String var4 = var2 ? this.getRelativeToLocalPathInUnixStyle(var1) : this.getRelativeToLocalPathInUnixStyle(var3);
      String var5;
      if (var4.equals(".")) {
         var5 = var1.getName();
      } else {
         var5 = var4 + "/" + var1.getName();
         this.addRequestsForDirectory(var3, true);
      }

      if (var2) {
         var5 = var4;
      }

      String var6;
      String var7;
      if (var2) {
         var6 = this.clientServices.getRepositoryForDirectory(var1.getAbsolutePath());
         var7 = this.clientServices.getStickyTagForDirectory(var1);
      } else {
         var6 = this.clientServices.getRepositoryForDirectory(var3.getAbsolutePath());
         if (var6.endsWith(".")) {
            var6 = var6.substring(0, var6.length() - 1) + var1.getName();
         } else {
            var6 = var6 + "/" + var1.getName();
         }

         var7 = this.clientServices.getStickyTagForDirectory(var3);
      }

      this.requests.add(new DirectoryRequest(var5, var6));
      if (var7 != null) {
         this.requests.add(new StickyRequest(var7));
      }

      if (!var2) {
         this.argumentRequests.add(new ArgumentRequest(var5));
         this.newDirList.add(new AddCommand.Paths(var5, var6));
      }

   }

   protected void addRequestsForFile(File var1) throws IOException, CommandException {
      File var2 = var1.getParentFile();
      String var3 = this.getRelativeToLocalPathInUnixStyle(var2);
      String var4 = this.clientServices.getRepositoryForDirectory(var2.getAbsolutePath());
      this.requests.add(new DirectoryRequest(var3, var4));
      String var5 = this.clientServices.getStickyTagForDirectory(var2);
      if (var5 != null) {
         this.requests.add(new StickyRequest(var5));
      }

      Entry var6 = this.clientServices.getEntry(var1);
      if (var6 != null) {
         this.requests.add(new EntryRequest(var6));
      } else {
         Object var7 = (Map)this.dir2WrapperMap.get(var3);
         if (var7 == null) {
            File var8 = new File(var2, ".cvswrappers");
            if (var8.exists()) {
               var7 = new HashMap(5);
               WrapperUtils.readWrappersFromFile(var8, (Map)var7);
            } else {
               var7 = EMPTYWRAPPER;
            }

            this.dir2WrapperMap.put(var3, var7);
         }

         boolean var9 = this.isBinary(this.clientServices, var1.getName(), (Map)var7);
         if (var9) {
            this.requests.add(new KoptRequest("-kb"));
         }

         this.requests.add(new IsModifiedRequest(var1));
      }

      if (var3.equals(".")) {
         this.argumentRequests.add(new ArgumentRequest(var1.getName()));
      } else {
         this.argumentRequests.add(new ArgumentRequest(var3 + "/" + var1.getName()));
      }

   }

   private boolean isBinary(ClientServices var1, String var2, Map var3) throws CommandException {
      KeywordSubstitutionOptions var4 = this.getKeywordSubst();
      if (var4 == KeywordSubstitutionOptions.BINARY) {
         return true;
      } else {
         boolean var5 = false;
         if (this.wrapperMap == null) {
            this.wrapperMap = WrapperUtils.mergeWrapperMap(var1);
         }

         Iterator var6 = this.wrapperMap.keySet().iterator();

         SimpleStringPattern var7;
         while(var6.hasNext()) {
            var7 = (SimpleStringPattern)var6.next();
            if (var7.doesMatch(var2)) {
               var4 = (KeywordSubstitutionOptions)this.wrapperMap.get(var7);
               var5 = true;
               break;
            }
         }

         if (!var5 && var3 != null && var3 != EMPTYWRAPPER) {
            var6 = var3.keySet().iterator();

            while(var6.hasNext()) {
               var7 = (SimpleStringPattern)var6.next();
               if (var7.doesMatch(var2)) {
                  var4 = (KeywordSubstitutionOptions)var3.get(var7);
                  var5 = true;
                  break;
               }
            }
         }

         return var4 == KeywordSubstitutionOptions.BINARY;
      }
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      if (this.files != null && this.files.length != 0) {
         var1.ensureConnection();
         this.clientServices = var1;
         this.setLocalDirectory(var1.getLocalPath());
         String var3 = var1.getLocalPath();
         File var4 = new File(var3, "CVS");
         if (!var4.isDirectory()) {
            MessageEvent var14 = new MessageEvent(this, "cvs [add aborted]: there is no version here; do 'cvs checkout' first", true);
            this.messageSent(var14);
            var2.fireCVSEvent(var14);
         } else {
            this.newDirList.clear();
            super.execute(var1, var2);
            this.requests = new LinkedList();
            if (var1.isFirstCommand()) {
               this.requests.add(new RootRequest(var1.getRepository()));
            }

            String var5 = this.getMessage();
            if (var5 != null) {
               var5 = var5.trim();
            }

            if (var5 != null && var5.length() > 0) {
               this.addMessageRequest(var5);
            }

            if (this.getKeywordSubst() != null && !this.getKeywordSubst().equals("")) {
               this.requests.add(new ArgumentRequest("-k" + this.getKeywordSubst()));
            }

            try {
               for(int var6 = 0; var6 < this.files.length; ++var6) {
                  this.addRequests(this.files[var6]);
               }

               this.requests.add(new DirectoryRequest(".", var1.getRepositoryForDirectory(this.getLocalDirectory())));
               this.requests.addAll(this.argumentRequests);
               this.argumentRequests.clear();
               this.requests.add(CommandRequest.ADD);
               var1.processRequests(this.requests);
            } catch (CommandException var11) {
               throw var11;
            } catch (Exception var12) {
               throw new CommandException(var12, var12.getLocalizedMessage());
            } finally {
               this.requests.clear();
            }
         }
      } else {
         throw new CommandException("No files have been specified for adding.", CommandException.getLocalMessage("AddCommand.noFilesSpecified", (Object[])null));
      }
   }

   private void addMessageRequest(String var1) {
      this.requests.add(new ArgumentRequest("-m"));
      StringTokenizer var2 = new StringTokenizer(var1, "\n", false);
      boolean var3 = true;

      while(var2.hasMoreTokens()) {
         if (var3) {
            this.requests.add(new ArgumentRequest(var2.nextToken()));
            var3 = false;
         } else {
            this.requests.add(new ArgumentxRequest(var2.nextToken()));
         }
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("add ");
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

   public Builder createBuilder(EventManager var1) {
      return new AddBuilder(var1, this);
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'm') {
         this.setMessage(var2);
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
      return "m:k:";
   }

   public void messageSent(MessageEvent var1) {
      String var2 = var1.getMessage();
      if (var2.endsWith(" added to the repository")) {
         var2 = var2.substring("Directory ".length(), var2.indexOf(" added to the repository")).trim();
         this.createCvsFiles(var2);
      }

      super.messageSent(var1);
   }

   private void createCvsFiles(String var1) {
      String var3 = var1;
      if (var1.lastIndexOf(47) >= 0) {
         var3 = var1.substring(var1.lastIndexOf(47) + 1, var1.length());
      }

      if (this.newDirList.size() == 0) {
         System.err.println("JavaCVS: Bug in AddCommand|createCvsFiles");
         System.err.println("         newDirInRepository = " + var1);
      } else {
         AddCommand.Paths var4 = (AddCommand.Paths)this.newDirList.remove(0);
         String var5 = var4.getPartPath();
         String var6 = var4.getRepositoryPath();
         String var2 = var4.getRepositoryPath();
         String var7 = var6;
         if (var6.lastIndexOf(47) >= 0) {
            var7 = var6.substring(var6.lastIndexOf(47) + 1, var6.length());
         }

         if (!var7.equalsIgnoreCase(var3)) {
            System.err.println("JavaCVS: Bug in AddCommand|createCvsFiles");
            System.err.println("         newDirInRepository = " + var1);
            System.err.println("         tempDirName = " + var7);
            System.err.println("         dirName = " + var3);
         } else {
            try {
               if (var2.startsWith(".")) {
                  var2 = var2.substring(1);
               }

               this.clientServices.updateAdminData(var5, var2, (Entry)null);
               this.createCvsTagFile(var5, var2);
            } catch (IOException var9) {
               System.err.println("TODO: couldn't create/update Cvs admin files");
            }

         }
      }
   }

   private void createCvsTagFile(String var1, String var2) throws IOException {
      File var3 = new File(this.getLocalDirectory(), var1);
      File var4 = var3.getParentFile();
      String var5 = this.clientServices.getStickyTagForDirectory(var4);
      if (var5 != null) {
         File var6 = new File(var3, "CVS/Tag");
         var6.createNewFile();
         PrintWriter var7 = new PrintWriter(new BufferedWriter(new FileWriter(var6)));
         var7.println(var5);
         var7.close();
      }

   }

   public void resetCVSCommand() {
      this.setMessage((String)null);
      this.setKeywordSubst((KeywordSubstitutionOptions)null);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer();
      if (this.getMessage() != null) {
         var1.append("-m \"");
         var1.append(this.getMessage());
         var1.append("\" ");
      }

      if (this.getKeywordSubst() != null) {
         var1.append("-k");
         var1.append(this.getKeywordSubst().toString());
         var1.append(" ");
      }

      return var1.toString();
   }

   private static class Paths {
      private final String partPath;
      private final String repositoryPath;

      public Paths(String var1, String var2) {
         this.partPath = var1;
         this.repositoryPath = var2;
      }

      public String getPartPath() {
         return this.partPath;
      }

      public String getRepositoryPath() {
         return this.repositoryPath;
      }
   }
}
