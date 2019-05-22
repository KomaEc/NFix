package org.netbeans.lib.cvsclient.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.text.MessageFormat;
import javax.net.SocketFactory;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class PServerConnection extends AbstractConnection {
   protected static final String OPEN_PREAMBLE = "BEGIN AUTH REQUEST\n";
   protected static final String OPEN_POSTAMBLE = "END AUTH REQUEST\n";
   protected static final String VERIFY_PREAMBLE = "BEGIN VERIFICATION REQUEST\n";
   protected static final String VERIFY_POSTAMBLE = "END VERIFICATION REQUEST\n";
   protected static final String AUTHENTICATION_SUCCEEDED_RESPONSE = "I LOVE YOU";
   private static final String AUTHENTICATION_SUCCEEDED_RESPONSE_RAW = "I LOVE YOU\n";
   protected static final String AUTHENTICATION_FAILED_RESPONSE = "I HATE YOU";
   private static final String AUTHENTICATION_FAILED_RESPONSE_RAW = "I HATE YOU\n";
   protected String userName;
   protected String encodedPassword;
   public static final int DEFAULT_PORT = 2401;
   protected int port;
   protected String hostName;
   protected Socket socket;
   protected SocketFactory socketFactory;

   public PServerConnection() {
      this.port = 2401;
   }

   public PServerConnection(CVSRoot var1) {
      this(var1, (SocketFactory)null);
   }

   public PServerConnection(CVSRoot var1, SocketFactory var2) {
      this.port = 2401;
      if (!"pserver".equals(var1.getMethod())) {
         throw new IllegalArgumentException("CVS Root '" + var1 + "' does not represent :pserver: connection type.");
      } else {
         this.socketFactory = var2 != null ? var2 : SocketFactory.getDefault();
         String var3 = var1.getUserName();
         if (var3 == null) {
            var3 = System.getProperty("user.name");
         }

         this.setUserName(var3);
         String var4 = var1.getPassword();
         if (var4 != null) {
            this.setEncodedPassword(StandardScrambler.getInstance().scramble(var4));
         }

         this.setHostName(var1.getHostName());
         this.setRepository(var1.getRepository());
         int var5 = var1.getPort();
         if (var5 == 0) {
            var5 = 2401;
         }

         this.setPort(var5);
      }
   }

   private void openConnection(String var1, String var2) throws AuthenticationException, CommandAbortedException {
      if (this.hostName == null) {
         String var14 = AuthenticationException.getBundleString("AuthenticationException.HostIsNull");
         throw new AuthenticationException("HostIsNull", var14);
      } else {
         String var4;
         try {
            this.socket = this.socketFactory.createSocket(this.hostName, this.port);
            BufferedOutputStream var3 = new BufferedOutputStream(this.socket.getOutputStream(), 32768);
            LoggedDataOutputStream var15 = new LoggedDataOutputStream(var3);
            this.setOutputStream(var15);
            BufferedInputStream var5 = new BufferedInputStream(this.socket.getInputStream(), 32768);
            LoggedDataInputStream var6 = new LoggedDataInputStream(var5);
            this.setInputStream(var6);
            var15.writeBytes(var1, "US-ASCII");
            var15.writeBytes(this.getRepository() + "\n");
            var15.writeBytes(this.userName + "\n");
            var15.writeBytes(this.getEncodedPasswordNotNull() + "\n", "US-ASCII");
            var15.writeBytes(var2, "US-ASCII");
            var15.flush();
            if (Thread.interrupted()) {
               this.reset();
               String var16 = CommandException.getLocalMessage("Client.connectionAborted", (Object[])null);
               throw new CommandAbortedException("Aborted during connecting to the server.", var16);
            } else {
               byte[] var7 = var6.readBytes("I LOVE YOU\n".length());
               String var8 = new String(var7, "utf8");
               String var9;
               if (Thread.interrupted()) {
                  this.reset();
                  var9 = CommandException.getLocalMessage("Client.connectionAborted", (Object[])null);
                  throw new CommandAbortedException("Aborted during connecting to the server.", var9);
               } else if (!"I LOVE YOU\n".equals(var8)) {
                  if ("I HATE YOU\n".equals(var8)) {
                     var9 = this.getLocalMessage("AuthenticationException.badPassword", (Object[])null);
                     throw new AuthenticationException("AuthenticationFailed", var9);
                  } else {
                     if (var8 == null) {
                        var8 = "";
                     }

                     var9 = this.getLocalMessage("AuthenticationException.AuthenticationFailed", new Object[]{var8});
                     throw new AuthenticationException("AuthenticationFailed", var9);
                  }
               }
            }
         } catch (AuthenticationException var10) {
            this.reset();
            throw var10;
         } catch (ConnectException var11) {
            this.reset();
            var4 = this.getLocalMessage("AuthenticationException.ConnectException", new Object[]{this.hostName, Integer.toString(this.port)});
            throw new AuthenticationException("ConnectException", var11, var4);
         } catch (NoRouteToHostException var12) {
            this.reset();
            var4 = this.getLocalMessage("AuthenticationException.NoRouteToHostException", new Object[]{this.hostName});
            throw new AuthenticationException("NoRouteToHostException", var12, var4);
         } catch (IOException var13) {
            this.reset();
            var4 = this.getLocalMessage("AuthenticationException.IOException", new Object[]{this.hostName});
            throw new AuthenticationException("IOException", var13, var4);
         }
      }
   }

   private void reset() {
      this.socket = null;
      this.setInputStream((LoggedDataInputStream)null);
      this.setOutputStream((LoggedDataOutputStream)null);
   }

   public void verify() throws AuthenticationException {
      try {
         this.openConnection("BEGIN VERIFICATION REQUEST\n", "END VERIFICATION REQUEST\n");
      } catch (CommandAbortedException var9) {
      }

      if (this.socket != null) {
         try {
            this.socket.close();
         } catch (IOException var7) {
            String var2 = AuthenticationException.getBundleString("AuthenticationException.Throwable");
            throw new AuthenticationException("General error", var7, var2);
         } finally {
            this.reset();
         }

      }
   }

   public void open() throws AuthenticationException, CommandAbortedException {
      this.openConnection("BEGIN AUTH REQUEST\n", "END AUTH REQUEST\n");
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String var1) {
      this.userName = var1;
   }

   public String getEncodedPassword() {
      return this.encodedPassword;
   }

   private String getEncodedPasswordNotNull() {
      return this.encodedPassword == null ? StandardScrambler.getInstance().scramble("") : this.encodedPassword;
   }

   public void setEncodedPassword(String var1) {
      this.encodedPassword = var1;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int var1) {
      this.port = var1;
   }

   public String getHostName() {
      return this.hostName;
   }

   public void setHostName(String var1) {
      this.hostName = var1;
   }

   public void close() throws IOException {
      if (this.isOpen()) {
         try {
            this.socket.close();
         } finally {
            this.reset();
         }

      }
   }

   public void modifyInputStream(ConnectionModifier var1) throws IOException {
      var1.modifyInputStream(this.getInputStream());
   }

   public void modifyOutputStream(ConnectionModifier var1) throws IOException {
      var1.modifyOutputStream(this.getOutputStream());
   }

   private String getLocalMessage(String var1, Object[] var2) {
      String var3 = AuthenticationException.getBundleString(var1);
      if (var3 == null) {
         return null;
      } else {
         var3 = MessageFormat.format(var3, var2);
         return var3;
      }
   }

   public boolean isOpen() {
      return this.socket != null;
   }
}
