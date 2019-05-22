package org.netbeans.lib.cvsclient.request;

import org.netbeans.lib.cvsclient.admin.Entry;

public class EntryRequest extends Request {
   private Entry entry;

   public EntryRequest(Entry var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("EntryRequest: Entry must not be null");
      } else {
         this.entry = var1;
      }
   }

   public String getRequestString() throws UnconfiguredRequestException {
      return "Entry " + this.entry.toString() + "\n";
   }

   public boolean isResponseExpected() {
      return false;
   }

   public Entry getEntry() {
      return this.entry;
   }
}
