package org.netbeans.lib.cvsclient.request;

import java.io.IOException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.GzipModifier;

public class GzipStreamRequest extends Request {
   private int level = 6;

   public GzipStreamRequest() {
   }

   public GzipStreamRequest(int var1) {
      this.level = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      return "Gzip-stream " + this.level + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }

   public void modifyOutputStream(Connection var1) throws IOException {
      var1.modifyOutputStream(new GzipModifier());
   }

   public void modifyInputStream(Connection var1) throws IOException {
      var1.modifyInputStream(new GzipModifier());
   }

   public boolean modifiesInputStream() {
      return true;
   }
}
