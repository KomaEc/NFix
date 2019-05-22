package org.netbeans.lib.cvsclient.request;

public final class StickyRequest extends Request {
   private String sticky;

   public StickyRequest(String var1) {
      this.sticky = var1;
   }

   public String getRequestString() throws UnconfiguredRequestException {
      if (this.sticky == null) {
         throw new UnconfiguredRequestException("Sticky tag has not been set");
      } else {
         return "Sticky " + this.sticky + "\n";
      }
   }

   public void setStickyTag(String var1) {
      this.sticky = var1;
   }

   public boolean isResponseExpected() {
      return false;
   }
}
