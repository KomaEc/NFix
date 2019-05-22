package org.netbeans.lib.cvsclient.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class ExtConnection extends AbstractConnection {
   private final String command;
   private Process process;

   public ExtConnection(String var1) {
      this.command = var1;
   }

   public void open() throws AuthenticationException, CommandAbortedException {
      try {
         this.process = Runtime.getRuntime().exec(this.command);
         this.setInputStream(new LoggedDataInputStream(new BufferedInputStream(this.process.getInputStream())));
         this.setOutputStream(new LoggedDataOutputStream(new BufferedOutputStream(this.process.getOutputStream())));
      } catch (IOException var2) {
         throw new AuthenticationException(var2, "Failed to execute: " + this.command);
      }
   }

   public void verify() throws AuthenticationException {
      try {
         this.open();
         this.verifyProtocol();
         this.process.destroy();
      } catch (Exception var2) {
         throw new AuthenticationException(var2, "Failed to execute: " + this.command);
      }
   }

   public void close() throws IOException {
      if (this.isOpen()) {
         this.process.destroy();
      }

   }

   public boolean isOpen() {
      if (this.process == null) {
         return false;
      } else {
         try {
            this.process.exitValue();
            return false;
         } catch (IllegalThreadStateException var2) {
            return true;
         }
      }
   }

   public int getPort() {
      return 0;
   }

   public void modifyInputStream(ConnectionModifier var1) throws IOException {
      var1.modifyInputStream(this.getInputStream());
   }

   public void modifyOutputStream(ConnectionModifier var1) throws IOException {
      var1.modifyOutputStream(this.getOutputStream());
   }
}
