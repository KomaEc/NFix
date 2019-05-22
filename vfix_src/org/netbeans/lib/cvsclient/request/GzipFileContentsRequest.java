package org.netbeans.lib.cvsclient.request;

public class GzipFileContentsRequest extends Request {
   private int compressionLevel;

   public GzipFileContentsRequest() {
      this(6);
   }

   public GzipFileContentsRequest(int var1) {
      this.compressionLevel = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      return "gzip-file-contents " + this.compressionLevel + " \n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
