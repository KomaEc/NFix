package org.netbeans.lib.cvsclient.request;

import java.io.File;

public class UnchangedRequest extends Request {
   private String filename;

   public UnchangedRequest(String var1) {
      this.filename = var1;
   }

   public UnchangedRequest(File var1) {
      this.filename = var1.getName();
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.filename == null) {
         throw new UnconfiguredRequestException("Filename must be set");
      } else {
         return "Unchanged " + this.filename + "\n";
      }
   }

   public boolean isResponseExpected() {
      return false;
   }
}
