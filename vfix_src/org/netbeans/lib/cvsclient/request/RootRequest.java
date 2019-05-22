package org.netbeans.lib.cvsclient.request;

public final class RootRequest extends Request {
   private final String cvsRoot;

   public RootRequest(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("cvsRoot must not be null!");
      } else {
         this.cvsRoot = var1;
      }
   }

   public String getRequestString() throws UnconfiguredRequestException {
      return "Root " + this.cvsRoot + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
