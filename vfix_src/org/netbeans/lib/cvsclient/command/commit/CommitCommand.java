package org.netbeans.lib.cvsclient.command.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.ArgumentxRequest;
import org.netbeans.lib.cvsclient.request.CommandRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.EntryRequest;
import org.netbeans.lib.cvsclient.request.StickyRequest;

public class CommitCommand extends BasicCommand {
   private final List argumentRequests = new LinkedList();
   private String message;
   private boolean forceCommit;
   private String logMessageFromFile;
   private boolean noModuleProgram;
   private String toRevisionOrBranch;

   public CommitCommand() {
      this.resetCVSCommand();
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   public boolean isForceCommit() {
      return this.forceCommit;
   }

   public void setForceCommit(boolean var1) {
      this.forceCommit = var1;
   }

   protected void addRequestsForDirectory(File var1) throws IOException {
      if (var1.exists()) {
         String var2 = this.getRelativeToLocalPathInUnixStyle(var1);

         try {
            String var3 = this.clientServices.getRepositoryForDirectory(var1.getAbsolutePath());
            this.requests.add(new DirectoryRequest(var2, var3));
            String var4 = this.clientServices.getStickyTagForDirectory(var1);
            if (var4 != null) {
               this.requests.add(new StickyRequest(var4));
            }
         } catch (IOException var11) {
            System.err.println("An error occurred reading the respository for the directory " + var2 + ": " + var11);
            var11.printStackTrace();
         }

         Set var12 = this.clientServices.getAllFiles(var1);
         File[] var13 = var1.listFiles();
         var12.addAll(Arrays.asList(var13));
         LinkedList var5 = null;
         if (this.isRecursive()) {
            var5 = new LinkedList();
         }

         Iterator var6 = var12.iterator();

         File var7;
         while(var6.hasNext()) {
            var7 = (File)var6.next();
            if (!var7.getName().equals("CVS")) {
               try {
                  Entry var8 = this.clientServices.getEntry(var7);
                  if (var8 != null) {
                     if (var7.isFile()) {
                        this.sendEntryAndModifiedRequests(var8, var7);
                     } else if (this.isRecursive() && var7.isDirectory()) {
                        File var9 = new File(var7, "CVS");
                        if (var9.exists()) {
                           var5.add(var7);
                        }
                     }
                  }
               } catch (IOException var10) {
                  System.err.println("An error occurred getting the Entry for file " + var7 + ": " + var10);
                  var10.printStackTrace();
               }
            }
         }

         if (this.isRecursive()) {
            var6 = var5.iterator();

            while(var6.hasNext()) {
               var7 = (File)var6.next();
               this.addRequestsForDirectory(var7);
            }
         }

      }
   }

   protected void addRequestsForFile(File var1) throws IOException {
      File var2 = var1.getParentFile();
      String var3 = this.getRelativeToLocalPathInUnixStyle(var2);

      try {
         this.requests.add(new DirectoryRequest(var3, this.clientServices.getRepositoryForDirectory(var2.getAbsolutePath())));
         String var4 = this.clientServices.getStickyTagForDirectory(var2);
         if (var4 != null) {
            this.requests.add(new StickyRequest(var4));
         }
      } catch (IOException var6) {
         System.err.println("An error occurred reading the respository for the directory " + var3 + ": " + var6);
         var6.printStackTrace();
      }

      try {
         Entry var7 = this.clientServices.getEntry(var1);
         if (var7 != null) {
            this.sendEntryAndModifiedRequests(var7, var1);
         }
      } catch (IOException var5) {
         System.err.println("An error occurred getting the Entry for file " + var1 + ": " + var5);
         var5.printStackTrace();
      }

   }

   protected boolean doesCheckFileTime() {
      return !this.isForceCommit();
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      var1.ensureConnection();
      super.execute(var1, var2);

      try {
         if (this.isForceCommit()) {
            this.requests.add(1, new ArgumentRequest("-f"));
            if (this.isRecursive()) {
               this.requests.add(1, new ArgumentRequest("-R"));
            }
         }

         if (this.isNoModuleProgram()) {
            this.requests.add(1, new ArgumentRequest("-n"));
         }

         if (this.getToRevisionOrBranch() != null) {
            this.requests.add(1, new ArgumentRequest("-r"));
            this.requests.add(2, new ArgumentRequest(this.getToRevisionOrBranch()));
         }

         String var3 = this.getMessage();
         if (this.getLogMessageFromFile() != null) {
            var3 = this.loadLogFile(this.getLogMessageFromFile());
         }

         if (var3 != null) {
            var3 = var3.trim();
         }

         if (var3 == null || var3.length() == 0) {
            var3 = "no message";
         }

         this.addMessageRequest(var3);
         this.addRequestForWorkingDirectory(var1);
         this.requests.addAll(this.argumentRequests);
         this.argumentRequests.clear();
         this.addArgumentRequests();
         this.requests.add(CommandRequest.COMMIT);
         var1.processRequests(this.requests);
      } catch (CommandException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new CommandException(var9, var9.getLocalizedMessage());
      } finally {
         this.requests.clear();
      }

   }

   protected void addArgumentRequests() {
      if (this.isForceCommit()) {
         Iterator var1 = this.requests.iterator();
         String var2 = "";
         LinkedList var3 = new LinkedList();

         while(var1.hasNext()) {
            Object var4 = var1.next();
            if (var4 instanceof DirectoryRequest) {
               DirectoryRequest var5 = (DirectoryRequest)var4;
               var2 = var5.getLocalDirectory();
            } else if (var4 instanceof EntryRequest) {
               EntryRequest var7 = (EntryRequest)var4;
               String var6 = null;
               if (var2.length() == 0) {
                  var6 = var7.getEntry().getName();
               } else {
                  var6 = var2 + '/' + var7.getEntry().getName();
               }

               var3.add(new ArgumentRequest(var6));
            }
         }

         var1 = var3.iterator();

         while(var1.hasNext()) {
            this.requests.add(var1.next());
         }
      } else {
         super.addArgumentRequests();
      }

   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer("commit ");
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
      if (var1 == 'm') {
         this.setMessage(var2);
      } else if (var1 == 'l') {
         this.setRecursive(false);
      } else if (var1 == 'R') {
         this.setRecursive(true);
      } else if (var1 == 'f') {
         this.setForceCommit(true);
      } else if (var1 == 'F') {
         this.setLogMessageFromFile(var2);
      } else if (var1 == 'r') {
         this.setToRevisionOrBranch(var2);
      } else {
         if (var1 != 'n') {
            return false;
         }

         this.setNoModuleProgram(true);
      }

      return true;
   }

   public String getOptString() {
      return "m:flRnF:r:";
   }

   public Builder createBuilder(EventManager var1) {
      return new CommitBuilder(var1, this.getLocalDirectory(), this.clientServices.getRepository());
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

   public String getLogMessageFromFile() {
      return this.logMessageFromFile;
   }

   public void setLogMessageFromFile(String var1) {
      this.logMessageFromFile = var1;
   }

   public boolean isNoModuleProgram() {
      return this.noModuleProgram;
   }

   public void setNoModuleProgram(boolean var1) {
      this.noModuleProgram = var1;
   }

   public String getToRevisionOrBranch() {
      return this.toRevisionOrBranch;
   }

   public void setToRevisionOrBranch(String var1) {
      this.toRevisionOrBranch = var1;
   }

   private String loadLogFile(String var1) throws CommandException {
      StringBuffer var2 = new StringBuffer();
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new FileReader(var1));

         String var4;
         while((var4 = var3.readLine()) != null) {
            var2.append(var4 + "\n");
         }
      } catch (FileNotFoundException var14) {
         throw new CommandException(var14, CommandException.getLocalMessage("CommitCommand.logInfoFileNotExists", new Object[]{var1}));
      } catch (IOException var15) {
         throw new CommandException(var15, CommandException.getLocalMessage("CommitCommand.errorReadingLogFile", new Object[]{var1}));
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var13) {
            }
         }

      }

      return var2.toString();
   }

   public void resetCVSCommand() {
      this.setMessage((String)null);
      this.setRecursive(true);
      this.setForceCommit(false);
      this.setLogMessageFromFile((String)null);
      this.setNoModuleProgram(false);
      this.setToRevisionOrBranch((String)null);
   }

   public String getCVSArguments() {
      StringBuffer var1 = new StringBuffer();
      if (!this.isRecursive()) {
         var1.append("-l ");
      }

      if (this.isForceCommit()) {
         var1.append("-f ");
         if (this.isRecursive()) {
            var1.append("-R ");
         }
      }

      if (this.isNoModuleProgram()) {
         var1.append("-n ");
      }

      if (this.getToRevisionOrBranch() != null) {
         var1.append("-r ");
         var1.append(this.getToRevisionOrBranch() + " ");
      }

      if (this.getLogMessageFromFile() != null) {
         var1.append("-F ");
         var1.append(this.getLogMessageFromFile());
         var1.append(" ");
      }

      if (this.getMessage() != null) {
         var1.append("-m \"");
         var1.append(this.getMessage());
         var1.append("\" ");
      }

      return var1.toString();
   }
}
