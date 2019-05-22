package org.apache.maven.scm.provider.cvslib.cvsjava.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.connection.AbstractConnection;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.ConnectionModifier;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class ExtConnection extends AbstractConnection {
   private String host;
   private int port;
   private String userName;
   private String password;
   private Connection connection;
   private Session session;
   private BufferedReader stderrReader;

   public ExtConnection(CVSRoot cvsRoot) {
      this(cvsRoot.getHostName(), cvsRoot.getPort(), cvsRoot.getUserName(), cvsRoot.getPassword(), cvsRoot.getRepository());
   }

   public ExtConnection(String host, int port, String username, String password, String repository) {
      this.userName = username;
      if (this.userName == null) {
         this.userName = System.getProperty("user.name");
      }

      this.password = password;
      this.host = host;
      this.setRepository(repository);
      this.port = port;
      if (this.port == 0) {
         this.port = 22;
      }

   }

   public void open() throws AuthenticationException, CommandAbortedException {
      this.connection = new Connection(this.host, this.port);

      String command;
      try {
         this.connection.connect();
      } catch (IOException var7) {
         command = "Cannot connect. Reason: " + var7.getMessage();
         throw new AuthenticationException(command, var7, command);
      }

      File privateKey = this.getPrivateKey();

      String message;
      try {
         boolean authenticated;
         if (privateKey != null && privateKey.exists()) {
            authenticated = this.connection.authenticateWithPublicKey(this.userName, privateKey, this.getPassphrase());
         } else {
            authenticated = this.connection.authenticateWithPassword(this.userName, this.password);
         }

         if (!authenticated) {
            message = "Authentication failed.";
            throw new AuthenticationException(message, message);
         }
      } catch (IOException var8) {
         this.closeConnection();
         message = "Cannot authenticate. Reason: " + var8.getMessage();
         throw new AuthenticationException(message, var8, message);
      }

      try {
         this.session = this.connection.openSession();
      } catch (IOException var6) {
         message = "Cannot open session. Reason: " + var6.getMessage();
         throw new CommandAbortedException(message, message);
      }

      command = "cvs server";

      try {
         this.session.execCommand(command);
      } catch (IOException var5) {
         String message = "Cannot execute remote command: " + command;
         throw new CommandAbortedException(message, message);
      }

      InputStream stdout = new StreamGobbler(this.session.getStdout());
      InputStream stderr = new StreamGobbler(this.session.getStderr());
      this.stderrReader = new BufferedReader(new InputStreamReader(stderr));
      this.setInputStream(new LoggedDataInputStream(stdout));
      this.setOutputStream(new LoggedDataOutputStream(this.session.getStdin()));
   }

   public void verify() throws AuthenticationException {
      try {
         this.open();
         this.verifyProtocol();
         this.close();
      } catch (Exception var3) {
         String message = "Failed to verify the connection: " + var3.getMessage();
         throw new AuthenticationException(message, var3, message);
      }
   }

   private void closeConnection() {
      try {
         if (this.stderrReader != null) {
            while(true) {
               String line = this.stderrReader.readLine();
               if (line == null) {
                  break;
               }

               System.err.println(line);
            }
         }
      } catch (IOException var2) {
      }

      if (this.session != null) {
         System.out.println("Exit code:" + this.session.getExitStatus());
         this.session.close();
      }

      if (this.connection != null) {
         this.connection.close();
      }

      this.reset();
   }

   private void reset() {
      this.connection = null;
      this.session = null;
      this.stderrReader = null;
      this.setInputStream((LoggedDataInputStream)null);
      this.setOutputStream((LoggedDataOutputStream)null);
   }

   public void close() throws IOException {
      this.closeConnection();
   }

   public boolean isOpen() {
      return this.connection != null;
   }

   public int getPort() {
      return this.port;
   }

   public void modifyInputStream(ConnectionModifier modifier) throws IOException {
      modifier.modifyInputStream(this.getInputStream());
   }

   public void modifyOutputStream(ConnectionModifier modifier) throws IOException {
      modifier.modifyOutputStream(this.getOutputStream());
   }

   private File getPrivateKey() {
      File privateKey = null;
      if (this.password == null) {
         String pk = System.getProperty("maven.scm.cvs.java.ssh.privateKey");
         if (pk != null) {
            privateKey = new File(pk);
         } else {
            privateKey = this.findPrivateKey();
         }
      }

      return privateKey;
   }

   private String getPassphrase() {
      String passphrase = System.getProperty("maven.scm.cvs.java.ssh.passphrase");
      if (passphrase == null) {
         passphrase = "";
      }

      return passphrase;
   }

   private File findPrivateKey() {
      String privateKeyDirectory = System.getProperty("maven.scm.ssh.privateKeyDirectory");
      if (privateKeyDirectory == null) {
         privateKeyDirectory = System.getProperty("user.home");
      }

      File privateKey = new File(privateKeyDirectory, ".ssh/id_dsa");
      if (!privateKey.exists()) {
         privateKey = new File(privateKeyDirectory, ".ssh/id_rsa");
      }

      return privateKey;
   }
}
