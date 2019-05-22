package org.netbeans.lib.cvsclient.request;

import java.io.File;
import org.netbeans.lib.cvsclient.file.FileDetails;
import org.netbeans.lib.cvsclient.file.FileMode;

public class ModifiedRequest extends Request {
   private FileDetails fileDetails;

   public ModifiedRequest(File var1, boolean var2) {
      this.fileDetails = new FileDetails(var1, var2);
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.fileDetails == null) {
         throw new UnconfiguredRequestException("FileDetails is null in ModifiedRequest");
      } else {
         FileMode var1 = new FileMode(this.fileDetails.getFile());
         return "Modified " + this.fileDetails.getFile().getName() + "\n" + var1.toString() + "\n";
      }
   }

   public boolean isResponseExpected() {
      return false;
   }

   public FileDetails getFileForTransmission() {
      return this.fileDetails;
   }
}
