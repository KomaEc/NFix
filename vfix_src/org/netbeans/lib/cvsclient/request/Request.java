package org.netbeans.lib.cvsclient.request;

import java.io.IOException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.file.FileDetails;

public abstract class Request {
   public abstract String getRequestString() throws UnconfiguredRequestException;

   public abstract boolean isResponseExpected();

   public FileDetails getFileForTransmission() {
      return null;
   }

   public void modifyOutputStream(Connection var1) throws IOException {
   }

   public void modifyInputStream(Connection var1) throws IOException {
   }

   public boolean modifiesInputStream() {
      return false;
   }
}
