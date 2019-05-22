package org.netbeans.lib.cvsclient.request;

import org.netbeans.lib.cvsclient.util.BugLog;

public class SetRequest extends Request {
   private String keyValue;

   public SetRequest(String var1) {
      BugLog.getInstance().assertTrue(var1.indexOf(61) > 0, "Wrong SetRequest=" + var1);
      this.keyValue = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      String var1 = "Set " + this.keyValue + "\n";
      return var1;
   }

   public boolean isResponseExpected() {
      return false;
   }
}
