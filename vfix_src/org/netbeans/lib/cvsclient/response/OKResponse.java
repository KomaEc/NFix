package org.netbeans.lib.cvsclient.response;

import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;

class OKResponse implements Response {
   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      TerminationEvent var3 = new TerminationEvent(this, false);
      var2.getEventManager().fireCVSEvent(var3);
   }

   public boolean isTerminalResponse() {
      return true;
   }
}
