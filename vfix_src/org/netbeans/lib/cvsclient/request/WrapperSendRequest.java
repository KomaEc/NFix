package org.netbeans.lib.cvsclient.request;

public class WrapperSendRequest extends Request {
   public String getRequestString() throws UnconfiguredRequestException {
      return "wrapper-sendme-rcsOptions \n";
   }

   public boolean isResponseExpected() {
      return true;
   }
}
