package org.netbeans.lib.cvsclient.response;

import java.util.Date;
import org.netbeans.lib.cvsclient.event.EnhancedMessageEvent;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class MergedResponse extends UpdatedResponse {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      super.process(var1, var2);
      EventManager var3 = var2.getEventManager();
      if (var3.isFireEnhancedEventSet()) {
         var3.fireCVSEvent(new EnhancedMessageEvent(this, "Merged_Response_File_Path", this.localFile));
      }

   }

   protected String getEntryConflict(Date var1, boolean var2) {
      return !var2 ? "Result of merge" : "Result of merge+" + this.getDateFormatter().format(var1);
   }
}
