package org.netbeans.lib.cvsclient.request;

public class GlobalOptionRequest extends Request {
   private final String option;

   public GlobalOptionRequest(String var1) {
      this.option = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.option == null) {
         throw new UnconfiguredRequestException("Global option has not been set");
      } else {
         return "Global_option " + this.option + "\n";
      }
   }

   public boolean isResponseExpected() {
      return false;
   }
}
