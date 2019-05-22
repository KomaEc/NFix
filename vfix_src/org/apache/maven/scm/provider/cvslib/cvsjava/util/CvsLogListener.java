package org.apache.maven.scm.provider.cvslib.cvsjava.util;

import org.netbeans.lib.cvsclient.event.CVSAdapter;
import org.netbeans.lib.cvsclient.event.MessageEvent;

public class CvsLogListener extends CVSAdapter {
   private final StringBuffer taggedLine = new StringBuffer();
   private StringBuffer stdout = new StringBuffer();
   private StringBuffer stderr = new StringBuffer();

   public void messageSent(MessageEvent e) {
      String line = e.getMessage();
      StringBuffer stream = e.isError() ? this.stderr : this.stdout;
      if (e.isTagged()) {
         String message = MessageEvent.parseTaggedMessage(this.taggedLine, e.getMessage());
         if (message != null) {
            stream.append(message).append("\n");
         }
      } else {
         stream.append(line).append("\n");
      }

   }

   public StringBuffer getStdout() {
      return this.stdout;
   }

   public StringBuffer getStderr() {
      return this.stderr;
   }
}
