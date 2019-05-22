package org.apache.maven.scm.provider.cvslib.cvsjava.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.commandLine.CommandFactory;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;
import org.netbeans.lib.cvsclient.connection.AbstractConnection;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.ConnectionFactory;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.connection.StandardScrambler;
import org.netbeans.lib.cvsclient.event.CVSListener;

public class CvsConnection {
   private String repository;
   private String localPath;
   private Connection connection;
   private Client client;
   private GlobalOptions globalOptions;

   private CvsConnection() {
   }

   public boolean executeCommand(Command command) throws CommandException, AuthenticationException {
      return this.client.executeCommand(command, this.globalOptions);
   }

   public void setRepository(String repository) {
      this.repository = repository;
   }

   public void setLocalPath(String localPath) {
      this.localPath = localPath;
   }

   public void setGlobalOptions(GlobalOptions globalOptions) {
      this.globalOptions = globalOptions;
   }

   private void connect(CVSRoot root, String password) throws AuthenticationException, CommandAbortedException {
      if ("ext".equals(root.getMethod())) {
         String cvsRsh = System.getProperty("maven.scm.cvs.java.cvs_rsh");
         if (cvsRsh == null) {
            try {
               cvsRsh = CommandLineUtils.getSystemEnvVars().getProperty("CVS_RSH");
            } catch (IOException var5) {
            }
         }

         if (cvsRsh != null) {
            if (cvsRsh.indexOf(32) < 0) {
               String username = root.getUserName();
               if (username == null) {
                  username = System.getProperty("user.name");
               }

               cvsRsh = cvsRsh + " " + username + "@" + root.getHostName() + " cvs server";
            }

            AbstractConnection conn = new org.netbeans.lib.cvsclient.connection.ExtConnection(cvsRsh);
            conn.setRepository(root.getRepository());
            this.connection = conn;
         } else {
            this.connection = new ExtConnection(root);
         }
      } else {
         this.connection = ConnectionFactory.getConnection(root);
         if ("pserver".equals(root.getMethod())) {
            ((PServerConnection)this.connection).setEncodedPassword(password);
         }
      }

      this.connection.open();
      this.client = new Client(this.connection, new StandardAdminHandler());
      this.client.setLocalPath(this.localPath);
   }

   private void disconnect() {
      if (this.connection != null && this.connection.isOpen()) {
         try {
            this.connection.close();
         } catch (IOException var2) {
         }
      }

   }

   private void addListener(CVSListener listener) {
      if (this.client != null) {
         this.client.getEventManager().addCVSListener(listener);
      }

   }

   private static String getCVSRoot(String workingDir) {
      String root = null;
      BufferedReader r = null;
      if (workingDir == null) {
         workingDir = System.getProperty("user.dir");
      }

      try {
         File f = new File(workingDir);
         File rootFile = new File(f, "CVS/Root");
         if (rootFile.exists()) {
            r = new BufferedReader(new FileReader(rootFile));
            root = r.readLine();
         }
      } catch (IOException var13) {
      } finally {
         try {
            if (r != null) {
               r.close();
            }
         } catch (IOException var12) {
            System.err.println("Warning: could not close CVS/Root file!");
         }

      }

      if (root == null) {
         root = System.getProperty("cvs.root");
      }

      return root;
   }

   private static int processGlobalOptions(String[] args, GlobalOptions globalOptions) {
      String getOptString = globalOptions.getOptString();
      GetOpt go = new GetOpt(args, getOptString);

      int ch;
      String arg;
      boolean success;
      do {
         if ((ch = go.getopt()) == -1) {
            return go.optIndexGet();
         }

         arg = go.optArgGet();
         success = globalOptions.setCVSCommand((char)ch, arg);
      } while(success);

      throw new IllegalArgumentException("Failed to set CVS Command: -" + ch + " = " + arg);
   }

   private static String lookupPassword(String cvsRoot, ScmLogger logger) {
      File passFile = new File(System.getProperty("cygwin.user.home", System.getProperty("user.home")) + File.separatorChar + ".cvspass");
      BufferedReader reader = null;
      String password = null;

      label110: {
         Object var6;
         try {
            reader = new BufferedReader(new FileReader(passFile));
            password = processCvspass(cvsRoot, reader);
            break label110;
         } catch (IOException var16) {
            if (logger.isWarnEnabled()) {
               logger.warn("Could not read password for '" + cvsRoot + "' from '" + passFile + "'", var16);
            }

            var6 = null;
         } finally {
            if (reader != null) {
               try {
                  reader.close();
               } catch (IOException var15) {
                  if (logger.isErrorEnabled()) {
                     logger.error("Warning: could not close password file.");
                  }
               }
            }

         }

         return (String)var6;
      }

      if (password == null && logger.isErrorEnabled()) {
         logger.error("Didn't find password for CVSROOT '" + cvsRoot + "'.");
      }

      return password;
   }

   static String processCvspass(String cvsRoot, BufferedReader reader) throws IOException {
      String password = null;

      String line;
      while((line = reader.readLine()) != null) {
         if (line.startsWith("/")) {
            String[] cvspass = StringUtils.split(line, " ");
            String cvspassRoot = cvspass[1];
            if (compareCvsRoot(cvsRoot, cvspassRoot)) {
               int index = line.indexOf(cvspassRoot) + cvspassRoot.length() + 1;
               password = line.substring(index);
               break;
            }
         } else if (line.startsWith(cvsRoot)) {
            password = line.substring(cvsRoot.length() + 1);
            break;
         }
      }

      return password;
   }

   static boolean compareCvsRoot(String cvsRoot, String target) {
      String s1 = completeCvsRootPort(cvsRoot);
      String s2 = completeCvsRootPort(target);
      return s1 != null && s1.equals(s2);
   }

   private static String completeCvsRootPort(String cvsRoot) {
      String result = cvsRoot;
      int idx = cvsRoot.indexOf(58);

      for(int i = 0; i < 2 && idx != -1; ++i) {
         idx = cvsRoot.indexOf(58, idx + 1);
      }

      if (idx != -1 && cvsRoot.charAt(idx + 1) == '/') {
         StringBuilder sb = new StringBuilder();
         sb.append(cvsRoot.substring(0, idx + 1));
         sb.append("2401");
         sb.append(cvsRoot.substring(idx + 1));
         result = sb.toString();
      }

      return result;
   }

   public static boolean processCommand(String[] args, String localPath, CVSListener listener, ScmLogger logger) throws Exception {
      GlobalOptions globalOptions = new GlobalOptions();
      globalOptions.setCVSRoot(getCVSRoot(localPath));

      int commandIndex;
      try {
         commandIndex = processGlobalOptions(args, globalOptions);
      } catch (IllegalArgumentException var14) {
         if (logger.isErrorEnabled()) {
            logger.error("Invalid argument: " + var14);
         }

         return false;
      }

      if (globalOptions.getCVSRoot() == null) {
         if (logger.isErrorEnabled()) {
            logger.error("No CVS root is set. Check your <repository> information in the POM.");
         }

         return false;
      } else {
         String cvsRoot = globalOptions.getCVSRoot();

         CVSRoot root;
         try {
            root = CVSRoot.parse(cvsRoot);
         } catch (IllegalArgumentException var13) {
            if (logger.isErrorEnabled()) {
               logger.error("Incorrect format for CVSRoot: " + cvsRoot + "\nThe correct format is: " + "[:method:][[user][:password]@][hostname:[port]]/path/to/repository" + "\nwhere \"method\" is pserver.");
            }

            return false;
         }

         String command = args[commandIndex];

         Command c;
         try {
            CommandFactory var10000 = CommandFactory.getDefault();
            ++commandIndex;
            c = var10000.createCommand(command, args, commandIndex, globalOptions, localPath);
         } catch (IllegalArgumentException var15) {
            if (logger.isErrorEnabled()) {
               logger.error("Illegal argument: " + var15.getMessage());
            }

            return false;
         }

         String password = null;
         if ("pserver".equals(root.getMethod())) {
            password = root.getPassword();
            if (password != null) {
               password = StandardScrambler.getInstance().scramble(password);
            } else {
               password = lookupPassword(cvsRoot, logger);
               if (password == null) {
                  password = StandardScrambler.getInstance().scramble("");
               }
            }
         }

         CvsConnection cvsCommand = new CvsConnection();
         cvsCommand.setGlobalOptions(globalOptions);
         cvsCommand.setRepository(root.getRepository());
         cvsCommand.setLocalPath(localPath);
         cvsCommand.connect(root, password);
         cvsCommand.addListener(listener);
         if (logger.isDebugEnabled()) {
            logger.debug("Executing CVS command: " + c.getCVSCommand());
         }

         boolean result = cvsCommand.executeCommand(c);
         cvsCommand.disconnect();
         return result;
      }
   }
}
