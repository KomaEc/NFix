package org.netbeans.lib.cvsclient.connection;

import java.io.IOException;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public class LocalConnection extends AbstractConnection {
   private static final String CVS_EXE_COMMAND = System.getProperty("Env-CVS_EXE", "cvs") + " server";
   protected Process process;

   public LocalConnection() {
      this.reset();
   }

   private void openConnection() throws AuthenticationException {
      try {
         this.process = Runtime.getRuntime().exec(CVS_EXE_COMMAND);
         this.setOutputStream(new LoggedDataOutputStream(this.process.getOutputStream()));
         this.setInputStream(new LoggedDataInputStream(this.process.getInputStream()));
      } catch (IOException var3) {
         this.reset();
         String var2 = AuthenticationException.getBundleString("AuthenticationException.ServerConnection");
         throw new AuthenticationException("Connection error", var3, var2);
      }
   }

   private void reset() {
      this.process = null;
      this.setInputStream((LoggedDataInputStream)null);
      this.setOutputStream((LoggedDataOutputStream)null);
   }

   public void verify() throws AuthenticationException {
      try {
         this.openConnection();
         this.verifyProtocol();
         this.process.destroy();
      } catch (Exception var6) {
         String var2 = AuthenticationException.getBundleString("AuthenticationException.ServerVerification");
         throw new AuthenticationException("Verification error", var6, var2);
      } finally {
         this.reset();
      }

   }

   public void open() throws AuthenticationException {
      this.openConnection();
   }

   public boolean isOpen() {
      return this.process != null;
   }

   public void close() throws IOException {
      try {
         if (this.process != null) {
            this.process.destroy();
         }
      } finally {
         this.reset();
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
