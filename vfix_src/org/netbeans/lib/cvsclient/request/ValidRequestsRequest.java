package org.netbeans.lib.cvsclient.request;

public class ValidRequestsRequest extends Request {
   public String getRequestString() throws UnconfiguredRequestException {
      return "valid-requests \n";
   }

   public boolean isResponseExpected() {
      return true;
   }
}
