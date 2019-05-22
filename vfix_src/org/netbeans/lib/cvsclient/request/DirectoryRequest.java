package org.netbeans.lib.cvsclient.request;

public final class DirectoryRequest extends Request {
   private final String localDirectory;
   private final String repository;

   public DirectoryRequest(String var1, String var2) {
      if (var1 != null && var2 != null) {
         this.localDirectory = var1;
         this.repository = var2;
      } else {
         throw new IllegalArgumentException("Both, directory and repository, must not be null!");
      }
   }

   public String getLocalDirectory() {
      return this.localDirectory;
   }

   public String getRepository() {
      return this.repository;
   }

   public String getRequestString() {
      return "Directory " + this.localDirectory + "\n" + this.repository + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
