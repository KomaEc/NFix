package org.netbeans.lib.cvsclient.request;

public class ValidResponsesRequest extends Request {
   public String getRequestString() throws UnconfiguredRequestException {
      return "Valid-responses E M MT Mbinary Updated Created Update-existing Rcs-diff Checked-in ok error Clear-static-directory Valid-requests Merged Removed Copy-file Mod-time Template Set-static-directory Module-expansion Clear-sticky Set-sticky New-entry\n";
   }

   public boolean isResponseExpected() {
      return false;
   }
}
