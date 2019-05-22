package org.netbeans.lib.cvsclient.request;

public class KoptRequest extends Request {
   private final String option;

   public KoptRequest(String var1) {
      this.option = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      return "Kopt " + this.option + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
