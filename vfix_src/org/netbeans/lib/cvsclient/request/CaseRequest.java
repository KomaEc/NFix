package org.netbeans.lib.cvsclient.request;

public class CaseRequest extends Request {
   public String getRequestString() throws UnconfiguredRequestException {
      return "Case \n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
