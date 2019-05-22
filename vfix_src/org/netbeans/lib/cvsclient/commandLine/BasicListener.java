package org.netbeans.lib.cvsclient.commandLine;

import java.io.PrintStream;
import org.netbeans.lib.cvsclient.event.BinaryMessageEvent;
import org.netbeans.lib.cvsclient.event.CVSAdapter;
import org.netbeans.lib.cvsclient.event.EnhancedMessageEvent;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.event.MessageEvent;

public class BasicListener extends CVSAdapter {
   private final StringBuffer taggedLine;
   private PrintStream stdout;
   private PrintStream stderr;

   public BasicListener() {
      this(System.out, System.err);
   }

   public BasicListener(PrintStream var1, PrintStream var2) {
      this.taggedLine = new StringBuffer();
      this.stdout = var1;
      this.stderr = var2;
   }

   public void messageSent(MessageEvent var1) {
      String var2 = var1.getMessage();
      if (!(var1 instanceof EnhancedMessageEvent)) {
         PrintStream var3 = var1.isError() ? this.stderr : this.stdout;
         if (var1.isTagged()) {
            String var4 = MessageEvent.parseTaggedMessage(this.taggedLine, var1.getMessage());
            if (var4 != null) {
               var3.println(var4);
            }
         } else {
            var3.println(var2);
         }

      }
   }

   public void messageSent(BinaryMessageEvent var1) {
      byte[] var2 = var1.getMessage();
      int var3 = var1.getMessageLength();
      this.stdout.write(var2, 0, var3);
   }

   public void fileInfoGenerated(FileInfoEvent var1) {
   }
}
