package org.netbeans.lib.cvsclient;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.admin.AdminHandler;
import org.netbeans.lib.cvsclient.admin.Entry;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.event.EnhancedMessageEvent;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.file.DefaultFileHandler;
import org.netbeans.lib.cvsclient.file.FileDetails;
import org.netbeans.lib.cvsclient.file.FileHandler;
import org.netbeans.lib.cvsclient.file.GzippedFileHandler;
import org.netbeans.lib.cvsclient.request.ExpandModulesRequest;
import org.netbeans.lib.cvsclient.request.GzipFileContentsRequest;
import org.netbeans.lib.cvsclient.request.Request;
import org.netbeans.lib.cvsclient.request.RootRequest;
import org.netbeans.lib.cvsclient.request.UnconfiguredRequestException;
import org.netbeans.lib.cvsclient.request.UseUnchangedRequest;
import org.netbeans.lib.cvsclient.request.ValidRequestsRequest;
import org.netbeans.lib.cvsclient.request.ValidResponsesRequest;
import org.netbeans.lib.cvsclient.request.WrapperSendRequest;
import org.netbeans.lib.cvsclient.response.ErrorMessageResponse;
import org.netbeans.lib.cvsclient.response.Response;
import org.netbeans.lib.cvsclient.response.ResponseException;
import org.netbeans.lib.cvsclient.response.ResponseFactory;
import org.netbeans.lib.cvsclient.response.ResponseServices;
import org.netbeans.lib.cvsclient.util.BugLog;
import org.netbeans.lib.cvsclient.util.DefaultIgnoreFileFilter;
import org.netbeans.lib.cvsclient.util.IgnoreFileFilter;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;
import org.netbeans.lib.cvsclient.util.Logger;
import org.netbeans.lib.cvsclient.util.StringPattern;

public class Client implements ClientServices, ResponseServices {
   private Connection connection;
   private FileHandler transmitFileHandler;
   private FileHandler gzipFileHandler = new GzippedFileHandler();
   private FileHandler uncompFileHandler = new DefaultFileHandler();
   private boolean dontUseGzipFileHandler;
   private Date modifiedDate;
   private AdminHandler adminHandler;
   private String localPath;
   private boolean isFirstCommand = true;
   private final EventManager eventManager = new EventManager(this);
   private GlobalOptions globalOptions;
   private PrintStream stderr;
   private boolean abort;
   private ResponseFactory responseFactory;
   private IgnoreFileFilter ignoreFileFilter;
   private Map validRequests;
   private Map wrappersMap;
   private boolean initialRequestsSent;
   private boolean printConnectionReuseWarning;
   private static final Set ALLOWED_CONNECTION_REUSE_REQUESTS;
   private LoggedDataInputStream loggedDataInputStream;
   private LoggedDataOutputStream loggedDataOutputStream;
   private boolean warned;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public Client(Connection var1, AdminHandler var2) {
      this.stderr = System.err;
      this.validRequests = new HashMap();
      this.wrappersMap = null;
      this.initialRequestsSent = false;
      this.printConnectionReuseWarning = false;
      this.setConnection(var1);
      this.setAdminHandler(var2);
      this.ignoreFileFilter = new DefaultIgnoreFileFilter();
      this.dontUseGzipFileHandler = false;
   }

   public void setErrorStream(PrintStream var1) {
      this.stderr = var1;
   }

   public Connection getConnection() {
      return this.connection;
   }

   public void setConnection(Connection var1) {
      this.connection = var1;
      this.initialRequestsSent = false;
      this.setIsFirstCommand(true);
   }

   public AdminHandler getAdminHandler() {
      return this.adminHandler;
   }

   public void setAdminHandler(AdminHandler var1) {
      this.adminHandler = var1;
   }

   public String getLocalPath() {
      return this.localPath;
   }

   public void setLocalPath(String var1) {
      for(var1 = var1.replace('\\', '/'); var1.endsWith("/"); var1 = var1.substring(0, var1.length() - 1)) {
      }

      this.localPath = var1;
   }

   public boolean isFirstCommand() {
      return this.isFirstCommand;
   }

   public void setIsFirstCommand(boolean var1) {
      this.isFirstCommand = var1;
   }

   public FileHandler getUncompressedFileHandler() {
      return this.uncompFileHandler;
   }

   public void setUncompressedFileHandler(FileHandler var1) {
      this.uncompFileHandler = var1;
   }

   public FileHandler getGzipFileHandler() {
      return this.gzipFileHandler;
   }

   public void setGzipFileHandler(FileHandler var1) {
      this.gzipFileHandler = var1;
   }

   public void dontUseGzipFileHandler() {
      this.setGzipFileHandler(new DefaultFileHandler());
      this.dontUseGzipFileHandler = true;
   }

   public boolean isAborted() {
      return this.abort;
   }

   public void ensureConnection() throws AuthenticationException {
      BugLog.getInstance().assertNotNull(this.getConnection());
      if (!this.getConnection().isOpen()) {
         final Throwable[] var1 = new Throwable[1];
         final boolean[] var2 = new boolean[]{false};
         Runnable var3 = new Runnable() {
            public void run() {
               try {
                  Client.this.getConnection().open();
                  synchronized(var2) {
                     var2[0] = true;
                  }
               } catch (Throwable var6) {
                  Throwable var1x = var6;
                  synchronized(var1) {
                     var1[0] = var1x;
                  }
               }

            }
         };
         Thread var4 = new Thread(var3, "CVS Server Probe");
         var4.start();

         try {
            var4.join(60000L);
            Throwable var6;
            synchronized(var1) {
               var6 = var1[0];
            }

            if (var6 != null) {
               if (var6 instanceof CommandAbortedException) {
                  this.abort();
                  return;
               }

               if (var6 instanceof AuthenticationException) {
                  throw (AuthenticationException)var6;
               }

               if (var6 instanceof RuntimeException) {
                  throw (RuntimeException)var6;
               }

               if (var6 instanceof Error) {
                  throw (Error)var6;
               }

               if (!$assertionsDisabled) {
                  throw new AssertionError(var6);
               }
            }

            boolean var5;
            synchronized(var2) {
               var5 = var2[0];
            }

            if (!var5) {
               var4.interrupt();
               throw new AuthenticationException("Timeout, no response from server.", "Timeout, no response from server.");
            }
         } catch (InterruptedException var11) {
            var4.interrupt();
            this.abort();
         }

      }
   }

   public void processRequests(List var1) throws IOException, UnconfiguredRequestException, ResponseException, CommandAbortedException {
      if (var1 != null && var1.size() != 0) {
         if (this.abort) {
            throw new CommandAbortedException("Aborted during request processing", CommandException.getLocalMessage("Client.commandAborted", (Object[])null));
         } else {
            this.loggedDataInputStream = null;
            this.loggedDataOutputStream = null;
            boolean var2 = true;
            if (this.isFirstCommand()) {
               this.setIsFirstCommand(false);
               int var3 = 0;
               if (!this.initialRequestsSent) {
                  var3 = this.fillInitialRequests(var1);
                  this.initialRequestsSent = true;
                  var2 = false;
               }

               if (this.globalOptions != null) {
                  Iterator var4 = this.globalOptions.createRequestList().iterator();

                  while(var4.hasNext()) {
                     Request var5 = (Request)var4.next();
                     var1.add(var3++, var5);
                  }

                  if (this.globalOptions.isUseGzip() && this.globalOptions.getCompressionLevel() != 0) {
                     var1.add(var3++, new GzipFileContentsRequest(this.globalOptions.getCompressionLevel()));
                  }
               }
            } else if (this.printConnectionReuseWarning && System.getProperty("javacvs.multiple_commands_warning") == null) {
               System.err.println("WARNING TO DEVELOPERS:");
               System.err.println("Please be warned that attempting to reuse one open connection for more commands is not supported by cvs servers very well.");
               System.err.println("You are advised to open a new Connection each time.");
               System.err.println("If you still want to proceed, please do: System.setProperty(\"javacvs.multiple_commands_warning\", \"false\")");
               System.err.println("That will disable this message.");
            }

            if (!ALLOWED_CONNECTION_REUSE_REQUESTS.contains(var1.get(var1.size() - 1).getClass())) {
               this.printConnectionReuseWarning = true;
            }

            boolean var14 = this.getEventManager().isFireEnhancedEventSet();
            int var13 = 0;
            if (var14) {
               Iterator var15 = var1.iterator();

               while(var15.hasNext()) {
                  Request var6 = (Request)var15.next();
                  FileDetails var7 = var6.getFileForTransmission();
                  if (var7 != null && var7.getFile().exists()) {
                     ++var13;
                  }
               }

               EnhancedMessageEvent var16 = new EnhancedMessageEvent(this, "Requests_Count", new Integer(var13));
               this.getEventManager().fireCVSEvent(var16);
            }

            LoggedDataOutputStream var17 = this.connection.getOutputStream();
            this.loggedDataOutputStream = var17;
            LinkedList var18 = new LinkedList();
            this.transmitFileHandler = this.getUncompressedFileHandler();
            Iterator var19 = var1.iterator();

            while(true) {
               while(var19.hasNext()) {
                  if (this.abort) {
                     throw new CommandAbortedException("Aborted during request processing", CommandException.getLocalMessage("Client.commandAborted", (Object[])null));
                  }

                  Request var8 = (Request)var19.next();
                  if (var8 instanceof GzipFileContentsRequest && this.dontUseGzipFileHandler) {
                     this.stderr.println("Warning: The server is not supporting gzip-file-contents request, no compression is used.");
                  } else {
                     if (var8 instanceof RootRequest) {
                        if (var2) {
                           continue;
                        }

                        var2 = true;
                     }

                     String var9 = var8.getRequestString();
                     var17.writeBytes(var9);
                     var8.modifyOutputStream(this.connection);
                     if (var8.modifiesInputStream()) {
                        var18.add(var8);
                     }

                     var17 = this.connection.getOutputStream();
                     FileDetails var10 = var8.getFileForTransmission();
                     if (var10 != null) {
                        File var11 = var10.getFile();
                        if (var11.exists()) {
                           Logger.logOutput((new String("<Sending file: " + var11.getAbsolutePath() + ">\n")).getBytes("utf8"));
                           EnhancedMessageEvent var12;
                           if (var14) {
                              var12 = new EnhancedMessageEvent(this, "File_Sent_To_Server", var11);
                              this.getEventManager().fireCVSEvent(var12);
                              --var13;
                           }

                           if (var10.isBinary()) {
                              this.transmitFileHandler.transmitBinaryFile(var11, var17);
                           } else {
                              this.transmitFileHandler.transmitTextFile(var11, var17);
                           }

                           if (var14 && var13 == 0) {
                              var12 = new EnhancedMessageEvent(this, "All_Requests_Were_Sent", "Ok");
                              this.getEventManager().fireCVSEvent(var12);
                           }
                        }
                     }

                     if (var8.isResponseExpected()) {
                        var17.flush();
                        Iterator var20 = var18.iterator();

                        while(var20.hasNext()) {
                           System.err.println("Modifying the inputstream...");
                           Request var21 = (Request)var20.next();
                           System.err.println("Request is a: " + var21.getClass().getName());
                           var21.modifyInputStream(this.connection);
                        }

                        var18.clear();
                        this.handleResponse();
                     }
                  }
               }

               var17.flush();
               this.transmitFileHandler = null;
               return;
            }
         }
      } else {
         throw new IllegalArgumentException("[processRequests] requests was either null or empty.");
      }
   }

   private ResponseFactory getResponseFactory() {
      if (this.responseFactory == null) {
         this.responseFactory = new ResponseFactory();
      }

      return this.responseFactory;
   }

   private void handleResponse() throws ResponseException, CommandAbortedException {
      try {
         LoggedDataInputStream var1 = this.connection.getInputStream();
         this.loggedDataInputStream = var1;
         int var2 = -1;

         try {
            var2 = var1.read();
         } catch (InterruptedIOException var9) {
            this.abort();
         }

         while(!this.abort && var2 != -1) {
            StringBuffer var3 = new StringBuffer();

            while(var2 != -1 && (char)var2 != '\n' && (char)var2 != ' ') {
               var3.append((char)var2);

               try {
                  var2 = var1.read();
               } catch (InterruptedIOException var11) {
                  this.abort();
                  break;
               }
            }

            String var4 = var3.toString();
            Response var5 = this.getResponseFactory().createResponse(var4);
            var5.process(var1, this);
            boolean var6 = var5.isTerminalResponse();
            if (var6 && var5 instanceof ErrorMessageResponse) {
               ErrorMessageResponse var7 = (ErrorMessageResponse)var5;
               String var8 = var7.getMessage();
               throw new CommandAbortedException(var8, var8);
            }

            if (var6 || this.abort) {
               break;
            }

            try {
               var2 = var1.read();
            } catch (InterruptedIOException var10) {
               this.abort();
               break;
            }
         }

         if (this.abort) {
            String var14 = CommandException.getLocalMessage("Client.commandAborted", (Object[])null);
            throw new CommandAbortedException("Aborted during request processing", var14);
         }
      } catch (EOFException var12) {
         throw new ResponseException(var12, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var13) {
         throw new ResponseException(var13);
      }
   }

   public boolean executeCommand(Command var1, GlobalOptions var2) throws CommandException, CommandAbortedException, AuthenticationException {
      BugLog.getInstance().assertNotNull(var1);
      BugLog.getInstance().assertNotNull(var2);
      this.globalOptions = var2;
      this.getUncompressedFileHandler().setGlobalOptions(var2);
      this.getGzipFileHandler().setGlobalOptions(var2);

      try {
         this.eventManager.addCVSListener(var1);
         var1.execute(this, this.eventManager);
      } finally {
         this.eventManager.removeCVSListener(var1);
      }

      return !var1.hasFailed();
   }

   public long getCounter() {
      long var1 = 0L;
      if (this.loggedDataInputStream != null) {
         var1 += this.loggedDataInputStream.getCounter();
      }

      if (this.loggedDataOutputStream != null) {
         var1 += this.loggedDataOutputStream.getCounter();
      }

      return var1;
   }

   public String convertPathname(String var1, String var2) {
      int var3 = var2.lastIndexOf(47);
      String var4 = var2.substring(var3 + 1);
      if (var1.startsWith("./")) {
         var1 = var1.substring(1);
      }

      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      return this.getLocalPath() + '/' + var1 + var4;
   }

   public String getRepository() {
      return this.connection.getRepository();
   }

   public void updateAdminData(String var1, String var2, Entry var3) throws IOException {
      String var4 = this.localPath + '/' + var1;
      if (var2.startsWith(this.getRepository())) {
         var2 = var2.substring(this.getRepository().length() + 1);
      } else if (!this.warned) {
         String var5 = "#65188 warning C/S protocol error (section 5.10). It's regurarly observed with cvs 1.12.xx servers.\n";
         var5 = var5 + "  unexpected pathname=" + var2 + " missing root prefix=" + this.getRepository() + "\n";
         var5 = var5 + "  relaxing, but who knows all consequences....";
         System.err.println(var5);
         this.warned = true;
      }

      this.adminHandler.updateAdminData(var4, var2, var3, this.globalOptions);
   }

   public void setNextFileDate(Date var1) {
      this.modifiedDate = var1;
   }

   public Date getNextFileDate() {
      Date var1 = this.modifiedDate;
      this.modifiedDate = null;
      return var1;
   }

   public Entry getEntry(File var1) throws IOException {
      return this.adminHandler.getEntry(var1);
   }

   public Iterator getEntries(File var1) throws IOException {
      return this.adminHandler.getEntries(var1);
   }

   public boolean exists(File var1) {
      return this.adminHandler.exists(var1);
   }

   public String getRepositoryForDirectory(String var1) throws IOException {
      try {
         String var2 = this.adminHandler.getRepositoryForDirectory(var1, this.getRepository());
         return var2;
      } catch (IOException var8) {
         try {
            var1 = (new File(var1)).getCanonicalPath();
         } catch (IOException var7) {
         }

         for(var1 = var1.replace('\\', '/'); var1.endsWith("/"); var1 = var1.substring(0, var1.length() - 1)) {
         }

         String var3 = this.getLocalPath();

         try {
            var3 = (new File(this.getLocalPath())).getCanonicalPath();
         } catch (IOException var6) {
         }

         for(var3 = var3.replace('\\', '/'); var3.endsWith("/"); var3 = var3.substring(0, var3.length() - 1)) {
         }

         int var4 = var3.length();
         String var5;
         if (var1.length() >= var4) {
            var5 = this.getRepository() + var1.substring(var4);
         } else {
            var5 = this.getRepository();
         }

         return var5;
      }
   }

   public String getRepositoryForDirectory(File var1) throws IOException {
      return this.adminHandler.getRepositoryForDirectory(var1.getAbsolutePath(), this.getRepository());
   }

   public void setEntry(File var1, Entry var2) throws IOException {
      this.adminHandler.setEntry(var1, var2);
   }

   public void removeEntry(File var1) throws IOException {
      this.adminHandler.removeEntry(var1);
   }

   public void removeLocalFile(String var1) throws IOException {
      this.transmitFileHandler.removeLocalFile(var1);
   }

   public void removeLocalFile(String var1, String var2) throws IOException {
      int var3 = var2.lastIndexOf(47);
      if (var3 > 0) {
         String var4 = var2.substring(var3 + 1);
         String var5 = var1 + var4;
         File var6 = new File(this.getLocalPath(), var5);
         this.removeLocalFile(var6.getAbsolutePath());
         this.removeEntry(var6);
      }
   }

   public void renameLocalFile(String var1, String var2) throws IOException {
      this.transmitFileHandler.renameLocalFile(var1, var2);
   }

   public EventManager getEventManager() {
      return this.eventManager;
   }

   public GlobalOptions getGlobalOptions() {
      return this.globalOptions;
   }

   public synchronized void abort() {
      this.abort = true;
   }

   public Set getAllFiles(File var1) throws IOException {
      return this.adminHandler.getAllFiles(var1);
   }

   public void setIgnoreFileFilter(IgnoreFileFilter var1) {
      this.ignoreFileFilter = var1;
   }

   public IgnoreFileFilter getIgnoreFileFilter() {
      return this.ignoreFileFilter;
   }

   public boolean shouldBeIgnored(File var1, String var2) {
      return this.ignoreFileFilter != null ? this.ignoreFileFilter.shouldBeIgnored(var1, var2) : false;
   }

   public String getStickyTagForDirectory(File var1) {
      return this.adminHandler.getStickyTagForDirectory(var1);
   }

   public void setValidRequests(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1);

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         this.validRequests.put(var3, this);
      }

   }

   private int fillInitialRequests(List var1) {
      byte var2 = 0;
      int var3 = var2 + 1;
      var1.add(var2, new RootRequest(this.getRepository()));
      var1.add(var3++, new UseUnchangedRequest());
      var1.add(var3++, new ValidRequestsRequest());
      var1.add(var3++, new ValidResponsesRequest());
      return var3;
   }

   public void addWrapper(StringPattern var1, KeywordSubstitutionOptions var2) {
      if (this.wrappersMap == null) {
         throw new IllegalArgumentException("This method should be called by WrapperSendResponse only.");
      } else {
         this.wrappersMap.put(var1, var2);
      }
   }

   public Map getWrappersMap() throws CommandException {
      if (this.wrappersMap == null) {
         this.wrappersMap = new HashMap();
         ArrayList var1 = new ArrayList();
         var1.add(new WrapperSendRequest());
         boolean var2 = this.isFirstCommand();

         try {
            this.processRequests(var1);
         } catch (Exception var7) {
            throw new CommandException(var7, "An error during obtaining server wrappers.");
         } finally {
            this.setIsFirstCommand(var2);
         }

         this.wrappersMap = Collections.unmodifiableMap(this.wrappersMap);
      }

      return this.wrappersMap;
   }

   static {
      $assertionsDisabled = !Client.class.desiredAssertionStatus();
      ALLOWED_CONNECTION_REUSE_REQUESTS = new HashSet(Arrays.asList(ExpandModulesRequest.class, WrapperSendRequest.class));
   }

   public interface Factory {
      Client createClient();
   }
}
