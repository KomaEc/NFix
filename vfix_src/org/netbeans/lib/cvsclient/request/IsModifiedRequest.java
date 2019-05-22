package org.netbeans.lib.cvsclient.request;

import java.io.File;

public final class IsModifiedRequest extends Request {
   private final String fileName;

   public IsModifiedRequest(File var1) {
      this.fileName = var1.getName();
   }

   public String getRequestString() {
      return "Is-modified " + this.fileName + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
