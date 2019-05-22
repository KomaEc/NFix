package org.netbeans.lib.cvsclient.connection;

import java.io.IOException;
import org.netbeans.lib.cvsclient.request.RootRequest;
import org.netbeans.lib.cvsclient.request.UnconfiguredRequestException;
import org.netbeans.lib.cvsclient.request.UseUnchangedRequest;
import org.netbeans.lib.cvsclient.request.ValidRequestsRequest;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.LoggedDataOutputStream;

public abstract class AbstractConnection implements Connection {
   private String repository = null;
   private LoggedDataInputStream inputStream;
   private LoggedDataOutputStream outputStream;

   public LoggedDataInputStream getInputStream() {
      return this.inputStream;
   }

   protected final void setInputStream(LoggedDataInputStream var1) {
      if (this.inputStream != var1) {
         if (this.inputStream != null) {
            try {
               this.inputStream.close();
            } catch (IOException var3) {
            }
         }

         this.inputStream = var1;
      }
   }

   public LoggedDataOutputStream getOutputStream() {
      return this.outputStream;
   }

   protected final void setOutputStream(LoggedDataOutputStream var1) {
      if (this.outputStream != var1) {
         if (this.outputStream != null) {
            try {
               this.outputStream.close();
            } catch (IOException var3) {
            }
         }

         this.outputStream = var1;
      }
   }

   public String getRepository() {
      return this.repository;
   }

   public void setRepository(String var1) {
      this.repository = var1;
   }

   protected void verifyProtocol() throws IOException {
      try {
         this.outputStream.writeBytes((new RootRequest(this.repository)).getRequestString(), "US-ASCII");
         this.outputStream.writeBytes((new UseUnchangedRequest()).getRequestString(), "US-ASCII");
         this.outputStream.writeBytes((new ValidRequestsRequest()).getRequestString(), "US-ASCII");
         this.outputStream.writeBytes("noop \n", "US-ASCII");
      } catch (UnconfiguredRequestException var4) {
         throw new RuntimeException("Internal error verifying CVS protocol: " + var4.getMessage());
      }

      this.outputStream.flush();
      StringBuffer var1 = new StringBuffer();

      int var2;
      while((var2 = this.inputStream.read()) != -1) {
         var1.append((char)var2);
         if (var2 == 10) {
            break;
         }
      }

      String var3 = var1.toString();
      if (!var3.startsWith("Valid-requests")) {
         throw new IOException("Unexpected server response: " + var3);
      }
   }
}
