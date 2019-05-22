package org.netbeans.lib.cvsclient.request;

public class ArgumentRequest extends Request {
   private final String argument;

   public ArgumentRequest(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("argument must not be null!");
      } else {
         this.argument = var1;
      }
   }

   public String getRequestString() {
      return "Argument " + this.argument + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
