package org.netbeans.lib.cvsclient.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.admin.DateComparator;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.request.ArgumentRequest;
import org.netbeans.lib.cvsclient.request.DirectoryRequest;
import org.netbeans.lib.cvsclient.request.EntryRequest;
import org.netbeans.lib.cvsclient.request.ModifiedRequest;
import org.netbeans.lib.cvsclient.request.QuestionableRequest;
import org.netbeans.lib.cvsclient.request.Request;
import org.netbeans.lib.cvsclient.request.RootRequest;
import org.netbeans.lib.cvsclient.request.StickyRequest;
import org.netbeans.lib.cvsclient.request.UnchangedRequest;

public abstract class BasicCommand extends BuildableCommand {
   protected List requests = new LinkedList();
   protected ClientServices clientServices;
   private boolean recursive = true;
   protected File[] files;

   /** @deprecated */
   public boolean getRecursive() {
      return this.recursive;
   }

   public boolean isRecursive() {
      return this.recursive;
   }

   public void setRecursive(boolean var1) {
      this.recursive = var1;
   }

   public void setFiles(File[] var1) {
      if (var1 == null) {
         this.files = var1;
      } else {
         this.files = new File[var1.length];
         int var2 = 0;
         int var3 = 0;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var1[var5];
            if (var6.isDirectory()) {
               this.files[var4 - (1 + var3)] = var6;
               ++var3;
            } else {
               this.files[var2] = var6;
               ++var2;
            }
         }

      }
   }

   public File[] getFiles() {
      return this.files;
   }

   public File getXthFile(int var1) {
      if (var1 >= 0 && var1 < this.files.length) {
         File var2 = this.files[var1];
         return !var2.isFile() ? null : var2;
      } else {
         return null;
      }
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

   private void addRequests(File var1) throws FileNotFoundException, IOException, CommandAbortedException {
      if (var1 == null) {
         throw new IllegalArgumentException("Cannot add requests for a null path.");
      } else {
         if (var1.exists() && !var1.isFile()) {
            this.addRequestsForDirectory(var1);
         } else {
            this.addRequestsForFile(var1);
         }

      }
   }

   protected boolean doesCheckFileTime() {
      return true;
   }

   protected void sendEntryAndModifiedRequests(Entry var1, File var2) {
      if (var1 != null) {
         if (var2 == null || var2.exists() || !var1.isNewUserFile()) {
            Date var3 = var1.getLastModified();
            boolean var4 = var1.hadConflicts();
            if (!var4) {
               var1.setConflict((String)null);
            } else if (this.fileRequiresConflictResolution(var2, var1)) {
               Entry var5 = new Entry(var1.toString());
               var5.setConflict("+=");
               var1 = var5;
            }

            this.addRequest(new EntryRequest(var1));
            if (var2 != null && var2.exists() && !var1.isUserFileToBeRemoved()) {
               if (this.doesCheckFileTime() && !var4 && var3 != null && DateComparator.getInstance().equals(var2.lastModified(), var3.getTime())) {
                  this.addRequest(new UnchangedRequest(var2.getName()));
               } else {
                  this.addRequest(new ModifiedRequest(var2, var1.isBinary()));
               }
            }
         }
      }
   }

   private final boolean fileRequiresConflictResolution(File var1, Entry var2) {
      if (var1 == null) {
         return false;
      } else {
         boolean var3 = false;
         if (var2.hadConflicts()) {
            long var4 = var2.getLastModified().getTime() / 1000L;
            long var6 = var1.lastModified() / 1000L;
            var3 = var6 <= var4;
         }

         return var3;
      }
   }

   protected void addRequestsForDirectory(File var1) throws IOException, CommandAbortedException {
      if (this.clientServices.exists(var1)) {
         if (this.clientServices.isAborted()) {
            throw new CommandAbortedException("Command aborted during request generation", "Command aborted during request generation");
         } else {
            this.addDirectoryRequest(var1);
            File[] var2 = var1.listFiles();
            ArrayList var3;
            if (var2 == null) {
               var3 = new ArrayList(0);
            } else {
               var3 = new ArrayList(Arrays.asList(var2));
               var3.remove(new File(var1, "CVS"));
            }

            LinkedList var4 = null;
            if (this.isRecursive()) {
               var4 = new LinkedList();
            }

            Iterator var5;
            File var7;
            for(var5 = this.clientServices.getEntries(var1); var5.hasNext(); var3.remove(var7)) {
               Entry var6 = (Entry)var5.next();
               var7 = new File(var1, var6.getName());
               if (var6.isDirectory()) {
                  if (this.isRecursive()) {
                     var4.add(new File(var1, var6.getName()));
                  }
               } else {
                  this.addRequestForFile(var7, var6);
               }
            }

            if (this.isRecursive() && !(new File(var1, "CVS")).exists()) {
               File[] var8 = var1.listFiles();
               if (var8 != null) {
                  for(int var9 = 0; var9 < var8.length; ++var9) {
                     if (var8[var9].isDirectory() && (new File(var8[var9], "CVS")).exists()) {
                        var4.add(var8[var9]);
                     }
                  }
               }
            }

            var5 = var3.iterator();

            while(var5.hasNext()) {
               String var10 = ((File)var5.next()).getName();
               if (!this.clientServices.shouldBeIgnored(var1, var10)) {
                  this.addRequest(new QuestionableRequest(var10));
               }
            }

            if (this.isRecursive()) {
               var5 = var4.iterator();

               while(var5.hasNext()) {
                  File var11 = (File)var5.next();
                  var7 = new File(var11, "CVS");
                  if (this.clientServices.exists(var7)) {
                     this.addRequestsForDirectory(var11);
                  }
               }
            }

         }
      }
   }

   protected void addRequestForFile(File var1, Entry var2) {
      this.sendEntryAndModifiedRequests(var2, var1);
   }

   protected void addRequestsForFile(File var1) throws IOException {
      this.addDirectoryRequest(var1.getParentFile());

      try {
         Entry var2 = this.clientServices.getEntry(var1);
         if (var2 != null) {
            this.addRequestForFile(var1, var2);
         } else if (var1.exists()) {
            boolean var3 = false;
            this.addRequest(new ModifiedRequest(var1, var3));
         }
      } catch (IOException var4) {
         System.err.println("An error occurred getting the Entry for file " + var1 + ": " + var4);
         var4.printStackTrace();
      }

   }

   protected final void addDirectoryRequest(File var1) {
      String var2 = this.getRelativeToLocalPathInUnixStyle(var1);

      try {
         String var3 = this.clientServices.getRepositoryForDirectory(var1.getAbsolutePath());
         this.addRequest(new DirectoryRequest(var2, var3));
         String var4 = this.clientServices.getStickyTagForDirectory(var1);
         if (var4 != null) {
            this.addRequest(new StickyRequest(var4));
         }
      } catch (FileNotFoundException var5) {
      } catch (IOException var6) {
         System.err.println("An error occurred reading the respository for the directory " + var2 + ": " + var6);
         var6.printStackTrace();
      }

   }

   protected void addArgumentRequests() {
      if (this.files != null) {
         for(int var1 = 0; var1 < this.files.length; ++var1) {
            File var2 = this.files[var1];
            String var3 = this.getRelativeToLocalPathInUnixStyle(var2);
            this.addRequest(new ArgumentRequest(var3));
         }

      }
   }

   public void execute(ClientServices var1, EventManager var2) throws CommandException, AuthenticationException {
      this.requests.clear();
      this.clientServices = var1;
      super.execute(var1, var2);
      if (var1.isFirstCommand()) {
         this.addRequest(new RootRequest(var1.getRepository()));
      }

      this.addFileRequests();
   }

   private void addFileRequests() throws CommandException {
      try {
         if (this.files != null && this.files.length > 0) {
            for(int var1 = 0; var1 < this.files.length; ++var1) {
               this.addRequests(this.files[var1]);
            }
         } else if (this.assumeLocalPathWhenUnspecified()) {
            this.addRequests(new File(this.getLocalDirectory()));
         }

      } catch (Exception var2) {
         throw new CommandException(var2, var2.getLocalizedMessage());
      }
   }

   protected boolean assumeLocalPathWhenUnspecified() {
      return true;
   }

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

   protected final void appendFileArguments(StringBuffer var1) {
      File[] var2 = this.getFiles();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (var3 > 0) {
               var1.append(' ');
            }

            var1.append(var2[var3].getName());
         }

      }
   }
}
