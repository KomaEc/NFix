package org.netbeans.lib.cvsclient.request;

public class ArgumentxRequest extends Request {
   private String argument;

   public ArgumentxRequest(String var1) {
      this.argument = var1;
   }

   public void setArgument(String var1) {
      this.argument = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.argument == null) {
         throw new UnconfiguredRequestException("Argument has not been set");
      } else {
         return "Argumentx " + this.argument + "\n";
      }
   }

   public boolean isResponseExpected() {
      return false;
   }
}
